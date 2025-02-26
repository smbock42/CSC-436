package com.sambock.blackjackgame

import com.sambock.blackjackgame.game.Deck
import com.sambock.blackjackgame.game.Rank
import com.sambock.blackjackgame.game.Suit
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class DeckTest {
    @Test
    fun testSingleDeckCount() {
        val deck = Deck()
        assertEquals("Single deck should have 52 cards", 52, deck.remainingCards())
    }

    @Test
    fun testMultiDeckCount() {
        val numberOfDecks = 2
        val deck = Deck(numberOfDecks)
        assertEquals("Two decks should have 104 cards", 52 * numberOfDecks, deck.remainingCards())
    }

    @Test
    fun testDrawCardReducesCount() {
        val deck = Deck()
        val initialCount = deck.remainingCards()
        deck.drawCard()
        assertEquals("Drawing one card should reduce the count by one", initialCount - 1, deck.remainingCards())
    }

    @Test(expected = IllegalStateException::class)
    fun testDrawCardFromEmptyDeck() {
        val deck = Deck()

        repeat(52) {deck.drawCard()}

        deck.drawCard()
    }

    @Test
    fun testResetDeckRestoresFullCount() {
        val deck = Deck()

        repeat(10) {deck.drawCard()}
        assertEquals("After drawing, the deck should have 42 cards",42, deck.remainingCards())

        deck.resetDeck()
        assertEquals("After reset, the deck should be full", 52, deck.remainingCards())
    }

    @Test
    fun testEquals() {
        val deck1 = Deck(numberOfDecks = 1, shuffle = false)
        val deck2 = Deck(numberOfDecks = 1, shuffle = false)
        assertEquals("Two identical decks should be equal to each other", deck1, deck2)

        deck1.drawCard()

        assertNotEquals("Should not be equal after pulling from deck 1", deck1, deck2)
    }

    @Test
    fun testShuffle() {
        val deckUnshuffled = Deck(numberOfDecks = 1, shuffle = false)
        val deckShuffled = Deck(numberOfDecks = 1, shuffle = true)

        assertNotEquals("Shuffled deck should not equal a non shuffled deck (unless it shuffles nothing)", deckUnshuffled, deckShuffled)
    }

    @Test
    fun testCardValueCountsSingleDeck() {
        val deck = Deck(numberOfDecks = 1, shuffle = false)

        val cards = deck.getCards()
        cards.forEach { it.flip() }

//        cards.forEach { print(it.shortName() + " ") }


        val rankCounts = cards.groupingBy { it.getRank() }.eachCount()
        for (rank in Rank.entries) {
            assertEquals("For rank ${rank.name}, there should be 4 cards", 4, rankCounts[rank])
        }

        val suitCounts = cards.groupingBy { it.getSuit() }.eachCount()
        for (suit in Suit.entries) {
            assertEquals("For suit ${suit.name}, there should be 13 cards", 13, suitCounts[suit])
        }
    }

    @Test
    fun testCardValueCountsMultiDeck() {
        val deck = Deck(numberOfDecks = 3, shuffle = false)

        val cards = deck.getCards()
        cards.forEach { it.flip() }

//        cards.forEach { print(it.shortName() + " ") }

        val rankCounts = cards.groupingBy { it.getRank() }.eachCount()
        for (rank in Rank.entries) {
            assertEquals("For rank ${rank.name}, there should be 12 cards", 12, rankCounts[rank])
        }

        val suitCounts = cards.groupingBy { it.getSuit() }.eachCount()
        for (suit in Suit.entries) {
            assertEquals("For suit ${suit.name}, there should be 39 cards", 39, suitCounts[suit])
        }
    }
}