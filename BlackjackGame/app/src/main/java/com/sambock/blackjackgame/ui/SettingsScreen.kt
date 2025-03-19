package com.sambock.blackjackgame.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController

@Composable
fun SettingsScreen(
    viewModel: BlackjackViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val chipCount by viewModel.game.collectAsStateWithLifecycle()
    val currentChips = chipCount.getPlayerChips()
    val soundEnabled by viewModel.soundEnabled.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Sound Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Sound Effects",
                style = MaterialTheme.typography.titleMedium
            )
            Switch(
                checked = soundEnabled,
                onCheckedChange = { viewModel.setSoundEnabled(it) }
            )
        }

        // Top Up Chips Button
        if (currentChips < 100) {
            Button(
                onClick = { viewModel.topUpChips() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Top Up Chips")
            }
        }

        Button(
            onClick = {
                viewModel.resetStats()
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text("Reset Statistics")
        }
    }
}