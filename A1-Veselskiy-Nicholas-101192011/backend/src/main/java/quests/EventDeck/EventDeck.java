package quests.EventDeck;

import quests.EventCard.EventDeckCard;
import quests.EventCard.QuestCard;
import quests.EventCard.EventCard;
import quests.EventCard.EventType;

import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

public class EventDeck {

    public EventDeck() {
        m_deck = new Stack<>();
        m_discardPile = new Stack<>();

        initialiseDeck();
    }


    private void initialiseDeck() {
        // Quest Cards
        //       Stages   Count
        HashMap<Integer, Integer> questMap = new HashMap<>();
        questMap.put(2, 3);
        questMap.put(3, 4);
        questMap.put(4, 3);
        questMap.put(5, 2);

        for (Integer stages : questMap.keySet()) {
            Integer count = questMap.get(stages);

            for (int i = 0; i < count; i++) {
                m_deck.push(new QuestCard(stages));
            }
        }

        // Event Cards
        // plague card
        m_deck.push(new EventCard(EventType.PLAGUE));
        // 2 Queen's favor cards
        m_deck.push(new EventCard(EventType.QUEENS_FAVOR));
        m_deck.push(new EventCard(EventType.QUEENS_FAVOR));
        // 2 Prosperity cards
        m_deck.push(new EventCard(EventType.PROSPERITY));
        m_deck.push(new EventCard(EventType.PROSPERITY));


        // Shuffle the deck
        Collections.shuffle(m_deck);
    }

    public EventDeckCard drawCard() {
        EventDeckCard result =  m_deck.pop();

        if (m_deck.isEmpty()) {
            m_deck.addAll(m_discardPile);
            m_discardPile.clear();

            Collections.shuffle(m_deck);
        }


        return result;
    }

    public int getDeckSize() {
        return m_deck.size();
    }

    public Stack<EventDeckCard> getDeck() {
        return m_deck;
    }

    public Stack<EventDeckCard> getDiscardPile() {
        return m_discardPile;
    }

    public void discardCard(EventDeckCard card) {
        m_discardPile.push(card);
    }

    private Stack<EventDeckCard> m_deck;
    private Stack<EventDeckCard> m_discardPile;
}
