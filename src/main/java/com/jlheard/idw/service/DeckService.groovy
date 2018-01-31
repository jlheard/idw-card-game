package com.jlheard.idw.service

import com.jlheard.idw.domain.Card
import com.jlheard.idw.domain.Deck
import com.jlheard.idw.utils.RandomNumberUtils


/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 10/21/15
 * Time: 1:25 AM
 */
class DeckService {

    static Deck shuffleDeck(Deck deck) {
        def tempDeck = new LinkedList<Card>(deck)
        Collections.shuffle(tempDeck)
        deck.clear()
        deck.addAll(tempDeck)

        return deck
    }

    static Deck cutDeck(Deck deck, int cutIndex = RandomNumberUtils.getRandomInt(20, 32)) {
        def topCut = deck.take(cutIndex)
        deck.removeAll(topCut)
        deck.addAll(topCut)

        return deck
    }

    static Card drawCard(Deck deck) {
        def drawnCard = deck.first()
        deck.remove(drawnCard)
        return drawnCard
    }

}
