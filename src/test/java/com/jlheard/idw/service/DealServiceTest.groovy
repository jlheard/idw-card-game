package com.jlheard.idw.service

import com.jlheard.idw.domain.Game
import com.jlheard.idw.domain.Player

/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 10/22/15
 * Time: 1:54 AM
 */
class DealServiceTest extends GroovyTestCase {

    void testDealCards() {

        def dealService = new DealService()
        def game = new Game()

        def player1 = new Player("player1")

        game.players.add(player1)

        def cardsToDealEachPlayer = GameService.NUMBER_OF_CARDS_TO_DEAL_EACH_PLAYER

        dealService.dealCards(game, cardsToDealEachPlayer)

        game.players.each { player ->
            assert player.hand.size() == cardsToDealEachPlayer
        }

        assert game.deck.size() == 52 - (cardsToDealEachPlayer * game.players.size())
        assert game.players.first().hand.size() == 52 - cardsToDealEachPlayer
        assert game.players.last().hand.size() == 52 - cardsToDealEachPlayer
    }

}
