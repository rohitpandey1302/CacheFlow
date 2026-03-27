package com.rohitpandey.domain.usecases

import com.rohitpandey.domain.models.Result
import com.rohitpandey.domain.repositories.PostRepository
import javax.inject.Inject

class SyncPostsUseCase @Inject constructor(
    private val repository: PostRepository,
) {
    suspend operator fun invoke(): Result<Unit> = repository.syncPosts()
}