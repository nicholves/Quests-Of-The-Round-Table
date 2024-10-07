package Game;

import AdventureDeck.AdventureDeck;
import EventDeck.EventDeck;
import Player.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public Game() {
        m_eventDeck = new EventDeck();
        m_adventureDeck = new AdventureDeck();

        // Create Players
        m_players = new Player[4];
        for (int i = 0; i < 4; ++i) {
            m_players[i] = new Player(i);
        }

        // Distribute Cards
        for (Player player : m_players) {
            for (int card = 0; card < 12; ++card) {
                player.addCardToHand(m_adventureDeck.drawCard());
            }
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

    public List<Player> computeWinners() {
        return new ArrayList<Player>();
    }

    private EventDeck m_eventDeck;
    private AdventureDeck m_adventureDeck;
    private Player[] m_players;
}
