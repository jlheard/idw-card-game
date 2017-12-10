package com.jlheard.idw.service

import com.jlheard.idw.domain.Deck
import com.jlheard.idw.domain.Game
import com.jlheard.idw.domain.GameStatus
import com.jlheard.idw.domain.Player
import groovy.mock.interceptor.StubFor

import static com.jlheard.idw.service.GameService.ADD_PLAYER_TO_GAME_STATUSES
import static com.jlheard.idw.service.GameService.DETERMINE_PLAYER_TO_DEAL_STATUSES
import static com.jlheard.idw.service.GameService.START_GAME_STATUSES

/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 10/25/15
 * Time: 12:38 PM
 */
class GameServiceTest extends GroovyTestCase {

    final static PLAYER_1 = new Player(name: "Player 1")

    GameService gameService

    @Override
    void setUp() {
        gameService = new GameService()
    }

    @Override
    void tearDown() {
        gameService = null
    }

    void testGameCreation() {
        gameService.createNewGame()
        assert GameStatus.NEW == gameService.game.status
    }

    void testAddPlayerToGameCorrectStatus() {
        ADD_PLAYER_TO_GAME_STATUSES.each {
            gameService.createNewGame()
            gameService.game.status = it
            assert gameService.addPlayerToGame(PLAYER_1)
        }
    }

    void testAddPlayerToGameIncorrectStatus() {
        (GameStatus.values() - ADD_PLAYER_TO_GAME_STATUSES).each {
            gameService.createNewGame()
            gameService.game.status = it

            assert !gameService.addPlayerToGame(PLAYER_1)
        }
    }


    void testStartGameWithCorrectStatus() {
        START_GAME_STATUSES.each {
            gameService.createNewGame()
            gameService.game.status = it
            assert gameService.startGame()
            assert gameService.game.status == GameStatus.IN_PROGRESS
        }
    }

    void testStartGameWithIncorrectStatus() {
        (GameStatus.values() - START_GAME_STATUSES).each {
            gameService.createNewGame()
            gameService.game.status = it
            assert !gameService.startGame()
            assert gameService.game.status == it
        }
    }

    void testStartNewRoundHappyPath() {
        def gs = new GameService()
        gs.createNewGame()
        gs.startGame()

        def deckServiceStub = new StubFor(DeckService)

        deckServiceStub.demand.getShuffledDeck(1) {}
        deckServiceStub.demand.cutDeck(1) {Deck d -> assert d == gs.game.deck}

        def dealServiceStub = new StubFor(DealService)

        dealServiceStub.demand.dealCards(1) {Game g, int cardsToDeal ->
            assert g == gs.game
            assert cardsToDeal == GameService.NUMBER_OF_CARDS_TO_DEAL_EACH_PLAYER
        }

        def deckService = deckServiceStub.proxyDelegateInstance()
        def dealService = dealServiceStub.proxyDelegateInstance()

        gs.deckService = deckService
        gs.dealService = dealService

        assert gs.startNewRound()

        deckServiceStub.verify(deckService)
        dealServiceStub.verify(dealService)

        assert gs.game.status == GameStatus.ROUND_IN_PROGRESS

    }

}
