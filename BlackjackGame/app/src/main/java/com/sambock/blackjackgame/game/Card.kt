package com.sambock.blackjackgame.game

enum class Suit { HEARTS, DIAMONDS, CLUBS, SPADES }

enum class Rank(val value: Int) {
    TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6),
    SEVEN(7), EIGHT(8), NINE(9), TEN(10),
    JACK(10), QUEEN(10), KING(10), ACE(11)
}


data class Card (private val suit: Suit, private val rank: Rank, private var isFaceUp: Boolean = false){

    val isRevealed: Boolean
        get() = isFaceUp

    fun flip() {
        isFaceUp = !isFaceUp
    }

    fun getRank(): Rank? = if (isFaceUp) rank else null

    fun getSuit(): Suit? = if (isFaceUp) suit else null

    fun shortName(): String {
        return if (isFaceUp) {
            val rankStr = when (rank) {
                Rank.TEN -> "10"
                Rank.JACK -> "J"
                Rank.QUEEN -> "Q"
                Rank.KING -> "K"
                Rank.ACE -> "A"
                else -> rank.value.toString()
            }
            val suitStr = when (suit) {
                Suit.HEARTS -> "H"
                Suit.DIAMONDS -> "D"
                Suit.CLUBS -> "C"
                Suit.SPADES -> "S"
            }
            "$rankStr$suitStr"

        } else {
            "XX"
        }
    }

    override fun toString(): String {
        return if (isFaceUp) {
            "${rank.name} of ${suit.name}"
        } else {
            "Face Down Card"
        }
    }

    internal fun actualRank(): Rank = rank
}