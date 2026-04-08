package com.rohitpandey.cache_flow.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.rohitpandey.cache_flow.common.ErrorScreen
import com.rohitpandey.cache_flow.common.LoadingIndicator
import com.rohitpandey.domain.models.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onPostClick: (Int) -> Unit,
    onFavoritesClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val posts: LazyPagingItems<Post> = viewModel.posts.collectAsLazyPagingItems()
    val snackBarHostState = remember { SnackbarHostState() }
    val listState = rememberLazyListState()

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            snackBarHostState.showSnackbar(message = msg, duration = SnackbarDuration.Short)
            viewModel.errorShown()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            when {
                posts.loadState.refresh is LoadState.Loading && posts.itemCount == 0 ->
                    LoadingIndicator(modifier = Modifier.fillMaxSize())

                posts.loadState.refresh is LoadState.Error && posts.itemCount == 0 -> {
                    val error = (posts.loadState.refresh as LoadState.Error).error
                    ErrorScreen(
                        message = error.localizedMessage ?: "Failed to load posts",
                        onRetry = { posts.retry() },
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                else -> ContentList(
                    posts = posts,
                    listState = listState,
                    onFavoritesClick = onFavoritesClick,
                    onPostClick = onPostClick,
                    onToggleFav = viewModel::toggleFavorite,
                )
            }
        }
    }
}