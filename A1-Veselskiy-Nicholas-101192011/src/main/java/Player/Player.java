package Player;

import AdventureCard.AdventureCard;

import java.util.ArrayList;
import java.util.List;

public class Player {
    public Player() {
        m_hand = new ArrayList<>();
    }

    public List<AdventureCard> getHand() {
        return m_hand;
    }

    public void addCardToHand(AdventureCard newCard) {

    }


    private List<AdventureCard> m_hand;
}
