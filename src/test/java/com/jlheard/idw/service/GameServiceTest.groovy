package com.jlheard.idw.service

import com.jlheard.idw.domain.Card
import com.jlheard.idw.domain.GameStatus
import com.jlheard.idw.domain.GameWinner
import com.jlheard.idw.domain.Player
import groovy.mock.interceptor.StubFor

import static com.jlheard.idw.service.GameService.ADD_PLAYER_TO_GAME_STATUSES
import static com.jlheard.idw.service.GameService.START_GAME_STATUSES

/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 10/25/15
 * Time: 12:38 PM
 */
class GameServiceTest extends GroovyTestCase {

    final static PLAYER_1 = new Player("Player 1")

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
            assert gameService.addPlayerToGame("Jason")
        }
    }

    void testAddPlayerToGameIncorrectStatus() {
        (GameStatus.values() - ADD_PLAYER_TO_GAME_STATUSES).each {
            gameService.createNewGame()
            gameService.game.status = it

            assert !gameService.addPlayerToGame("Jason")
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
        gs.startNewRound()

        assert gs.game.status == GameStatus.ROUND_IN_PROGRESS
    }

    void testStartNewTurn() {
        gameService.createNewGame()
        gameService.startGame()
        gameService.startNewRound()
        gameService.startNewTurn()

        assert gameService.game.status == GameStatus.AWAITING_PLAYER_TURN
    }

    //TODO: static method mocking is painful
    void testTakeTurn() {
        def mock = new StubFor(TurnService.class)

        def player1 = new Player("1")
        def player2 = new Player("2")

        TurnService.metaClass.static.determineBattleVictor = { Player p1, Player p2, LinkedHashSet<Card> l -> return player1 }

        gameService.createNewGame()
        gameService.startGame()
        gameService.startNewRound()
        gameService.startNewTurn()
//        gameService.takeTurn()


//        assert gameService.takeTurn() == player1
    }

    void testEndGame() {
        gameService.createNewGame()
        gameService.addPlayerToGame("1")
        gameService.addPlayerToGame("2")

        gameService.endGame()

        assert gameService.game.status == GameStatus.FINISHED
    }

    void testDetermineGameVictorPlayer1Wins() {
        def p1 = new Player("1")
        p1.hand.add(new Card())

        def p2 = new Player("2")
        gameService.createNewGame()
        gameService.addPlayerToGame(p1)
        gameService.addPlayerToGame(p2)

        assert gameService.determineGameVictor(p1, p2) == GameWinner.PLAYER_1
    }

    void testDetermineGameVictorPlayer2Wins() {
        def p1 = new Player("1")

        def p2 = new Player("2")
        p2.hand.add(new Card())

        gameService.createNewGame()
        gameService.addPlayerToGame(p1)
        gameService.addPlayerToGame(p2)

        assert gameService.determineGameVictor(p1, p2) == GameWinner.PLAYER_2
    }

    void testDetermineGameVictorTie() {
        def p1 = new Player("1")
        def p2 = new Player("2")

        gameService.createNewGame()
        gameService.addPlayerToGame(p1)
        gameService.addPlayerToGame(p2)

        assert gameService.determineGameVictor(p1, p2) == GameWinner.TIE
    }

    void testAddPlayerWithJoshCPUName() {

        gameService.createNewGame()
        gameService.addPlayerToGame(Player.JOSH)

        assert gameService.game.players.first().name == Player.HUMAN_JOSH_NAME

    }

}
