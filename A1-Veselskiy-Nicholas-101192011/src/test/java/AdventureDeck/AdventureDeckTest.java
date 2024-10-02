package AdventureDeck;
import AdventureCard.AdventureCard;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class AdventureDeckTest {

    @Test
    @DisplayName("Check the Adventure Deck has 100 Cards After Initialisation")
    void RESP_01_TEST_01() {
        AdventureDeck adventureDeck = new AdventureDeck();

        assertEquals(100, adventureDeck.getDeckSize());
    }

    @Test
    @DisplayName("Check the Adventure Deck has the correct card distribution")
    void RESP_01_TEST_03() {
        AdventureDeck adventureDeck = new AdventureDeck();

        HashMap<String, Integer> cards = new HashMap<String, Integer>();
        // foes
        cards.put("F5", 8);
        cards.put("F10", 7);
        cards.put("F15", 8);
        cards.put("F20", 7);
        cards.put("F25", 7);
        cards.put("F30", 4);
        cards.put("F35", 4);
        cards.put("F40", 2);
        cards.put("F50", 2);
        cards.put("F70", 1);

        // weapons
        cards.put("D5", 6);
        cards.put("H10", 12);
        cards.put("S10", 16);
        cards.put("B15", 8);
        cards.put("L20", 6);
        cards.put("E30", 2);

        // a counter to make sure deck is actually decreasing in size
        int counter = 0;
        while (adventureDeck.getDeckSize() > 0) {
            if (counter > 100) {
                fail("The Adventure Deck is not changing size");
            }

            AdventureCard card = adventureDeck.drawCard();
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