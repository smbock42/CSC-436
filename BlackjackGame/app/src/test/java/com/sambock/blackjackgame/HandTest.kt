package com.sambock.blackjackgame

import com.sambock.blackjackgame.game.Card
import com.sambock.blackjackgame.game.Hand
import com.sambock.blackjackgame.game.Rank
import com.sambock.blackjackgame.game.Suit
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class HandTest {
    @Test
    fun testScoreWithoutAce() {
        val hand = Hand()

        hand.addCard(Card(Suit.HEARTS, Rank.TEN, isFaceUp = true))
        hand.addCard(Card(Suit.SPADES, Rank.SEVEN, isFaceUp = true))

        assertEquals("Actual score should be 17", 17, hand.getActualScore())
        assertEquals("Visible score should be 17", 17, hand.getVisibleScore())
    }

    @Test
    fun testScoreWithAce() {
        val hand = Hand()

        hand.addCard(Card(Suit.HEARTS, Rank.ACE, isFaceUp = true))
        hand.addCard(Card(Suit.SPADES, Rank.SIX, isFaceUp = true))

        assertEquals("Actual score should be 17", 17, hand.getActualScore())
        assertEquals("Visible score should be 17", 17, hand.getVisibleScore())

    }

    @Test
    fun testScoreWithMultipleAces() {
        val hand = Hand()

        hand.addCard(Card(Suit.HEARTS, Rank.ACE, isFaceUp = true))
        hand.addCard(Card(Suit.DIAMONDS, Rank.ACE, isFaceUp = true))
        hand.addCard(Card(Suit.HEARTS, Rank.NINE, isFaceUp = true))

        assertEquals("Actual score should adjust to 21", 21, hand.getActualScore())
        assertEquals("Visible score should be 21", 21, hand.getVisibleScore())
    }

    @Test
    fun testScoreMixedVisibility() {
        val hand = Hand()

        hand.addCard(Card(Suit.HEARTS, Rank.ACE, isFaceUp = false))
        hand.addCard(Card(Suit.DIAMONDS, Rank.NINE, isFaceUp = true))

        assertEquals("Actual score should be to 20", 20, hand.getActualScore())
        assertEquals("Visible score should be to 21", 9, hand.getVisibleScore())

    }

    @Test
    fun testIsBust() {
        val hand = Hand()

        hand.addCard(Card(Suit.HEARTS, Rank.TEN, isFaceUp = true))
        assertFalse("Hand is good", hand.isBust())
        hand.addCard(Card(Suit.DIAMONDS, Rank.KING, isFaceUp = true))
        hand.addCard(Card(Suit.CLUBS, Rank.TWO, isFaceUp = true))
        assertTrue("Hand should be bust", hand.isBust())
    }
}