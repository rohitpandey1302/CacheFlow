package com.rohitpandey.cache_flow.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.rohitpandey.cache_flow.navigation.Screen
import com.rohitpandey.cache_flow.ui_models.DetailUiState
import com.rohitpandey.domain.models.Result.*
import com.rohitpandey.domain.usecases.GetPostDetailUseCase
import com.rohitpandey.domain.usecases.SyncCommentsUseCase
import com.rohitpandey.domain.usecases.ToggleFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val toggleFavoriteUseCase: ToggleFavouriteUseCase,
    private val syncCommentsUseCase: SyncCommentsUseCase,
): ViewModel() {
    private val postId: Int = savedStateHandle.toRoute<Screen.Detail>().postId

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        observePost()
        observeComments()
    }

    private fun observePost() {
        viewModelScope.launch {
            getPostDetailUseCase.getPost(postId).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Loading -> state.copy(isPostLoading = true, postError = null)
                        is Success -> state.copy(post = result.data, isPostLoading = false)
                        is Error -> state.copy(
                            isPostLoading = false,
                            postError = result.message ?: "Failed to load post",
                        )
                    }
                }
            }
        }
    }

    private fun observeComments() {
        viewModelScope.launch {
            getPostDetailUseCase.getComments(postId).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Loading -> state.copy(isCommentsLoading = true)
                        is Success -> state.copy(
                            comments = result.data,
                            isCommentsLoading = false,
                            commentsError = null,
                        )
                        is Error -> state.copy(
                            isCommentsLoading = false,
                            commentsError = result.message ?: "Failed to load comments",
                        )
                    }
                }
            }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch { toggleFavoriteUseCase(postId) }
    }

    fun retryComments() {
        viewModelScope.launch { syncCommentsUseCase(postId) }
    }
}