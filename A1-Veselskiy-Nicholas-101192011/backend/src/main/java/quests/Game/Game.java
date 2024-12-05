package quests.Game;

import quests.AdventureCard.AdventureCard;
import quests.AdventureCard.WeaponCard;
import quests.AdventureDeck.AdventureDeck;
import quests.EventCard.EventCardType;
import quests.EventDeck.EventDeck;
import quests.EventCard.EventCard;
import quests.EventCard.QuestCard;
import quests.AdventureCard.FoeCard;
import quests.EventCard.EventDeckCard;
import quests.Player.Player;
import quests.Window.NetworkedWindow;
import quests.Quest.Quest;
import quests.Attack.Attack;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Stack;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

public class Game extends Thread {
    public Game(BlockingQueue<String> input, PrintWriter writer) {
        m_eventDeck = new EventDeck();
        m_adventureDeck = new AdventureDeck();
        m_outputWindow = new NetworkedWindow();
        m_input = input;
        m_writer = writer;
        waiting_on_user = false;
    }

    public void run() {
        gameLoop();
    }

    public void initGame() {
        // Create Players
        m_players = new Player[4];
        for (int i = 0; i < 4; i++) {
            m_players[i] = new Player(i);
        }

        // Distribute Cards
        for (Player player : m_players) {
            for (int card = 0; card < 12; ++card) {
                player.addCardToHand(m_adventureDeck.drawCard());
            }
        }
    }

    public EventDeck getEventDeck() {
        return m_eventDeck;
    }

    public AdventureDeck getAdventureDeck() {
        return m_adventureDeck;
    }

    public Player getPlayer(int playerNumber) {
        return m_players[playerNumber];
    }

    public List<Player> computeWinners() {
        List<Player> winners = new ArrayList<Player>();

        for (Player p : m_players) {
            if (p.getNumShields() >= 7) {
                winners.add(p);
            }
        }

        return winners;
    }

    public void gameLoop() {
        int currentPlayerTurn = 0;

        while (true) {
            try {
                m_outputWindow.promptForNewPlayerTurn(m_input, m_writer, m_players[currentPlayerTurn]);
            }
            catch (InterruptedException e) {
                System.err.println("ERR: Something has gone wrong with the input queue");
            }
            take_player_turn(currentPlayerTurn);

            currentPlayerTurn = (currentPlayerTurn + 1) % 4;
            List<Player> winners = computeWinners();

            if (!winners.isEmpty()) {
                try {
                    m_outputWindow.congratulateWinners(m_writer, winners);
                    waiting_on_user = true;
                }
                catch (InterruptedException e) {
                    System.err.println("ERR: Something has gone wrong with the input queue");
                }
                break;
            }
        }
    }

    public void take_player_turn(int playerID) {
        try {
            m_outputWindow.promptToDrawEventDeckCard(m_input, m_writer, m_players[playerID]);
        }
        catch (InterruptedException e) {
            System.err.println("ERR: Something has gone wrong with the input queue");
        }

        drawFromEventDeck(m_players[playerID]);

        try {
            m_outputWindow.promptToEndTurn(m_input, m_writer);
        }
        catch (InterruptedException e) {
            System.err.println("ERR: Something has gone wrong with the input queue");
        }
    }

    public void trimPlayerHand(Player player) {
        if (player.getHandSize() > 12) {
            try {
                m_outputWindow.promptToTakeControl(m_input, m_writer, player);
            }
            catch (InterruptedException e) {
                System.err.println("ERR: Something has gone wrong with the input queue");
            }
        }
        while (player.getHandSize() > 12) {
            int cardToDiscard = 0;
            try {
                cardToDiscard = m_outputWindow.discardCard(m_input, m_writer, player);
            }
            catch (InterruptedException e) {
                System.err.println("ERR: Something has gone wrong with the input queue");
            }

            player.discardCard(cardToDiscard, m_adventureDeck.getDiscardPile());
        }
    }

    public void applyPlague(Player player) {
        int newNumShields = player.getNumShields() - 2;
        newNumShields = Math.max(0, newNumShields);

        player.setNumShields(newNumShields);
    }

