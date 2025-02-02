package com.sambock.blackjackgame.game

import com.sambock.blackjackgame.ui.getAllCards

class BlackjackGame {
    private val deck = mutableListOf<Card>()
    private val playerHand = mutableListOf<Card>()
    private val dealerHand = mutableListOf<Card>()

    init {
        resetGame()
    }

    fun resetGame() {
        deck.clear()
        deck.addAll(getAllCards().shuffled()) // Shuffle deck
        playerHand.clear()
        dealerHand.clear()

        // Deal two cards each
        playerHand.add(drawCard())
        playerHand.add(drawCard())
        dealerHand.add(drawCard())
        dealerHand.add(drawCard())
    }

    fun drawCard(): Card {
        return if (deck.isNotEmpty()) deck.removeAt(0) else throw IllegalStateException("Deck is empty!")
    }

    fun getPlayerHand(): List<Card> = playerHand
    fun getDealerHand(): List<Card> = dealerHand
    fun getDeckSize(): Int = deck.size

    fun calculateHandValue(hand: List<Card>): Int {
        var total = 0
        var aces = 0

        for (card in hand) {
            total += when (card.rank) {
                Rank.ACE -> {
                    aces += 1
                    11
                }
                Rank.JACK, Rank.QUEEN, Rank.KING -> 10
                else -> card.rank.value
            }
        }

        // Adjust Aces from 11 → 1 if needed
        while (total > 21 && aces > 0) {
            total -= 10
            aces -= 1
        }

        return total
    }

    fun playerHits() {
        playerHand.add(drawCard())
    }

    fun dealerPlays() {
        while (calculateHandValue(dealerHand) < 17) {
            dealerHand.add(drawCard())
        }
    }

    fun checkGameResult(): String {
        val playerTotal = calculateHandValue(playerHand)
        val dealerTotal = calculateHandValue(dealerHand)

        return when {
            playerTotal == 21 -> "Blackjack! Player Wins!"
            playerTotal > 21 -> "Player Busts! Dealer Wins!"
            dealerTotal > 21 -> "Dealer Busts! Player Wins!"
            playerTotal > dealerTotal -> "Player Wins!"
            playerTotal < dealerTotal -> "Dealer Wins!"
            else -> "It's a Tie!"
        }
    }
}