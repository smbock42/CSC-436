package com.sambock.blackjackgame.game

data class ChipHistoryEntry(val chipCount: Int, val timestamp: Long = System.currentTimeMillis())

class StatsManager {
    private var wins = 0
    private var losses = 0
    private var pushes = 0
    private var blackjacks = 0
    private var busts = 0
    private val chipHistory = mutableListOf<ChipHistoryEntry>()

    fun recordGameResult(result: GameResult, betAmount: Int, finalChips: Int) {
        when (result) {
            GameResult.WIN -> wins++
            GameResult.LOSE -> losses++
            GameResult.PUSH -> pushes++
            GameResult.BLACKJACK -> blackjacks++
            GameResult.BUST -> busts++
        }
        chipHistory.add(ChipHistoryEntry(finalChips))
    }

    fun getWins(): Int = wins
    fun getLosses(): Int = losses
    fun getPushes(): Int = pushes
    fun getBlackjacks(): Int = blackjacks
    fun getBusts(): Int = busts
    fun getChipHistory(): List<ChipHistoryEntry> = chipHistory

    fun getHandsPlayed(): Int = wins + losses + pushes
    fun getHandsWon(): Int = wins
    fun getHandsLost(): Int = losses

    fun getWinPercentage(): Double {
        val handsPlayed = getHandsPlayed()
        return if (handsPlayed > 0) wins.toDouble() / handsPlayed * 100 else 0.0
    }

    fun getTotalWinnings(): Int {
        return chipHistory.sumOf { if (it.chipCount > 1000) it.chipCount - 1000 else 0 }
    }

    fun getTotalLosses(): Int {
        return chipHistory.sumOf { if (it.chipCount < 1000) 1000 - it.chipCount else 0 }
    }

    fun getNetProfit(): Int {
        return getTotalWinnings() - getTotalLosses()
    }

    fun getLargestWin(): Int {
        return chipHistory.maxOfOrNull { it.chipCount } ?: 0
    }

    fun getLargestLoss(): Int {
        return chipHistory.minOfOrNull { it.chipCount } ?: 0
    }
}