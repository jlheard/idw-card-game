package com.jlheard.idw.service

import com.jlheard.idw.domain.Card
import com.jlheard.idw.domain.Game
import com.jlheard.idw.domain.GameStatus
import com.jlheard.idw.domain.Player

/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 12/9/17
 * Time: 3:47 PM
 */
class TurnServiceTest extends GroovyTestCase {

    void testStartTurnCorrectGameStatus() {
        def game = new Game()
        TurnService.startTurn(game)
        assert game.status == GameStatus.ROUND_IN_PROGRESS
    }

    void testEndTurnCorrectGameStatus() {
        def game = new Game()
        TurnService.endTurn(game)
        assert game.status == GameStatus.ROUND_FINISHED
    }

    void testGetVictorPlayer1() {
        def p1 = new Player("p1")
        def p2 = new Player("p2")

        def p1Army = [new Card(suit: Card.Suit.CLUB, rank: Card.Rank.JACK)]
        def p2Army = [new Card(suit: Card.Suit.CLUB, rank: Card.Rank.TEN)]

        assert p2 == TurnService.getVictor(
                [player: p2, army: p1Army],
                [player: p1, army: p2Army],
                []
        )

        assert p1.hand.size() == 0
        assert p2.hand.size() == p1Army.size() + p2Army.size()
    }

    void testGetVictorPlayer2() {
        def p1 = new Player("p1")
        def p2 = new Player("p2")

        def p1Army = [new Card(suit: Card.Suit.CLUB, rank: Card.Rank.NINE)]
        def p2Army = [new Card(suit: Card.Suit.CLUB, rank: Card.Rank.TEN)]

        assert p2 == TurnService.getVictor(
                [player: p1, army: p1Army],
                [player: p2, army: p2Army],
                []
        )

        assert p1.hand.size() == 0
        assert p2.hand.size() == p1Army.size() + p2Army.size()
    }

    void testGetVictorAbsoluteTie() {
        def p1 = new Player("p1")
        def p2 = new Player("p2")

        assert null == TurnService.getVictor(
                [player: p2, army: [new Card(suit: Card.Suit.CLUB, rank: Card.Rank.NINE)]],
                [player: p1, army: [new Card(suit: Card.Suit.HEART, rank: Card.Rank.NINE)]],
                []
        )

        assert p1.hand.size() == 0
        assert p2.hand.size() == 0
    }

    void testDetermineWarVictorPlayer1() {

        def p1 = new Player("p1")
        def p2 = new Player("p2")

        def p1Army = [
                new Card(suit: Card.Suit.CLUB, rank: Card.Rank.NINE),
        ]

        def p2Army = [
                new Card(suit: Card.Suit.SPADE, rank: Card.Rank.NINE),
        ]

        p1.hand.addAll(
                new Card(suit: Card.Suit.HEART, rank: Card.Rank.SEVEN),
                new Card(suit: Card.Suit.SPADE, rank: Card.Rank.JACK),
                new Card(suit: Card.Suit.SPADE, rank: Card.Rank.QUEEN),
                new Card(suit: Card.Suit.DIAMOND, rank: Card.Rank.ACE)
        )

        p2.hand.addAll(
                new Card(suit: Card.Suit.DIAMOND, rank: Card.Rank.TWO),
                new Card(suit: Card.Suit.HEART, rank: Card.Rank.QUEEN),
                new Card(suit: Card.Suit.CLUB, rank: Card.Rank.FOUR),
                new Card(suit: Card.Suit.SPADE, rank: Card.Rank.SEVEN)
        )

        assert p1 == TurnService.determineWarVictor(
                [player: p1, army: p1Army],
                [player: p2, army: p2Army],
                []
        )

        assert p1.hand.size() == p1Army.size() + p2Army.size() + (HandService.MAX_CARDS_TO_PLAY_AT_WAR * 2)

    }

