package com.sambock.blackjackgame

import com.sambock.blackjackgame.game.*
import org.junit.Assert.*
import org.junit.Test

class DealerTest {
    @Test
    fun testDealerHitsWhenBelowSeventeen() {
        val dealer = Dealer()
        val hand = dealer.primaryHand()

        hand.addCard(Card(Suit.HEARTS, Rank.NINE, true))
        hand.addCard(Card(Suit.SPADES, Rank.SEVEN, true))

        assertTrue("Dealer should hit on 16", dealer.shouldHit())
    }

    @Test
    fun testDealerStandsOnSeventeen() {
        val dealer = Dealer()
        val hand = dealer.primaryHand()

        hand.addCard(Card(Suit.HEARTS, Rank.TEN, true))
        hand.addCard(Card(Suit.SPADES, Rank.SEVEN, true))

        assertFalse("Dealer should stand on 17", dealer.shouldHit())
    }

    @Test
    fun testDealerStandsOnSoftSeventeen() {
        val dealer = Dealer()
        val hand = dealer.primaryHand()

        hand.addCard(Card(Suit.HEARTS, Rank.ACE, true))
        hand.addCard(Card(Suit.SPADES, Rank.SIX, true))

        assertFalse("Dealer should stand on soft 17", dealer.shouldHit())
    }

    @Test
    fun testInitialDealFirstCardHidden() {
        val dealer = Dealer()
        val deck = Deck(shuffle = false)

        dealer.dealInitialCards(deck)

        val cards = dealer.primaryHand().getCards()
        assertFalse("First card should be face-down", cards[0].isRevealed)
        assertTrue("Second card should be face-up", cards[1].isRevealed)
    }

    @Test
    fun testRevealHand() {
        val dealer = Dealer()
        val deck = Deck(shuffle = false)

        dealer.dealInitialCards(deck)
        dealer.revealHand()

        val cards = dealer.primaryHand().getCards()
        assertTrue("All cards should be revealed", cards.all { it.isRevealed })
    }
}