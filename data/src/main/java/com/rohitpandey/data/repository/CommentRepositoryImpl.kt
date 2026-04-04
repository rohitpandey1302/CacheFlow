package com.rohitpandey.data.repository

import com.rohitpandey.data.local.AppDatabase
import com.rohitpandey.data.mapper.toDomain
import com.rohitpandey.data.mapper.toEntity
import com.rohitpandey.domain.models.Comment
import com.rohitpandey.domain.models.Result
import com.rohitpandey.domain.models.Result.Loading.runCatchingResult
import com.rohitpandey.domain.repositories.CommentRepository
import com.rohitpandey.network.api.PostApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val postApiService: PostApiService,
): CommentRepository {

    private val commentDao = appDatabase.commentDao()

    override fun getCommentsForPost(postId: Int): Flow<Result<List<Comment>>> {
        return commentDao.observeCommentsForPost(postId)
            .map<_, Result<List<Comment>>> { entities ->
                if (entities.isEmpty()) {
                    // Cache miss → fetch and store, Flow will re-emit via Room
                    syncComments(postId)
                    Result.Loading
                } else {
                    Result.Success(entities.toDomain())
                }
            }
            .onStart { emit(Result.Loading) }
    }

    override suspend fun syncComments(postId: Int): Result<Unit> {
        return runCatchingResult {
            val commentDtos = postApiService.getCommentsForPost(postId)
            commentDao.clearCommentsForPost(postId)
            commentDao.upsertComments(commentDtos.map { it.toEntity() })
        }
    }
}