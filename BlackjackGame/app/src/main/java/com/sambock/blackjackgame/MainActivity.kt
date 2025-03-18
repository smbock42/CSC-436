package com.sambock.blackjackgame

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sambock.blackjackgame.ui.*

class MainActivity : ComponentActivity() {
    private val viewModel: BlackjackViewModel by viewModels {
        BlackjackViewModelFactory(this)
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "landing") {
                composable("landing") {
                    LandingScreen(
                        onNavigateToGame = { navController.navigate("game") },
                        onNavigateToSettings = { navController.navigate("settings") },
                        onNavigateToStats = { navController.navigate("stats") }
                    )
                }
                composable("game") {
                    BlackjackScreen(viewModel = viewModel, navController = navController)
                }
                composable("settings") {
                    SettingsScreen(viewModel = viewModel, navController = navController)
                }
                composable("stats") {
                    StatsScreenWithViewModel(viewModel = viewModel, navController = navController)
                }
            }
        }
    }
}