package quests.EventCard;

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

    @Override
    public String display() {
        return "Quest of " + m_value + " Stages!";
    }

    public int getValue() {
        return m_value;
    }

    private final EventCardType m_eventCardType;
    private final int m_value;
}
