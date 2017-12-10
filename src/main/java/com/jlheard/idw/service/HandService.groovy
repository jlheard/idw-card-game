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
        return hand.removeFirst()
    }

    static List<Card> playCardsForWar(Hand hand, int reinforcementSize) {
        def army = hand.take(reinforcementSize)
        hand.removeAll(army)

        return army
    }

    static Hand addSpoilsOfWar(Hand hand, List<Card> spoils) {
        Collections.shuffle(spoils)
        hand.addAll(spoils)
        return hand
    }

}
