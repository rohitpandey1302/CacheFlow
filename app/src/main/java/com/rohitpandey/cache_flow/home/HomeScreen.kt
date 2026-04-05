package com.rohitpandey.cache_flow.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.rohitpandey.cache_flow.common.ErrorScreen
import com.rohitpandey.cache_flow.common.LoadingIndicator
import com.rohitpandey.cache_flow.common.PostCard
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

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { msg ->
            snackBarHostState.showSnackbar(message = msg, duration = SnackbarDuration.Short)
            viewModel.errorShown()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { androidx.compose.material3.Text("Posts") },
                actions = {
                    IconButton(onClick = onFavoritesClick) {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = "Favourites",
                            tint = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackBarHostState) }
    ) {
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.refresh() },
            modifier = Modifier.fillMaxSize().padding(it)
        ) {
            when {
                posts.loadState.refresh is LoadState.Loading && posts.itemCount == 0 ->
                    LoadingIndicator(modifier = Modifier.fillMaxSize())

                posts.loadState.refresh is LoadState.Error && posts.itemCount == 0 -> {
                    val error = (posts.loadState.refresh as LoadState.Error).error
                    ErrorScreen(
                        message   = error.localizedMessage ?: "Failed to load posts",
                        onRetry   = { posts.retry() },
                        modifier  = Modifier.fillMaxSize(),
                    )
                }

                else -> {
                    LazyColumn(
                        contentPadding    = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier          = Modifier.fillMaxSize(),
                    ) {
                        items(
                            count = posts.itemCount,
                            key   = posts.itemKey { it.id },
                        ) { index ->
                            val post = posts[index]
                            if (post != null) {
                                PostCard(
                                    post = post,
                                    onClick = { onPostClick(post.id) },
                                    onFavoriteClick = { viewModel.toggleFavorite(post.id) },
                                )
                            }
                        }

                        if (posts.loadState.append is LoadState.Loading) {
                            item {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier         = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                ) {
                                    CircularProgressIndicator(strokeWidth = 2.dp)
                                }
                            }
                        }

                        if (posts.loadState.append is LoadState.Error) {
                            item {
                                val error = (posts.loadState.append as LoadState.Error).error
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment     = Alignment.CenterVertically,
                                    modifier              = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                ) {
                                    Text(
                                        text     = error.localizedMessage ?: "Load failed",
                                        style    = MaterialTheme.typography.bodySmall,
                                        color    = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.weight(1f),
                                    )
                                    TextButton(onClick = { posts.retry() }) {
                                        Text("Retry")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}