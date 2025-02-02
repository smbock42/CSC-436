package com.sambock.blackjackgame.game

enum class Suit { HEARTS, DIAMONDS, CLUBS, SPADES }

enum class Rank(val value: Int) {
    TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8),
    NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10), ACE(11)
}


data class Card (val suit: Suit, val rank: Rank, var isFaceUp: Boolean = false){
    override fun toString(): String = "${rank.name} of ${suit.name}"
}