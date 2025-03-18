package com.sambock.blackjackgame.game

class ChipManager(private var initialChips: Int = 1000) {
    var chipCount = initialChips

    fun getCurrentChips(): Int = chipCount

    fun placeBet(amount: Int): Boolean {
        return if (amount <= chipCount) {
            chipCount -= amount
            true
        } else false
    }

    fun addWinnings(amount: Int) {
        chipCount += amount
    }

    fun calculateWinnings(bet: Int, isBlackjack: Boolean): Int {
        return if (isBlackjack) {
            (bet * 2.5).toInt() // Blackjack pays 3:2
        } else {
            bet * 2 // Regular win pays 1:1
        }
    }
}