package com.rohitpandey.cache_flow.ui_models

import com.rohitpandey.domain.models.Post

data class FavouritesUiState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = true,
)