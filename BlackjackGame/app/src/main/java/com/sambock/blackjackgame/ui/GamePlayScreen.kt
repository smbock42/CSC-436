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
    println("GamePlayScreen: Composing with state: $state, isBusting: $isBusting")
    println("GamePlayScreen: Player hand: ${game.getPlayerHand().getCards().size} cards")
    println("GamePlayScreen: Dealer hand: ${game.getDealerHand().getCards().size} cards")
    
    val isDealerAnimating by viewModel.isDealerAnimating.collectAsState()
    var dealerActionState by remember { mutableStateOf("") }
    val lastCardState = remember { mutableStateOf<Card?>(null) }
    val lastCard = lastCardState.value

    LaunchedEffect(game) {
        println("GamePlayScreen: LaunchedEffect triggered")
    }

    LaunchedEffect(isDealerAnimating, game.getDealerHand().getCards().size) {
        if (isDealerAnimating) {
            while (isDealerAnimating) {
                dealerActionState = when {
                    game.getDealerHand().getVisibleScore() < 17 -> "Drawing card..."
                    game.getDealerHand().getVisibleScore() > 21 -> "Bust!"
                    else -> "Stands"
                }
                delay(500) // Add delay for animation
            }
        } else {
            dealerActionState = ""
        }
    }

    LaunchedEffect(isBusting) {
        if (isBusting) {
            val cards = game.getPlayerHand().getCards()
            if (cards.isNotEmpty()) {
                lastCardState.value = cards.last()
            }
            delay(1000)
            viewModel.updateGameState { game ->
                game.endHand()
            }
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
            dealerAction = dealerActionState,
            isAnimating = isDealerAnimating,
            modifier = Modifier.weight(1f)
        )

        // Player area (bottom)
        PlayerSection(
            hand = game.getPlayerHand(),
            currentBet = state.currentBet,
            canHit = state.canHit,
            canStand = state.canStand,
            canDouble = state.canDouble,
            onHit = onHit,
            onStand = onStand,
            onDouble = onDouble,
            modifier = Modifier.weight(1f)
        )
        if (isBusting && lastCard != null) {
            Text(text = "Bust! Last card: ${lastCard.rank.name} of ${lastCard.suit}")
        }
    }
}

@Composable
private fun DealerSection(
    hand: Hand,
    modifier: Modifier = Modifier,
    dealerAction: String = "",
    isAnimating: Boolean = false
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(
                text = "Dealer",
                style = MaterialTheme.typography.titleMedium
            )
            if (dealerAction.isNotEmpty()) {
                Text(
                    text = " - $dealerAction",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
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
            onHit = onHit,
            onStand = onStand,
            onDouble = onDouble,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
private fun ActionButtons(
    canHit: Boolean,
    canStand: Boolean,
    canDouble: Boolean,
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
        Button(
            onClick = onDouble,
            enabled = canDouble
        ) {
            Text("Double")
        }
    }
}
