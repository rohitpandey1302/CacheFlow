package com.rohitpandey.cache_flow.ui_models

data class HomeUiState(
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
)
