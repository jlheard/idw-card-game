package com.jlheard.idw.client

import com.jlheard.idw.domain.Card
import com.jlheard.idw.domain.GameWinner
import com.jlheard.idw.domain.Player
import com.jlheard.idw.service.DealService
import com.jlheard.idw.service.DeckService
import com.jlheard.idw.service.GameService
import com.jlheard.idw.service.TurnService

/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 12/10/17
 * Time: 5:11 PM
 */
class CommandLine {

    public static final Scanner sc = new Scanner(System.in)

    static void play() {

        println("Hello my name is Josh! My favorite card game is I Declare War! Shall we play a game?")
        println("Type Y or N. Type Q at anytime to quit")

        String response = sc.nextLine()

        shallWePlay(response)
    }

    private static shallWePlay(String response, GameService gameService = null) {
        if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes")) {
            println("\nLet's get started; Good luck!")
            gameService ? startTheGame(gameService): letsPlay()
        } else if (response.equalsIgnoreCase("n") || response.equalsIgnoreCase("no")) {
            println("\nAww next time then! Good-bye")
            System.exit(0)
        } else {
            println("\nSorry, I do not understand $response. Please type Y or N.")
            shallWePlay(sc.nextLine())
        }
    }

    private static letsPlay() {
        GameService gameService = new GameService()

        println("\nWhat is your name?")
        String playerName = sc.nextLine()

        if(playerName.trim().empty) {
            println("I see you like being anonymous! I'll just call you Player 1")
            playerName = "Player 1"
        }

        println("Hello $playerName, nice to meet you!")

        gameService.createNewGame()

        gameService.addJoshToGame()
        gameService.addPlayerToGame(playerName)

        startTheGame(gameService)
    }

    private static startTheGame(GameService gameService) {
        println("\nI am shuffling the deck...")
        DeckService.shuffleDeck(gameService.game.deck)

        cutTheDeck(gameService)
        println("\nYou have cut the deck...")

        println("I am dealing the deck...")
        DealService.dealCards(gameService.game, GameService.NUMBER_OF_CARDS_TO_DEAL_EACH_PLAYER)

        gameService.startGame()

        def gameWinner = null

        while (!gameWinner) {
            startTheBattle(gameService)
            gameWinner = gameService.determineGameVictor(gameService.game.players.first(), gameService.game.players.last())
        }

        endTheGame(gameService, gameWinner)
    }

    private static cutTheDeck(GameService gameService) {
        Scanner cutSC = new Scanner(System.in)
        def min = 0
        def max = gameService.game.deck.size()
        println("\nSelect any number between 0 and $max to cut the deck.")

        try {
            def input = cutSC.nextInt()
            if (min <= input && max >= input) {
                DeckService.cutDeck(gameService.game.deck, input)
            } else {
                println("\nPlease select a number between $min and $max!")
                cutTheDeck(gameService)
            }
        } catch (InputMismatchException ime) {
            println("\nPlease enter a valid number!")
            cutTheDeck(gameService)
        }
    }

    private static startTheBattle(GameService gameService) {

        def josh = gameService.game.players.first()
        def player = gameService.game.players.last()

        def joshHand = josh.hand
        def playerHand = player.hand

        gameService.startNewRound()
        gameService.startNewTurn()

        println("\nPress enter to start the next round.")
        sc.nextLine()

        println("I played:\t ${joshHand.first}")
        println("You played:\t ${playerHand.first}")

        def roundWinner = gameService.takeTurn()

        if (roundWinner) {
            endRound(gameService, roundWinner, joshHand, playerHand)
        } else {
            def map = iDeclareWar(josh, player)
            if(map.victor) {
                println("\nDuring the war I played:\t ${map.p1Card}")
                println("During the war you played:\t ${map.p2Card}")
                roundWinner = map.victor as Player
                endRound(gameService, roundWinner, joshHand, playerHand)
            } else {
                def count = 2
                def tieMap = iDeclareWarTie(josh, player, map.spoils, count)

                while (!tieMap.victor) {
                    count++
                    tieMap = iDeclareWarTie(josh, player, tieMap.spoils, count)
                }

                println("\nDuring the war I played:\t ${map.p1Card}")
                println("During the war you played:\t ${map.p2Card}")
                roundWinner = tieMap.victor as Player
                endRound(gameService, roundWinner, joshHand, playerHand)
            }
        }
    }

    private static iDeclareWar(Player player1, Player player2) {
        println("It is a tie. Press Enter to Get ready for war!")
        sc.nextLine()
        println("I - De-clare - War!")

        TurnService.determineWarVictor(playerToArgs(player1, [player1.hand.last]), playerToArgs(player2, [player2.hand.last]))
    }

    private static iDeclareWarTie(Player player1, Player player2, List<Card> spoils, int count) {
        println("Wow this makes tie number $count! Press Enter to Get ready for war again!!!")
        sc.nextLine()
        println("I - De-clare - War!")

        TurnService.determineWarVictor(playerToArgs(player1), playerToArgs(player2), spoils)
    }

    private static playerToArgs(Player player, List<Card> army = []) {
        [player: player, army: army]
    }

    private static endRound(GameService gameService, Player roundWinner, List<Card> joshHand, List<Card> playerHand) {
        println("$roundWinner.name wins this round!")
        gameService.endRound()

        println("\nI have ${joshHand.size()} cards remaning.")
        println("You have ${playerHand.size()} cards remaning.")

        // this will fail if the edge case of the missing cards arises for debugging purposes
        assert joshHand.size() + playerHand.size() == 52
    }

    private static endTheGame(GameService gameService, GameWinner gameWinner) {
        gameService.endGame()

        switch (gameWinner) {
            case GameWinner.PLAYER_1:
                println("You lose! Better luck next time!")
                break
            case GameWinner.PLAYER_2:
                println("Congratulations! You win! You just got lucky!")
                break
            case GameWinner.TIE:
                println("A strange game. The only winning move is not to play. How about a nice game of chess?")
                break
            default:
                println("ErR0r: All your base are belong to us!")
        }
        println("\nShall we play again?  Please enter Y or N.")
        shallWePlay(sc.nextLine(), gameService)
    }

}
