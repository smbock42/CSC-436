package com.sambock.blackjackgame.game

class Dealer : Player(){
    fun play(deck: Deck) {
        while (hand.getActualScore() < 17) {
            draw(deck)
        }
    }
}