package Game;

import AdventureCard.AdventureCard;
import AdventureCard.FoeCard;
import AdventureCard.WeaponCard;
import AdventureDeck.AdventureDeck;
import EventCard.EventCardType;
import EventDeck.EventDeck;
import EventCard.EventCard;
import EventCard.QuestCard;
import EventCard.EventDeckCard;
import Player.Player;
import Window.Window;
import Quest.Quest;
import Attack.Attack;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Game {
    public Game(Scanner scanner, PrintWriter writer) {
        m_eventDeck = new EventDeck();
        m_adventureDeck = new AdventureDeck();
        m_outputWindow = new Window();
        m_scanner = scanner;
        m_writer = writer;
    }

    public Game() {
        m_eventDeck = new EventDeck();
        m_adventureDeck = new AdventureDeck();
        m_outputWindow = new Window();
        m_scanner = new Scanner(System.in);
        m_writer = new PrintWriter(System.out, true);
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
            m_outputWindow.promptForNewPlayerTurn(m_scanner, m_writer, m_players[currentPlayerTurn]);

            take_player_turn(currentPlayerTurn);

            currentPlayerTurn = (currentPlayerTurn + 1) % 4;
            List<Player> winners = computeWinners();

            if (!winners.isEmpty()) {
                m_outputWindow.congratulateWinners(m_writer, winners);
                break;
            }
        }
    }

    public void take_player_turn(int playerID) {
        m_outputWindow.promptToDrawEventDeckCard(m_scanner, m_writer, m_players[playerID]);

        drawFromEventDeck(m_players[playerID]);

        m_outputWindow.promptToEndTurn(m_scanner, m_writer);
    }

    public void trimPlayerHand(Player player) {
        if (player.getHandSize() > 12) {
            m_outputWindow.promptToTakeControl(m_scanner, m_writer, player);
        }
        while (player.getHandSize() > 12) {
            int cardToDiscard = m_outputWindow.discardCard(m_scanner, m_writer, player);

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
        m_outputWindow.displayEventDeckCard(m_scanner, m_writer, card);

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
        }
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
            if (m_outputWindow.askToSponsor(m_scanner, m_writer, m_players[playerToAsk], questCard)) {
                return m_players[playerToAsk];
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
            ArrayList<AdventureCard> stage = m_outputWindow.buildQuestStage(m_scanner, m_writer, sponsor, previousStage);

            previousStage = Quest.getQuestStageValue(stage);

            result.addStage(stage);
        }

        return result;
    }

    public Attack buildAttack(Player player) {
        Attack attack = new Attack();

        while (true) {
            AdventureCard card = m_outputWindow.selectWeaponToAddToAttack(m_scanner, m_writer, player);
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
        }
    }

    public static boolean resolveQuestStage(Attack attack, Quest quest, int stage) {
        return attack.computeAttackValue() >= quest.computeStageValue(stage);
    }

    private EventDeck m_eventDeck;
    private AdventureDeck m_adventureDeck;
    private Player[] m_players;
    private Window m_outputWindow;
    private Scanner m_scanner;
    private PrintWriter m_writer;
}
