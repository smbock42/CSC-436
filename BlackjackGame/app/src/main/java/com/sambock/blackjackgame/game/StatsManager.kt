package com.sambock.blackjackgame.game

import com.sambock.blackjackgame.data.StatsDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class StatsManager(private val statsDataStore: StatsDataStore) {
    private var wins = 0
    private var losses = 0
    private var pushes = 0
    private var blackjacks = 0
    private var busts = 0
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        // Load initial stats
        scope.launch {
            val stats = statsDataStore.statsFlow.first()
            wins = (stats["wins"] as? Number)?.toInt() ?: 0
            losses = (stats["losses"] as? Number)?.toInt() ?: 0
            pushes = (stats["pushes"] as? Number)?.toInt() ?: 0
            blackjacks = (stats["blackjacks"] as? Number)?.toInt() ?: 0
            busts = (stats["busts"] as? Number)?.toInt() ?: 0
        }
    }

    fun recordGameResult(result: GameResult) {
        when (result) {
            GameResult.WIN -> wins++
            GameResult.LOSE -> losses++
            GameResult.PUSH -> pushes++
            GameResult.BLACKJACK -> blackjacks++
            GameResult.BUST -> busts++
        }
        persistStats()
    }

    private fun persistStats() {
        scope.launch {
            statsDataStore.updateStats(
                totalHands = getHandsPlayed(),
                wins = wins,
                losses = losses,
                pushes = pushes,
                blackjacks = blackjacks,
                busts = busts
            )
        }
    }

    fun reset() {
        wins = 0
        losses = 0
        pushes = 0
        blackjacks = 0
        busts = 0
        scope.launch {
            statsDataStore.resetStats()
        }
    }

    fun getWins(): Int = wins
    fun getLosses(): Int = losses
    fun getPushes(): Int = pushes
    fun getBlackjacks(): Int = blackjacks
    fun getBusts(): Int = busts

    fun getHandsPlayed(): Int = wins + losses + pushes
    fun getHandsWon(): Int = wins
    fun getHandsLost(): Int = losses

    fun getWinPercentage(): Double {
        val handsPlayed = getHandsPlayed()
        return if (handsPlayed > 0) wins.toDouble() / handsPlayed * 100 else 0.0
    }
}