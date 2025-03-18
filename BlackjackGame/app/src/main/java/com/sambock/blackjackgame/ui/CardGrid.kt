package com.sambock.blackjackgame.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sambock.blackjackgame.game.*

@Composable
fun CardGrid() {
    val allCards = remember { getAllCards() }

    Column {
        for (row in allCards.chunked(13)) { // 4 rows of 13 cards each
            Row(modifier = Modifier.fillMaxWidth()) {
                row.forEach { card ->
                    var isFaceUp by remember { mutableStateOf(true) }
                    CardView(Card(card.suit, card.rank, isFaceUp)) {
                        isFaceUp = !isFaceUp
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// Function to generate all 52 cards
fun getAllCards(): List<Card> {
    return Suit.entries.flatMap { suit ->
        Rank.entries.map { rank -> Card(suit, rank) }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCardGrid() {
    CardGrid()
}