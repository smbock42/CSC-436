package com.sambock.blackjackgame.game

class Hand {
    private val cards = mutableListOf<Card>()

    fun addCard(card: Card) {
        cards.add(card)
    }

    fun getScore(): Int {
        var score = cards.sumOf { it.getRank()!!.value }
        var aces = cards.count { it.getRank() == Rank.ACE}

        while (score > 21 && aces > 0) {
            score -= 10
            aces --
        }

        return score
    }

    fun isBust(): Boolean = getScore() > 21

    override fun toString(): String = cards.joinToString(", ")
}