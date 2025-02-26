package com.sambock.blackjackgame.game

class Deck(
    private val numberOfDecks: Int = 1,
    private val shuffle: Boolean = true
) {
    private val cards = mutableListOf<Card>()

    init {
        resetDeck(shuffle)
    }

    fun resetDeck(shuffle: Boolean = this.shuffle) {
        create()
        if (shuffle) {
            shuffle()
        }
    }

    private fun create() {
        destroyDeck()
        repeat(numberOfDecks) {
            for (suit in Suit.entries) {
                for (rank in Rank.entries) {
                    cards.add(Card(suit, rank))
                }
            }
        }
    }

    private fun shuffle() {
        cards.shuffle()
    }

    fun drawCard(): Card {
        return if (cards.isNotEmpty()) {
            cards.removeAt(0)
        } else {
            throw IllegalStateException("Deck is empty!")
        }
    }

    private fun destroyDeck() {
        cards.clear()
    }

    fun remainingCards(): Int = cards.size

    internal fun getCards(): List<Card> = cards.toList()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Deck) return false

        if (this.cards.size != other.cards.size) return false

        val orderEqual = this.cards == other.cards

        val frequencyEqual = this.cards.groupingBy { it }.eachCount() ==
                other.cards.groupingBy { it }.eachCount()

        return orderEqual && frequencyEqual
    }

    override fun hashCode(): Int {
        val frequencyHash = cards.groupingBy { it }.eachCount().hashCode()
        return 31 * cards.hashCode() + frequencyHash
    }
}