package quests.AdventureCard;

public class FoeCard implements AdventureCard {

    public FoeCard(int value) {
        m_letter = 'F';
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
        if (other.getLetter() != 'F') {
            return -1;
        }

        return m_value - other.getValue();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;

        if (!(obj instanceof FoeCard)) return false;

        return m_value == ((FoeCard) obj).m_value && m_letter == ((FoeCard) obj).m_letter;
    }

    private final char m_letter;
    private final int m_value;
}
