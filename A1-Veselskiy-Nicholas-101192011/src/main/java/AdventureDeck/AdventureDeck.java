package AdventureDeck;

import AdventureCard.AdventureCard;
import AdventureCard.FoeCard;
import AdventureCard.WeaponCard;

import java.util.HashMap;
import java.util.Stack;
import java.util.Collections;

class CountPowerPair {
    public CountPowerPair(int count, int power) {
        this.count = count;
        this.power = power;
    }

    public int count;
    public int power;
}

public class AdventureDeck {

    public AdventureDeck() {
        m_deck = new Stack<>();
        m_discardPile = new Stack<>();

        initialiseDeck();
    }

    private void initialiseDeck() {
        // Foes
        //       Power    Count
        HashMap<Integer, Integer> foesMap = new HashMap<>();
        foesMap.put(5, 8);
        foesMap.put(10, 7);
        foesMap.put(15, 8);
        foesMap.put(20, 7);
        foesMap.put(25, 7);
        foesMap.put(30, 4);
        foesMap.put(35, 4);
        foesMap.put(40, 2);
        foesMap.put(50, 2);
        foesMap.put(70, 1);

        for (Integer power : foesMap.keySet()) {
            Integer count = foesMap.get(power);

            for (int i = 0; i < count; i++) {
                m_deck.push(new FoeCard(power));
            }
        }


        // Weapons
        HashMap<Character, CountPowerPair> weaponsMap = new HashMap<>();
        weaponsMap.put('D', new CountPowerPair(6, 5));
        weaponsMap.put('H', new CountPowerPair(12, 10));
        weaponsMap.put('S', new CountPowerPair(16, 10));
        weaponsMap.put('B', new CountPowerPair(8, 15));
        weaponsMap.put('L', new CountPowerPair(6, 20));
        weaponsMap.put('E', new CountPowerPair(2, 30));

        for (Character name : weaponsMap.keySet()) {
            int count = weaponsMap.get(name).count;
            int power = weaponsMap.get(name).power;

            for (int i = 0; i < count; i++) {
                m_deck.push(new WeaponCard(name, power));
            }
        }

        // Shuffle the deck
        Collections.shuffle(m_deck);
    }

    public AdventureCard drawCard() {
        return m_deck.pop();
    }

    public int getDeckSize() {
        return m_deck.size();
    }

    public Stack<AdventureCard> getDeck() {
        return m_deck;
    }

    public Stack<AdventureCard> getDiscardPile() {
        return m_discardPile;
    }


    private Stack<AdventureCard> m_deck;
    private Stack<AdventureCard> m_discardPile;
}
