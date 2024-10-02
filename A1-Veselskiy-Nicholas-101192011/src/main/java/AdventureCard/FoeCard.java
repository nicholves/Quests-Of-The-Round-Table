package AdventureCard;

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

    private final char m_letter;
    private final int m_value;
}
