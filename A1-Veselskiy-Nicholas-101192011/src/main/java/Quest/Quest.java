package Quest;

import AdventureCard.AdventureCard;
import EventCard.EventDeckCard;
import EventCard.QuestCard;

import java.util.ArrayList;
import java.util.List;

public class Quest {
    public Quest(QuestCard card) {
        m_stages = new ArrayList<>();

        m_originatorCard = card;
    }

    public void addStage(List<AdventureCard> stage) {
        m_stages.add(stage);
    }

    public QuestCard getOriginatorCard() {
        return m_originatorCard;
    }

    public static int getQuestStageValue(List<AdventureCard> stage) {
        int sum = 0;

        for (AdventureCard card : stage) {
            sum += card.getValue();
        }

        return sum;
    }

    public int getNumStages() {
        return m_originatorCard.getValue();
    }

    public List<AdventureCard> getStage(int index) {
        return m_stages.get(index);
    }

    public boolean validQuest() {
        return m_stages.size() == m_originatorCard.getValue();
    }

    public int computeStageValue(int stage) {
        int sum = 0;
        for (AdventureCard card : m_stages.get(stage)) {
            sum += card.getValue();
        }

        return sum;
    }

    public int discardAll(List<EventDeckCard> questDeck, List<AdventureCard> adventureDeckDiscardPile) {
        questDeck.add(m_originatorCard);
        m_originatorCard = null;

        int numCards = 0;
        for (List<AdventureCard> stage : m_stages) {
            numCards += stage.size();

            adventureDeckDiscardPile.addAll(stage);
        }

        m_stages.clear();

        return numCards;
    }

    private List<List<AdventureCard>> m_stages;
    private QuestCard m_originatorCard;
}
