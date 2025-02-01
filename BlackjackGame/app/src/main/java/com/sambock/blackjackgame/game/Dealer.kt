package com.sambock.blackjackgame.game

class Dealer : Player(){
    fun play(deck: Deck) {
        while (hand.getScore() < 17) {
            draw(deck)
        }
    }
}