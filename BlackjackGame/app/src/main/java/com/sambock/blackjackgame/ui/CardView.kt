package com.sambock.blackjackgame.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sambock.blackjackgame.R
import com.sambock.blackjackgame.game.Card
import com.sambock.blackjackgame.game.Rank
import com.sambock.blackjackgame.game.Suit

@Composable
fun CardView(
    card: Card,
    modifier: Modifier = Modifier,
    onFlip: () -> Unit = {}
) {
    var isFlipping by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (card.isRevealed) 0f else 180f,
        animationSpec = tween(durationMillis = 500, easing = FastOutSlowInEasing),
        finishedListener = { isFlipping = false }
    )

    val alpha by animateFloatAsState(
        targetValue = if (isFlipping) 0.5f else 1f,
        animationSpec = tween(durationMillis = 250)
    )

    val cardDrawable = if (card.isRevealed) getCardDrawable(card) else R.drawable.card_back

    Box(
        modifier = Modifier
            .size(100.dp, 150.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
            .background(Color.White)
            .clickable { if (!isFlipping) { isFlipping = true; onFlip() } },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = cardDrawable),
            contentDescription = "Playing Card",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer(rotationY = rotation, alpha = alpha)
        )
    }
}

@Preview
@Composable
fun JackSpades() {
    var card by remember { mutableStateOf(Card(Suit.SPADE, Rank.JACK, true)) }

    CardView(card = card) {
        card = Card(card.suit, card.rank, !card.isRevealed) // Toggle face-up state
    }
}

@Preview
@Composable
fun TwoClubs() {
    var card by remember { mutableStateOf(Card(Suit.CLUB, Rank.TWO, true)) }

    CardView(card = card) {
        card = Card(card.suit, card.rank, !card.isRevealed) // Toggle face-up state
    }
}

@Preview
@Composable
fun SevenSpades() {
    var card by remember { mutableStateOf(Card(Suit.SPADE, Rank.SEVEN, true)) }

    CardView(card = card) {
        card = Card(card.suit, card.rank, !card.isRevealed) // Toggle face-up state
    }
}

@Preview
@Composable
fun AceHearts() {
    var card by remember { mutableStateOf(Card(Suit.HEART, Rank.ACE, false)) }

    CardView(card = card) {
        card = Card(card.suit, card.rank, !card.isRevealed) // Toggle face-up state
    }
}

@Preview
@Composable
fun KingDiamonds() {
    var card by remember { mutableStateOf(Card(Suit.DIAMOND, Rank.KING, true)) }

    CardView(card = card) {
        card = Card(card.suit, card.rank, !card.isRevealed) // Toggle face-up state
    }
}