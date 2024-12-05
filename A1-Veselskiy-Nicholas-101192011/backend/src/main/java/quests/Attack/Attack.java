package quests.Attack;

import quests.AdventureCard.AdventureCard;
import quests.AdventureCard.WeaponCard;

import java.util.ArrayList;
import java.util.List;

public class Attack {
    public Attack() {
        m_cards = new ArrayList<WeaponCard>();
    }

    public int computeAttackValue() {
        int sum = 0;

        for (WeaponCard card : m_cards) {
            sum += card.getValue();
        }

        return sum;
    }

    public List<WeaponCard> getCardsInAttack() {
        return m_cards;
    }

    public boolean addCardToAttack(WeaponCard weapon) {
        for (WeaponCard card : m_cards) {
            if (card.getLetter() == weapon.getLetter()) {
                return false;
            }
        }

        m_cards.add(weapon);
        return true;
    }

    public void discardAll(List<AdventureCard> discardPile) {
        discardPile.addAll(m_cards);

        m_cards.clear();
    }

    private List<WeaponCard> m_cards;
}
