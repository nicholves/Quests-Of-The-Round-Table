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

    private final EventCardType m_eventCardType;
    private final EventType m_eventType;
}
