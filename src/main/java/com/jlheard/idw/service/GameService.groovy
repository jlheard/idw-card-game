package com.jlheard.idw.service

import com.jlheard.idw.domain.Game
import com.jlheard.idw.domain.GameStatus
import com.jlheard.idw.domain.Player
import org.springframework.stereotype.Service

import static com.jlheard.idw.domain.GameStatus.*


/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 10/21/15
 * Time: 9:36 PM
 */
@Service
class GameService {

    public static final ADD_PLAYER_TO_GAME_STATUSES = [NEW]
    public static final DETERMINE_PLAYER_TO_DEAL_STATUSES = [NEW]
    public static final END_ROUND_STATUSES = [ROUND_IN_PROGRESS]
    public static final START_GAME_STATUSES = [NEW]
    public static final START_NEW_ROUND_STATUSES = [IN_PROGRESS, ROUND_FINISHED]
    public static final START_NEW_TURN_STATUSES = [ROUND_IN_PROGRESS]
    public static final TAKE_TURN_STATUSES = [AWAITING_PLAYER_TURN]
    public static final int NUMBER_OF_CARDS_TO_DEAL_EACH_PLAYER = 26

    Game game

    def addPlayerToGame(Player player) {
        if(isCorrectGameStatus(ADD_PLAYER_TO_GAME_STATUSES)) {
            game.players << player
        } else {
            false
        }
    }

    def addPlayerToGame(String username) {
        if(isCorrectGameStatus(ADD_PLAYER_TO_GAME_STATUSES)) {
            game.players.add(new Player(username))
        } else {
            false
        }
    }

    void createNewGame() {
        game = new Game()
        game.status = NEW
    }

    def endRound() {
        return isCorrectGameStatus(END_ROUND_STATUSES)
    }

    def findPlayerByUsername(String username) {
        game.players.find{it.name == username}
    }

    private def isCorrectGameStatus(List<GameStatus> correctStatuses) {
        game.status in correctStatuses
    }

    def startGame() {
        if(isCorrectGameStatus(START_GAME_STATUSES)) {
            game.status = IN_PROGRESS
            true
        } else {
            false
        }
    }

    def startNewRound() {
        if(isCorrectGameStatus(START_NEW_ROUND_STATUSES)) {
            game.status = ROUND_IN_PROGRESS
            true
        } else {
            false
        }
    }

    def startNewTurn() {
        if(isCorrectGameStatus(START_NEW_TURN_STATUSES)) {
            game.status = AWAITING_PLAYER_TURN
            true
        } else {
            false
        }
    }

    def takeTurn(String username) {
        return isCorrectGameStatus(TAKE_TURN_STATUSES)
    }

    static determineGameVictor(Player player1, Player player2) {
        if(player1.hand.size() == 0) {
            return player2
        }

        if(player2.hand.size() == 0) {
            return player1
        }
    }

}