    public void applyQueenFavor(Player player) {
        player.addCardToHand(m_adventureDeck.drawCard());
        player.addCardToHand(m_adventureDeck.drawCard());

        // discard to hand size
        trimPlayerHand(player);
    }

    public void applyProsperity() {
        for (Player player : m_players) {
            player.addCardToHand(m_adventureDeck.drawCard());
            player.addCardToHand(m_adventureDeck.drawCard());

            // discard to hand size
            trimPlayerHand(player);
        }
    }

    public void drawFromEventDeck(Player player) {
        EventDeckCard card = m_eventDeck.drawCard();
        try {
            m_outputWindow.displayEventDeckCard(m_input, m_writer, card);
        }
        catch (InterruptedException e) {
            System.err.println("ERR: Something has gone wrong with the input queue");
        }

        if (card.getType() == EventCardType.EVENTTYPE) {
            EventCard eCard = (EventCard) card;

            applyEvent(eCard, player);
        }

        if (card.getType() == EventCardType.QUESTTYPE) {
            QuestCard qCard = (QuestCard) card;

            // find a sponsor for the quest
            Player sponsor = findSponsor(qCard, player.getPlayerId());

            if (sponsor == null) { // everyone declined to sponsor
                m_eventDeck.discardCard(qCard);
                return;
            }

            // build the quest
            Quest quest = buildQuest(sponsor, qCard);

            ArrayList<Player> eligibleParticipants = new ArrayList<Player>();
            for (Player gamer : m_players) {
                if (gamer != sponsor) {
                    eligibleParticipants.add(gamer);
                }
            }

            List<Player> winners = runQuest(quest, eligibleParticipants);

            if (winners.isEmpty()) {
                try {
                    m_outputWindow.questFailedMessage(m_input, m_writer);
                }
                catch (InterruptedException e) {
                    System.err.println("ERR: Something has gone wrong with the input queue");
                }
            }

            for (Player winner : winners) {
                winner.setNumShields(winner.getNumShields() + quest.getNumStages());
            }

            resolveQuest(quest, sponsor);
        }
    }

    public List<Player> runQuest(Quest quest, List<Player> eligibleParticipants) {
        int currentStage = 0;
        while (currentStage < quest.getNumStages()) {
            if (eligibleParticipants.isEmpty()) {
                break;
            }

            try {
                eligibleParticipants = m_outputWindow.askForParticipation(m_input, m_writer, eligibleParticipants);
            }
            catch (InterruptedException e) {
                System.err.println("ERR: Something has gone wrong with the input queue");
            }

            if (eligibleParticipants.isEmpty()) {
                break;
            }

            m_outputWindow.displayParticipants(m_writer, eligibleParticipants);

            // each participant draws a card then trims
            for (Player player : eligibleParticipants) {
                player.addCardToHand(m_adventureDeck.drawCard());
                trimPlayerHand(player);
            }

            ArrayList<Boolean> victorious = new ArrayList<>();
            for (Player player : eligibleParticipants) {
                try {
                    m_outputWindow.promptToTakeControl(m_input, m_writer, player);
                }
                catch (InterruptedException e) {
                    System.err.println("ERR: Something has gone wrong with the input queue");
                }
                Attack attack = buildAttack(player);

                victorious.add(resolveQuestStage(attack, quest, currentStage));

                attack.discardAll(m_adventureDeck.getDiscardPile());
            }

            ArrayList<Player> nextParticipants = new ArrayList<Player>();
            for (int i = 0; i < eligibleParticipants.size(); i++) {
                if (victorious.get(i)) {
                    nextParticipants.add(eligibleParticipants.get(i));
                }
            }
            eligibleParticipants = nextParticipants;

            if (!eligibleParticipants.isEmpty()) {
                try {
                    m_outputWindow.displayWinnersOfQuestStage(m_input, m_writer, eligibleParticipants);
                }
                catch (InterruptedException e) {
                    System.err.println("ERR: Something has gone wrong with the input queue");
                }
            }

            currentStage++;
        }

        return eligibleParticipants;
    }

    public void applyEvent(EventCard card, Player drawer) {
        switch (card.getEventType()) {
            case PLAGUE -> applyPlague(drawer);
            case PROSPERITY -> applyProsperity();
            case QUEENS_FAVOR -> applyQueenFavor(drawer);
        }
    }

