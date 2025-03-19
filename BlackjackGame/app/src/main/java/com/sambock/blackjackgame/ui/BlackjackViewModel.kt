package com.sambock.blackjackgame.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sambock.blackjackgame.data.ChipDataStore
import com.sambock.blackjackgame.game.BlackjackGame
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BlackjackViewModel(
    private val chipDataStore: ChipDataStore
) : ViewModel() {
    private val _game = MutableStateFlow(BlackjackGame())
    val game = _game.asStateFlow()

    private var currentChips = 1000

    init {
        viewModelScope.launch {
            chipDataStore.chipCount.collect { chips ->
                currentChips = chips
                val currentGame = _game.value
                val newGame = BlackjackGame(chips)
                newGame.copyStateFrom(currentGame)
                _game.value = newGame
            }
        }
    }

    private fun updateChips(newCount: Int) {
        viewModelScope.launch {
            chipDataStore.saveChipCount(newCount)
            currentChips = newCount
        }
    }

    fun resetGame() {
        viewModelScope.launch {
            val newGame = BlackjackGame(currentChips)
            _game.value = newGame
        }
    }

    fun updateGameState(operation: (BlackjackGame) -> Unit) {
        viewModelScope.launch {
            val currentGame = _game.value
            val previousChips = currentGame.getPlayerChips()
            
            operation(currentGame)

            // Save chips if they changed
            if (previousChips != currentGame.getPlayerChips()) {
                updateChips(currentGame.getPlayerChips())
            }
            val newGame = BlackjackGame(currentGame.getPlayerChips()).apply {
                copyStateFrom(currentGame)
            }
            _game.value = newGame
        }
    }

    fun startNewHand(betAmount: Int) {
        viewModelScope.launch {
            val currentGame = _game.value
            println("ViewModel: Creating new game with chips: $currentChips")
            
            val newGame = BlackjackGame(currentGame.getPlayerChips())
            newGame.copyStateFrom(currentGame)

            if (newGame.startNewHand(betAmount)) {
                println("ViewModel: Hand started successfully")
                // Update chip count first
                updateChips(newGame.getPlayerChips())
                
                _game.value = newGame
            } else {
                println("ViewModel: Failed to start new hand")
            }
        }
    }

    fun hit() {
        updateGameState { game ->
            game.hit()
        }
    }

    private var _isDealerAnimating = MutableStateFlow(false)
    val isDealerAnimating = _isDealerAnimating.asStateFlow()

    fun stand() {
        viewModelScope.launch {
            _isDealerAnimating.value = true
            val currentGame = _game.value
            
            // Reveal dealer cards with animation
            currentGame.dealer.primaryHand().getCards().forEach { card ->
                delay(500)
                card.flip()
                _game.value = BlackjackGame(currentGame.getPlayerChips()).apply {
                    copyStateFrom(currentGame)
                }
            }

            // Execute stand action in a new coroutine to handle suspension
            viewModelScope.launch {
                updateGameState { game ->
                    game.stand()
                }
            }

            delay(2000)
            _isDealerAnimating.value = false
        }
    }

    fun double() {
        updateGameState { game ->
            game.double()
        }
    }

    fun getStats() = _game.value.getStatsManager()

    fun topUpChips() {
        viewModelScope.launch {
            chipDataStore.saveChipCount(1000)
        }
    }
}