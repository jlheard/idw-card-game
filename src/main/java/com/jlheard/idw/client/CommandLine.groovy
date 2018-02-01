package com.jlheard.idw.client

import com.jlheard.idw.domain.Card
import com.jlheard.idw.domain.Game
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

    private static shallWePlay(String response, Game game = null) {
        if (response.equalsIgnoreCase("y") || response.equalsIgnoreCase("yes")) {
            println("\nLet's get started; Good luck!")
            game ? GameService.startGame(game): letsPlay()
        } else if (response.equalsIgnoreCase("n") || response.equalsIgnoreCase("no")) {
            println("\nAww next time then! Good-bye")
            System.exit(0)
        } else {
            println("\nSorry, I do not understand $response. Please type Y or N.")
            shallWePlay(sc.nextLine())
        }
    }

    private static letsPlay() {
        println("\nWhat is your name?")
        String playerName = sc.nextLine()

        if(playerName.trim().empty) {
            println("I see you like being anonymous! I'll just call you Player 1")
            playerName = "Player 1"
        }

        println("Hello $playerName, nice to meet you!")

        def game = GameService.createNewGame()

        GameService.addJoshToGame(game)
        GameService.addPlayerToGame(game, playerName)

        startTheGame(game)
    }

    private static startTheGame(Game game) {
        println("\nI am shuffling the deck...")
        DeckService.shuffleDeck(game.deck)

        cutTheDeck(game)
        println("\nYou have cut the deck...")

        println("I am dealing the deck...")
        DealService.dealCards(game, GameService.NUMBER_OF_CARDS_TO_DEAL_EACH_PLAYER)

        GameService.startGame(game)

        def gameWinner = null

        while (!gameWinner) {
            startTheBattle(game)
            gameWinner = GameService.determineGameVictor(game.players.first(), game.players.last())
        }

        endTheGame(game, gameWinner)
    }

    private static cutTheDeck(Game game) {
        Scanner cutSC = new Scanner(System.in)
        def min = 0
        def max = game.deck.size()
        println("\nSelect any number between 0 and $max to cut the deck.")

        try {
            def input = cutSC.nextInt()
            if (min <= input && max >= input) {
                DeckService.cutDeck(game.deck, input)
            } else {
                println("\nPlease select a number between $min and $max!")
                cutTheDeck(game)
            }
        } catch (InputMismatchException ime) {
            println("\nPlease enter a valid number!")
            cutTheDeck(game)
        }
    }

    private static startTheBattle(Game game) {

        def josh = game.players.first()
        def player = game.players.last()

        def joshHand = josh.hand
        def playerHand = player.hand

        GameService.startNewRound(game)
        GameService.startNewTurn(game)

        println("\nPress enter to start the next round.")
        sc.nextLine()

        def joshPlayed = joshHand.first()
        def playerPlayed = playerHand.first()

        println("I played:\t ${joshPlayed}")
        println("You played:\t ${playerPlayed}")

        def roundWinner = GameService.takeTurn(game)

        if (roundWinner) {
            endRound(game, roundWinner, joshHand, playerHand)
        } else {
            def map = iDeclareWar(josh, player, joshPlayed, playerPlayed)
            if(map.victor) {
                println("\nDuring the war I played:\t ${map.p1Card}")
                println("During the war you played:\t ${map.p2Card}")
                roundWinner = map.victor as Player
                endRound(game, roundWinner, joshHand, playerHand)
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
                endRound(game, roundWinner, joshHand, playerHand)
            }
        }
    }

    private static iDeclareWar(Player player1, Player player2, Card joshPlayed, Card playerPlayed) {
        println("It is a tie. Press Enter to Get ready for war!")
        sc.nextLine()
        println("I - De-clare - War!")

        TurnService.determineWarVictor(playerToArgs(player1, [joshPlayed] as LinkedHashSet<Card>), playerToArgs(player2, [playerPlayed] as LinkedHashSet<Card>))
    }

    private static iDeclareWarTie(Player player1, Player player2, LinkedHashSet<Card> spoils, int count) {
        println("Wow this makes tie number $count! Press Enter to Get ready for war again!!!")
        sc.nextLine()
        println("I - De-clare - War!")

        TurnService.determineWarVictor(playerToArgs(player1), playerToArgs(player2), spoils)
    }

    private static playerToArgs(Player player, LinkedHashSet<Card> army = []) {
        [player: player, army: army]
    }

    private static endRound(Game game, Player roundWinner, Set<Card> joshHand, Set<Card> playerHand) {
        println("$roundWinner.name wins this round!")
        GameService.endRound(game)

        println("\nI have ${joshHand.size()} cards remaning.")
        println("You have ${playerHand.size()} cards remaning.")

        // this will fail if the edge case of the missing cards arises for debugging purposes
        if(joshHand.size() + playerHand.size() != 52) {
            throw new Exception("Always need 52 cards in play!")
        }
    }

    private static endTheGame(Game game, GameWinner gameWinner) {
        GameService.endGame(game)

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
        shallWePlay(sc.nextLine(), game)
    }

}
