package com.jlheard.idw.service

import com.jlheard.idw.domain.Card
import com.jlheard.idw.domain.Hand

/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 12/9/17
 * Time: 3:09 PM
 */
class HandService {

    public static final int MAX_CARDS_TO_PLAY_AT_WAR = 4

    static Card playCard(Hand hand) {
        def cardDrawn = hand.first()
        hand.remove(cardDrawn)
        return cardDrawn
    }

    static Set<Card> playCardsForWar(Hand hand, int reinforcementSize) {
        def army = hand.take(reinforcementSize)
        hand.removeAll(army)

        return army
    }

    static Hand addSpoilsOfWar(Hand hand, LinkedHashSet<Card> spoils) {
        def tempSpoils = new LinkedList<Card>(spoils)
        Collections.shuffle(tempSpoils)
        hand.addAll(tempSpoils)
        return hand
    }

}
