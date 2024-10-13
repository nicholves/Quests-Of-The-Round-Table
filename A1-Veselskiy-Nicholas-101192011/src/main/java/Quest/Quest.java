package Quest;

import AdventureCard.AdventureCard;
import EventCard.QuestCard;

import java.util.ArrayList;
import java.util.List;

public class Quest {
    public Quest(QuestCard card) {
        m_stages = new ArrayList<>();

        m_originatorCard = card;
    }

    public void addStage(List<AdventureCard> stage) {

    }

    public QuestCard getOriginatorCard() {
        return m_originatorCard;
    }

    public static int getQuestStageValue(List<AdventureCard> stage) {
        return 0;
    }

    public int getNumStages() {
        return m_originatorCard.getValue();
    }

    public List<AdventureCard> getStage(int index) {
        return m_stages.get(index);
    }

    public boolean validQuest() {
        return false;
    }


    private List<List<AdventureCard>> m_stages;
    private QuestCard m_originatorCard;
}
