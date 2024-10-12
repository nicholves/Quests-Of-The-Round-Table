package Game;

import AdventureCard.AdventureCard;
import AdventureCard.WeaponCard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Player.Player;

import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Scanner;

class GameTest {
    @Test
    @DisplayName("Check that cards are distributed to players on game initialization")
    public void RESP_02_TEST_01() {
        Game game = new Game();
        game.initGame();

        for (int i = 0; i < 4; i++) {
            assertEquals(12, game.getPlayer(i).getHand().size());
        }

        assertEquals(100 - 12 * 4, game.getAdventureDeck().getDeckSize());
    }

    @Test
    @DisplayName("Check to ensure the game returns and empty list of winners if there are no winners")
    public void RESP_05_TEST_01() {
        Game game = new Game();
        game.initGame();

        Player player0 = game.getPlayer(0);
        Player player1 = game.getPlayer(1);
        Player player2 = game.getPlayer(2);
        Player player3 = game.getPlayer(3);

        player1.setNumShields(6);
        player2.setNumShields(0);
        player3.setNumShields(4);
        player0.setNumShields(1);

        List<Player> winners = game.computeWinners();

        assertEquals(0, winners.size());
    }

    @Test
    @DisplayName("Check to ensure the game returns a singleton list of winners if there is exactly 1 winner")
    public void RESP_05_TEST_02() {
        Game game = new Game();
        game.initGame();

        Player player0 = game.getPlayer(0);
        Player player1 = game.getPlayer(1);
        Player player2 = game.getPlayer(2);
        Player player3 = game.getPlayer(3);

        player1.setNumShields(7);
        player2.setNumShields(0);
        player3.setNumShields(4);
        player0.setNumShields(1);

        List<Player> winners = game.computeWinners();

        assertEquals(1, winners.size());
        assertEquals(player1, winners.getFirst());
    }

    @Test
    @DisplayName("Check to ensure the game returns a singleton list of winners if there is exactly 1 " +
            "winner who won greater than 7 shields")
    public void RESP_05_TEST_03() {
        Game game = new Game();
        game.initGame();

        Player player0 = game.getPlayer(0);
        Player player1 = game.getPlayer(1);
        Player player2 = game.getPlayer(2);
        Player player3 = game.getPlayer(3);

        player1.setNumShields(6);
        player2.setNumShields(8);
        player3.setNumShields(4);
        player0.setNumShields(1);

        List<Player> winners = game.computeWinners();

        assertEquals(1, winners.size());
        assertEquals(player2, winners.getFirst());
    }

    @Test
    @DisplayName("Check to ensure the game returns multiple winners if there are multiple winners")
    public void RESP_05_TEST_04() {
        Game game = new Game();
        game.initGame();

        Player player0 = game.getPlayer(0);
        Player player1 = game.getPlayer(1);
        Player player2 = game.getPlayer(2);
        Player player3 = game.getPlayer(3);

        player1.setNumShields(7);
        player2.setNumShields(8);
        player3.setNumShields(4);
        player0.setNumShields(7);

        List<Player> winners = game.computeWinners();

        assertEquals(3, winners.size());
        assertTrue(winners.contains(player1));
        assertTrue(winners.contains(player2));
        assertTrue(winners.contains(player0));
    }

    @Test
    @DisplayName("Applies the plague event to a player with greater than 2 shields and ensures they have two less than they started with")
    public void RESP_09_TEST_01() {
        Game game = new Game();
        game.initGame();

        Player player = game.getPlayer(0);
        player.setNumShields(5);

        game.applyPlague(player);

        assertEquals(3, player.getNumShields());
    }

    @Test
    @DisplayName("Applies the plague event to a player with less than or equal to 2 shields and ensures they have two 0 shields remaining")
    public void RESP_09_TEST_02() {
        Game game = new Game();
        game.initGame();

        Player player1 = game.getPlayer(0);
        player1.setNumShields(1);

        Player player2 = game.getPlayer(0);
        player2.setNumShields(2);

        game.applyPlague(player1);
        game.applyPlague(player2);

        assertEquals(0, player1.getNumShields());
        assertEquals(0, player2.getNumShields());
    }

    @Test
    @DisplayName("Applies the queen's favor event to a player and forces them to discard down to 12 cards")
    public void RESP_10_TEST_01() {
        String input = "\n0\n\n0\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();
        Game game = new Game(scanner, new PrintWriter(output));
        game.initGame();

        // fabricate two excalibers to be drawn so they won't be discarded
        game.getAdventureDeck().getDeck().push(new WeaponCard('E', 30));
        game.getAdventureDeck().getDeck().push(new WeaponCard('E', 30));

        Player player = game.getPlayer(0);

        game.applyQueenFavor(player);

        assertEquals(12, player.getHandSize());

        int excaliberCount = 0;
        for (AdventureCard card : player.getHand()) {
            if (card.getLetter() == 'E') {
                excaliberCount++;
            }
        }

        assertTrue(excaliberCount >= 2);
    }

    @Test
    @DisplayName("Applies the queen's favor event to a player when that player does not need to discard")
    public void RESP_10_TEST_02() {
        String input = "";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();
        Game game = new Game(scanner, new PrintWriter(output));
        game.initGame();

        Player player = game.getPlayer(0);
        player.getHand().removeFirst();
        player.getHand().removeFirst();

        assertEquals(10, player.getHandSize());

        game.applyQueenFavor(player);

        assertEquals(12, player.getHandSize());
    }
}