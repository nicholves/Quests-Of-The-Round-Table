package Window;

import Attack.Attack;
import EventCard.EventCard;
import EventCard.EventDeckCard;
import Game.Game;
import EventCard.EventType;
import EventCard.QuestCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Window.Window;
import Player.Player;
import AdventureCard.*;
import AdventureDeck.AdventureDeck;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class WindowTest {
    @Test
    @DisplayName("The interface prompts the user to press enter to end their turn and then flushes the screen")
    public void RESP_03_TEST_01() {
        Window window = new Window();

        String input = "\n";
        StringWriter output = new StringWriter();

        window.promptToEndTurn(new Scanner(input), new PrintWriter(output));


        boolean assertion = output.toString().contains("<Enter>");
        assertion = assertion && output.toString().contains("\n");

        assertTrue(assertion);
    }

    @Test
    @DisplayName("Prompts the next player for their next turn. After the new player has confirmed that they are ready" +
            " to play by pressing enter the game should display their hand.")
    public void RESP_04_TEST_01() {
        Window window = new Window();
        int playerId = 3;
        Player player = new Player(playerId);

        player.addCardToHand(new FoeCard(5));
        player.addCardToHand(new FoeCard(10));
        player.addCardToHand(new FoeCard(5));
        player.addCardToHand(new WeaponCard('D', 5));
        player.addCardToHand(new WeaponCard('L', 20));
        player.addCardToHand(new FoeCard(70));
        player.addCardToHand(new WeaponCard('D', 5));
        player.addCardToHand(new FoeCard(25));
        player.addCardToHand(new FoeCard(50));
        player.addCardToHand(new WeaponCard('H', 10));
        player.addCardToHand(new WeaponCard('S', 10));
        player.addCardToHand(new FoeCard(35));

        String input = "\n";
        StringWriter output = new StringWriter();
        window.promptForNewPlayerTurn(new Scanner(input), new PrintWriter(output), player);

        String result = output.toString();

        assertTrue(result.split("\n").length > 1);

        String firstLine = result.split("\n")[0];
        String secondLine = result.split("\n")[1];

        assertTrue(firstLine.contains(String.valueOf(playerId)));
        assertTrue("Player 3, your hand contains: F5, F5, F10, F25, F35, F50, F70, D5, D5, S10, H10, L20".equals(secondLine.strip()));
    }

    @Test
    @DisplayName("The interface displays the winners of the game correctly with multiple winners")
    public void RESP_06_TEST_01() {
        Window window = new Window();

        Player player0 = new Player(0);
        Player player3 = new Player(3);
        List<Player> winners = new ArrayList<Player>();
        winners.add(player0);
        winners.add(player3);


        StringWriter output = new StringWriter();
        window.congratulateWinners(new PrintWriter(output), winners);


        String resultString = output.toString();
        assertTrue(resultString.contains("0"));
        assertTrue(resultString.contains("3"));
    }


    @Test
    @DisplayName("The interface displays the winners of the game correctly with a single winner")
    public void RESP_06_TEST_02() {
        Window window = new Window();
        Player player3 = new Player(2);
        List<Player> winners = new ArrayList<Player>();
        winners.add(player3);


        StringWriter output = new StringWriter();
        window.congratulateWinners(new PrintWriter(output), winners);


        String resultString = output.toString();
        assertTrue(resultString.contains("2"));
    }

    @Test
    @DisplayName("The interface prompts the user to press enter only when they are that player in the seat")
    public void RESP_07_TEST_01() {
        Window window = new Window();
        Player player = new Player(3);

        String input = "\n";
        StringWriter output = new StringWriter();

        window.promptToTakeControl(new Scanner(input), new PrintWriter(output), player);

        String result = output.toString();
        assertTrue(result.contains("3"));
        assertFalse(result.contains("0"));
        assertFalse(result.contains("1"));
        assertFalse(result.contains("2"));
    }

    @Test
    @DisplayName("The interface prompts the user with their hand and asks them which card they would like to discard")
    public void RESP_08_TEST_01() {
        Window window = new Window();
        Player player = new Player(3);
        Game game = new Game();


        player.addCardToHand(new FoeCard(5));
        player.addCardToHand(new FoeCard(10));
        player.addCardToHand(new FoeCard(5));
        player.addCardToHand(new WeaponCard('D', 5));
        player.addCardToHand(new WeaponCard('L', 20));
        player.addCardToHand(new FoeCard(70));
        player.addCardToHand(new WeaponCard('D', 5));
        player.addCardToHand(new FoeCard(25));
        player.addCardToHand(new FoeCard(50));
        player.addCardToHand(new WeaponCard('H', 10));
        player.addCardToHand(new WeaponCard('S', 10));
        player.addCardToHand(new FoeCard(35));
        player.addCardToHand(new FoeCard(5));
        player.addCardToHand(new WeaponCard('L', 20));

        String input = "0\n12\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();

        int cardToRemove = window.discardCard(scanner, new PrintWriter(output), player);

        assertEquals(0, cardToRemove);

        cardToRemove = window.discardCard(scanner, new PrintWriter(output), player);
        assertEquals(12, cardToRemove);
    }

    @Test
    @DisplayName("The interface prompts the user with their hand and asks them which card they would like to discard ignoring invalid entries")
    public void RESP_08_TEST_02() {
        Window window = new Window();
        Player player = new Player(3);
        Game game = new Game();


        player.addCardToHand(new FoeCard(5));
        player.addCardToHand(new FoeCard(10));
        player.addCardToHand(new FoeCard(5));
        player.addCardToHand(new WeaponCard('D', 5));
        player.addCardToHand(new WeaponCard('L', 20));
        player.addCardToHand(new FoeCard(70));
        player.addCardToHand(new WeaponCard('D', 5));
        player.addCardToHand(new FoeCard(25));
        player.addCardToHand(new FoeCard(50));
        player.addCardToHand(new WeaponCard('H', 10));
        player.addCardToHand(new WeaponCard('S', 10));
        player.addCardToHand(new FoeCard(35));
        player.addCardToHand(new FoeCard(5));

        String input = "15\n0\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();

        int cardToRemove = window.discardCard(scanner, new PrintWriter(output), player);

        assertEquals(0, cardToRemove);

        String result = output.toString();

        assertTrue(result.contains("An invalid selection was made. Please choose a value between: 0 and 12"));
    }

    @Test
    @DisplayName("The interface prompts the user to press enter before drawing a card")
    public void RESP_13_TEST_01() {
        Window window = new Window();
        Player player = new Player(3);

        String input = "\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();

        window.promptToDrawEventDeckCard(scanner, new PrintWriter(output), player);

        String result = output.toString();
        assertTrue(result.contains("3"));
    }

    @Test
    @DisplayName("The game displays a quest card to the user and waits for input to proceed")
    public void RESP_14_TEST_01() {
        Window window = new Window();

        String input = "\nn\nn\nn\nn\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();

        Game game = new Game(scanner, new PrintWriter(output));
        game.initGame();
        game.getEventDeck().getDeck().push(new QuestCard(3));

        game.drawFromEventDeck(game.getPlayer(0));

        String result = output.toString();
        assertTrue(result.contains("The card drawn was a: Quest of 3 Stages!"));
    }

    @Test
    @DisplayName("The game displays a plague card to the user and waits for input to proceed")
    public void RESP_14_TEST_02() {
        Window window = new Window();

        String input = "\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();

        Game game = new Game(scanner, new PrintWriter(output));
        game.initGame();
        game.getEventDeck().getDeck().push(new EventCard(EventType.PLAGUE));

        game.drawFromEventDeck(game.getPlayer(0));

        String result = output.toString();
        assertTrue(result.contains("The card drawn was a: Plague, causing you to lose two shields"));
    }

    @Test
    @DisplayName("The game displays a Queen's Favor card to the user and waits for input to proceed")
    public void RESP_14_TEST_03() {
        Window window = new Window();

        String input = "\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();

        Game game = new Game(scanner, new PrintWriter(output));
        game.initGame();
        game.getEventDeck().getDeck().push(new EventCard(EventType.QUEENS_FAVOR));

        Player player = game.getPlayer(0);
        player.getHand().clear();

        game.drawFromEventDeck(game.getPlayer(0));

        String result = output.toString();
        assertTrue(result.contains("The card drawn was a: Queen's Favor, causing you to draw two cards"));
    }

    @Test
    @DisplayName("The game displays a Prosperity card to the user and waits for input to proceed")
    public void RESP_14_TEST_04() {
        Window window = new Window();

        String input = "\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();

        Game game = new Game(scanner, new PrintWriter(output));
        game.initGame();
        Player player0 = game.getPlayer(0);
        Player player1 = game.getPlayer(1);
        Player player2 = game.getPlayer(2);
        Player player3 = game.getPlayer(3);

        player0.getHand().clear();
        player1.getHand().clear();
        player2.getHand().clear();
        player3.getHand().clear();


        game.getEventDeck().getDeck().push(new EventCard(EventType.PROSPERITY));

        game.drawFromEventDeck(game.getPlayer(0));

        String result = output.toString();
        assertTrue(result.contains("The card drawn was a: Prosperity, causing everyone to draw two cards"));
    }

    @Test
    @DisplayName("The game builds a quest stage when valid positions are entered")
    public void RESP_19_TEST_01() {
        Window window = new Window();
        Player player = new Player(3);

        String input = "1\n0\nQuit\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();

        player.addCardToHand(new FoeCard(5));
        player.addCardToHand(new WeaponCard('L', 20));


        ArrayList<AdventureCard> stage = window.buildQuestStage(scanner, new PrintWriter(output), player, 0);

        assertTrue(Game.validateQuestStage(stage, 0));
    }

    @Test
    @DisplayName("The game handles invalid positions being entered when building a quest stage")
    public void RESP_19_TEST_02() {
        Window window = new Window();
        Player player = new Player(3);

        String input = "1\n0\nQuit\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();

        player.addCardToHand(new FoeCard(5));


        ArrayList<AdventureCard> stage = window.buildQuestStage(scanner, new PrintWriter(output), player, 0);

        assertTrue(Game.validateQuestStage(stage, 0));

        String result = output.toString();

        assertTrue(result.contains("Invalid index"));
    }

    @Test
    @DisplayName("The game handles producing a warning when trying to add multiple foes to a quest")
    public void RESP_19_TEST_03() {
        Window window = new Window();
        Player player = new Player(3);

        String input = "1\n0\nQuit\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();

        player.addCardToHand(new FoeCard(5));
        player.addCardToHand(new FoeCard(5));


        ArrayList<AdventureCard> stage = window.buildQuestStage(scanner, new PrintWriter(output), player, 0);

        assertTrue(Game.validateQuestStage(stage, 0));

        String result = output.toString();

        assertTrue(result.contains("a maximum of 1 foe"));

        // the card shouldn't be removed if it was invalid to add
        assertEquals(1, player.getHandSize());
    }

    @Test
    @DisplayName("The game handles producing a warning when trying to add duplicate weapons to a quest")
    public void RESP_19_TEST_04() {
        Window window = new Window();
        Player player = new Player(3);

        String input = "1\n1\n0\nQuit\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();

        player.addCardToHand(new FoeCard(5));

        player.addCardToHand(new WeaponCard('L', 20));
        player.addCardToHand(new WeaponCard('L', 20));


        ArrayList<AdventureCard> stage = window.buildQuestStage(scanner, new PrintWriter(output), player, 0);

        assertTrue(Game.validateQuestStage(stage, 0));

        String result = output.toString();

        assertTrue(result.contains("1 copy of a weapon"));

        // the card shouldn't be removed if it was invalid to add
        assertEquals(1, player.getHandSize());
    }

    @Test
    @DisplayName("The game should not accept a quest stage without a foe and produce a warning")
    public void RESP_19_TEST_05() {
        Window window = new Window();
        Player player = new Player(3);

        String input = "1\nQuit\n0\nQuit\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();

        player.addCardToHand(new FoeCard(5));
        player.addCardToHand(new WeaponCard('L', 20));


        ArrayList<AdventureCard> stage = window.buildQuestStage(scanner, new PrintWriter(output), player, 0);

        assertTrue(Game.validateQuestStage(stage, 0));

        String result = output.toString();

        assertTrue(result.contains("cannot contain 0 foes"));

        // the card shouldn't be removed if it was invalid to add
        assertEquals(0, player.getHandSize());
    }

    @Test
    @DisplayName("The game should not accept a quest stage without a greater value than the previous stage")
    public void RESP_19_TEST_06() {
        Window window = new Window();
        Player player = new Player(3);

        String input = "0\nQuit\n0\nQuit\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();

        player.addCardToHand(new FoeCard(5));
        player.addCardToHand(new WeaponCard('L', 20));


        ArrayList<AdventureCard> stage = window.buildQuestStage(scanner, new PrintWriter(output), player, 5);

        assertTrue(Game.validateQuestStage(stage, 0));

        String result = output.toString();

        assertTrue(result.contains("greater than"));

        // the card shouldn't be removed if it was invalid to add
        assertEquals(0, player.getHandSize());
    }

    @Test
    @DisplayName("The game asks each player in turn order whether they would like to participate in the current stage of the quest. all refuse")
    public void RESP_22_TEST_01() {
        Window window = new Window();

        String input = "n\nn\nn\n";
        Scanner scanner = new Scanner(input);
        PrintWriter output = new PrintWriter(new StringWriter());

        Player player0 = new Player(0);
        Player player2 = new Player(2);
        Player player3 = new Player(3);

        ArrayList<Player> players = new ArrayList<Player>();
        players.add(player0);
        players.add(player2);
        players.add(player3);

        List<Player> playersAfterReduce = window.askForParticipation(scanner, output, players);

        assertEquals(0, playersAfterReduce.size());
    }

    @Test
    @DisplayName("The game asks each player in turn order whether they would like to participate in the current stage of the quest. 1 refuse 2 accept")
    public void RESP_22_TEST_02() {
        Window window = new Window();

        String input = "n\ny\ny\n";
        Scanner scanner = new Scanner(input);
        PrintWriter output = new PrintWriter(new StringWriter());

        Player player0 = new Player(0);
        Player player2 = new Player(2);
        Player player3 = new Player(3);

        ArrayList<Player> players = new ArrayList<Player>();
        players.add(player0);
        players.add(player2);
        players.add(player3);

        List<Player> playersAfterReduce = window.askForParticipation(scanner, output, players);

        assertEquals(2, playersAfterReduce.size());
        assertEquals(player2, playersAfterReduce.getFirst());
        assertEquals(player3, playersAfterReduce.get(1));
    }

    @Test
    @DisplayName("The game correctly outputs the weapons involved in an attack")
    public void RESP_25_TEST_01() {
        Window window = new Window();

        String input = "";
        Scanner scanner = new Scanner(input);
        PrintWriter output = new PrintWriter(new StringWriter());

        Attack attack = new Attack();
        attack.addCardToAttack(new WeaponCard('D', 5));
        attack.addCardToAttack(new WeaponCard('L', 20));

        window.printAttack(scanner, output, attack);

        String result = output.toString();

        assertTrue(result.contains("D5"));
        assertTrue(result.contains("L20"));
    }
}