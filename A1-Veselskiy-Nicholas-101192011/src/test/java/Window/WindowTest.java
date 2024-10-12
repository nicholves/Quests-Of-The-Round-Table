package Window;

import Game.Game;
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

        String input = "\n0\n\n12\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();

        int cardToRemove = window.discardCard(scanner, new PrintWriter(output), player);

        assertEquals(0, cardToRemove);

        cardToRemove = window.discardCard(scanner, new PrintWriter(output), player);
        assertEquals(12, cardToRemove);
    }
}