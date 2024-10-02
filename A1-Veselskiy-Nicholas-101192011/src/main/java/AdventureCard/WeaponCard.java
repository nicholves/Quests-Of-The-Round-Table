package AdventureCard;

public class WeaponCard implements AdventureCard{

    public WeaponCard(char letter, int value) {

    }

    @Override
    public String asString() {
        return "";
    }

    @Override
    public char getLetter() {
        return 0;
    }

    @Override
    public int getValue() {
        return 0;
    }

    private char m_letter;
    private int m_value;
}
