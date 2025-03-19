package com.sambock.blackjackgame.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sambock.blackjackgame.game.StatsManager

@Composable
fun StatsScreenWithViewModel(
    viewModel: BlackjackViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    StatsScreen(
        statsManager = viewModel.getStats(),
        onReset = { viewModel.resetStats() },
        navController = navController,
        modifier = modifier
    )
}

@Composable
fun StatsScreen(
    statsManager: StatsManager,
    onReset: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var showResetDialog by remember { mutableStateOf(false) }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset Statistics") },
            text = { Text("Are you sure you want to reset all statistics? This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onReset()
                        showResetDialog = false
                    }
                ) {
                    Text("Reset")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.headlineMedium
            )
            TextButton(onClick = { showResetDialog = true }) {
                Text("Reset")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Game Statistics
        StatisticsSection(
            title = "Game Statistics",
            stats = listOf(
                "Total Hands" to statsManager.getHandsPlayed().toString(),
                "Wins" to statsManager.getWins().toString(),
                "Losses" to statsManager.getLosses().toString(),
                "Pushes" to statsManager.getPushes().toString(),
                "Win Rate" to "%.1f%%".format(statsManager.getWinPercentage()),
                "Blackjacks" to statsManager.getBlackjacks().toString(),
                "Busts" to statsManager.getBusts().toString()
            )
        )
    }
}

@Composable
private fun StatisticsSection(
    title: String,
    stats: List<Pair<String, String>>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                stats.forEach { (label, value) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = value,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}