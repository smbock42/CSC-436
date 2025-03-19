package com.sambock.blackjackgame.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LandingScreen(
    onNavigateToGame: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToStats: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Blackjack Game",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Button(onClick = onNavigateToGame) {
            Text("Play Game")
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = onNavigateToSettings) {
            Text("Settings")
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Button(onClick = onNavigateToStats) {
            Text("Stats")
        }
    }
}

@Preview
@Composable
fun LandingScreenPreview() {
    LandingScreen(
        onNavigateToGame = {},
        onNavigateToSettings = {},
        onNavigateToStats = {})
}
