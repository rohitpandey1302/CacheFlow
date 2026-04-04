package com.rohitpandey.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.rohitpandey.data.DataConstants
import com.rohitpandey.data.local.AppDatabase
import com.rohitpandey.data.local.entity.PostEntity
import com.rohitpandey.data.local.entity.RemoteKeyEntity
import com.rohitpandey.data.mapper.toEntity
import com.rohitpandey.network.api.PostApiService
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val appDatabase: AppDatabase,
    private val postApiService: PostApiService,
): RemoteMediator<Int, PostEntity>() {

    private val postDao      = appDatabase.postDao()
    private val remoteKeyDao = appDatabase.remoteKeyDao()

    override suspend fun initialize(): InitializeAction {
        val latestCreatedAt = remoteKeyDao.latestCreatedAt() ?: return InitializeAction.LAUNCH_INITIAL_REFRESH
        val cacheAge = System.currentTimeMillis() - latestCreatedAt
        return if (cacheAge < DataConstants.CACHE_TIMEOUT_MILLIS) {
            // Cache is fresh — skip the initial remote refresh, serve DB data immediately
            Timber.d("Cache fresh (age=${cacheAge}ms) — skipping initial refresh")
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            Timber.d("Cache stale (age=${cacheAge}ms) — launching refresh")
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> STARTING_PAGE

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = true)

                    remoteKeyDao.remoteKeyByPostId(lastItem.id)?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            Timber.d("Fetching page=$page loadType=$loadType")
            val posts = postApiService.getPosts(page = page, limit = state.config.pageSize)
            val endOfPaginationReached = posts.isEmpty()

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    // Preserve favourites — read current IDs before clearing
                    remoteKeyDao.clearAll()
                    postDao.clearNonFavorites()
                }

                // Read existing favourite flags so they survive the upsert
                val favouriteIds = posts
                    .map { it.id }
                    .filter { postDao.getPostById(it)?.isFavourite == true }
                    .toSet()

                val entities = posts.map { dto ->
                    dto.toEntity(isFavorite = dto.id in favouriteIds)
                }

                val keys = posts.map { dto ->
                    RemoteKeyEntity(
                        postId = dto.id,
                        prevKey = if (page == STARTING_PAGE) null else page - 1,
                        nextKey = if (endOfPaginationReached) null else page + 1,
                    )
                }

                postDao.upsertPosts(entities)
                remoteKeyDao.upsertAll(keys)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (e: IOException) {
            Timber.d("Network error in PostRemoteMediator, e:- $e")
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            Timber.d("HTTP error ${e.code()} in PostRemoteMediator, e:- $e")
            MediatorResult.Error(e)
        }
    }

    companion object {
        private const val STARTING_PAGE = 1
    }
}