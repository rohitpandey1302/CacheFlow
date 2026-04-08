package com.rohitpandey.cache_flow.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rohitpandey.cache_flow.ui_models.FavouritesUiState
import com.rohitpandey.domain.usecases.GetFavouritePostsUseCase
import com.rohitpandey.domain.usecases.ToggleFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class FavouritesViewModel @Inject constructor(
    getFavouritePostsUseCase: GetFavouritePostsUseCase,
    private val toggleFavouriteUseCase: ToggleFavouriteUseCase,
): ViewModel() {
    val uiState: StateFlow<FavouritesUiState> = getFavouritePostsUseCase()
        .map { FavouritesUiState(posts = it, isLoading = false) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FavouritesUiState(isLoading = true)
        )

    fun toggleFavourite(postId: Int) {
        viewModelScope.launch { toggleFavouriteUseCase(postId) }
    }
}