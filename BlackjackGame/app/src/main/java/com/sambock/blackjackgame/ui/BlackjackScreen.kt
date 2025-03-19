package com.sambock.blackjackgame.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sambock.blackjackgame.game.*
import androidx.navigation.NavController

@Composable
fun BlackjackScreen(
    viewModel: BlackjackViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val game by viewModel.game.collectAsStateWithLifecycle()
    val state = game.getCurrentState()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header with chip count
        GameHeader(chipCount = game.getPlayerChips())

        // Main game content
        when (state) {
            is GameState.Betting -> {
                BettingScreen(
                    maxBet = game.getPlayerChips(),
                    onBetPlaced = { viewModel.startNewHand(it) },
                    navController = navController
                )
            }
            is GameState.Playing -> {
                GamePlayScreen(
                    game = game,
                    state = state,
                    onHit = { viewModel.hit() },
                    onStand = { viewModel.stand() },
                    onDouble = { viewModel.double() },
                    viewModel = viewModel
                )
            }
            is GameState.Busting -> {
                // Show busting animation but don't allow actions
                GamePlayScreen(
                    game = game,
                    state = GameState.Playing(currentBet = state.currentBet), 
                    onHit = { },
                    onStand = { },
                    onDouble = { },
                    viewModel = viewModel,
                    isBusting = true
                )
            }
            is GameState.Complete -> {
                GameOverScreen(
                    result = state.result,
                    winAmount = state.winAmount,
                    onPlayAgain = { viewModel.resetGame() },
                    onMainMenu = { navController.navigate("landing") }
                )
            }
        }
    }
}

@Composable
private fun GameHeader(
    chipCount: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primaryContainer,
        tonalElevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Chips: $chipCount",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Preview
@Composable
fun ShowScreen() {
    //val fakeViewModel: BlackjackViewModel = viewModel()
    //BlackjackScreen(
    //    viewModel = fakeViewModel
    //)
}