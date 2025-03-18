package com.sambock.blackjackgame.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun SettingsScreen(
    viewModel: BlackjackViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var hasChanges by remember { mutableStateOf(false) }
    var minimumBet by remember { mutableStateOf(5) }
    var soundEnabled by remember { mutableStateOf(true) }
    var vibrationEnabled by remember { mutableStateOf(true) }

    val chipCount by viewModel.game.collectAsStateWithLifecycle()
    val currentChips = chipCount.getPlayerChips()

    fun updateSettings() {
        hasChanges = true
    }

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

        // Minimum Bet Setting
        Column {
            Text(
                text = "Minimum Bet: $minimumBet",
                style = MaterialTheme.typography.titleMedium
            )
            Slider(
                value = minimumBet.toFloat(),
                onValueChange = { minimumBet = it.toInt() },
                valueRange = 5f..50f,
                steps = 9,
                modifier = Modifier.padding(top = 8.dp)
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
                onCheckedChange = { soundEnabled = it }
            )
        }

        // Vibration Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Vibration",
                style = MaterialTheme.typography.titleMedium
            )
            Switch(
                checked = vibrationEnabled,
                onCheckedChange = { vibrationEnabled = it }
            )
        }

        // Top Up Chips Button
        if (currentChips < 100) {
            Button(
                onClick = {
                    viewModel.topUpChips()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text("Top Up Chips")
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Apply Button
        Button(
            onClick = {
                // TODO: Save settings
                hasChanges = false
            },
            enabled = hasChanges,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
        ) {
            Text(if (hasChanges) "Apply Changes" else "No Changes")
        }
    }
}