package com.sambock.blackjackgame.game

class Player {
    private val primaryHand = Hand()

    fun primaryHand(): Hand = primaryHand

    fun hasBlackjack(): Boolean {
        return primaryHand.isBlackjack()
    }

    fun getHandValue(): Int {
        return primaryHand.getActualScore()
    }
}