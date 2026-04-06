package com.rohitpandey.cache_flow.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.rohitpandey.cache_flow.common.PostCard
import com.rohitpandey.domain.models.Post

@Composable
internal fun ContentList(
    posts: LazyPagingItems<Post>,
    listState: LazyListState,
    onFavoritesClick: () -> Unit,
    onPostClick: (Int) -> Unit,
    onToggleFav: (Int) -> Unit,
) {
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        item(key = "header") {
            GradientHeader(
                postCount = posts.itemCount,
                onFavoritesClick = onFavoritesClick,
            )
        }

        item(key = "label") {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
            ) {
                Text(
                    text = "Latest posts",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = "${posts.itemCount} items",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        items(
            count = posts.itemCount,
            key = posts.itemKey { it.id },
        ) { index ->
            val post = posts[index] ?: return@items
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(300)) + slideInVertically(tween(300)) { it / 4 },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            ) {
                PostCard(
                    post = post,
                    onClick = { onPostClick(post.id) },
                    onFavoriteClick = { onToggleFav(post.id) },
                )
            }
        }

        if (posts.loadState.append is LoadState.Loading) {
            item(key = "append_loading") {
                Box(
                    contentAlignment = Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }

        if (posts.loadState.append is LoadState.Error) {
            val appendError = (posts.loadState.append as LoadState.Error).error
            item(key = "append_error") {
                AppendErrorFooter(
                    message = appendError.localizedMessage ?: "Load failed",
                    onRetry = { posts.retry() },
                )
            }
        }
    }
}