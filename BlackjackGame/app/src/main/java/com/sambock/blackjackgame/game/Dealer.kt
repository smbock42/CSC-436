package com.sambock.blackjackgame.game

class Dealer {
    private val primaryHand = Hand()

    fun primaryHand(): Hand = primaryHand

    // Dealer must hit on 16 and below, stand on 17 and above
    fun shouldHit(): Boolean {
        val hand = primaryHand()
        return hand.getActualScore() < 17
    }

    // Get first card face-down, rest face-up
    fun dealInitialCards(deck: Deck) {
        val hand = primaryHand()
        val firstCard = deck.drawCard() // First card face-down
        val secondCard = deck.drawCard().apply { flip() } // Second card face-up

        hand.addCard(firstCard)
        hand.addCard(secondCard)
    }

    // Reveal first card
    fun revealHand() {
        primaryHand().getCards().forEach { card ->
            if (!card.isRevealed) card.flip()
        }
    }
}