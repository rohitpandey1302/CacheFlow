package com.rohitpandey.domain.usecases

import androidx.paging.PagingData
import com.rohitpandey.domain.models.Post
import com.rohitpandey.domain.repositories.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPagedPostsUseCase @Inject constructor(
    private val repository: PostRepository,
) {
    operator fun invoke(): Flow<PagingData<Post>> = repository.getPagedPosts()
}