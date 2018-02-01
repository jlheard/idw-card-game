package com.jlheard.idw.service

import com.jlheard.idw.domain.*

import static com.jlheard.idw.service.GameService.ADD_PLAYER_TO_GAME_STATUSES
import static com.jlheard.idw.service.GameService.START_GAME_STATUSES

/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 10/25/15
 * Time: 12:38 PM
 */
class GameServiceTest extends GroovyTestCase {

    private Game game

    @Override
    void setUp() {
        game = GameService.createNewGame()
    }

    @Override
    void tearDown() {
        game = null
    }

    void testGameCreation() {
        assert GameStatus.NEW == game.status
    }

    void testAddPlayerToGameCorrectStatus() {
        ADD_PLAYER_TO_GAME_STATUSES.each {
            game.status = it
            assert GameService.addPlayerToGame(game, "Jason")
        }
    }

    void testAddPlayerToGameIncorrectStatus() {
        (GameStatus.values() - ADD_PLAYER_TO_GAME_STATUSES).each {
            game.status = it

            assert !GameService.addPlayerToGame(game, "Jason")
        }
    }


    void testStartGameWithCorrectStatus() {
        START_GAME_STATUSES.each {
            game.status = it
            assert GameService.startGame(game)
            assert game.status == GameStatus.IN_PROGRESS
        }
    }

    void testStartGameWithIncorrectStatus() {
        (GameStatus.values() - START_GAME_STATUSES).each {
            game.status = it
            assert !GameService.startGame(game)
            assert game.status == it
        }
    }

    void testStartNewRoundHappyPath() {
        GameService.startGame(game)
        GameService.startNewRound(game)

        assert game.status == GameStatus.ROUND_IN_PROGRESS
    }

    void testStartNewTurn() {
        GameService.startGame(game)
        GameService.startNewRound(game)
        GameService.startNewTurn(game)

        assert game.status == GameStatus.AWAITING_PLAYER_TURN
    }

    void testTakeTurn() {
        def player1 = new Player("1")
        def player2 = new Player("2")

        player1.hand.add(new Card(rank: Card.Rank.ACE, suit: Card.Suit.SPADE))
        player2.hand.add(new Card(rank: Card.Rank.THREE, suit: Card.Suit.CLUB))

        assert GameService.addPlayerToGame(game, player1)
        assert GameService.addPlayerToGame(game, player2)

        GameService.startGame(game)
        GameService.startNewRound(game)
        GameService.startNewTurn(game)

        assert GameService.takeTurn(game) == player1
    }

    void testEndGame() {
        GameService.addPlayerToGame(game, "1")
        GameService.addPlayerToGame(game, "2")

        GameService.endGame(game)

        assert game.status == GameStatus.FINISHED
    }

    void testDetermineGameVictorPlayer1Wins() {
        def p1 = new Player("1")
        p1.hand.add(new Card())

        def p2 = new Player("2")
        GameService.addPlayerToGame(game, p1)
        GameService.addPlayerToGame(game, p2)

        assert GameService.determineGameVictor(p1, p2) == GameWinner.PLAYER_1
    }

    void testDetermineGameVictorPlayer2Wins() {
        def p1 = new Player("1")

        def p2 = new Player("2")
        p2.hand.add(new Card())

        GameService.addPlayerToGame(game, p1)
        GameService.addPlayerToGame(game, p2)

        assert GameService.determineGameVictor(p1, p2) == GameWinner.PLAYER_2
    }

    void testDetermineGameVictorTie() {
        def p1 = new Player("1")
        def p2 = new Player("2")

        GameService.addPlayerToGame(game, p1)
        GameService.addPlayerToGame(game, p2)

        assert GameService.determineGameVictor(p1, p2) == GameWinner.TIE
    }

    void testAddPlayerWithJoshCPUName() {

        GameService.addPlayerToGame(game, Player.JOSH)

        assert game.players.first().name == Player.HUMAN_JOSH_NAME

    }

}
