package com.sambock.blackjackgame

import com.sambock.blackjackgame.game.Card
import com.sambock.blackjackgame.game.Player
import com.sambock.blackjackgame.game.Rank
import com.sambock.blackjackgame.game.Suit
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class PlayerTest {
    @Test
    fun testPlayerStartsWithOneHand() {
        val player = Player()
        assertEquals("Player should start with one hand", 1, player.hands.size)
    }

    @Test
    fun testPlayerStartsWithTwoHands() {
        val player = Player(startingHands = 2)
        assertEquals("Player should start with two hands", 2, player.hands.size)
    }

    @Test
    fun testPlayerStartsWithThreeHands() {
        val player = Player(startingHands = 3)
        assertEquals("Player should start with three hands", 3, player.hands.size)
    }

    @Test
    fun testSuccessfulSplit() {
        val player = Player(startingHands = 1)
        val hand = player.primaryHand()

        val card1 = Card(Suit.HEARTS, Rank.ACE, isFaceUp = true)
        val card2 = Card(Suit.CLUBS, Rank.ACE, isFaceUp = true)
        hand.addCard(card1)
        hand.addCard(card2)

        assertEquals("Before split, there should be 1 hand", 1, player.hands.size)

        val result = player.splitHand(0)
        assertTrue("Split should be successful", result)
        assertEquals("After splitting, player should have 2 hands", 2, player.hands.size)
        assertEquals("Original hand should have 1 card after split", 1, player.hands[0].getCards().size)
        assertEquals("New hand should have 1 card after split", 1, player.hands[1].getCards().size)
        val newHandCard = player.hands[1].getCards().first()
        assertEquals("Card in new hand should be an Ace", Rank.ACE, newHandCard.actualRank())
    }

    @Test
    fun testUnsuccessfulSplitDifferentRanks() {
        val player = Player(startingHands = 1)
        val hand = player.primaryHand()

        val card1 = Card(Suit.HEARTS, Rank.ACE, isFaceUp = true)
        val card2 = Card(Suit.CLUBS, Rank.KING, isFaceUp = true)
        hand.addCard(card1)
        hand.addCard(card2)

        val result = player.splitHand(0)
        assertFalse("Split should fail because cards have different ranks", result)
        assertEquals("Player should still have one hand", 1, player.hands.size)
    }

    @Test
    fun testUnsuccessfulSplitMoreThanTwoCards() {
        val player = Player(startingHands = 1)
        val hand = player.primaryHand()

        val card1 = Card(Suit.HEARTS, Rank.QUEEN, isFaceUp = true)
        val card2 = Card(Suit.CLUBS, Rank.QUEEN, isFaceUp = true)
        val card3 = Card(Suit.DIAMONDS, Rank.TWO, isFaceUp = true)
        hand.addCard(card1)
        hand.addCard(card2)
        hand.addCard(card3)

        val result = player.splitHand(0)
        assertFalse("Split should fail because the hand has more than two cards", result)
        assertEquals("Player should still have one hand", 1, player.hands.size)
    }

    @Test
    fun testMultipleSplits() {
        val player = Player(startingHands = 2)

        val hand0 = player.hands[0]
        hand0.addCard(Card(Suit.HEARTS, Rank.QUEEN, isFaceUp = true))
        hand0.addCard(Card(Suit.CLUBS, Rank.QUEEN, isFaceUp = true))

        val hand1 = player.hands[1]
        hand1.addCard(Card(Suit.SPADES, Rank.TEN, isFaceUp = true))
        hand1.addCard(Card(Suit.DIAMONDS, Rank.TEN, isFaceUp = true))

        val result0 = player.splitHand(0)
        assertTrue("First split should be successful", result0)
        assertEquals("After first split, player should have 3 hands", 3, player.hands.size)

        val result1 = player.splitHand(1)
        assertTrue("Second split should be successful", result1)
        assertEquals("After second split, player should have 4 hands", 4, player.hands.size)
    }
}