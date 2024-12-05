package quests.AdventureCard;

public interface AdventureCard extends Comparable<AdventureCard> {
    public default String asString() {
        return "";
    }
    public char getLetter();
    public int getValue();
}
