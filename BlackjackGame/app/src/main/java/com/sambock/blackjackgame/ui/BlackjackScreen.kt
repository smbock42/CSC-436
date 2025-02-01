package com.sambock.blackjackgame.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.sambock.blackjackgame.game.BlackjackGame

@Composable
fun BlackjackScreen(game: BlackjackGame) {
    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Blackjack", fontSize = 30.sp)

        Button(onClick = { game.playerHit() }) { Text("Hit") }
        Button(onClick = { game.playerStand() }) { Text("Stand")}
    }
}

@Preview(showBackground = true)
@Composable
fun ShowScreen() {
    BlackjackScreen(game = BlackjackGame())
}