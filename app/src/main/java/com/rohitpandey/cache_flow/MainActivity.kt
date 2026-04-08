package com.rohitpandey.cache_flow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import com.rohitpandey.cache_flow.navigation.AppNavHost
import com.rohitpandey.cache_flow.theme.CacheFlowTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CacheFlowTheme {
                val navController = rememberNavController()
                AppNavHost(
                    navHostController = navController,
                    modifier         = Modifier.fillMaxSize()
                )
            }
        }
    }
}