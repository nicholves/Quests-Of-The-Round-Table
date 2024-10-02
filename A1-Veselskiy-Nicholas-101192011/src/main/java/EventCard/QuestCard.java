package EventCard;

import EventCard.EventDeckCard;

public class QuestCard implements EventDeckCard {
    public QuestCard(int value) {
        m_eventCardType = EventCardType.QUESTTYPE;

        m_value = value;
    }

    @Override
    public String asString() {
        return "Q" + m_value;
    }

    @Override
    public EventCardType getType() {
        return m_eventCardType;
    }

    private final EventCardType m_eventCardType;
    private final int m_value;
}
