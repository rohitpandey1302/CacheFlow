package com.rohitpandey.domain.models

data class Post(
    val id: Int,
    val userId: Int,
    val title: String,
    val body: String,
    val isFavourite: Boolean = false,
    val isSynced: Boolean = false,
)
