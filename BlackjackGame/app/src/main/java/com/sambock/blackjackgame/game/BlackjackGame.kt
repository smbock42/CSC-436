package com.sambock.blackjackgame.game

class BlackjackGame(
    private val initialChips: Int = 1000
) {
    private var chipManager = ChipManager(initialChips)
    private var statsManager = StatsManager()
    val deck = Deck()
    private val player = Player()
    val dealer = Dealer()
    private var currentState: GameState = GameState.Betting
    private var currentBet: Int = 0

    fun startNewHand(betAmount: Int): Boolean {
        if (!chipManager.placeBet(betAmount)) {
            return false
        }

        currentBet = betAmount
        deck.resetDeck(shuffle = true)

        // Clear hands
        player.primaryHand().clear()
        dealer.primaryHand().clear()

        // Deal initial cards
        dealer.primaryHand().addCard(deck.drawCard()) // Face down
        player.primaryHand().addCard(deck.drawCard().apply { flip() })
        dealer.primaryHand().addCard(deck.drawCard().apply { flip() })
        player.primaryHand().addCard(deck.drawCard().apply { flip() })

        currentState = GameState.Playing(
            currentBet = betAmount,
            canDouble = true,
            canSplit = player.primaryHand().canSplit()
        )

        return true
    }

    // Added for animation control
    fun addCardToPlayer(card: Card) {
        player.primaryHand().addCard(card)
    }

    fun addCardToDealer(card: Card) {
        dealer.primaryHand().addCard(card)
    }

    fun doubleBet(): Boolean {
        if (!chipManager.placeBet(currentBet)) return false
        currentBet *= 2
        return true
    }

    fun hit() {
        if (currentState !is GameState.Playing) return

        val card = deck.drawCard()
        card.flip()
        player.primaryHand().addCard(card)

        if (player.primaryHand().isBust()) {
            currentState = GameState.Busting(currentBet)
            endHand()
        }
    }

    fun stand() {
        if (currentState !is GameState.Playing) return
        endHand()
    }

    fun double() {
        if (currentState !is GameState.Playing) return
        if (!chipManager.placeBet(currentBet)) return
        currentBet *= 2
    }

    fun endHand() {
        val playerHand = player.primaryHand()
        val dealerHand = dealer.primaryHand()

        dealer.revealHand()

        val result = when {
            playerHand.isBust() -> GameResult.BUST
            playerHand.isBlackjack() && !dealerHand.isBlackjack() -> GameResult.BLACKJACK
            !playerHand.isBust() && dealerHand.isBust() -> GameResult.WIN
            !playerHand.isBust() && playerHand.getActualScore() > dealerHand.getActualScore() -> GameResult.WIN
            playerHand.getActualScore() == dealerHand.getActualScore() -> GameResult.PUSH
            else -> GameResult.LOSE
        }

        val winAmount = when (result) {
            GameResult.BLACKJACK -> chipManager.calculateWinnings(currentBet, true)
            GameResult.WIN -> chipManager.calculateWinnings(currentBet, false)
            GameResult.PUSH -> currentBet
            else -> 0 // LOSE or BUST
        }

        if (winAmount > 0) {
            chipManager.addWinnings(winAmount)
        }

        statsManager.recordGameResult(result, currentBet, chipManager.getCurrentChips())
        
        currentState = GameState.Complete(result, winAmount)
    }

    fun checkForBlackjack(): Boolean {
        return player.primaryHand().isBlackjack()
    }

    fun getCurrentState() = currentState
    
    fun copyStateFrom(other: BlackjackGame) {
        chipManager.copyFrom(other.chipManager)
        statsManager = other.statsManager
        currentBet = other.currentBet
        
        when (val otherState = other.currentState) {
            is GameState.Playing -> {
                currentState = GameState.Playing(
                    currentBet = otherState.currentBet,
                    canDouble = otherState.canDouble,
                    canSplit = otherState.canSplit
                )
                
                dealer.primaryHand().clear()
                other.dealer.primaryHand().getCards().forEach { card ->
                    dealer.primaryHand().addCard(card.copyWithState())
                }
                
                player.primaryHand().clear()
                other.player.primaryHand().getCards().forEach { card ->
                    player.primaryHand().addCard(card.copyWithState())
                }
            }
            is GameState.Complete -> {
                currentState = GameState.Complete(
                    result = otherState.result,
                    winAmount = otherState.winAmount
                )
            }
            is GameState.Betting -> {
                currentState = GameState.Betting
            }
            is GameState.Busting -> {
                currentState = GameState.Busting(otherState.currentBet)
            }
        }
    }

    fun shouldDealerHit(): Boolean = dealer.shouldHit()
    fun getPlayerChips() = chipManager.getCurrentChips()
    fun getPlayerHand() = player.primaryHand()
    fun getDealerHand() = dealer.primaryHand()
    fun getStatsManager() = statsManager
}

fun ChipManager.copyFrom(other: ChipManager) {
    chipCount = other.getCurrentChips()
}