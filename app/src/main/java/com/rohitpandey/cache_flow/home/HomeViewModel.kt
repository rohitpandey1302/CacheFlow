package com.rohitpandey.cache_flow.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.rohitpandey.cache_flow.ui_models.HomeUiState
import com.rohitpandey.domain.models.Post
import com.rohitpandey.domain.models.Result.Error
import com.rohitpandey.domain.usecases.GetPagedPostsUseCase
import com.rohitpandey.domain.usecases.SyncPostsUseCase
import com.rohitpandey.domain.usecases.ToggleFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    getPagedPostsUseCase: GetPagedPostsUseCase,
    private val syncPostsUseCase: SyncPostsUseCase,
    private val toggleFavoriteUseCase: ToggleFavouriteUseCase,
): ViewModel() {

    val posts: Flow<PagingData<Post>> =
        getPagedPostsUseCase().cachedIn(viewModelScope)

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isRefreshing = true, errorMessage = null)
            when (val result = syncPostsUseCase()) {
                is Error -> _uiState.value = _uiState.value.copy(
                    isRefreshing = false,
                    errorMessage = result.message ?: "Sync failed",
                )
                else -> _uiState.value = _uiState.value.copy(isRefreshing = false)
            }
        }
    }

    fun toggleFavorite(postId: Int) {
        viewModelScope.launch { toggleFavoriteUseCase(postId) }
    }

    fun errorShown() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}