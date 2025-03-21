package com.sambock.blackjackgame.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sambock.blackjackgame.data.ChipDataStore
import com.sambock.blackjackgame.data.StatsDataStore
import com.sambock.blackjackgame.game.BlackjackGame
import com.sambock.blackjackgame.game.Card
import com.sambock.blackjackgame.sound.SoundManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BlackjackViewModel(
    private val chipDataStore: ChipDataStore,
    private val statsDataStore: StatsDataStore,
    context: Context
) : ViewModel() {
    private val _game = MutableStateFlow(BlackjackGame(statsDataStore))
    val game = _game.asStateFlow()
    private val soundManager = SoundManager(context)

    private var currentChips = 0

    private var _isDealerAnimating = MutableStateFlow(false)
    val isDealerAnimating = _isDealerAnimating.asStateFlow()

    private var _isAnimating = MutableStateFlow(false)
    val isAnimating = _isAnimating.asStateFlow()

    private var _lastDrawnCard = MutableStateFlow<Card?>(null)
    val lastDrawnCard = _lastDrawnCard.asStateFlow()

    private var _isBusted = MutableStateFlow(false)
    val isBusted = _isBusted.asStateFlow()

    private var _soundEnabled = MutableStateFlow(true)
    val soundEnabled = _soundEnabled.asStateFlow()

    init {
        viewModelScope.launch {
            chipDataStore.chipCount.collect { chips ->
                if (currentChips == 0) {
                    currentChips = chips
                    _game.value = BlackjackGame(statsDataStore, chips)
                } else {
                    val currentGame = _game.value
                    currentChips = chips
                    val newGame = BlackjackGame(statsDataStore, chips)
                    newGame.copyStateFrom(currentGame)
                    _game.value = newGame
                }
            }
        }
    }

    fun setSoundEnabled(enabled: Boolean) {
        _soundEnabled.value = enabled
        soundManager.setSoundEnabled(enabled)
    }

    private fun checkWinAndPlaySound(playerHand: com.sambock.blackjackgame.game.Hand, dealerHand: com.sambock.blackjackgame.game.Hand) {
        when {
            playerHand.isBust() -> soundManager.playLoseSound()
            dealerHand.isBust() -> soundManager.playWinSound()
            playerHand.isBlackjack() && !dealerHand.isBlackjack() -> soundManager.playWinSound()
            !playerHand.isBlackjack() && dealerHand.isBlackjack() -> soundManager.playLoseSound()
            playerHand.getActualScore() > dealerHand.getActualScore() -> soundManager.playWinSound()
            playerHand.getActualScore() < dealerHand.getActualScore() -> soundManager.playLoseSound()
            // Push (tie) - no sound
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
            val newGame = BlackjackGame(statsDataStore, currentChips)
            _game.value = newGame
        }
    }

    fun resetStats() {
        viewModelScope.launch {
            _game.value.getStatsManager().reset()
        }
    }

    private fun updateGameState(operation: (BlackjackGame) -> Unit) {
        val currentGame = _game.value
        val previousChips = currentGame.getPlayerChips()
        
        operation(currentGame)

        if (previousChips != currentGame.getPlayerChips()) {
            updateChips(currentGame.getPlayerChips())
        }
        val newGame = BlackjackGame(statsDataStore, currentGame.getPlayerChips()).apply {
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
        soundManager.playDealSound()
        updateGameState { }  // Force UI update to show flipped card
        delay(1500)
        _lastDrawnCard.value = null

        // Step 2: Deal cards while dealer should hit
        while (currentGame.shouldDealerHit()) {
            val newCard = currentGame.deck.drawCard().apply { flip() }
            _lastDrawnCard.value = newCard
            soundManager.playDealSound()
            // Add card to dealer's hand
            currentGame.dealer.primaryHand().addCard(newCard)
            // Force UI update to show the new card
            _game.value = BlackjackGame(statsDataStore, currentGame.getPlayerChips()).apply {
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
        checkWinAndPlaySound(currentGame.getPlayerHand(), currentGame.dealer.primaryHand())
        _isDealerAnimating.value = false
    }

    fun startNewHand(betAmount: Int) {
        viewModelScope.launch {
            val currentGame = _game.value
            
            val newGame = BlackjackGame(statsDataStore, currentGame.getPlayerChips())
            newGame.copyStateFrom(currentGame)

            if (newGame.startNewHand(betAmount)) {
                updateChips(newGame.getPlayerChips())
                _game.value = newGame
                soundManager.playDealSound()

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
                    checkWinAndPlaySound(newGame.getPlayerHand(), newGame.dealer.primaryHand())
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
            soundManager.playDealSound()
            currentGame.addCardToPlayer(card)
            
            updateGameState { }  // Update UI to show new card
            delay(1500)  // Show the card
            _lastDrawnCard.value = null

            if (currentGame.getPlayerHand().isBust()) {
                _isBusted.value = true
                soundManager.playLoseSound()
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
            soundManager.playDealSound()
            currentGame.addCardToPlayer(card)
            updateGameState { }  // Update UI with new card
            delay(1500)  // Show the doubled card
            
            _lastDrawnCard.value = null
            _isAnimating.value = false

            if (currentGame.getPlayerHand().isBust()) {
                _isBusted.value = true
                soundManager.playLoseSound()
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
            val newGame = BlackjackGame(statsDataStore, newChips)
            newGame.copyStateFrom(currentGame)
            _game.value = newGame
        }
    }

    override fun onCleared() {
        super.onCleared()
        soundManager.release()
    }
}