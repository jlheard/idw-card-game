package com.jlheard.idw.domain


/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 10/22/15
 * Time: 12:08 AM
 */
class CardTest extends GroovyTestCase {

    void testCardForEquality() {
        assert new Card(suit: Card.Suit.SPADE, rank: Card.Rank.JACK) == new Card(suit: Card.Suit.SPADE, rank: Card.Rank.JACK)
    }

    void testCompareCardValuesRegardlessOfSuit() {
        assert new Card(suit: Card.Suit.SPADE, rank: Card.Rank.JACK) == new Card(suit: Card.Suit.SPADE, rank: Card.Rank.JACK)
        assert new Card(suit: Card.Suit.DIAMOND, rank: Card.Rank.JACK) > new Card(suit: Card.Suit.SPADE, rank: Card.Rank.TEN)
        assert new Card(suit: Card.Suit.SPADE, rank: Card.Rank.JACK) < new Card(suit: Card.Suit.SPADE, rank: Card.Rank.QUEEN)
    }

}
