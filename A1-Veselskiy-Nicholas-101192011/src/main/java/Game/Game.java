package Game;

import AdventureDeck.AdventureDeck;
import EventDeck.EventDeck;

public class Game {
    public Game() {

    }

    public EventDeck getEventDeck() {
        return m_eventDeck;
    }

    public AdventureDeck getAdventureDeck() {
        return m_adventureDeck;
    }

    private EventDeck m_eventDeck;
    private AdventureDeck m_adventureDeck;
}
