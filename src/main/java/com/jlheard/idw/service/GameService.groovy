package com.jlheard.idw.service

import com.jlheard.idw.domain.Game
import com.jlheard.idw.domain.GameStatus
import com.jlheard.idw.domain.GameWinner
import com.jlheard.idw.domain.Player

import static com.jlheard.idw.domain.GameStatus.*


/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 10/21/15
 * Time: 9:36 PM
 */
class GameService {

    public static final ADD_PLAYER_TO_GAME_STATUSES = [NEW]
    public static final END_ROUND_STATUSES = [ROUND_IN_PROGRESS]
    public static final START_GAME_STATUSES = [NEW, FINISHED]
    public static final START_NEW_ROUND_STATUSES = [IN_PROGRESS, ROUND_FINISHED]
    public static final START_NEW_TURN_STATUSES = [ROUND_IN_PROGRESS]
    public static final TAKE_TURN_STATUSES = [AWAITING_PLAYER_TURN]
    public static final int NUMBER_OF_CARDS_TO_DEAL_EACH_PLAYER = 26


    static void addJoshToGame(Game game) {
        game.players << Player.JOSH
    }

    static boolean addPlayerToGame(Game game, String name) {
        addPlayerToGame(game, new Player(name))
    }

    static boolean addPlayerToGame(Game game, Player player) {
        if(isCorrectGameStatus(game, ADD_PLAYER_TO_GAME_STATUSES)) {
            if(player.name.trim() == Player.JOSH.name) {
                player.name = Player.HUMAN_JOSH_NAME
            }
            game.players << player
        } else {
            false
        }
    }

    static Game createNewGame() {
        def game = new Game()
        game.status = NEW
        return game
    }

    static def endRound(Game game) {
        if(isCorrectGameStatus(game, END_ROUND_STATUSES)) {
            game.status = ROUND_FINISHED
            return true
        }

        return false
    }

    private static boolean isCorrectGameStatus(Game game, List<GameStatus> correctStatuses) {
        game.status in correctStatuses
    }

    static boolean startGame(Game game) {
        if(isCorrectGameStatus(game, START_GAME_STATUSES)) {
            game.status = IN_PROGRESS
            true
        } else {
            false
        }
    }

    static boolean startNewRound(Game game) {
        if(isCorrectGameStatus(game, START_NEW_ROUND_STATUSES)) {
            game.status = ROUND_IN_PROGRESS
            true
        } else {
            false
        }
    }

    static boolean startNewTurn(Game game) {
        if(isCorrectGameStatus(game, START_NEW_TURN_STATUSES)) {
            game.status = AWAITING_PLAYER_TURN
            true
        } else {
            false
        }
    }

    static Player takeTurn(Game game) {
        if(isCorrectGameStatus(game, TAKE_TURN_STATUSES)) {
            def roundWinner = TurnService.determineBattleVictor(game.players.first(), game.players.last())
            return roundWinner
        }

        return null
    }

    static void endGame(Game game) {
        game.status = FINISHED
    }

    static GameWinner determineGameVictor(Player player1, Player player2) {
        if(player1.hand.size() == 0 && player2.hand.size() == 0) {
            return GameWinner.TIE
        } else if(player2.hand.size() == 0) {
            return GameWinner.PLAYER_1
        } else if(player1.hand.size() == 0) {
            return GameWinner.PLAYER_2
        } else {
            return null
        }
    }

}
