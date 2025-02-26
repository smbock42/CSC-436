package com.sambock.blackjackgame.game

class Hand {
    private val cards = mutableListOf<Card>()

    fun addCard(card: Card) {
        cards.add(card)
    }

    internal fun getActualScore(): Int {
        var score = cards.sumOf { it.actualRank().value }
        var aces = cards.count{ it.actualRank() == Rank.ACE }

        while (score > 21 && aces > 0) {
            score -= 10
            aces --
        }
        return score
    }

    fun getVisibleScore(): Int {
        val visibleCards = cards.filter { it.isRevealed }
        var score = visibleCards.sumOf { it.actualRank().value }
        var aces = visibleCards.count { it.actualRank() == Rank.ACE }
        while (score > 21 && aces > 0) {
            score -= 10
            aces --
        }
        return score
    }


    fun isBust(): Boolean = getActualScore() > 21

    override fun toString(): String = cards.joinToString(", ")
}