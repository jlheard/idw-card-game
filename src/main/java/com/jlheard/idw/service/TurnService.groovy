package com.jlheard.idw.service

import com.jlheard.idw.domain.*
import org.springframework.stereotype.Service

/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 10/27/15
 * Time: 11:04 PM
 */
@Service
class TurnService {

    static void startTurn(Game game) {
        game.status = GameStatus.ROUND_IN_PROGRESS
    }

    static determineBattleVictor(Player player1, Player player2, List<Card> spoils, boolean tie = false) {
        spoils = tie ? spoils : new ArrayList<Card>()

        def player1Army = tie ? [] : [HandService.playCard(player1.hand)]
        def player2Army = tie ? [] : [HandService.playCard(player2.hand)]
        def player1Args = [player: player1, army: player1Army]
        def player2Args = [player: player2, army: player2Army]

        def victor = getVictor(player1Args, player2Args, spoils, tie)

        if(!victor && player1.hand && player2.hand) {
            victor = determineBattleVictor(player1, player2, spoils, true)
        }

        return victor
    }

    static determineWarVictor(Map player1Args, Map player2Args, spoils) {
        def victor

        def player1 = player1Args.player as Player
        def player1Army = player1Args.army as LinkedList<Card>

        def player2 = player2Args.player as Player
        def player2Army = player2Args.army as LinkedList<Card>

        def reinforcementSize = determineReinforcementSize(player1.hand, player2.hand)

        def player1Reinforcements = HandService.playCardsForWar(player1.hand, reinforcementSize)
        player1Army.addAll(player1Reinforcements)

        def player2Reinforcements = HandService.playCardsForWar(player2.hand, reinforcementSize)
        player2Army.addAll(player2Reinforcements)

        spoils.addAll(player1Army + player2Army)

        victor = getWinner([player: player1, army: player1Army], [player: player2, army: player2Army])

        if(victor) {
            HandService.addSpoilsOfWar(victor.hand, spoils)
        }

        return victor
    }

    protected static getVictor(Map player1Args, Map player2Args, List<Card> spoils, boolean tie = false) {

        def victor = tie ? null : getWinner(player1Args, player2Args)

        if(victor) {
            def player1Army = player1Args.army as List<Card>
            def player2Army = player2Args.army as List<Card>

            spoils.addAll(player1Army + player2Army)
            HandService.addSpoilsOfWar(victor.hand, spoils)
        } else {
            victor = determineWarVictor(player1Args, player2Args, spoils)
        }

        return victor
    }

    static Player getWinner(Map player1Args, Map player2Args) {
        def winner = null

        def player1Army = player1Args.army as List<Card>
        def player1 = player1Args.player as Player
        def player1Soldier = player1Army.last() as Card

        def player2 = player2Args.player as Player
        def player2Army = player2Args.army as List<Card>
        def player2Soldier = player2Army.last() as Card

        if(player1Soldier > player2Soldier) {
            winner = player1
        } else if (player2Soldier > player1Soldier) {
            winner = player2
        }

        return winner
    }

    static int determineReinforcementSize(Hand p1Hand, Hand p2Hand) {
        def reinforcementSize
        def p1HandSize = p1Hand.size()
        def p2HandSize = p2Hand.size()

        if(p1HandSize < HandService.MAX_CARDS_TO_PLAY_AT_WAR || p2HandSize < HandService.MAX_CARDS_TO_PLAY_AT_WAR) {
            if(p1HandSize < p2HandSize) {
                reinforcementSize = p1HandSize
            } else if (p2HandSize < p1HandSize) {
                reinforcementSize = p2HandSize
            } else {
                reinforcementSize = p1HandSize
            }
        } else {
            reinforcementSize = HandService.MAX_CARDS_TO_PLAY_AT_WAR
        }

        return reinforcementSize
    }

}