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
        Collections.shuffle(deck)

        return deck
    }

    static Deck cutDeck(Deck deck) {
        def cutIndex = RandomNumberUtils.getRandomInt(20, 32)
        def topCut = deck.take(cutIndex)
        deck.removeAll(topCut)
        deck.addAll(topCut)

        return deck
    }

    static Card drawCard(Deck deck) {
        return deck.removeFirst()
    }

}
