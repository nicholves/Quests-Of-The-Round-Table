package quests.AdventureCard;

public class WeaponCard implements AdventureCard{

    public WeaponCard(char letter, int value) {
        m_letter = letter;
        m_value = value;
    }

    @Override
    public String asString() {
        return String.valueOf(m_letter) + m_value;
    }

    @Override
    public char getLetter() {
        return m_letter;
    }

    @Override
    public int getValue() {
        return m_value;
    }

    @Override
    public int compareTo(AdventureCard other) {
        if (other.getLetter() == 'F') {
            return 1;
        }

        if (m_value != other.getValue()) {
            return m_value - other.getValue();
        }

        // we must be either swords or horses
        int newValueThis = m_letter == 'S' ? 1 : 0;
        int newValueOther = other.getLetter() == 'S' ? 1 : 0;

        return newValueOther - newValueThis;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof WeaponCard)) return false;

        return m_value == ((WeaponCard) obj).m_value && m_letter == ((WeaponCard) obj).m_letter;
    }


    private final char m_letter;
    private final int m_value;
}
