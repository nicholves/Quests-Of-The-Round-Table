package AdventureCard;

public interface AdventureCard {
    public default String asString() {
        return "";
    }
    public char getLetter();
    public int getValue();
}
