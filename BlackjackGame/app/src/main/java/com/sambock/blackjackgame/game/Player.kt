package com.sambock.blackjackgame.game

open class Player {
    val hand = Hand()

    fun draw(deck: Deck) {
        val card = deck.drawCard()
        card?.let { hand.addCard(it) }
    }

    fun hasBlackjack(): Boolean = hand.getScore() == 21
}