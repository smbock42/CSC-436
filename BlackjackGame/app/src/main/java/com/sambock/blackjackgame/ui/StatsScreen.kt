package com.sambock.blackjackgame.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.sambock.blackjackgame.game.StatsManager
import androidx.navigation.NavController
import com.sambock.blackjackgame.game.ChipHistoryEntry

@Composable
fun StatsScreenWithViewModel(
    viewModel: BlackjackViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    StatsScreen(
        statsManager = viewModel.getStats(),
        navController = navController,
        modifier = modifier
    )
}

@Composable
fun StatsScreen(
    statsManager: StatsManager,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
            Text(
                text = "Statistics",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        // Win/Loss Statistics
        StatisticsSection(
            title = "Game Statistics",
            stats = listOf(
                "Hands Played" to statsManager.getHandsPlayed().toString(),
                "Hands Won" to statsManager.getHandsWon().toString(),
                "Hands Lost" to statsManager.getHandsLost().toString(),
                "Pushes" to statsManager.getPushes().toString(),
                "Win Rate" to "%.1f%%".format(statsManager.getWinPercentage()),
                "Blackjacks" to statsManager.getBlackjacks().toString()
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Financial Statistics
        StatisticsSection(
            title = "Financial Statistics",
            stats = listOf(
                "Total Winnings" to "$${statsManager.getTotalWinnings()}",
                "Total Losses" to "$${statsManager.getTotalLosses()}",
                "Net Profit" to "$${statsManager.getNetProfit()}",
                "Largest Win" to "$${statsManager.getLargestWin()}",
                "Largest Loss" to "$${statsManager.getLargestLoss()}"
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Chip History Chart
        Text(
            text = "Chip History",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        ChipHistoryChart(
            history = statsManager.getChipHistory(),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(8.dp)
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

@Composable
private fun ChipHistoryChart(
    history: List<ChipHistoryEntry>,
    modifier: Modifier = Modifier
) {
    if (history.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text("No game history yet")
        }
        return
    }

    Canvas(modifier = modifier) {
        val path = Path()
        val width = size.width
        val height = size.height
        val maxChips = history.maxOfOrNull { it.chipCount } ?: 0
        val minChips = history.minOfOrNull { it.chipCount } ?: 0
        val range = (maxChips - minChips).coerceAtLeast(1)

        // Draw coordinate system
        drawLine(
            Color.Gray,
            Offset(0f, height),
            Offset(width, height),
            1f
        )
        drawLine(
            Color.Gray,
            Offset(0f, 0f),
            Offset(0f, height),
            1f
        )

        history.forEachIndexed { index, entry ->
            val x = (index.toFloat() / (history.size - 1)) * width
            val y = height - ((entry.chipCount - minChips).toFloat() / range) * height

            if (index == 0) {
                path.moveTo(x, y)
            } else {
                path.lineTo(x, y)
            }
        }

        // Draw the line
        drawPath(
            path,
            Color.Blue,
            style = Stroke(
                width = 3f,
                cap = StrokeCap.Round
            )
        )
    }
}