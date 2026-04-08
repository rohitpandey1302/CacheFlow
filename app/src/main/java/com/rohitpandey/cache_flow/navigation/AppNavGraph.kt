package com.rohitpandey.cache_flow.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.rohitpandey.cache_flow.AppViewModel
import com.rohitpandey.cache_flow.common.OfflineScreen
import com.rohitpandey.cache_flow.detail.DetailScreen
import com.rohitpandey.cache_flow.favourites.FavouritesScreen
import com.rohitpandey.cache_flow.home.HomeScreen
import com.rohitpandey.cache_flow.utils.connectivity.NetworkStatus

@Composable
internal fun AppNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val appViewModel: AppViewModel = hiltViewModel()
    val networkStatus by appViewModel.networkStatus.collectAsStateWithLifecycle()

    val isOffline = networkStatus == NetworkStatus.Unavailable
            || networkStatus == NetworkStatus.Lost

    Box(modifier = modifier) {
        NavHost(
            navController = navHostController,
            startDestination = Screen.Home,
            modifier = modifier,
        ) {
            composable<Screen.Home> {
                HomeScreen(
                    onPostClick = { postId -> navHostController.navigate(Screen.Detail(postId)) },
                    onFavoritesClick = { navHostController.navigate(Screen.Favorites) },
                )
            }

            composable<Screen.Detail> { backStackEntry ->
                val route = backStackEntry.toRoute<Screen.Detail>()
                DetailScreen(
                    postId = route.postId,
                    onBack = { navHostController.popBackStack() }
                )
            }

            composable<Screen.Favorites> {
                FavouritesScreen(
                    onPostClick = { postId -> navHostController.navigate(Screen.Detail(postId)) },
                    onBack = { navHostController.popBackStack() },
                )
            }
        }

        AnimatedVisibility(
            visible = isOffline,
            modifier = Modifier.align(Alignment.BottomCenter)
                .navigationBarsPadding()
        ) {
            OfflineScreen(networkStatus = networkStatus)
        }
    }
}