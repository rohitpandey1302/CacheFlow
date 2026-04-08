package com.rohitpandey.cache_flow.detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rohitpandey.cache_flow.common.ErrorScreen
import com.rohitpandey.cache_flow.common.LoadingIndicator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    postId: Int,
    onBack: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { padding ->
        AnimatedContent(
            targetState = state.isPostLoading to state.postError,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            modifier = Modifier.fillMaxSize().padding(padding),
            label = "detail_content"
        ) { (loading, error) ->
            when {
                loading -> Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { LoadingIndicator(modifier = Modifier.size(48.dp), strokeWidth = 3.dp) }

                error != null -> ErrorScreen(
                    message = error,
                    onRetry = null,
                    modifier = Modifier.fillMaxSize().padding(padding),
                )

                else -> DetailContent(
                    uiState = state,
                    onBack = onBack,
                    onFavouriteClick = viewModel::toggleFavorite,
                    onRetryComments = viewModel::retryComments,
                )
            }

        }
    }
}