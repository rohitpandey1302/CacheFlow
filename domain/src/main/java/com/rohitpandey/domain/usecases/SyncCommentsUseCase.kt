package com.rohitpandey.domain.usecases

import com.rohitpandey.domain.models.Result
import com.rohitpandey.domain.repositories.CommentRepository
import javax.inject.Inject

class SyncCommentsUseCase @Inject constructor(
    private val repository: CommentRepository,
) {
    suspend operator fun invoke(postId: Int): Result<Unit> =
        repository.syncComments(postId)
}