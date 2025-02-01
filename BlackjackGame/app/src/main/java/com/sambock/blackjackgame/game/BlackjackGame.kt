package com.sambock.blackjackgame.game

class BlackjackGame {
    private val deck = Deck()
    private val player = Player()
    private val dealer = Dealer()

    fun startGame() {
        player.draw(deck)
        dealer.draw(deck)
        player.draw(deck)
        dealer.draw(deck)

        println("Player Hand: ${player.hand} | Score: ${player.hand.getScore()}")
        println("Dealer Hand: ${dealer.hand} (One card hidden)")
    }

    fun playerHit() {
        player.draw(deck)
        println("Player Hand: ${player.hand} | Score: ${player.hand.getScore()}")

        if (player.hand.isBust()) {
            println("Bust! You lose.")
        }
    }

    fun playerStand() {
        dealer.play(deck)
        println("Dealer Hand: ${dealer.hand} | Score: ${dealer.hand.getScore()}")

        when {
            dealer.hand.isBust() -> println("Dealer busts! You win!")
            player.hand.getScore() > dealer.hand.getScore() -> println("You win!")
            player.hand.getScore() < dealer.hand.getScore() -> println("Dealer wins!")
            else -> println("It's a tie!")
        }
    }
}