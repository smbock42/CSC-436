package com.sambock.blackjackgame.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sambock.blackjackgame.game.GameResult

@Composable
fun GameOverScreen(
    result: GameResult,
    winAmount: Int,
    onPlayAgain: () -> Unit,
    onMainMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val message = when (result) {
            GameResult.WIN -> "You Win!"
            GameResult.LOSE -> "You Lose!"
            GameResult.PUSH -> "Push!"
            GameResult.BLACKJACK -> "Blackjack!"
            GameResult.BUST -> "Bust!"
            else -> "Game Over!"
        }

        Text(
            text = message,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (winAmount > 0) {
            Text(
                text = "You won $winAmount chips!",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            ElevatedButton(onClick = onMainMenu) {
                Text("Main Menu")
            }
            
            ElevatedButton(onClick = onPlayAgain) {
                Text("Play Again")
            }
        }
    }
}