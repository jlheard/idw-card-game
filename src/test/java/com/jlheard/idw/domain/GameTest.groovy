package com.jlheard.idw.domain

/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 12/9/17
 * Time: 2:33 PM
 */
class GameTest extends GroovyTestCase {

    def game = new Game()

    void testGameInitStatusIsCorrect() {
        assert game.status == GameStatus.INITIATED
    }

    void testDeckIsInit() {
        assert !game.deck.empty
    }

    void testIdIsInit() {
        assert !game.getId().empty
    }
}
