package com.sambock.blackjackgame.game

class BlackjackGame(
    private val initialChips: Int = 1000
) {
    private var chipManager = ChipManager(initialChips)
    private var statsManager = StatsManager()
    private val deck = Deck()
    private val player = Player()
    val dealer = Dealer()
    private var currentState: GameState = GameState.Betting
    private var currentBet: Int = 0

    fun startNewHand(betAmount: Int): Boolean {
        println("BlackjackGame: Starting new hand with bet: $betAmount")
        println("BlackjackGame: Current chips before bet: ${chipManager.getCurrentChips()}")
        
        if (!chipManager.placeBet(betAmount)) {
            println("BlackjackGame: Failed to place bet")
            return false
        }

        println("BlackjackGame: Bet placed successfully")
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

        println("BlackjackGame: Initial deal complete")
        println("BlackjackGame: Player hand: ${player.primaryHand().getCards().size} cards")
        println("BlackjackGame: Dealer hand: ${dealer.primaryHand().getCards().size} cards")

        currentState = GameState.Playing(
            currentBet = betAmount,
            canDouble = true,
            canSplit = player.primaryHand().canSplit()
        )
        println("BlackjackGame: New state set to: $currentState")

        if (player.primaryHand().isBlackjack()) {
            println("BlackjackGame: Player has blackjack")
            endHand()
        }

        println("BlackjackGame: Chips after hand start: ${chipManager.getCurrentChips()}")
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

        dealer.revealHand()
        while (dealer.shouldHit()) {
            val card = deck.drawCard()
            card.flip()
            dealer.primaryHand().addCard(card)
        }

        endHand()
    }

    fun double() {
        if (currentState !is GameState.Playing) return
        if (!chipManager.placeBet(currentBet)) return

        currentBet *= 2
        val card = deck.drawCard()
        card.flip()
        player.primaryHand().addCard(card)

        if (player.primaryHand().isBust()) {
            currentState = GameState.Busting(currentBet)
            endHand()
        } else {
            stand()
        }
    }

    fun endHand() {
        val playerHand = player.primaryHand()
        val dealerHand = dealer.primaryHand()

        dealer.revealHand() // Make sure dealer's cards are revealed for the final result

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

        // Record game statistics
        statsManager.recordGameResult(result, currentBet, chipManager.getCurrentChips())
        
        currentState = GameState.Complete(result, winAmount)
    }

    fun getCurrentState() = currentState
    
    // Helper method to copy another game's state
    fun copyStateFrom(other: BlackjackGame) {
        // Copy chip state
        chipManager.copyFrom(other.chipManager)
        statsManager = other.statsManager
        currentBet = other.currentBet
        
        // Copy current state
        when (val otherState = other.currentState) {
            is GameState.Playing -> {
                currentState = GameState.Playing(
                    currentBet = otherState.currentBet,
                    canDouble = otherState.canDouble,
                    canSplit = otherState.canSplit
                )
                
                // Copy dealer's hand
                dealer.primaryHand().clear()
                other.dealer.primaryHand().getCards().forEach { card ->
                    dealer.primaryHand().addCard(card.copyWithState())
                }
                
                // Copy player's hand
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
    fun getPlayerChips() = chipManager.getCurrentChips()
    fun getPlayerHand() = player.primaryHand()
    fun getDealerHand() = dealer.primaryHand()
    fun getStatsManager() = statsManager
}

fun ChipManager.copyFrom(other: ChipManager) {
    chipCount = other.getCurrentChips()
}