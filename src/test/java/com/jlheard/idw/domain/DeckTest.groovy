package com.jlheard.idw.domain

import org.junit.Test

/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 10/21/15
 * Time: 12:06 AM
 */
class DeckTest extends GroovyTestCase {

    void testPopulateDeckWith52CardsOfEverySuitAndRank() {

        def deck = new Deck()

        assert deck.size() == 52

        Card.Rank.values().each { rank ->
            Card.Suit.values().each { suit ->
                assert new Card(suit: suit, rank: rank) in deck
            }
        }
    }
}
