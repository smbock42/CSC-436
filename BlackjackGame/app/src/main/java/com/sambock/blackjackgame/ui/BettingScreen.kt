package com.sambock.blackjackgame.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun BettingScreen(
    maxBet: Int,
    onBetPlaced: (Int) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    var currentBet by remember { mutableStateOf("5") } // Store as string to handle empty input
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
            Text(
                text = "Place Your Bet",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
        
        Card(
            modifier = Modifier
                .padding(bottom = 16.dp)
                .width(280.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Available Chips: $maxBet",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = currentBet.ifEmpty { "0" },
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = currentBet,
                        onValueChange = { newValue ->
                            // Allow empty or valid number
                            if (newValue.isEmpty() || newValue.toIntOrNull() != null) {
                                currentBet = newValue
                            }
                        },
                        label = { Text("Bet Amount") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true
                    )
                    Button(
                        onClick = { currentBet = maxBet.toString() },
                        enabled = currentBet.toIntOrNull() != maxBet,
                        modifier = Modifier.width(80.dp)
                    ) {
                        Text("Max")
                    }
                }
            }
        }
    
        // Casino-style chip buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            val chipValues = listOf(5, 25, 100, 500, 1000)
            chipValues.chunked(3).forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    row.forEach { value ->
                        ChipButton(
                            value = value,
                            onClick = {
                                val currentValue = currentBet.toIntOrNull() ?: 0
                                val newValue = currentValue + value
                                if (newValue <= maxBet) {
                                    currentBet = newValue.toString()
                                }
                            },
                            enabled = (currentBet.toIntOrNull() ?: 0) + value <= maxBet
                        )
                    }
                }
            }
            // Quick action buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(vertical = 4.dp)
            ) {
                Button(
                    onClick = { currentBet = "" },
                    modifier = Modifier.width(90.dp)
                ) {
                    Text("Clear")
                }
                Button(
                    onClick = {
                        val current = currentBet.toIntOrNull() ?: 0
                        currentBet = (current / 2).coerceAtLeast(5).toString()
                    },
                    modifier = Modifier.width(90.dp),
                    enabled = currentBet.toIntOrNull() ?: 0 > 5
                ) {
                    Text("Half")
                }
                Button(
                    onClick = {
                        val current = currentBet.toIntOrNull() ?: 0
                        val doubled = if (current == 0) 5 else current * 2
                        if (doubled <= maxBet) {
                            currentBet = doubled.toString()
                        }
                    },
                    modifier = Modifier.width(100.dp),
                    enabled = (currentBet.toIntOrNull() ?: 0) * 2 <= maxBet || currentBet.isEmpty()
                ) {
                    Text("Double")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Deal button
        val betAmount = currentBet.toIntOrNull() ?: 0
        ElevatedButton(
            onClick = {
                if (betAmount in 5..maxBet) {
                    println("BettingScreen: Placing bet of $betAmount")
                    onBetPlaced(betAmount)
                }
            },
            modifier = Modifier.size(width = 200.dp, height = 56.dp),
            enabled = betAmount >= 5 && betAmount <= maxBet,
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Deal",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    "($betAmount)",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun ChipButton(
    value: Int,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.size(90.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        // Choose text style based on number length
        val style = MaterialTheme.typography.titleMedium
        
        Text(
            text = value.toString(),
            style = style,
            textAlign = TextAlign.Center
        )
    }
}