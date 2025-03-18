package com.sambock.blackjackgame.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sambock.blackjackgame.game.Hand
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HandView(
    hand: Hand,
    showValue: Boolean = false,
    modifier: Modifier = Modifier
) {
    var visibleCards by remember { mutableStateOf(0) }
    val cards = hand.getCards()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(cards.size) {
        // Animate new cards being dealt
        while (visibleCards < cards.size) {
            delay(300) // Delay between each card
            visibleCards++
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy((-40).dp),
            contentPadding = PaddingValues(horizontal = 40.dp)
        ) {
            itemsIndexed(cards) { index, card ->
                AnimatedVisibility(
                    visible = index < visibleCards,
                    enter = slideInHorizontally() + fadeIn(),
                    modifier = Modifier.padding(end = if (index == cards.size - 1) 0.dp else 4.dp)
                ) {
                    CardView(card = card)
                }
            }
        }

        if (showValue) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Hand Value: ${hand.getVisibleScore()}",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}