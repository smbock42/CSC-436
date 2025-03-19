package com.sambock.blackjackgame.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sambock.blackjackgame.data.ChipDataStore
import com.sambock.blackjackgame.game.BlackjackGame
import com.sambock.blackjackgame.game.Card
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BlackjackViewModel(
    private val chipDataStore: ChipDataStore
) : ViewModel() {
    private val _game = MutableStateFlow(BlackjackGame())
    val game = _game.asStateFlow()

    private var currentChips = 0

    private var _isDealerAnimating = MutableStateFlow(false)
    val isDealerAnimating = _isDealerAnimating.asStateFlow()

    private var _isAnimating = MutableStateFlow(false)
    val isAnimating = _isAnimating.asStateFlow()

    private var _lastDrawnCard = MutableStateFlow<Card?>(null)
    val lastDrawnCard = _lastDrawnCard.asStateFlow()

    private var _isBusted = MutableStateFlow(false)
    val isBusted = _isBusted.asStateFlow()

    init {
        viewModelScope.launch {
            chipDataStore.chipCount.collect { chips ->
                if (currentChips == 0) {
                    currentChips = chips
                    _game.value = BlackjackGame(chips)
                } else {
                    val currentGame = _game.value
                    currentChips = chips
                    val newGame = BlackjackGame(chips)
                    newGame.copyStateFrom(currentGame)
                    _game.value = newGame
                }
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
            _isBusted.value = false
            val newGame = BlackjackGame(currentChips)
            _game.value = newGame
        }
    }

    private fun updateGameState(operation: (BlackjackGame) -> Unit) {
        val currentGame = _game.value
        val previousChips = currentGame.getPlayerChips()
        
        operation(currentGame)

        if (previousChips != currentGame.getPlayerChips()) {
            updateChips(currentGame.getPlayerChips())
        }
        val newGame = BlackjackGame(currentGame.getPlayerChips()).apply {
            copyStateFrom(currentGame)
        }
        _game.value = newGame
    }

    private suspend fun dealerPlay() {
        _isDealerAnimating.value = true
        val currentGame = _game.value
        
        // Step 1: Reveal dealer's hole card first
        val holeCard = currentGame.dealer.primaryHand().getCards().first()
        holeCard.flip()
        _lastDrawnCard.value = holeCard
        updateGameState { }  // Force UI update to show flipped card
        delay(1500)
        _lastDrawnCard.value = null

        // Step 2: Deal cards while dealer should hit
        while (currentGame.shouldDealerHit()) {
            val newCard = currentGame.deck.drawCard().apply { flip() }
            _lastDrawnCard.value = newCard
            // Add card to dealer's hand
            currentGame.dealer.primaryHand().addCard(newCard)
            // Force UI update to show the new card
            _game.value = BlackjackGame(currentGame.getPlayerChips()).apply {
                copyStateFrom(currentGame)
            }
            delay(1500)  // Wait for card animation
            _lastDrawnCard.value = null  // Clear last drawn card
            delay(500)  // Small pause between cards
        }

        // Final step: Show result
        delay(1000)
        updateGameState { game ->
            game.endHand()
        }
        _isDealerAnimating.value = false
    }

    fun startNewHand(betAmount: Int) {
        viewModelScope.launch {
            val currentGame = _game.value
            
            val newGame = BlackjackGame(currentGame.getPlayerChips())
            newGame.copyStateFrom(currentGame)

            if (newGame.startNewHand(betAmount)) {
                updateChips(newGame.getPlayerChips())
                _game.value = newGame

                if (newGame.checkForBlackjack()) {
                    _isDealerAnimating.value = true
                    val holeCard = newGame.dealer.primaryHand().getCards().first()
                    holeCard.flip()
                    _lastDrawnCard.value = holeCard
                    updateGameState { }
                    delay(1500)
                    _lastDrawnCard.value = null
                    _isDealerAnimating.value = false
                    
                    updateGameState { game ->
                        game.endHand()
                    }
                }
            }
        }
    }

    fun hit() {
        viewModelScope.launch {
            _isAnimating.value = true
            val currentGame = _game.value
            val card = currentGame.deck.drawCard().apply { flip() }
            _lastDrawnCard.value = card
            currentGame.addCardToPlayer(card)
            
            updateGameState { }  // Update UI to show new card
            delay(1500)  // Show the card
            _lastDrawnCard.value = null

            if (currentGame.getPlayerHand().isBust()) {
                _isBusted.value = true
                delay(1500)  // Extra delay to see the bust
                updateGameState { game ->
                    game.endHand()
                }
            }
            _isAnimating.value = false
        }
    }

    fun stand() {
        viewModelScope.launch {
            dealerPlay()
        }
    }

    fun double() {
        viewModelScope.launch {
            _isAnimating.value = true
            val currentGame = _game.value

            // First double the bet
            if (!currentGame.doubleBet()) {
                _isAnimating.value = false
                return@launch
            }
            
            // Draw and show the double down card
            val card = currentGame.deck.drawCard().apply { flip() }
            _lastDrawnCard.value = card
            currentGame.addCardToPlayer(card)
            updateGameState { }  // Update UI with new card
            delay(1500)  // Show the doubled card
            
            _lastDrawnCard.value = null
            _isAnimating.value = false

            if (currentGame.getPlayerHand().isBust()) {
                _isBusted.value = true
                delay(1500)  // Show the bust before ending
                updateGameState { game ->
                    game.endHand()
                }
            } else {
                // If not bust, dealer plays
                dealerPlay()
            }
        }
    }

    fun getStats() = _game.value.getStatsManager()

    fun topUpChips() {
        viewModelScope.launch {
            val newChips = 1000
            chipDataStore.saveChipCount(newChips)
            val currentGame = _game.value
            val newGame = BlackjackGame(newChips)
            newGame.copyStateFrom(currentGame)
            _game.value = newGame
        }
    }
}