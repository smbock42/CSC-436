package com.sambock.blackjackgame.game

sealed class GameState {
    object Betting : GameState()
    data class Playing(
        val currentBet: Int,
        val canHit: Boolean = true,
        val canStand: Boolean = true,
        val canDouble: Boolean = true,
        val canSplit: Boolean = false
    ) : GameState()
    object Busting : GameState()
    data class Complete(val result: GameResult, val winAmount: Int) : GameState()
}