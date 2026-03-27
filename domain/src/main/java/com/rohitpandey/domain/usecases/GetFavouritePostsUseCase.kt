package com.rohitpandey.domain.usecases

import com.rohitpandey.domain.models.Post
import com.rohitpandey.domain.repositories.PostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavouritePostsUseCase @Inject constructor(
    private val repository: PostRepository,
) {
    operator fun invoke(): Flow<List<Post>> = repository.getFavouritePosts()
}