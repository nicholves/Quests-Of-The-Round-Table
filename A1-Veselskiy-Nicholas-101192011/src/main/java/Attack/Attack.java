package Attack;

import AdventureCard.WeaponCard;

import java.util.ArrayList;
import java.util.List;

public class Attack {
    public Attack() {
        m_cards = new ArrayList<WeaponCard>();
    }

    public int computeAttackValue() {
        return 0;
    }

    public List<WeaponCard> getCardsInAttack() {
        return m_cards;
    }

    private List<WeaponCard> m_cards;
}
