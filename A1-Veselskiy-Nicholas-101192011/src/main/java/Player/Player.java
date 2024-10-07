package Player;

import AdventureCard.AdventureCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    public Player(int playerID) {
        m_hand = new ArrayList<>();
        m_playerID = playerID;
        m_numShields = 0;
    }

    public List<AdventureCard> getHand() {
        sortHand();
        return m_hand;
    }

    public void addCardToHand(AdventureCard newCard) {
        m_hand.add(newCard);
    }

    private void sortHand() {
        Collections.sort(m_hand);
    }

    public int getPlayerId() {
        return m_playerID;
    }

    public int getNumShields() {
        return 0;
    }

    public void setNumShields(int shields) {

    }


    private List<AdventureCard> m_hand;
    private int m_numShields;
    private final int m_playerID;
}
