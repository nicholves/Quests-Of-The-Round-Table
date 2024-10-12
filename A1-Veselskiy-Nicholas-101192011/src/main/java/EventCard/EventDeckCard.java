package EventCard;

public interface EventDeckCard {
    public default String asString() {
        return "";
    }

    public default String display() {
        return "";
    }

    public EventCardType getType();
}
