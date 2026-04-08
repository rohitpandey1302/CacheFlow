package com.rohitpandey.cache_flow.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.rohitpandey.cache_flow.detail.DetailScreen
import com.rohitpandey.cache_flow.favourites.FavouritesScreen
import com.rohitpandey.cache_flow.home.HomeScreen

@Composable
internal fun AppNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
) {
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
}