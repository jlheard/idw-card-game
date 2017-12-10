package com.jlheard.idw.domain

/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 10/21/15
 * Time: 12:51 AM
 */
class Player {

    public static final JOSH = new Player("Josh (CPU)")

    public static final String HUMAN_JOSH_NAME = "Josh (HUMAN)"

    String name
    Hand hand = new Hand()

    Player(String name) {
        this.name = name
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Player)) return false

        Player player = (Player) o

        if (name != player.name) return false

        return true
    }

    int hashCode() {
        return name.hashCode()
    }


    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                '}';
    }
}
