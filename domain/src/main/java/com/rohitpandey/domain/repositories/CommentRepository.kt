package com.rohitpandey.domain.repositories

import com.rohitpandey.domain.models.Comment
import com.rohitpandey.domain.models.Result
import kotlinx.coroutines.flow.Flow

interface CommentRepository {
    fun getCommentsForPost(postId: Int): Flow<Result<List<Comment>>>

    suspend fun syncComments(postId: Int): Result<Unit>
}