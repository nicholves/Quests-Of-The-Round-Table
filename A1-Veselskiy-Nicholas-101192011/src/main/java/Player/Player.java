package Player;

import AdventureCard.AdventureCard;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public Player(int playerID) {
        m_hand = new ArrayList<>();
        m_playerID = playerID;
    }

    public List<AdventureCard> getHand() {
        return m_hand;
    }

    public void addCardToHand(AdventureCard newCard) {
        m_hand.add(newCard);
    }

    private void sortHand() {

    }

    public int getPlayerId() {
        return 0;
    }


    private List<AdventureCard> m_hand;
    private final int m_playerID;
}
