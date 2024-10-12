package Game;

import AdventureCard.FoeCard;
import AdventureDeck.AdventureDeck;
import EventDeck.EventDeck;
import Player.Player;
import Window.Window;

import java.io.PrintWriter;
import java.util.ArrayList;
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

        m_outputWindow.promptToEndTurn(m_scanner, m_writer);
    }

    public void trimPlayerHand(Player player) {
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

    }

    private EventDeck m_eventDeck;
    private AdventureDeck m_adventureDeck;
    private Player[] m_players;
    private Window m_outputWindow;
    private Scanner m_scanner;
    private PrintWriter m_writer;
}
