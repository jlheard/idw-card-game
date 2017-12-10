package com.jlheard.idw.domain

/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 10/20/15
 * Time: 11:23 PM
 */
class Card implements Comparable<Card> {

    public enum Suit {
        CLUB, DIAMOND, HEART, SPADE
    }

    public enum Rank {
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN,
        JACK, QUEEN, KING, ACE
    }

    Suit suit
    Rank rank

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Card)) return false

        Card card = (Card) o

        if (rank != card.rank) return false
        if (suit != card.suit) return false

        return true
    }

    int hashCode() {
        int result
        result = suit.hashCode()
        result = 31 * result + rank.hashCode()
        return result
    }

    @Override
    int compareTo(Card o) {
        return this?.rank?.ordinal() <=> o?.rank?.ordinal()
    }

    @Override
    public String toString() {
        return "${rank.name()} OF ${suit.name()}S";
    }

}
