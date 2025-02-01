package com.sambock.blackjackgame.game

class Deck {
    private val cards = mutableListOf<Card>()

    init {
        resetDeck()
    }

    private fun resetDeck() {
        cards.clear()
        for (suit in Suit.entries) {
            for (rank in Rank.entries) {
                cards.add(Card(suit, rank))
            }
        }
        cards.shuffle()
    }

    fun drawCard(): Card? = if (cards.isNotEmpty()) cards.removeAt(0) else null

    fun remainingCards(): Int = cards.size
}