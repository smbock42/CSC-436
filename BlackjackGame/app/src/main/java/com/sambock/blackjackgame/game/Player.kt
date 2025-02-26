package com.sambock.blackjackgame.game

open class Player(startingHands: Int = 1) {
    init {
        require(startingHands in 1..3) { "Player must start 1 to 3 starting hands."}
    }

    val hands: MutableList<Hand> = mutableListOf()

    init {
        repeat(startingHands) {
            hands.add(Hand())
        }
    }

    fun primaryHand(): Hand = hands.first()

    fun splitHand(handIndex: Int): Boolean {
        if (handIndex < 0 || handIndex >= hands.size) return false

        val handToSplit = hands[handIndex]
        val cards = handToSplit.getCards()

        if (cards.size != 2 || cards[0].actualRank() != cards[1].actualRank()) return false

        val removedCard = handToSplit.removeCard(cards[1]) ?: return false

        val newHand = Hand().apply { addCard(removedCard) }
        hands.add(newHand)
        return true
    }
}