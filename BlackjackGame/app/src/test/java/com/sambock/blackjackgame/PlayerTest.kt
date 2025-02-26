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

        // Set up the hand with exactly two cards of the same rank.
        val card1 = Card(Suit.HEARTS, Rank.ACE, isFaceUp = true)
        val card2 = Card(Suit.CLUBS, Rank.ACE, isFaceUp = true)
        hand.addCard(card1)
        hand.addCard(card2)

        // Pre-split, there should be 1 hand.
        assertEquals("Before split, there should be 1 hand", 1, player.hands.size)

        // Attempt to split the hand.
        val result = player.splitHand(0)
        assertTrue("Split should be successful", result)
        // After splitting, we expect 2 hands.
        assertEquals("After splitting, player should have 2 hands", 2, player.hands.size)
        // The original hand should now have 1 card.
        assertEquals("Original hand should have 1 card after split", 1, player.hands[0].getCards().size)
        // The new hand should also have 1 card.
        assertEquals("New hand should have 1 card after split", 1, player.hands[1].getCards().size)
        // Check that the card in the new hand is an Ace.
        val newHandCard = player.hands[1].getCards().first()
        assertEquals("Card in new hand should be an Ace", Rank.ACE, newHandCard.actualRank())
    }

    @Test
    fun testUnsuccessfulSplitDifferentRanks() {
        val player = Player(startingHands = 1)
        val hand = player.primaryHand()

        // Set up the hand with two cards of different ranks.
        val card1 = Card(Suit.HEARTS, Rank.ACE, isFaceUp = true)
        val card2 = Card(Suit.CLUBS, Rank.KING, isFaceUp = true)
        hand.addCard(card1)
        hand.addCard(card2)

        val result = player.splitHand(0)
        assertFalse("Split should fail because cards have different ranks", result)
        // Player still has only one hand.
        assertEquals("Player should still have one hand", 1, player.hands.size)
    }

    @Test
    fun testUnsuccessfulSplitMoreThanTwoCards() {
        val player = Player(startingHands = 1)
        val hand = player.primaryHand()

        // Set up the hand with three cards.
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
        // Start with a player having two starting hands.
        val player = Player(startingHands = 2)

        // Set up both hands to be eligible for splitting.
        // For hand 0, add two Queens.
        val hand0 = player.hands[0]
        hand0.addCard(Card(Suit.HEARTS, Rank.QUEEN, isFaceUp = true))
        hand0.addCard(Card(Suit.CLUBS, Rank.QUEEN, isFaceUp = true))
        // For hand 1, add two Tens.
        val hand1 = player.hands[1]
        hand1.addCard(Card(Suit.SPADES, Rank.TEN, isFaceUp = true))
        hand1.addCard(Card(Suit.DIAMONDS, Rank.TEN, isFaceUp = true))

        // Attempt to split hand 0.
        val result0 = player.splitHand(0)
        assertTrue("First split should be successful", result0)
        // Now there should be 3 hands.
        assertEquals("After first split, player should have 3 hands", 3, player.hands.size)

        // Attempt to split hand 1 (which is still at index 1 after the first split).
        val result1 = player.splitHand(1)
        assertTrue("Second split should be successful", result1)
        // Now the player should have 4 hands.
        assertEquals("After second split, player should have 4 hands", 4, player.hands.size)
    }
}