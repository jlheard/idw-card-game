package com.jlheard.idw.service

import com.jlheard.idw.domain.Card
import com.jlheard.idw.domain.Hand
import com.jlheard.idw.domain.Player

/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 12/9/17
 * Time: 3:47 PM
 */
class TurnServiceTest extends GroovyTestCase {

    void testGetVictorPlayer1() {
        def p1 = new Player("p1")
        def p2 = new Player("p2")

        def p1Army = [new Card(suit: Card.Suit.CLUB, rank: Card.Rank.JACK)]
        def p2Army = [new Card(suit: Card.Suit.CLUB, rank: Card.Rank.TEN)]

        assert p2 == TurnService.getVictor(
                [player: p2, army: p1Army],
                [player: p1, army: p2Army],
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
                [player: p2, army: p2Army]
        )

        assert p1.hand.size() == 0
        assert p2.hand.size() == p1Army.size() + p2Army.size()
    }

    void testGetVictorAbsoluteTie() {
        def p1 = new Player("p1")
        def p2 = new Player("p2")

        assert null == TurnService.getVictor(
                [player: p2, army: [new Card(suit: Card.Suit.CLUB, rank: Card.Rank.NINE)]],
                [player: p1, army: [new Card(suit: Card.Suit.HEART, rank: Card.Rank.NINE)]]
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
                [player: p2, army: p2Army]
        ).victor

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
                [player: p2, army: p2Army]
        ).victor

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
                [player: p2, army: p2Army]
        )

    }

    void testMultiTiePlayer1Wins() {

        def p1 = new Player("p1")
        def p2 = new Player("p2")

        p1.hand.addAll(
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
                new Card(suit: Card.Suit.DIAMOND, rank: Card.Rank.TWO),
                new Card(suit: Card.Suit.HEART, rank: Card.Rank.QUEEN),
                new Card(suit: Card.Suit.CLUB, rank: Card.Rank.FOUR),
                new Card(suit: Card.Suit.SPADE, rank: Card.Rank.SEVEN),
                new Card(suit: Card.Suit.DIAMOND, rank: Card.Rank.JACK),
                new Card(suit: Card.Suit.CLUB, rank: Card.Rank.KING),
                new Card(suit: Card.Suit.HEART, rank: Card.Rank.ACE),
                new Card(suit: Card.Suit.SPADE, rank: Card.Rank.TWO)
        )

        def p1Args = [player: p1, army: [new Card(suit: Card.Suit.SPADE, rank: Card.Rank.NINE)]]
        def p2Args = [player: p2, army: [new Card(suit: Card.Suit.CLUB, rank: Card.Rank.NINE)]]

        def args = TurnService.determineWarVictor(p1Args, p2Args)
        def spoils = args.spoils
        assert spoils instanceof LinkedHashSet<Card>
        assert args.p1Card.suit == Card.Suit.DIAMOND
        assert args.p1Card.rank == Card.Rank.SEVEN
        assert args.p2Card.suit == Card.Suit.SPADE
        assert args.p2Card.rank == Card.Rank.SEVEN

        p1Args = [player: p1, army: []]
        p2Args = [player: p2, army: []]

        args = TurnService.determineWarVictor(p1Args, p2Args, spoils)
        assert args instanceof Map

        assert args.victor == p1
        assert args.p1Card.suit == Card.Suit.DIAMOND
        assert args.p1Card.rank == Card.Rank.QUEEN
        assert args.p2Card.suit == Card.Suit.SPADE
        assert args.p2Card.rank == Card.Rank.TWO
        assert p1.hand.size() == 18

    }

    void testDetermineReinforcementSizeWhenMax() {
        assert TurnService.determineReinforcementSize(initHandWithCards(12), initHandWithCards(24)) == HandService.MAX_CARDS_TO_PLAY_AT_WAR
    }

    void testDetermineReinforcementSizeWhenP1IsLow() {
        def p1Hand = initHandWithCards(2)
        def p2Hand = initHandWithCards(17)
        assert TurnService.determineReinforcementSize(p1Hand, p2Hand) == p1Hand.size()
    }

    void testDetermineReinforcementSizeWhenP2IsLow() {
        def p1Hand = initHandWithCards(34)
        def p2Hand = initHandWithCards(3)
        assert TurnService.determineReinforcementSize(p1Hand, p2Hand) == p2Hand.size()
    }

    void testDetermineReinforcementSizeWhenBothPlayersAreLow() {
        def p1Hand = initHandWithCards(3)
        def p2Hand = initHandWithCards(3)
        assert TurnService.determineReinforcementSize(p1Hand, p2Hand) == p1Hand.size()
        assert TurnService.determineReinforcementSize(p1Hand, p2Hand) == p2Hand.size()
    }

    private initHandWithCards(int numCards) {
        def hand = new Hand()
        numCards.times {
            hand.add(new Card())
        }

        return hand
    }

}
