package com.sambock.blackjackgame.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sambock.blackjackgame.game.BlackjackGame
import com.sambock.blackjackgame.game.Card

@Composable
fun BlackjackScreen() {
    val game = remember { mutableStateOf(BlackjackGame()) }
    val isGameActive = remember { mutableStateOf(false) }
    val hasBlackjack = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // 📌 Dealer's Hand (Top)
        Text(text = "Dealer's Hand", fontSize = 20.sp)
        Row {
            game.value.getDealerHand().forEachIndexed { index, card ->
                val isFaceUp = isGameActive.value && (index != 0 || hasBlackjack.value)
                CardView(card.copy(isFaceUp = isFaceUp)) {}
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 📌 Player's Hand (Below Dealer)
        Text(text = "Your Hand", fontSize = 20.sp)
        Row {
            game.value.getPlayerHand().forEach { card ->
                CardView(card.copy(isFaceUp = true)) {} // ✅ Always face-up
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🎮 Game Controls
        Row {
            Button(onClick = {
                game.value.resetGame()
                isGameActive.value = true
                hasBlackjack.value = game.value.calculateHandValue(game.value.getPlayerHand()) == 21
                if (hasBlackjack.value) game.value.dealerPlays() // Auto dealer plays if Blackjack
            }) {
                Text("Deal New Hand")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isGameActive.value && !hasBlackjack.value) {
            Row {
                Button(onClick = { game.value.playerHits() }) {
                    Text("Hit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = {
                    game.value.dealerPlays()
                    isGameActive.value = false
                }) {
                    Text("Stand")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🏆 Result
        Text(text = game.value.checkGameResult(), fontSize = 18.sp)
    }
}

@Preview
@Composable
fun ShowScreen() {
    BlackjackScreen()
}