package com.rohitpandey.cache_flow.ui_models

import com.rohitpandey.domain.models.Comment
import com.rohitpandey.domain.models.Post

data class DetailUiState(
    val post: Post? = null,
    val comments: List<Comment> = emptyList(),
    val isPostLoading: Boolean = true,
    val isCommentsLoading: Boolean = true,
    val postError: String? = null,
    val commentsError: String? = null,
)
