package com.jlheard.idw.domain

/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 10/20/15
 * Time: 11:33 PM
 */
class Deck extends LinkedList<Card> {

    Deck() {
        Card.Rank.values().each { value ->
            Card.Suit.values().each { suit ->
                add(new Card(suit: suit, rank: value))
            }
        }
    }
}
