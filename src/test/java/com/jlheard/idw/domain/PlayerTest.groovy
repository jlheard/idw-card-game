package com.jlheard.idw.domain

import com.jlheard.idw.service.DeckService

/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 10/21/15
 * Time: 1:20 AM
 */
class PlayerTest extends GroovyTestCase {

    void testPlayerEqualsByUsername() {
        def userName = "test-user"
        assert new Player(userName) == new Player(userName)
    }

    void testPlayerHandInit() {
        assert new Player("").hand != null
    }
}
