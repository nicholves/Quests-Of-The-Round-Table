package Game;

import AdventureCard.AdventureCard;
import AdventureCard.WeaponCard;
import AdventureCard.FoeCard;
import EventCard.EventCard;
import EventCard.EventType;
import EventCard.QuestCard;
import Quest.Quest;
import Attack.Attack;
import EventCard.EventDeckCard;
import EventCard.EventCardType;
import Window.Window;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Player.Player;

import static org.junit.jupiter.api.Assertions.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;
import java.util.regex.Pattern;

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


    @Test
    @DisplayName("The player can add weapon card from their hand to the current attack")
    public void RESP_21_TEST_01() {
        Player player = new Player(3);

        String input = "1\nQuit\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();
        Game game = new Game(scanner, new PrintWriter(output));

        player.addCardToHand(new FoeCard(5));
        player.addCardToHand(new WeaponCard('L', 20));

        Attack attack = game.buildAttack(player);

        assertEquals(1, attack.getCardsInAttack().size());
        assertEquals(1, player.getHandSize());

        assertEquals(20, attack.computeAttackValue());
    }

    @Test
    @DisplayName("The player cannot add a duplicate weapon card from their hand to the current attack")
    public void RESP_21_TEST_02() {
        Player player = new Player(3);

        String input = "1\n1\nQuit\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();
        Game game = new Game(scanner, new PrintWriter(output));

        player.addCardToHand(new FoeCard(5));
        player.addCardToHand(new WeaponCard('L', 20));
        player.addCardToHand(new WeaponCard('L', 20));

        Attack attack = game.buildAttack(player);

        assertEquals(1, attack.getCardsInAttack().size());
        assertEquals(2, player.getHandSize());

        assertEquals(20, attack.computeAttackValue());

        String result = output.toString();
        assertTrue(result.contains("duplicate weapon"));
    }

    @Test
    @DisplayName("The player cannot add a foe card from their hand to the current attack")
    public void RESP_21_TEST_03() {
        Player player = new Player(3);

        String input = "0\n1\nQuit\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();
        Game game = new Game(scanner, new PrintWriter(output));

        player.addCardToHand(new FoeCard(5));
        player.addCardToHand(new WeaponCard('L', 20));

        Attack attack = game.buildAttack(player);

        assertEquals(1, attack.getCardsInAttack().size());
        assertEquals(1, player.getHandSize());

        assertEquals(20, attack.computeAttackValue());

        String result = output.toString();
        assertTrue(result.contains("Foe cards"));
    }

    @Test
    @DisplayName("The game validates a reasonable index is chosen for adding weapons to an attack")
    public void RESP_21_TEST_04() {
        Player player = new Player(3);

        String input = "-1\n5\n1\nQuit\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();
        Game game = new Game(scanner, new PrintWriter(output));

        player.addCardToHand(new FoeCard(5));
        player.addCardToHand(new WeaponCard('L', 20));

        Attack attack = game.buildAttack(player);

        assertEquals(1, attack.getCardsInAttack().size());
        assertEquals(1, player.getHandSize());

        assertEquals(20, attack.computeAttackValue());

        String result = output.toString();
        assertTrue(result.contains("invalid index"));
    }

    @Test
    @DisplayName("The game correctly identifies an attack with equal value to the quest stage as succeeding")
    public void RESP_23_TEST_01() {
        Quest quest = new Quest(new QuestCard(1));

        ArrayList<AdventureCard> questStage = new ArrayList<AdventureCard>();
        questStage.add(new FoeCard(5));
        quest.addStage(questStage);

        Attack attack = new Attack();
        attack.addCardToAttack(new WeaponCard('D', 5));

        assertTrue(Game.resolveQuestStage(attack, quest, 0));
    }

    @Test
    @DisplayName("The game correctly identifies an attack with greater value to the quest stage as succeeding")
    public void RESP_23_TEST_02() {
        Quest quest = new Quest(new QuestCard(1));

        ArrayList<AdventureCard> questStage = new ArrayList<AdventureCard>();
        questStage.add(new FoeCard(5));
        quest.addStage(questStage);

        Attack attack = new Attack();
        attack.addCardToAttack(new WeaponCard('D', 5));
        attack.addCardToAttack(new WeaponCard('L', 20));

        assertTrue(Game.resolveQuestStage(attack, quest, 0));
    }

    @Test
    @DisplayName("The game correctly identifies an attack with less value to the quest stage as succeeding")
    public void RESP_23_TEST_03() {
        Quest quest = new Quest(new QuestCard(1));

        ArrayList<AdventureCard> questStage = new ArrayList<AdventureCard>();
        questStage.add(new FoeCard(10));
        quest.addStage(questStage);

        Attack attack = new Attack();
        attack.addCardToAttack(new WeaponCard('D', 5));

        assertFalse(Game.resolveQuestStage(attack, quest, 0));
    }

    @Test
    @DisplayName("the game draws the appropriate number of cards for the sponsor and trims their hand when resolving a quest")
    public void RESP_24_TEST_01() {
        Game game = new Game();
        Quest quest = new Quest(new QuestCard(3));

        ArrayList<AdventureCard> stage1 = new ArrayList<AdventureCard>();
        ArrayList<AdventureCard> stage2 = new ArrayList<AdventureCard>();
        ArrayList<AdventureCard> stage3 = new ArrayList<AdventureCard>();

        stage1.add(new FoeCard(5));
        stage2.add(new FoeCard(10));
        stage3.add(new FoeCard(15));

        quest.addStage(stage1);
        quest.addStage(stage2);
        quest.addStage(stage3);

        Player sponsor = new Player(3);

        game.resolveQuest(quest, sponsor);

        assertEquals(6, sponsor.getHandSize());
    }

    @Test
    @DisplayName("the game discards cards approprietly when resolving a quest")
    public void RESP_24_TEST_02() {
        Game game = new Game();
        Quest quest = new Quest(new QuestCard(3));

        ArrayList<AdventureCard> stage1 = new ArrayList<AdventureCard>();
        ArrayList<AdventureCard> stage2 = new ArrayList<AdventureCard>();
        ArrayList<AdventureCard> stage3 = new ArrayList<AdventureCard>();

        stage1.add(new FoeCard(5));
        stage2.add(new FoeCard(10));
        stage3.add(new FoeCard(15));

        quest.addStage(stage1);
        quest.addStage(stage2);
        quest.addStage(stage3);

        Player sponsor = new Player(3);

        game.resolveQuest(quest, sponsor);

        assertEquals(1, game.getEventDeck().getDiscardPile().size());
        assertEquals(3, game.getAdventureDeck().getDiscardPile().size());
    }

    @Test
    @DisplayName("the game draws trims the sponsor's hand if their hand size is greater than 12 after resolving a quest")
    public void RESP_24_TEST_03() {
        String input = "\n0\n";
        Scanner scanner = new Scanner(input);
        StringWriter output = new StringWriter();

        Quest quest = new Quest(new QuestCard(3));
        Game game = new Game(scanner, new PrintWriter(output));

        ArrayList<AdventureCard> stage1 = new ArrayList<AdventureCard>();
        ArrayList<AdventureCard> stage2 = new ArrayList<AdventureCard>();
        ArrayList<AdventureCard> stage3 = new ArrayList<AdventureCard>();

        stage1.add(new FoeCard(5));
        stage2.add(new FoeCard(10));
        stage3.add(new FoeCard(15));

        quest.addStage(stage1);
        quest.addStage(stage2);
        quest.addStage(stage3);

        Player sponsor = new Player(3);

        for (int i = 0; i < 7; ++i) {
            sponsor.addCardToHand(new FoeCard(5));
        }

        game.resolveQuest(quest, sponsor);

        assertEquals(12, sponsor.getHandSize());
    }

    @Test
    @DisplayName("Compulsory A test for Assignment 1")
    public void A_TEST_JP_SCENARIO() {
        String userInput = "";


        userInput += "\n"; // p1 confirms the drawn card (Quest of 4 stages)

        userInput += "n\n"; // p1 declines to accept sponsoring
        userInput += "y\n"; // p2 accepts sponsoring

        // p2 builds the quest from the slides
        userInput += "0\n6\nQuit\n"; // Stage 1 being a thief and a horse
        userInput += "1\n4\nQuit\n"; // Stage 2 being a robber-knight and a sword
        userInput += "1\n2\n3\nQuit\n"; // Stage 3 being a robber-knight and a dagger and a Battleaxe
        userInput += "1\n2\nQuit\n"; // Stage 4 being a giant and a Battleaxe


        // 6) Stage 1:
        // all players participate
        userInput += "y\n";
        userInput += "y\n";
        userInput += "y\n";

        // p1's draw
        userInput += "y\n"; // p1 confirms control
        userInput += "0\n"; // p1 discards an F5 to trimming

        // p3's draw
        userInput += "y\n"; // p3 confirms control
        userInput += "0\n"; // p3 discards an F5 to trimming

        // p4's draw
        userInput += "y\n"; // p4 confirms control
        userInput += "0\n"; // p4 discards an F5 to trimming

        // p1's attack
        userInput += "\n"; // p1 confirms control
        userInput += "4\n"; // p1 adds a dagger to their attack
        userInput += "4\n"; // p1 adds a sword to their attack
        userInput += "Quit\n"; // p1 confirms attack

        // p3's attack
        userInput += "\n"; // p3 confirms control
        userInput += "5\n"; // p3 adds a sword to their attack
        userInput += "3\n"; // p3 adds a dagger to their attack
        userInput += "Quit\n"; // p3 confirms attack

        // p4's attack
        userInput += "\n"; // p4 confirms control
        userInput += "4\n"; // p4 adds a dagger to their attack
        userInput += "5\n"; // p4 adds a horse to their attack
        userInput += "Quit\n"; // p4 confirms attack

        // all attack are sufficient
        userInput += "\n"; // confirm the victory screen

        // 7) Stage 2:
        // all players participate
        userInput += "y\n";
        userInput += "y\n";
        userInput += "y\n";


        // p1's attack
        userInput += "\n"; // p1 confirms control
        userInput += "6\n"; // p1 adds a horse to their attack
        userInput += "5\n"; // p1 adds a sword to their attack
        userInput += "Quit\n"; // p1 confirms attack

        // p3's attack
        userInput += "\n"; // p3 confirms control
        userInput += "8\n"; // p3 adds an axe to their attack
        userInput += "4\n"; // p3 adds a sword to their attack
        userInput += "Quit\n"; // p3 confirms attack

        // p4's attack
        userInput += "\n"; // p4 confirms control
        userInput += "5\n"; // p4 adds a horse to their attack
        userInput += "6\n"; // p4 adds an axe to their attack
        userInput += "Quit\n"; // p4 confirms attack


        // p1's attack is insufficient p1 is eliminated
        userInput += "\n"; // confirm the victory screen

        // 8) Stage 3:
        // all 2 players participate
        userInput += "y\n";
        userInput += "y\n";

        // p3's attack
        userInput += "\n"; // p3 confirms control
        userInput += "9\n"; // p3 adds a lance to their attack
        userInput += "5\n"; // p3 adds a horse to their attack
        userInput += "4\n"; // p3 adds a sword to their attack
        userInput += "Quit\n"; // p3 confirms attack

        // p4's attack
        userInput += "\n"; // p4 confirms control
        userInput += "6\n"; // p4 adds an axe to their attack
        userInput += "4\n"; // p4 adds a sword to their attack
        userInput += "6\n"; // p4 adds a lance to their attack
        userInput += "Quit\n"; // p4 confirms attack

        // Confirm the winners of this stage, p3 and p4
        userInput += "\n";

        // 8) Stage 4:
        // all 2 players participate
        userInput += "y\n";
        userInput += "y\n";

        // p3's attack
        userInput += "\n"; // p3 confirms control
        userInput += "6\n"; // p3 adds an axe to their attack
        userInput += "5\n"; // p3 adds a horse to their attack
        userInput += "5\n"; // p3 adds a Lance to their attack
        userInput += "Quit\n"; // p3 confirms attack

        // p4's attack
        userInput += "\n"; // p4 confirms control
        userInput += "3\n"; // p4 adds a dagger to their attack
        userInput += "3\n"; // p4 adds a sword to their attack
        userInput += "3\n"; // p4 adds a lance to their attack
        userInput += "4\n"; // p4 adds an excalibur to their attack
        userInput += "Quit\n"; // p4 confirms attack

        // Confirm the winners of this stage, just p4 who is victorious in the entire quest
        userInput += "\n";


        // trim the hand of the quest sponsor (p2) who now has to discard 4 cards
        userInput += "\n"; // confirm p2 has control
        userInput += "0\n0\n0\n0\n"; // we just discard the four lowest foes in their hand randomly

        Scanner scanner = new Scanner(userInput);

        boolean useSTDOut = true;
        Writer output = new StringWriter();
        Game game = new Game(scanner, useSTDOut ? new PrintWriter(System.out) : new PrintWriter(output));
        // 1) Start game, decks are created, hands of the 4 players are set up with random cards
        game.initGame();

        // 2) Rig the 4 hands to the hold the cards of the 4 posted initial hands

        // return all the player hands to the adventure deck
        for (int i = 0; i < 4; ++i) {
            game.getAdventureDeck().getDeck().addAll(game.getPlayer(i).getHand());
            game.getPlayer(i).getHand().clear();
        }

        List<AdventureCard> p1Hand =  game.getPlayer(0).getHand();
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
            game.getAdventureDeck().getDeck().remove(card);
        }


        List<AdventureCard> p2Hand =  game.getPlayer(1).getHand();
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
            game.getAdventureDeck().getDeck().remove(card);
        }

        List<AdventureCard> p3Hand =  game.getPlayer(2).getHand();
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
            game.getAdventureDeck().getDeck().remove(card);
        }

        List<AdventureCard> p4Hand =  game.getPlayer(3).getHand();
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
            game.getAdventureDeck().getDeck().remove(card);
        }

        // ensure we have the normal amount of cards
        assertEquals(12, p1Hand.size());
        assertEquals(12, p2Hand.size());
        assertEquals(12, p3Hand.size());
        assertEquals(12, p4Hand.size());

        int numPlayers = 4;
        int initialCards = 100;
        assertEquals(initialCards - (numPlayers * 12), game.getAdventureDeck().getDeckSize());


        Stack<EventDeckCard> eventDeck = game.getEventDeck().getDeck();

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
        Stack<AdventureCard> aDeck = game.getAdventureDeck().getDeck();

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

        assertEquals(initialCards - (numPlayers * 12), game.getAdventureDeck().getDeckSize());

        // run the scenario
        game.drawFromEventDeck(game.getPlayer(0));


        // Assertions
        assertEquals(0, game.getPlayer(0).getNumShields()); // P1 has zero shields

        // P1 Assertions
        Window window = game.getOutputWindow();
        StringWriter player1Hand = new StringWriter();
        window.displayPlayerHand(scanner, new PrintWriter(player1Hand), game.getPlayer(0));

        String result = player1Hand.toString();

        assertTrue(result.contains("F5, F10, F15, F15, F30, H10, B15, B15, L20")); // P1 should end with the following hand


        // ---------------------
        // P3 Assertions
        assertEquals(0, game.getPlayer(2).getNumShields()); // P3 has zero shields

        StringWriter player3Hand = new StringWriter();
        window.displayPlayerHand(scanner, new PrintWriter(player3Hand), game.getPlayer(2));

        result = player3Hand.toString();

        assertTrue(result.contains("F5, F5, F15, F30, S10")); // P3 should end with the following hand


        //----------------------
        // P4 Assertions
        assertEquals(4, game.getPlayer(3).getNumShields()); // P4 has 4 shields

        StringWriter player4Hand = new StringWriter();
        window.displayPlayerHand(scanner, new PrintWriter(player4Hand), game.getPlayer(3));

        result = player4Hand.toString();

        assertTrue(result.contains("F15, F15, F40, L20")); // P4 should end with the following hand


        //----------------------
        // P2 Assertions
        assertEquals(12, game.getPlayer(1).getHandSize());
    }

    // this small helper function to remove a set of cards from the deck we don't want duplicates of
    // Note cannot use removeAll() because we want to remove exactly one copy of each card in cardsToRemove
    private void removeFromDeck(Stack<AdventureCard> deck,  Queue<AdventureCard> cardsToRemove) {
        for (AdventureCard card : cardsToRemove) {
            deck.remove(card);
        }
    }
}