package Game;

import AdventureCard.AdventureCard;
import AdventureCard.WeaponCard;
import AdventureCard.FoeCard;
import EventCard.EventCard;
import EventCard.EventType;
import EventCard.QuestCard;
import Quest.Quest;
import Attack.Attack;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Player.Player;

import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
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
        String input = "\n0\n0\n";
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

    @Test
    @DisplayName("Applies the prosperity event to a group of players where none will need to discard")
    public void RESP_11_TEST_01() {
        String input = "";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();
        Game game = new Game(scanner, new PrintWriter(output));
        game.initGame();

        int beforeProsperity = game.getAdventureDeck().getDeckSize();

        Player player1 = game.getPlayer(0);
        player1.getHand().removeFirst();
        player1.getHand().removeFirst();

        Player player2 = game.getPlayer(1);
        player2.getHand().removeFirst();
        player2.getHand().removeFirst();
        player2.getHand().removeFirst();
        player2.getHand().removeFirst();

        Player player3 = game.getPlayer(2);
        player3.getHand().removeFirst();
        player3.getHand().removeFirst();
        player3.getHand().removeFirst();
        player3.getHand().removeFirst();
        player3.getHand().removeFirst();
        player3.getHand().removeFirst();

        Player player4 = game.getPlayer(3);
        player4.getHand().removeFirst();
        player4.getHand().removeFirst();
        player4.getHand().removeFirst();
        player4.getHand().removeFirst();
        player4.getHand().removeFirst();
        player4.getHand().removeFirst();
        player4.getHand().removeFirst();
        player4.getHand().removeFirst();

        assertEquals(10, player1.getHandSize());
        assertEquals(8, player2.getHandSize());
        assertEquals(6, player3.getHandSize());
        assertEquals(4, player4.getHandSize());

        game.applyProsperity();

        assertEquals(12, player1.getHandSize());
        assertEquals(10, player2.getHandSize());
        assertEquals(8, player3.getHandSize());
        assertEquals(6, player4.getHandSize());

        int numPlayers = 4;
        assertEquals(beforeProsperity - numPlayers * 2,  game.getAdventureDeck().getDeckSize());
    }

    @Test
    @DisplayName("Applies the prosperity event to a group of players where multiple will need to discard")
    public void RESP_11_TEST_02() {
        String input = "\n0\n0\n" + "\n0\n0\n" + "\n0\n0\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();
        Game game = new Game(scanner, new PrintWriter(output));
        game.initGame();

        Player player = game.getPlayer(0);
        player.getHand().removeFirst();
        player.getHand().removeFirst();

        assertEquals(10, player.getHandSize());

        game.applyProsperity();

        assertEquals(12, game.getPlayer(0).getHandSize());
        assertEquals(12, game.getPlayer(1).getHandSize());
        assertEquals(12, game.getPlayer(2).getHandSize());
        assertEquals(12, game.getPlayer(3).getHandSize());

        int numPlayersWhoDiscarded = 3;
        assertEquals(2 * numPlayersWhoDiscarded, game.getAdventureDeck().getDiscardPile().size());
    }

    @Test
    @DisplayName("Applies the plague event to a player who drew that card")
    public void RESP_12_TEST_01() {
        String input = "\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();
        Game game = new Game(scanner, new PrintWriter(output));
        game.initGame();

        game.getEventDeck().getDeck().push(new EventCard(EventType.PLAGUE));

        Player player = game.getPlayer(0);
        player.setNumShields(3);


        game.drawFromEventDeck(player);

        assertEquals(1, player.getNumShields());
    }

    @Test
    @DisplayName("Applies the queen's favor event to a player who drew that card")
    public void RESP_12_TEST_02() {
        String input = "\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();
        Game game = new Game(scanner, new PrintWriter(output));
        game.initGame();

        game.getEventDeck().getDeck().push(new EventCard(EventType.QUEENS_FAVOR));

        Player player = game.getPlayer(0);
        player.getHand().removeFirst();
        player.getHand().removeFirst();

        assertEquals(10, player.getHandSize());

        game.drawFromEventDeck(player);

        assertEquals(12, player.getHandSize());
    }

    @Test
    @DisplayName("Applies the prosperity event when drawn")
    public void RESP_12_TEST_03() {
        String input = "\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();
        Game game = new Game(scanner, new PrintWriter(output));
        game.initGame();

        game.getEventDeck().getDeck().push(new EventCard(EventType.PROSPERITY));

        int beforeProsperity = game.getAdventureDeck().getDeckSize();
        Player player1 = game.getPlayer(0);
        player1.getHand().removeFirst();
        player1.getHand().removeFirst();

        Player player2 = game.getPlayer(1);
        player2.getHand().removeFirst();
        player2.getHand().removeFirst();
        player2.getHand().removeFirst();
        player2.getHand().removeFirst();

        Player player3 = game.getPlayer(2);
        player3.getHand().removeFirst();
        player3.getHand().removeFirst();
        player3.getHand().removeFirst();
        player3.getHand().removeFirst();
        player3.getHand().removeFirst();
        player3.getHand().removeFirst();

        Player player4 = game.getPlayer(3);
        player4.getHand().removeFirst();
        player4.getHand().removeFirst();
        player4.getHand().removeFirst();
        player4.getHand().removeFirst();
        player4.getHand().removeFirst();
        player4.getHand().removeFirst();
        player4.getHand().removeFirst();
        player4.getHand().removeFirst();

        assertEquals(10, player1.getHandSize());
        assertEquals(8, player2.getHandSize());
        assertEquals(6, player3.getHandSize());
        assertEquals(4, player4.getHandSize());

        game.drawFromEventDeck(player1);

        assertEquals(12, player1.getHandSize());
        assertEquals(10, player2.getHandSize());
        assertEquals(8, player3.getHandSize());
        assertEquals(6, player4.getHandSize());

        int numPlayers = 4;
        assertEquals(beforeProsperity - numPlayers * 2,  game.getAdventureDeck().getDeckSize());
    }

    @Test
    @DisplayName("The game asks the users of the game whether they would like to sponsor and the first player does")
    public void RESP_15_TEST_01() {
        String input = "y\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();
        Game game = new Game(scanner, new PrintWriter(output));
        game.initGame();

        Player player = game.findSponsor(new QuestCard(3),1);

        assertEquals(game.getPlayer(1), player);
    }

    @Test
    @DisplayName("The game asks the users of the game whether they would like to sponsor and the third player does after the first two refuse")
    public void RESP_15_TEST_02() {
        String input = "n\nn\ny\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();
        Game game = new Game(scanner, new PrintWriter(output));
        game.initGame();

        Player player = game.findSponsor(new QuestCard(3),1);

        assertEquals(game.getPlayer(3), player);
    }

    @Test
    @DisplayName("The game asks the users of the game whether they would like to sponsor and they all refuse")
    public void RESP_15_TEST_03() {
        String input = "n\nn\nn\nn\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();
        Game game = new Game(scanner, new PrintWriter(output));
        game.initGame();

        Player player = game.findSponsor(new QuestCard(3),1);

        assertNull(player);
    }

    @Test
    @DisplayName("The game correctly outputs the quest information for each player who would like to sponsor")
    public void RESP_15_TEST_04() {
        String input = "n\ny\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();
        Game game = new Game(scanner, new PrintWriter(output));
        game.initGame();

        Player player = game.findSponsor(new QuestCard(3), 1);

        String result = output.toString();
        String[] lines = result.split("\n");

        int questInfoCount = 0;
        for (String line : lines) {
            if (line.contains("Quest of 3 Stages!")) {
                questInfoCount++;
            }
        }

        assertEquals(2, questInfoCount);
    }

    @Test
    @DisplayName("The game invalidates a quest which does not have a foe card")
    public void RESP_18_TEST_01() {
        ArrayList<AdventureCard> stage = new ArrayList<AdventureCard>();

        stage.add(new WeaponCard('L', 20));
        stage.add(new WeaponCard('D', 5));

        assertFalse(Game.validateQuestStage(stage, 0));
    }

    @Test
    @DisplayName("The game invalidates a quest which does not have a greater total then the previous stage")
    public void RESP_18_TEST_02() {
        ArrayList<AdventureCard> stage = new ArrayList<AdventureCard>();

        stage.add(new FoeCard(5));
        stage.add(new WeaponCard('L', 20));
        stage.add(new WeaponCard('D', 5));

        assertFalse(Game.validateQuestStage(stage, 30));
        assertFalse(Game.validateQuestStage(stage, 31));
    }

    @Test
    @DisplayName("The game invalidates a quest which has 1 repeated weapon")
    public void RESP_18_TEST_03() {
        ArrayList<AdventureCard> stage = new ArrayList<AdventureCard>();

        stage.add(new FoeCard(5));
        stage.add(new WeaponCard('L', 20));
        stage.add(new WeaponCard('L', 20));
        stage.add(new WeaponCard('D', 5));

        assertFalse(Game.validateQuestStage(stage, 0));
    }

    @Test
    @DisplayName("The game invalidates a quest which has multiple repeated weapon")
    public void RESP_18_TEST_04() {
        ArrayList<AdventureCard> stage = new ArrayList<AdventureCard>();

        stage.add(new FoeCard(5));
        stage.add(new WeaponCard('L', 20));
        stage.add(new WeaponCard('L', 20));
        stage.add(new WeaponCard('D', 5));
        stage.add(new WeaponCard('D', 5));
        stage.add(new WeaponCard('D', 5));

        assertFalse(Game.validateQuestStage(stage, 0));
    }

    @Test
    @DisplayName("The game validates a valid quest")
    public void RESP_18_TEST_05() {
        ArrayList<AdventureCard> stage = new ArrayList<AdventureCard>();

        stage.add(new FoeCard(5));
        stage.add(new WeaponCard('L', 20));
        stage.add(new WeaponCard('D', 5));

        assertTrue(Game.validateQuestStage(stage, 25));
    }

    @Test
    @DisplayName("The game can build a full valid quest with all stages")
    public void RESP_20_TEST_01() {
        String input = "0\nQuit\n" + "0\n1\n1\nQuit\n" + "0\n0\nQuit\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();
        Game game = new Game(scanner, new PrintWriter(output));

        Player player = new Player(3);

        player.addCardToHand(new FoeCard(5));
        player.addCardToHand(new FoeCard(10));
        player.addCardToHand(new FoeCard(15));
        player.addCardToHand(new WeaponCard('S', 10));
        player.addCardToHand(new WeaponCard('H', 10));
        player.addCardToHand(new WeaponCard('L', 20));
        player.addCardToHand(new WeaponCard('E', 30));

        QuestCard card = new QuestCard(3);
        Quest result = game.buildQuest(player, card);

        assertEquals(card, result.getOriginatorCard());

        assertEquals(3, result.getNumStages());

        assertEquals(5, Quest.getQuestStageValue(result.getStage(0)));
        assertEquals(30, Quest.getQuestStageValue(result.getStage(1)));
        assertEquals(35, Quest.getQuestStageValue(result.getStage(2)));

        assertTrue(result.validQuest());
    }
}