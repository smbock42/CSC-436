package com.sambock.blackjackgame.ui

import com.sambock.blackjackgame.R
import com.sambock.blackjackgame.game.Card
import com.sambock.blackjackgame.game.Suit
import com.sambock.blackjackgame.game.Rank

// Predefined map of card drawable resources
private val cardResourceMap = mapOf(
    "ah" to R.drawable.card_ah,
    "2h" to R.drawable.card_2h,
    "3h" to R.drawable.card_3h,
    "4h" to R.drawable.card_4h,
    "5h" to R.drawable.card_5h,
    "6h" to R.drawable.card_6h,
    "7h" to R.drawable.card_7h,
    "8h" to R.drawable.card_8h,
    "9h" to R.drawable.card_9h,
    "10h" to R.drawable.card_10h,
    "jh" to R.drawable.card_jh,
    "qh" to R.drawable.card_qh,
    "kh" to R.drawable.card_kh,

    "as" to R.drawable.card_as,
    "2s" to R.drawable.card_2s,
    "3s" to R.drawable.card_3s,
    "4s" to R.drawable.card_4s,
    "5s" to R.drawable.card_5s,
    "6s" to R.drawable.card_6s,
    "7s" to R.drawable.card_7s,
    "8s" to R.drawable.card_8s,
    "9s" to R.drawable.card_9s,
    "10s" to R.drawable.card_10s,
    "js" to R.drawable.card_js,
    "qs" to R.drawable.card_qs,
    "ks" to R.drawable.card_ks,

    "ad" to R.drawable.card_ad,
    "2d" to R.drawable.card_2d,
    "3d" to R.drawable.card_3d,
    "4d" to R.drawable.card_4d,
    "5d" to R.drawable.card_5d,
    "6d" to R.drawable.card_6d,
    "7d" to R.drawable.card_7d,
    "8d" to R.drawable.card_8d,
    "9d" to R.drawable.card_9d,
    "10d" to R.drawable.card_10d,
    "jd" to R.drawable.card_jd,
    "qd" to R.drawable.card_qd,
    "kd" to R.drawable.card_kd,

    "ac" to R.drawable.card_ac,
    "2c" to R.drawable.card_2c,
    "3c" to R.drawable.card_3c,
    "4c" to R.drawable.card_4c,
    "5c" to R.drawable.card_5c,
    "6c" to R.drawable.card_6c,
    "7c" to R.drawable.card_7c,
    "8c" to R.drawable.card_8c,
    "9c" to R.drawable.card_9c,
    "10c" to R.drawable.card_10c,
    "jc" to R.drawable.card_jc,
    "qc" to R.drawable.card_qc,
    "kc" to R.drawable.card_kc,
)

// Function to get the drawable resource ID for a given card
fun getCardDrawable(card: Card): Int {
    val suitLetter = when (card.suit) {
        Suit.HEART -> "h"
        Suit.DIAMOND -> "d"
        Suit.CLUB -> "c"
        Suit.SPADE -> "s"
    }

    val rankLetter = when (card.rank) {
        Rank.ACE -> "a"
        Rank.JACK -> "j"
        Rank.QUEEN -> "q"
        Rank.KING -> "k"
        else -> card.rank.value.toString() // 2-10 remain the same
    }

    val key = "$rankLetter$suitLetter"
    return cardResourceMap[key] ?: R.drawable.card_back // Default to card back if not found
}