    public Player findSponsor(QuestCard questCard, int startingPlayerIndex) {
        int playersAsked = 0;

        int playerToAsk = startingPlayerIndex;
        while (playersAsked < 4) {
            try {
                if (m_outputWindow.askToSponsor(m_input, m_writer, m_players[playerToAsk], questCard)) {
                    return m_players[playerToAsk];
                }
            }
            catch (InterruptedException e) {
                System.err.println("ERR: Something has gone wrong with the input queue");
            }

            playersAsked++;
            playerToAsk = (playerToAsk + 1) % 4;
        }

        return null;
    }

    public static boolean validateQuestStage(List<AdventureCard> cards, int previousStageValue) {
        // A quest must contain exactly one foe card
        boolean containsFoe = false;
        for (AdventureCard card : cards) {
            if (card.getLetter() == 'F') {
                if (containsFoe) {
                    return false; // We have more than one foe card in the stage
                }
                containsFoe = true;
            }
        }

        if (!containsFoe) {
            return false;
        }

        // A quest must have a unique set of weapons
        HashSet<Character> includedWeapons = new HashSet<Character>();
        for (AdventureCard card : cards) {
            if (includedWeapons.contains(card.getLetter())) {
                return false;
            }

            includedWeapons.add(card.getLetter());
        }

        // calculate the sum of this quest stage
        int sum = 0;
        for (AdventureCard card : cards) {
            sum += card.getValue();
        }

        // this stage must have a strictly greater value than the previous stage of the quest
        return sum > previousStageValue;
    }

    public Quest buildQuest(Player sponsor, QuestCard originatorCard) {
        Quest result = new Quest(originatorCard);

        int previousStage = 0;
        while (!result.validQuest()) {
            ArrayList<AdventureCard> stage = null;
            try {
                stage = m_outputWindow.buildQuestStage(m_input, m_writer, sponsor, previousStage);
            }
            catch (InterruptedException e) {
                System.err.println("ERR: Something has gone wrong with the input queue");
            }

            previousStage = Quest.getQuestStageValue(stage);

            result.addStage(stage);
        }

        return result;
    }

    public Attack buildAttack(Player player) {
        Attack attack = new Attack();

        while (true) {
            AdventureCard card = null;
            try {
                card = m_outputWindow.selectWeaponToAddToAttack(m_input, m_writer, player);
            }
            catch (InterruptedException e) {
                System.err.println("ERR: Something has gone wrong with the input queue");
            }
            if (card == null) {
                return attack;
            }

            if (card.getLetter() == 'F') { // foe cards cannot be added to attacks
                m_outputWindow.reportAddingFoeToAttack(m_writer);
                player.getHand().add(card);
                continue;
            }


            if (!attack.addCardToAttack((WeaponCard) card)) {
                m_outputWindow.reportDuplicateWeaponAdding(m_writer);
                player.getHand().add(card);
            }

            if (!attack.getCardsInAttack().isEmpty()) {
                try {
                    m_outputWindow.printAttack(m_input, m_writer, attack);
                }
                catch (InterruptedException e) {
                    System.err.println("ERR: Something has gone wrong with the input queue");
                }
            }
        }
    }

    public static boolean resolveQuestStage(Attack attack, Quest quest, int stage) {
        return attack.computeAttackValue() >= quest.computeStageValue(stage);
    }

    public void resolveQuest(Quest quest, Player sponsor) {
        int numStages = quest.getNumStages();
        int cardsToDraw = quest.discardAll(m_eventDeck.getDiscardPile(), m_adventureDeck.getDiscardPile()) + numStages;

        for (int i = 0; i < cardsToDraw; ++i) {
            sponsor.addCardToHand(m_adventureDeck.drawCard());
        }

        trimPlayerHand(sponsor);
    }

    public NetworkedWindow getOutputWindow() {
        return m_outputWindow;
    }

