package Game;

import AdventureDeck.AdventureDeck;
import EventDeck.EventDeck;
import Player.Player;

public class Game {
    public Game() {
        m_eventDeck = new EventDeck();
        m_adventureDeck = new AdventureDeck();

        m_players = new Player[4];
        for (int i = 0; i < 4; ++i) {
            m_players[i] = new Player();
        }
    }

    public EventDeck getEventDeck() {
        return m_eventDeck;
    }

    public AdventureDeck getAdventureDeck() {
        return m_adventureDeck;
    }

    public Player getPlayer(int playerNumber) {
        return m_players[playerNumber];
    }

    private EventDeck m_eventDeck;
    private AdventureDeck m_adventureDeck;
    private Player[] m_players;
}
