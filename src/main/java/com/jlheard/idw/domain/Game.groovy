package com.jlheard.idw.domain


/**
 * Created with IntelliJ IDEA.
 * User: jheard
 * Date: 10/21/15
 * Time: 9:06 PM
 */
class Game {

    @Override
    public String toString() {
        return "Game{" +
                "id='" + id + '\'' +
                ", status=" + status +
                '}';
    }

    private String id = UUID.randomUUID()

    Deck deck = new Deck()

    GameStatus status = GameStatus.INITIATED

    ArrayList<Player> players = [Player.JOSH]

    String getId() {
        return id
    }
}
