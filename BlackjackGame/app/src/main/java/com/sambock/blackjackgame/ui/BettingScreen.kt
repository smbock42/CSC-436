package com.sambock.blackjackgame.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var currentBet by remember { mutableStateOf(5) } // Default minimum bet
    
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
                    text = "$currentBet",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = currentBet.toString(),
                        onValueChange = { newValue ->
                            val newBet = newValue.toIntOrNull() ?: 0
                            currentBet = newBet
                            if (newBet in 5..maxBet) {
                                currentBet = newBet
                            }
                        },
                        label = { Text("Custom Amount") },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true
                    )
                    Button(
                        onClick = { currentBet = maxBet },
                        enabled = currentBet != maxBet,
                        modifier = Modifier.width(80.dp)
                    ) {
                        Text("Max")
                    }
                }
            }
        }
    
        // Quick adjust chips
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            listOf(5, 10, 25, 50, 100).chunked(3).forEach { row ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    row.forEach { amount ->
                        Column {
                            FilledIconButton(
                                onClick = {
                                    val newBet = currentBet + amount
                                    if (newBet <= maxBet) currentBet = newBet
                                },
                                enabled = currentBet + amount <= maxBet,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Text("+$amount")
                            }
                            Spacer(Modifier.height(4.dp))
                            FilledIconButton(
                                onClick = {
                                    val newBet = currentBet - amount
                                    if (newBet >= 5) currentBet = newBet
                                },
                                enabled = currentBet > amount,
                                modifier = Modifier.size(48.dp)
                            ) {
                                Text("-$amount")
                            }
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        // Deal button
        ElevatedButton(
            onClick = {
                if (currentBet in 5..maxBet) {
                    println("BettingScreen: Placing bet of $currentBet")
                    onBetPlaced(currentBet)
                } else {
                    println("BettingScreen: Invalid bet amount: $currentBet (max: $maxBet)")
                }
            },
            modifier = Modifier.size(width = 200.dp, height = 56.dp),
            enabled = currentBet >= 5 && currentBet <= maxBet,
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
                    "($currentBet)",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
private fun ChipButton(
    value: Int,
    currentBet: Int,
    maxBet: Int,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = value <= maxBet,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (currentBet == value) 
                MaterialTheme.colorScheme.primary 
            else 
                MaterialTheme.colorScheme.secondary
        ),
        modifier = Modifier.size(64.dp)
    ) {
        Text("$value")
    }
}