    public void rigA1Scenario() {
        // return all the player hands to the adventure deck
        for (int i = 0; i < 4; ++i) {
            this.getAdventureDeck().getDeck().addAll(this.getPlayer(i).getHand());
            this.getPlayer(i).getHand().clear();
        }

        List<AdventureCard> p1Hand =  this.getPlayer(0).getHand();
        // set up P1 hand
        p1Hand.add(new FoeCard(5));
        p1Hand.add(new FoeCard(5));
        p1Hand.add(new FoeCard(15));
        p1Hand.add(new FoeCard(15));

        p1Hand.add(new WeaponCard('D', 5));
        p1Hand.add(new WeaponCard('S', 10));
        p1Hand.add(new WeaponCard('S', 10));
        p1Hand.add(new WeaponCard('H', 10));
        p1Hand.add(new WeaponCard('H', 10));
        p1Hand.add(new WeaponCard('B', 15));
        p1Hand.add(new WeaponCard('B', 15));
        p1Hand.add(new WeaponCard('L', 20));

        for (AdventureCard card : p1Hand) {
            this.getAdventureDeck().getDeck().remove(card);
        }


        List<AdventureCard> p2Hand =  this.getPlayer(1).getHand();
        // set up P2 hand
        p2Hand.add(new FoeCard(5));
        p2Hand.add(new FoeCard(5));
        p2Hand.add(new FoeCard(15));
        p2Hand.add(new FoeCard(15));
        p2Hand.add(new FoeCard(40));

        p2Hand.add(new WeaponCard('D', 5));
        p2Hand.add(new WeaponCard('S', 10));
        p2Hand.add(new WeaponCard('H', 10));
        p2Hand.add(new WeaponCard('H', 10));
        p2Hand.add(new WeaponCard('B', 15));
        p2Hand.add(new WeaponCard('B', 15));
        p2Hand.add(new WeaponCard('E', 30));

        for (AdventureCard card : p2Hand) {
            this.getAdventureDeck().getDeck().remove(card);
        }

        List<AdventureCard> p3Hand =  this.getPlayer(2).getHand();
        // set up P3 hand
        p3Hand.add(new FoeCard(5));
        p3Hand.add(new FoeCard(5));
        p3Hand.add(new FoeCard(5));
        p3Hand.add(new FoeCard(15));

        p3Hand.add(new WeaponCard('D', 5));
        p3Hand.add(new WeaponCard('S', 10));
        p3Hand.add(new WeaponCard('S', 10));
        p3Hand.add(new WeaponCard('S', 10));
        p3Hand.add(new WeaponCard('H', 10));
        p3Hand.add(new WeaponCard('H', 10));
        p3Hand.add(new WeaponCard('B', 15));
        p3Hand.add(new WeaponCard('L', 20));

        for (AdventureCard card : p3Hand) {
            this.getAdventureDeck().getDeck().remove(card);
        }

        List<AdventureCard> p4Hand =  this.getPlayer(3).getHand();
        // set up P4 hand
        p4Hand.add(new FoeCard(5));
        p4Hand.add(new FoeCard(15));
        p4Hand.add(new FoeCard(15));
        p4Hand.add(new FoeCard(40));

        p4Hand.add(new WeaponCard('D', 5));
        p4Hand.add(new WeaponCard('D', 5));
        p4Hand.add(new WeaponCard('S', 10));
        p4Hand.add(new WeaponCard('H', 10));
        p4Hand.add(new WeaponCard('H', 10));
        p4Hand.add(new WeaponCard('B', 15));
        p4Hand.add(new WeaponCard('L', 20));
        p4Hand.add(new WeaponCard('E', 30));

        for (AdventureCard card : p4Hand) {
            this.getAdventureDeck().getDeck().remove(card);
        }

        int numPlayers = 4;
        int initialCards = 100;


        Stack<EventDeckCard> eventDeck = this.getEventDeck().getDeck();

        // remove one quest of size 4 from the deck somewhere
        QuestCard cardToRemove = null;
        for (EventDeckCard card : eventDeck) {
            if (card.getType() == EventCardType.QUESTTYPE) {
                QuestCard qCard = (QuestCard)card;
                if (qCard.getValue() == 4) {
                    cardToRemove = qCard;
                    break;
                }
            }
        }
        eventDeck.remove(cardToRemove);

        // ensure the top card is a quest of size 4
        eventDeck.push(new QuestCard(4));

        // build the top of the adventure deck how we need it
        Stack<AdventureCard> aDeck = this.getAdventureDeck().getDeck();

        Queue<AdventureCard> newCards = new LinkedList<AdventureCard>();

        newCards.add(new WeaponCard('L', 20));
        newCards.add(new FoeCard(30));

        newCards.add(new WeaponCard('S', 10));
        newCards.add(new WeaponCard('B', 15));

        newCards.add(new WeaponCard('L', 20));
        newCards.add(new WeaponCard('L', 20));
        newCards.add(new FoeCard(10));

        newCards.add(new WeaponCard('B', 15));
        newCards.add(new WeaponCard('S', 10));
        newCards.add(new FoeCard(30));

        // remove the cards we don't want duplicates of in the deck
        removeFromDeck(aDeck, newCards);

        // add the cards we do want on top
        aDeck.addAll(newCards);
    }

