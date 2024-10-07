package EventDeck;
import EventCard.EventDeckCard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class EventDeckTest {

    @Test
    @DisplayName("Check the Event Deck has 17 Cards After Initialisation")
    public void RESP_01_TEST_02() {
        EventDeck eventDeck = new EventDeck();

        assertEquals(17, eventDeck.getDeckSize());
    }

    @Test
    @DisplayName("Check the Event Deck has the correct card distribution")
    public void RESP_01_TEST_04() {
        EventDeck eventDeck = new EventDeck();

        HashMap<String, Integer> cards = new HashMap<String, Integer>();
        // Events
        cards.put("Plague", 1);
        cards.put("Queen's favor", 2);
        cards.put("Prosperity", 2);

        // Quests
        cards.put("Q2", 3);
        cards.put("Q3", 4);
        cards.put("Q4", 3);
        cards.put("Q5", 2);

        // a counter to make sure deck is actually decreasing in size
        int counter = 0;
        while (eventDeck.getDeckSize() > 0) {
            if (counter > 17) {
                fail("The Event Deck is not changing size or has too many cards");
            }

            EventDeckCard card = eventDeck.drawCard();
            String representation = card.asString();

            if (!cards.containsKey(representation)) {
                fail("Got an invalid card");
            }

            cards.put(representation, cards.get(representation) - 1);

            counter++;
        }

        // should now be zero cards we didn't find
        for (int value : cards.values()) {
            assertEquals(0, value);
        }
    }
}