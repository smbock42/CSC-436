package com.sambock.blackjackgame.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sambock.blackjackgame.game.*
import kotlinx.coroutines.delay

@Composable
fun GamePlayScreen(
    game: BlackjackGame,
    state: GameState.Playing,
    onHit: () -> Unit,
    onStand: () -> Unit,
    onDouble: () -> Unit,
    viewModel: BlackjackViewModel,
    modifier: Modifier = Modifier,
    isBusting: Boolean = false
) {
    val isDealerAnimating by viewModel.isDealerAnimating.collectAsState()
    val isAnimating by viewModel.isAnimating.collectAsState()
    val lastDrawnCard by viewModel.lastDrawnCard.collectAsState()
    val isBusted by viewModel.isBusted.collectAsState()
    
    var statusMessage by remember { mutableStateOf("") }

    // Update status message based on game state and animations
    LaunchedEffect(isDealerAnimating, isAnimating, lastDrawnCard, isBusted) {
        statusMessage = when {
            isDealerAnimating -> when {
                lastDrawnCard != null -> "Dealer draws ${lastDrawnCard?.rank?.name} of ${lastDrawnCard?.suit}"
                game.getDealerHand().getVisibleScore() > 21 -> "Dealer busts with ${game.getDealerHand().getVisibleScore()}!"
                !game.dealer.shouldHit() -> "Dealer stands at ${game.getDealerHand().getVisibleScore()}"
                else -> "Dealer's turn..."
            }
            isAnimating && lastDrawnCard != null -> {
                val message = "You drew ${lastDrawnCard?.rank?.name} of ${lastDrawnCard?.suit}"
                if (game.getPlayerHand().isBust()) {
                    "$message - Bust with ${game.getPlayerHand().getActualScore()}!"
                } else {
                    "$message (Total: ${game.getPlayerHand().getActualScore()})"
                }
            }
            isBusted -> "Bust with ${game.getPlayerHand().getActualScore()}!"
            else -> ""
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Dealer area (top)
        DealerSection(
            hand = game.getDealerHand(),
            dealerScore = game.getDealerHand().getVisibleScore(),
            isAnimating = isDealerAnimating,
            modifier = Modifier.weight(1f)
        )

        // Status message
        if (statusMessage.isNotEmpty()) {
            Text(
                text = statusMessage,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(8.dp)
            )
        }

        // Player area (bottom)
        PlayerSection(
            hand = game.getPlayerHand(),
            currentBet = state.currentBet,
            canHit = state.canHit && !isAnimating && !isDealerAnimating && !isBusted,
            canStand = state.canStand && !isAnimating && !isDealerAnimating && !isBusted,
            canDouble = state.canDouble && !isAnimating && !isDealerAnimating && !isBusted,
            chips = game.getPlayerChips(),
            onHit = onHit,
            onStand = onStand,
            onDouble = onDouble,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun DealerSection(
    hand: Hand,
    dealerScore: Int,
    modifier: Modifier = Modifier,
    isAnimating: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Dealer" + if (isAnimating || hand.getCards().all { it.isRevealed }) " ($dealerScore)" else "",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        HandView(
            hand = hand,
            showValue = isAnimating || hand.getCards().all { it.isRevealed },
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
private fun PlayerSection(
    hand: Hand,
    currentBet: Int,
    canHit: Boolean,
    canStand: Boolean,
    canDouble: Boolean,
    chips: Int,
    onHit: () -> Unit,
    onStand: () -> Unit,
    onDouble: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Hand (Bet: $currentBet)",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = "Value: ${hand.getActualScore()}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        HandView(
            hand = hand,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        ActionButtons(
            canHit = canHit,
            canStand = canStand,
            canDouble = canDouble,
            hasEnoughChipsToDouble = chips >= currentBet,
            currentBet = currentBet,
            onHit = onHit,
            onStand = onStand,
            onDouble = onDouble,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ActionButtons(
    canHit: Boolean,
    canStand: Boolean,
    canDouble: Boolean,
    hasEnoughChipsToDouble: Boolean,
    currentBet: Int,
    onHit: () -> Unit,
    onStand: () -> Unit,
    onDouble: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onHit,
            enabled = canHit
        ) {
            Text("Hit")
        }
        Button(
            onClick = onStand,
            enabled = canStand
        ) {
            Text("Stand")
        }
        Box {
            Button(
                onClick = onDouble,
                enabled = canDouble && hasEnoughChipsToDouble
            ) {
                Text("Double")
            }
        }
    }
}
