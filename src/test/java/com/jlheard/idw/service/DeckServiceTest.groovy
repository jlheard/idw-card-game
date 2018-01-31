package com.jlheard.idw.service

import com.jlheard.idw.domain.Deck
import com.jlheard.idw.utils.RandomNumberUtils
import groovy.mock.interceptor.StubFor

/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 10/21/15
 * Time: 1:33 AM
 */
class DeckServiceTest extends GroovyTestCase {

    void testGetShuffledDeck() {
        def collectionsStub = new StubFor(Collections)

        collectionsStub.demand.shuffle(1){}

        collectionsStub.use {
            assert DeckService.shuffleDeck(new Deck()) != null
        }
    }

    void testCutDeck() {
        def deck = new Deck()
        def cutIndex = RandomNumberUtils.getRandomInt(20, 32)
        def expectedFirstCard = deck[cutIndex]
        def expectedMiddleCard = deck.first()

        def randomStub = new StubFor(RandomNumberUtils)

        randomStub.demand.getRandomInt(1) {int max, int min -> cutIndex}

        randomStub.use {
            DeckService.cutDeck(deck)

            assert deck.size() == 52
            assert expectedFirstCard == deck.first()
            assert expectedMiddleCard == deck[deck.size()-cutIndex]
        }


    }

    void testDrawCard() {
        def deck = new Deck()

        def expectedCard = deck.first()
        def drawnCard = DeckService.drawCard(deck)

        assert deck.size() == 51
        assert expectedCard == drawnCard
        assert !(drawnCard in deck)
    }

}
