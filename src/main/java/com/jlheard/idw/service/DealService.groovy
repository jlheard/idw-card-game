package com.jlheard.idw.service

import com.jlheard.idw.domain.Game


/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 10/22/15
 * Time: 12:48 AM
 */
class DealService {

    static dealCards(Game game, int cardsToDealEachPlayer) {
        int totalCardsDealt = 0
        int totalCardsToDeal = game.players.size() * cardsToDealEachPlayer

        while (totalCardsDealt < totalCardsToDeal) {
            game.players.each { player ->
                def cardDealt = DeckService.drawCard(game.deck)
                player.hand << cardDealt

                totalCardsDealt++
            }
        }
    }
}
