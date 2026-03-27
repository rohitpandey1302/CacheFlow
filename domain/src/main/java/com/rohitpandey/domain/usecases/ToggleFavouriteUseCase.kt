package com.rohitpandey.domain.usecases

import com.rohitpandey.domain.repositories.PostRepository
import javax.inject.Inject

class ToggleFavouriteUseCase @Inject constructor(
    private val repository: PostRepository,
) {
    suspend operator fun invoke(postId: Int) = repository.toggleFavourite(postId)
}