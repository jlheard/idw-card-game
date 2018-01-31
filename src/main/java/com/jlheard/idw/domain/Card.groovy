package com.jlheard.idw.domain

/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 10/20/15
 * Time: 11:23 PM
 */
class Card implements Comparable<Card> {

    public enum Suit {
        CLUB("♣"), DIAMOND("♦"), HEART("♥"), SPADE("♠")

        private String symbol

        Suit(String symbol) {
            this.symbol = symbol
        }
    }

    public enum Rank {
        TWO("2"), THREE("3"), FOUR("4"), FIVE("5"), SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10"),
        JACK("J"), QUEEN("Q"), KING("K"), ACE("A")

        private String shorthand

        Rank(String shorthand) {
            this.shorthand = shorthand
        }
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
        result = (suit != null ? suit.hashCode() : 0)
        result = 31 * result + (rank != null ? rank.hashCode() : 0)
        return result
    }

    @Override
    int compareTo(Card o) {
        return this?.rank?.ordinal() <=> o?.rank?.ordinal()
    }

    @Override
    public String toString() {
        return "${rank.shorthand}$suit.symbol";
    }

}
