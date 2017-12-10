package com.jlheard.idw.service

import com.jlheard.idw.domain.Card
import com.jlheard.idw.domain.Hand

/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 12/9/17
 * Time: 3:09 PM
 */
class HandServiceTest extends GroovyTestCase {

    void testPlayCard() {
        def hand = new Hand()
        hand.add(new Card(rank: Card.Rank.JACK, suit: Card.Suit.SPADE))

        def expectedCard = hand.first
        def sizeAfterAddingCard = hand.size()
        def drawnCard = HandService.playCard(hand)

        assert hand.size() == sizeAfterAddingCard - 1
        assert expectedCard == drawnCard
        assert !(drawnCard in hand)
    }

    void testAddSpoilsOfWar() {
        def hand = new Hand()

        def jackOfSpade = new Card(rank: Card.Rank.JACK, suit: Card.Suit.SPADE)
        def queenOfHeart = new Card(rank: Card.Rank.QUEEN, suit: Card.Suit.HEART)
        def puppyToes = new Card(rank: Card.Rank.THREE, suit: Card.Suit.CLUB)

        hand.add(jackOfSpade)

        def spoils = [queenOfHeart, puppyToes]

        HandService.addSpoilsOfWar(hand, spoils)

        assert hand.size() == 1 + spoils.size()

        // should be after the existing cards in hand
        assert hand.indexOf(queenOfHeart) > hand.indexOf(jackOfSpade)
        assert hand.indexOf(puppyToes) > hand.indexOf(jackOfSpade)
    }

    void testPlayCardsForWar() {
        def hand = new Hand()

        def jackOfSpade = new Card(rank: Card.Rank.JACK, suit: Card.Suit.SPADE)
        def queenOfHeart = new Card(rank: Card.Rank.QUEEN, suit: Card.Suit.HEART)
        def puppyToes = new Card(rank: Card.Rank.THREE, suit: Card.Suit.CLUB)
        def aceOfDiamonds = new Card(rank: Card.Rank.ACE, suit: Card.Suit.DIAMOND)
        def sevenOfClubs = new Card(rank: Card.Rank.SEVEN, suit: Card.Suit.CLUB)

        hand.addAll(jackOfSpade, queenOfHeart, puppyToes, aceOfDiamonds, sevenOfClubs)

        def initSize = hand.size()

        assert HandService.playCardsForWar(hand).size() == HandService.MAX_CARDS_TO_PLAY_AT_WAR
        assert hand.size() == initSize - HandService.MAX_CARDS_TO_PLAY_AT_WAR
    }

    void testPlayCardsForWarNotEnoughCards() {
        def hand = new Hand()

        def jackOfSpade = new Card(rank: Card.Rank.JACK, suit: Card.Suit.SPADE)
        def queenOfHeart = new Card(rank: Card.Rank.QUEEN, suit: Card.Suit.HEART)

        hand.addAll(jackOfSpade, queenOfHeart)

        def initSize = hand.size()
        def army = HandService.playCardsForWar(hand)

        assert army.size() == initSize
        assert hand.size() == initSize - army.size()
    }
}
