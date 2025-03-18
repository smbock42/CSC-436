package com.sambock.blackjackgame.game

class Hand {
    private val cards = mutableListOf<Card>()

    fun addCard(card: Card) {
        cards.add(card)
    }

    fun getCards(): List<Card> = cards.toList()

    fun removeCard(card: Card): Card? {
        val index = cards.indexOf(card)
        return if (index != -1) {
            cards.removeAt(index)
        } else {
            null
        }
    }

    internal fun getActualScore(): Int {
        var score = cards.sumOf { it.rank.value }
        var aces = cards.count{ it.rank == Rank.ACE }

        while (score > 21 && aces > 0) {
            score -= 10
            aces --
        }
        return score
    }

    fun getVisibleScore(): Int {
        val visibleCards = cards.filter { it.isRevealed }
        var score = visibleCards.sumOf { it.rank.value }
        var aces = visibleCards.count { it.rank == Rank.ACE }
        while (score > 21 && aces > 0) {
            score -= 10
            aces --
        }
        return score
    }

    fun canSplit(): Boolean {
        return cards.size == 2 && cards[0].rank == cards[1].rank
    }

    fun isBlackjack(): Boolean {
        return cards.size == 2 && getActualScore() == 21
    }

    fun clear() {
        cards.clear()
    }

    fun isBust(): Boolean {
        return getActualScore() > 21
    }

    override fun toString(): String = cards.joinToString(", ")
}