package com.rohitpandey.cache_flow.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data class Detail(val postId: Int) : Screen

    @Serializable
    data object Favorites : Screen
}