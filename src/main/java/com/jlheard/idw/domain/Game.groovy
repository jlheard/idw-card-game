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

    ArrayList<Player> players = new ArrayList<>(2)

    String getId() {
        return id
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Game)) return false

        Game game = (Game) o

        if (id != game.id) return false

        return true
    }

    int hashCode() {
        return (id != null ? id.hashCode() : 0)
    }
}