    public void rig2Winner2QuestScenario() {
        // return all the player hands to the adventure deck
        for (int i = 0; i < 4; ++i) {
            this.getAdventureDeck().getDeck().addAll(this.getPlayer(i).getHand());
            this.getPlayer(i).getHand().clear();
        }

        List<AdventureCard> p1Hand =  this.getPlayer(0).getHand();
        // set up P1 hand
        p1Hand.add(new FoeCard(5));
        p1Hand.add(new FoeCard(5));
        p1Hand.add(new FoeCard(10));
        p1Hand.add(new FoeCard(10));
        p1Hand.add(new FoeCard(15));
        p1Hand.add(new FoeCard(15));

        p1Hand.add(new WeaponCard('D', 5));
        p1Hand.add(new WeaponCard('H', 10));
        p1Hand.add(new WeaponCard('H', 10));
        p1Hand.add(new WeaponCard('B', 15));
        p1Hand.add(new WeaponCard('B', 15));
        p1Hand.add(new WeaponCard('L', 20));

        for (AdventureCard card : p1Hand) {
            this.getAdventureDeck().getDeck().remove(card);
        }


        List<AdventureCard> p2Hand =  this.getPlayer(1).getHand();
        // set up P2 hand
        p2Hand.add(new FoeCard(40));
        p2Hand.add(new FoeCard(50));

        p2Hand.add(new WeaponCard('S', 10));
        p2Hand.add(new WeaponCard('S', 10));
        p2Hand.add(new WeaponCard('S', 10));
        p2Hand.add(new WeaponCard('H', 10));
        p2Hand.add(new WeaponCard('H', 10));
        p2Hand.add(new WeaponCard('B', 15));
        p2Hand.add(new WeaponCard('B', 15));
        p2Hand.add(new WeaponCard('L', 20));
        p2Hand.add(new WeaponCard('L', 20));
        p2Hand.add(new WeaponCard('E', 30));

        for (AdventureCard card : p2Hand) {
            this.getAdventureDeck().getDeck().remove(card);
        }

        List<AdventureCard> p3Hand =  this.getPlayer(2).getHand();
        // set up P3 hand
        p3Hand.add(new FoeCard(5));
        p3Hand.add(new FoeCard(5));
        p3Hand.add(new FoeCard(5));
        p3Hand.add(new FoeCard(5));

        p3Hand.add(new WeaponCard('D', 5));
        p3Hand.add(new WeaponCard('D', 5));
        p3Hand.add(new WeaponCard('D', 5));

        p3Hand.add(new WeaponCard('H', 10));
        p3Hand.add(new WeaponCard('H', 10));
        p3Hand.add(new WeaponCard('H', 10));
        p3Hand.add(new WeaponCard('H', 10));
        p3Hand.add(new WeaponCard('H', 10));

        for (AdventureCard card : p3Hand) {
            this.getAdventureDeck().getDeck().remove(card);
        }

        List<AdventureCard> p4Hand =  this.getPlayer(3).getHand();
        // set up P4 hand
        p4Hand.add(new FoeCard(50));
        p4Hand.add(new FoeCard(70));

        p4Hand.add(new WeaponCard('S', 10));
        p4Hand.add(new WeaponCard('S', 10));
        p4Hand.add(new WeaponCard('S', 10));

        p4Hand.add(new WeaponCard('H', 10));
        p4Hand.add(new WeaponCard('H', 10));

        p4Hand.add(new WeaponCard('B', 15));
        p4Hand.add(new WeaponCard('B', 15));

        p4Hand.add(new WeaponCard('L', 20));
        p4Hand.add(new WeaponCard('L', 20));

        p4Hand.add(new WeaponCard('E', 30));

        for (AdventureCard card : p4Hand) {
            this.getAdventureDeck().getDeck().remove(card);
        }

        int numPlayers = 4;
        int initialCards = 100;

        // event deck shenanigans
        Stack<EventDeckCard> eventDeck = this.getEventDeck().getDeck();

        // remove one quest of size 4 from the deck somewhere
        QuestCard cardToRemove = null;
        for (EventDeckCard card : eventDeck) {
            if (card.getType() == EventCardType.QUESTTYPE) {
                QuestCard qCard = (QuestCard)card;
                if (qCard.getValue() == 4) {
                    cardToRemove = qCard;
                    break;
                }
            }
        }
        eventDeck.remove(cardToRemove);

        // remove one quest of size 3 from the deck somewhere
        cardToRemove = null;
        for (EventDeckCard card : eventDeck) {
            if (card.getType() == EventCardType.QUESTTYPE) {
                QuestCard qCard = (QuestCard)card;
                if (qCard.getValue() == 3) {
                    cardToRemove = qCard;
                    break;
                }
            }
        }
        eventDeck.remove(cardToRemove);

        // ensure the second from the top quest card is a quest of 3 stages
        eventDeck.push(new QuestCard(3));
        // ensure the top card is a quest of size 4
        eventDeck.push(new QuestCard(4));


        // adventure deck shenanigans

        // build the top of the adventure deck how we need it
        Stack<AdventureCard> aDeck = this.getAdventureDeck().getDeck();

        Queue<AdventureCard> newCards = new LinkedList<AdventureCard>();

        newCards.add(new WeaponCard('L', 20));
        newCards.add(new WeaponCard('B', 15));
        newCards.add(new WeaponCard('B', 15));
        newCards.add(new WeaponCard('S', 10));
        newCards.add(new FoeCard(30));
        newCards.add(new FoeCard(25));
        newCards.add(new FoeCard(20));
        newCards.add(new FoeCard(20));

        newCards.add(new FoeCard(25));
        newCards.add(new FoeCard(25));
        newCards.add(new FoeCard(15));
        newCards.add(new FoeCard(15));
        newCards.add(new WeaponCard('D', 5));
        newCards.add(new WeaponCard('D', 5));

        newCards.add(new FoeCard(30));
        newCards.add(new FoeCard(25));
        newCards.add(new FoeCard(25));
        newCards.add(new FoeCard(20));
        newCards.add(new FoeCard(20));
        newCards.add(new FoeCard(20));
        newCards.add(new FoeCard(20));
        newCards.add(new FoeCard(15));
        newCards.add(new FoeCard(15));
        newCards.add(new FoeCard(10));
        newCards.add(new FoeCard(5));

        newCards.add(new FoeCard(20));
        newCards.add(new FoeCard(15));
        newCards.add(new FoeCard(15));
        newCards.add(new FoeCard(30));
        newCards.add(new FoeCard(30));
        newCards.add(new FoeCard(10));
        newCards.add(new FoeCard(10));
        newCards.add(new FoeCard(40));
        newCards.add(new FoeCard(5));

        // remove the cards we don't want duplicates of in the deck
        removeFromDeck(aDeck, newCards);

        // add the cards we do want on top
        aDeck.addAll(newCards);
    }

    // this small helper function to remove a set of cards from the deck we don't want duplicates of
    // Note cannot use removeAll() because we want to remove exactly one copy of each card in cardsToRemove
    private void removeFromDeck(Stack<AdventureCard> deck,  Queue<AdventureCard> cardsToRemove) {
        for (AdventureCard card : cardsToRemove) {
            deck.remove(card);
        }
    }


    private EventDeck m_eventDeck;
    private AdventureDeck m_adventureDeck;
    private Player[] m_players;
    private NetworkedWindow m_outputWindow;
    private BlockingQueue<String> m_input;
    private PrintWriter m_writer;
    public static volatile boolean waiting_on_user;
}
