package com.sambock.blackjackgame.ui

import android.graphics.Paint.Align
import android.inputmethodservice.Keyboard.Row
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sambock.blackjackgame.R
import com.sambock.blackjackgame.game.Card
import com.sambock.blackjackgame.game.Rank
import com.sambock.blackjackgame.game.Suit

@Composable
fun CardView(card: Card) {
    val cardColor = if (card.suit == Suit.HEARTS || card.suit == Suit.DIAMONDS) Color.Red else Color.Black
    val suitDrawable = getSuitDrawable(card.suit)
    val rankText = getRankDisplay(card.rank)
    val suitPositions = getSuitPositions(card.rank)

    Box(
        modifier = Modifier
            .size(100.dp, 150.dp) // Card Size
            .clip(RoundedCornerShape(10.dp)) // Rounded edges
            .border(2.dp, Color.Black, RoundedCornerShape(10.dp))
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top-left rank
            Text(
                text = rankText,
                color = cardColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.weight(1f)) // Push elements apart

            // Display structured suit symbols for numbered cards
            if (card.rank.value in 2..10) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    suitPositions.forEach { row ->
                        Row(horizontalArrangement = Arrangement.Center) {
                            row.forEach { shouldDisplay ->
                                if (shouldDisplay) { // Prevents false values from rendering
                                    Image(
                                        painter = painterResource(id = suitDrawable),
                                        contentDescription = "Suit Symbol",
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                // Face cards: Show only one large suit symbol
                Image(
                    painter = painterResource(id = suitDrawable),
                    contentDescription = "Suit Symbol",
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f)) // Push elements apart

            // Bottom-right rank (mirrored)
            Text(
                text = rankText,
                color = cardColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.End)
                    .graphicsLayer(rotationZ = 180f) // Upside-down
            )
        }
    }
}

fun getRankDisplay(rank: Rank): String {
    return when (rank) {
        Rank.ACE -> "A"
        Rank.JACK -> "J"
        Rank.QUEEN -> "Q"
        Rank.KING -> "K"
        else -> rank.value.toString()
    }
}

fun getSuitDrawable(suit: Suit): Int {
    return when (suit) {
        Suit.HEARTS -> R.drawable.ic_heart
        Suit.DIAMONDS -> R.drawable.ic_diamond
        Suit.CLUBS -> R.drawable.ic_club
        Suit.SPADES -> R.drawable.ic_spade
    }
}

fun getSuitPositions(rank: Rank): List<List<Boolean>> {
    return when (rank.value) {
        2 -> listOf(
            listOf(true),
            listOf(true)
        )
        3 -> listOf(
            listOf(true),
            listOf(true),
            listOf(true)
        )
        4 -> listOf(
            listOf(true, true),
            listOf(true, true)
        )
        5 -> listOf(
            listOf(true, false, true),
            listOf(false, true, false),
            listOf(true, false, true)
        )
        6 -> listOf(
            listOf(true, true),
            listOf(true, true),
            listOf(true, true)
        )
        7 -> listOf(
            listOf(false, true, false),
            listOf(true, true, true),
            listOf(false, true, false),
            listOf(true, true)
        )
        8 -> listOf(
            listOf(true, true),
            listOf(true, false, true),
            listOf(true, true),
            listOf(true, false, true),
            listOf(true, true)
        )
        9 -> listOf(
            listOf(true, false, true),
            listOf(true, true, true),
            listOf(false, true, false),
            listOf(true, true, true),
            listOf(true, false, true)
        )
        10 -> listOf(
            listOf(true, true),
            listOf(true, true),
            listOf(true, true),
            listOf(true, true),
            listOf(true, true)
        )
        else -> emptyList() // Face cards display only one large suit symbol
    }
}

fun getSymbolCount(rank: Rank): Int {
    return if (rank.value in 2..10) rank.value else 1
}

@Preview()
@Composable
fun TwoClubs() {
    CardView(card = Card(Suit.CLUBS, Rank.TWO))
}

@Preview()
@Composable
fun SevenSpades() {
    CardView(card = Card(Suit.SPADES, Rank.SEVEN))
}

@Preview()
@Composable
fun AceHearts() {
    CardView(card = Card(Suit.HEARTS, Rank.ACE))
}

@Preview()
@Composable
fun KingDiamonds() {
    CardView(card = Card(Suit.DIAMONDS, Rank.KING))
}