package com.sambock.blackjackgame

import com.sambock.blackjackgame.game.Card
import com.sambock.blackjackgame.game.Rank
import com.sambock.blackjackgame.game.Suit
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CardTest {
    @Test
    fun testCardInitialization() {
        val card = Card(Suit.HEARTS, Rank.ACE)

        assertNull(card.getRank())
        assertNull(card.getSuit())

        assertEquals("XX", card.shortName())
        assertEquals("Face Down Card", card.toString())

        assertFalse(card.isRevealed)
    }

    @Test
    fun testFlip() {
        val card = Card(Suit.SPADES, Rank.TEN)
        assertFalse(card.isRevealed)
        card.flip()
        assertTrue(card.isRevealed)
        card.flip()
        assertFalse(card.isRevealed)
    }

    @Test
    fun testToString() {
        val card = Card(Suit.SPADES, Rank.KING, isFaceUp = false)
        assertEquals("Face Down Card", card.toString())

        //flip card up
        card.flip()
        assertEquals("KING of SPADES", card.toString())

        card.flip()
        assertEquals("Face Down Card", card.toString())

    }

    @Test
    fun testShortName() {
        val card = Card(Suit.SPADES, Rank.KING, isFaceUp = false)
        assertEquals("XX", card.shortName())

        card.flip()
        assertEquals("KS", card.shortName())
    }

    @Test
    fun testUpsideDown() {
        val card = Card(Suit.HEARTS, Rank.ACE, isFaceUp = false)
        assertNull(card.getSuit())
        assertNull(card.getRank())



        card.flip()
        assertEquals(Suit.HEARTS, card.getSuit())
        assertEquals(Rank.ACE, card.getRank())
    }
}