    void testDetermineWarVictorPlayer2() {

        def p1 = new Player("p1")
        def p2 = new Player("p2")

        def p1Army = [
                new Card(suit: Card.Suit.CLUB, rank: Card.Rank.NINE),
        ]

        def p2Army = [
                new Card(suit: Card.Suit.SPADE, rank: Card.Rank.NINE),
        ]

        p2.hand.addAll(
                new Card(suit: Card.Suit.HEART, rank: Card.Rank.SEVEN),
                new Card(suit: Card.Suit.SPADE, rank: Card.Rank.JACK),
                new Card(suit: Card.Suit.SPADE, rank: Card.Rank.QUEEN),
                new Card(suit: Card.Suit.DIAMOND, rank: Card.Rank.ACE)
        )

        p1.hand.addAll(
                new Card(suit: Card.Suit.DIAMOND, rank: Card.Rank.TWO),
                new Card(suit: Card.Suit.HEART, rank: Card.Rank.QUEEN),
                new Card(suit: Card.Suit.CLUB, rank: Card.Rank.FOUR),
                new Card(suit: Card.Suit.SPADE, rank: Card.Rank.SEVEN)
        )

        assert p2 == TurnService.determineWarVictor(
                [player: p1, army: p1Army],
                [player: p2, army: p2Army],
                []
        )

        assert p2.hand.size() == p1Army.size() + p2Army.size() + (HandService.MAX_CARDS_TO_PLAY_AT_WAR * 2)

    }

    void testVictorDraw() {

        def p1 = new Player("p1")
        def p2 = new Player("p2")

        def p1Army = [
                new Card(suit: Card.Suit.CLUB, rank: Card.Rank.NINE),
        ]

        def p2Army = [
                new Card(suit: Card.Suit.SPADE, rank: Card.Rank.NINE),
        ]

        p1.hand.addAll(
                new Card(suit: Card.Suit.HEART, rank: Card.Rank.SEVEN),
                new Card(suit: Card.Suit.SPADE, rank: Card.Rank.JACK),
                new Card(suit: Card.Suit.SPADE, rank: Card.Rank.QUEEN),
                new Card(suit: Card.Suit.DIAMOND, rank: Card.Rank.SEVEN)
        )

        p2.hand.addAll(
                new Card(suit: Card.Suit.DIAMOND, rank: Card.Rank.TWO),
                new Card(suit: Card.Suit.HEART, rank: Card.Rank.QUEEN),
                new Card(suit: Card.Suit.CLUB, rank: Card.Rank.FOUR),
                new Card(suit: Card.Suit.SPADE, rank: Card.Rank.SEVEN)
        )

        assert null == TurnService.getVictor(
                [player: p1, army: p1Army],
                [player: p2, army: p2Army],
                []
        )

    }

    void testMultiTiePlayer1Wins() {

        def p1 = new Player("p1")
        def p2 = new Player("p2")

        p1.hand.addAll(
                new Card(suit: Card.Suit.SPADE, rank: Card.Rank.NINE),
                new Card(suit: Card.Suit.HEART, rank: Card.Rank.SEVEN),
                new Card(suit: Card.Suit.SPADE, rank: Card.Rank.JACK),
                new Card(suit: Card.Suit.SPADE, rank: Card.Rank.QUEEN),
                new Card(suit: Card.Suit.DIAMOND, rank: Card.Rank.SEVEN),
                new Card(suit: Card.Suit.DIAMOND, rank: Card.Rank.FOUR),
                new Card(suit: Card.Suit.HEART, rank: Card.Rank.THREE),
                new Card(suit: Card.Suit.DIAMOND, rank: Card.Rank.FOUR),
                new Card(suit: Card.Suit.DIAMOND, rank: Card.Rank.QUEEN)
        )

        p2.hand.addAll(
                new Card(suit: Card.Suit.CLUB, rank: Card.Rank.NINE),
                new Card(suit: Card.Suit.DIAMOND, rank: Card.Rank.TWO),
                new Card(suit: Card.Suit.HEART, rank: Card.Rank.QUEEN),
                new Card(suit: Card.Suit.CLUB, rank: Card.Rank.FOUR),
                new Card(suit: Card.Suit.SPADE, rank: Card.Rank.SEVEN),
                new Card(suit: Card.Suit.DIAMOND, rank: Card.Rank.JACK),
                new Card(suit: Card.Suit.CLUB, rank: Card.Rank.KING),
                new Card(suit: Card.Suit.HEART, rank: Card.Rank.ACE),
                new Card(suit: Card.Suit.SPADE, rank: Card.Rank.TWO)
        )

        assert p1 == TurnService.determineBattleVictor(p1, p2, [])
        assert p1.hand.size() == 18

    }


}
