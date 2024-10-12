package EventCard;

public class EventCard implements EventDeckCard {
    public EventCard(EventType event) {
        m_eventCardType = EventCardType.EVENTTYPE;

        m_eventType = event;
    }

    @Override
    public String asString() {
        return switch (m_eventType) {
            case EventType.PLAGUE -> "Plague";
            case EventType.PROSPERITY -> "Prosperity";
            case EventType.QUEENS_FAVOR -> "Queen's favor";
        };
    }

    public EventType getEventType() {
        return m_eventType;
    }

    @Override
    public EventCardType getType() {
        return m_eventCardType;
    }

    @Override
    public String display() {
        String type = "";
        String effect = "";

        switch (m_eventType) {
            case EventType.PLAGUE -> type += "Plague";
            case EventType.QUEENS_FAVOR -> type += "Queen's Favor";
            case EventType.PROSPERITY -> type += "Prosperity";
        }


        switch (m_eventType) {
            case EventType.PLAGUE -> effect += "causing you to lose two shields!";
            case EventType.QUEENS_FAVOR -> effect += "causing you to draw two cards!";
            case EventType.PROSPERITY -> effect += "causing everyone to draw two cards!";
        }

        return type + ", " + effect;
    }

    private final EventCardType m_eventCardType;
    private final EventType m_eventType;
}
