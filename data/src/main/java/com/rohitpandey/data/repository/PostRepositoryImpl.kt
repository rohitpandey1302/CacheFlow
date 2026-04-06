package com.rohitpandey.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.rohitpandey.data.DataConstants
import com.rohitpandey.data.local.AppDatabase
import com.rohitpandey.data.mapper.toDomain
import com.rohitpandey.data.mapper.toEntity
import com.rohitpandey.data.paging.PostRemoteMediator
import com.rohitpandey.domain.models.Post
import com.rohitpandey.domain.models.Result
import com.rohitpandey.domain.models.Result.Loading.runCatchingResult
import com.rohitpandey.domain.repositories.PostRepository
import com.rohitpandey.network.api.PostApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val postApiService: PostApiService,
): PostRepository {

    private val postDao = appDatabase.postDao()
    private val remoteKeyDao = appDatabase.remoteKeyDao()

    @OptIn(ExperimentalPagingApi::class)
    override fun getPagedPosts(): Flow<PagingData<Post>> {
        return Pager(
            config = PagingConfig(
            pageSize = DataConstants.PAGING_PAGE_SIZE,
            prefetchDistance = DataConstants.PAGING_PREFETCH,
            enablePlaceholders = false,
        ),
            remoteMediator = PostRemoteMediator(appDatabase, postApiService),
            pagingSourceFactory = { postDao.getPagedPosts() },
        ).flow.map { pagingData ->
            pagingData.map { entity -> entity.toDomain() }
        }
    }

    override fun getPostById(id: Int): Flow<Result<Post>> {
        return postDao.observePostById(id)
            .map<_, Result<Post>> { entity ->
                if (entity != null) {
                    Result.Success(entity.toDomain())
                } else {
                    // Not in cache — trigger a network fetch
                    fetchAndCachePost(id)
                }
            }
            .onStart { emit(Result.Loading) }
    }

    override fun getFavouritePosts(): Flow<List<Post>> {
        return postDao.getFavoritePosts().map { it.toDomain() }
    }

    override suspend fun toggleFavourite(postId: Int) {
        postDao.toggleFavorite(postId)
    }

    override suspend fun syncPosts(): Result<Unit> {
        return runCatchingResult {
            val posts = postApiService.getPosts(page = 1, limit = DataConstants.PAGING_PAGE_SIZE)
            val favouriteIds = posts
                .map { it.id }
                .filter { postDao.getPostById(it)?.isFavourite == true }
                .toSet()

            val entities = posts.map { dto ->
                dto.toEntity(isFavorite = dto.id in favouriteIds)
            }

            remoteKeyDao.clearAll()
            postDao.clearNonFavorites()
            postDao.upsertPosts(entities)
        }
    }

    private suspend fun fetchAndCachePost(id: Int): Result<Post> =
        runCatchingResult {
            val dto = postApiService.getPostById(id)
            val isFav = postDao.getPostById(id)?.isFavourite ?: false
            val entity = dto.toEntity(isFavorite = isFav)
            postDao.upsertPost(entity)
            entity.toDomain()
        }
}