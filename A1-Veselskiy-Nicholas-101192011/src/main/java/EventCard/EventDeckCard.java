package EventCard;

public interface EventDeckCard {
    public default String asString() {
        return "";
    }
    public EventCardType getType();
}
