import AdventureCard.AdventureCard;
import EventCard.EventCard;
import EventCard.EventType;
import EventCard.EventCardType;
import EventCard.EventDeckCard;
import EventCard.QuestCard;
import AdventureCard.WeaponCard;
import AdventureCard.FoeCard;
import Game.Game;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import java.io.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class StepDefinitions {
    private Game game;
    private PrintWriter writer;
    private Scanner scanner;
    private PrintWriter userInput;
    private PipedOutputStream pos;
    private PipedInputStream pis;
    private StringWriter output;


    @Given("the A1 Scenario Deck is setup")
    public void the_A1_Scenario_Deck_is_setup()  {
        try {
            pos = new PipedOutputStream();
            pis = new PipedInputStream(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        writer = new PrintWriter(new StringWriter());
        userInput = new PrintWriter(pos);
        scanner = new Scanner(pis);
        game = new Game(scanner, writer);
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
    }

    @When("p2 decides to sponsor")
    public void P2_Sponsors() {
        userInput.print("\n"); // p1 confirms the drawn card (Quest of 4 stages)

        userInput.print("n\n"); // p1 declines to accept sponsoring
        userInput.print("y\n"); // p2 accepts sponsoring
    }

    @When("p2 builds the quest from the slides")
    public void P2_Builds_Quest() {
        // p2 builds the quest from the slides
        userInput.print("0\n6\nQuit\n"); // Stage 1 being a thief and a horse
        userInput.print("1\n4\nQuit\n"); // Stage 2 being a robber-knight and a sword
        userInput.print("1\n2\n3\nQuit\n"); // Stage 3 being a robber-knight and a dagger and a Battleaxe
        userInput.print("1\n2\nQuit\n"); // Stage 4 being a giant and a Battleaxe
    }

    @When("all {int} Players participate")
    public void All_Players_Participate(int numPlayers) {
        for (int i = 0; i < numPlayers; ++i) {
            userInput.print("y\n");
        }
    }

    @When("players act in stage 1")
    public void Players_Stage_1() {
        // p1's draw
        userInput.print("y\n"); // p1 confirms control
        userInput.print("0\n"); // p1 discards an F5 to trimming

        // p3's draw
        userInput.print("y\n"); // p3 confirms control
        userInput.print("0\n"); // p3 discards an F5 to trimming

        // p4's draw
        userInput.print("y\n"); // p4 confirms control
        userInput.print("0\n"); // p4 discards an F5 to trimming

        // p1's attack
        userInput.print("\n"); // p1 confirms control
        userInput.print("4\n"); // p1 adds a dagger to their attack
        userInput.print("4\n"); // p1 adds a sword to their attack
        userInput.print("Quit\n"); // p1 confirms attack

        // p3's attack
        userInput.print("\n"); // p3 confirms control
        userInput.print("5\n"); // p3 adds a sword to their attack
        userInput.print("3\n"); // p3 adds a dagger to their attack
        userInput.print("Quit\n"); // p3 confirms attack

        // p4's attack
        userInput.print("\n"); // p4 confirms control
        userInput.print("4\n"); // p4 adds a dagger to their attack
        userInput.print("5\n"); // p4 adds a horse to their attack
        userInput.print("Quit\n"); // p4 confirms attack

        // all attack are sufficient
        userInput.print("\n"); // confirm the victory screen
    }

    @When("players act in stage 2")
    public void Players_Stage_2() {
        // p1's attack
        userInput.print("\n"); // p1 confirms control
        userInput.print("6\n"); // p1 adds a horse to their attack
        userInput.print("5\n"); // p1 adds a sword to their attack
        userInput.print("Quit\n"); // p1 confirms attack

        // p3's attack
        userInput.print("\n"); // p3 confirms control
        userInput.print("8\n"); // p3 adds an axe to their attack
        userInput.print("4\n"); // p3 adds a sword to their attack
        userInput.print("Quit\n"); // p3 confirms attack

        // p4's attack
        userInput.print("\n"); // p4 confirms control
        userInput.print("5\n"); // p4 adds a horse to their attack
        userInput.print("6\n"); // p4 adds an axe to their attack
        userInput.print("Quit\n"); // p4 confirms attack


        // p1's attack is insufficient p1 is eliminated
        userInput.print("\n"); // confirm the victory screen
    }

    @When("players act in stage 3")
    public void Players_Stage_3() {
        // p3's attack
        userInput.print("\n"); // p3 confirms control
        userInput.print("9\n"); // p3 adds a lance to their attack
        userInput.print("5\n"); // p3 adds a horse to their attack
        userInput.print("4\n"); // p3 adds a sword to their attack
        userInput.print("Quit\n"); // p3 confirms attack

        // p4's attack
        userInput.print("\n"); // p4 confirms control
        userInput.print("6\n"); // p4 adds an axe to their attack
        userInput.print("4\n"); // p4 adds a sword to their attack
        userInput.print("6\n"); // p4 adds a lance to their attack
        userInput.print("Quit\n"); // p4 confirms attack

        // Confirm the winners of this stage, p3 and p4
        userInput.print("\n");
    }

    @When("players act in stage 4")
    public void Players_Stage_4() {
        // p3's attack
        userInput.print("\n"); // p3 confirms control
        userInput.print("6\n"); // p3 adds an axe to their attack
        userInput.print("5\n"); // p3 adds a horse to their attack
        userInput.print("5\n"); // p3 adds a Lance to their attack
        userInput.print("Quit\n"); // p3 confirms attack

        // p4's attack
        userInput.print("\n"); // p4 confirms control
        userInput.print("3\n"); // p4 adds a dagger to their attack
        userInput.print("3\n"); // p4 adds a sword to their attack
        userInput.print("3\n"); // p4 adds a lance to their attack
        userInput.print("4\n"); // p4 adds an excalibur to their attack
        userInput.print("Quit\n"); // p4 confirms attack

        // Confirm the winners of this stage, just p4 who is victorious in the entire quest
        userInput.print("\n");
    }

    @When("the sponsoror trims their hand")
    public void sponsor_trim() {
        // trim the hand of the quest sponsor (p2) who now has to discard 4 cards
        userInput.print("\n"); // confirm p2 has control
        userInput.print("0\n0\n0\n0\n"); // we just discard the four lowest foes in their hand randomly
    }

    @When("the sponsoror trims their highest {int} cards")
    public void sponsor_trim_best(int numCards) {
        // trim the hand of the quest sponsor who now has to discard 4 cards
        userInput.print("\n");
        for (int i = 0; i < numCards; ++i) {
            userInput.print("11\n"); // we just discard the numCards highest weapons from his hand
        }
    }

    @When("player {int} draws from the event deck for their turn")
    public void player_take_turn(int playerid) {
        userInput.close(); // this confirms the user input we inputted
        game.drawFromEventDeck(game.getPlayer(playerid - 1));
    }

    @Then("player {int} should have {int} shields")
    public void player_num_should_have_num_shields(int playerid, int numShields) {
        assertEquals(numShields, game.getPlayer(playerid - 1).getNumShields());
    }

    @Then("player {int} should have {int} cards in hand")
    public void player_num_should_have_num_cards_in_hand(int playerid, int numCards) {
        assertEquals(numCards, game.getPlayer(playerid - 1).getHandSize());
    }

    // this small helper function to remove a set of cards from the deck we don't want duplicates of
    // Note cannot use removeAll() because we want to remove exactly one copy of each card in cardsToRemove
    private void removeFromDeck(Stack<AdventureCard> deck,  Queue<AdventureCard> cardsToRemove) {
        for (AdventureCard card : cardsToRemove) {
            deck.remove(card);
        }
    }

    // -------------------------------------------------------------------------------------------------------- //
    // -------------------------------------------------------------------------------------------------------- //
    // -------------------------------------------------------------------------------------------------------- //
    // -------------------------------------------------------------------------------------------------------- //
    // -------------------------------------------------------------------------------------------------------- //
    // -------------------------------------------------------------------------------------------------------- //
    // -------------------------------------------------------------------------------------------------------- //

    // 0 winner scenario steps



    @Given("The 0 Winner Scenario Deck is setup")
    public void the_0_Winner_Scenario_Deck_is_setup()  {
        try {
            pos = new PipedOutputStream();
            pis = new PipedInputStream(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        writer = new PrintWriter(new StringWriter());
        userInput = new PrintWriter(pos);
        scanner = new Scanner(pis);
        game = new Game(scanner, writer);
        game.initGame();

        // 2) Rig the 4 hands to the hold the cards for the scenario

        // return all the player hands to the adventure deck
        for (int i = 0; i < 4; ++i) {
            game.getAdventureDeck().getDeck().addAll(game.getPlayer(i).getHand());
            game.getPlayer(i).getHand().clear();
        }

        // p1 will have nothing but high level foes
        List<AdventureCard> p1Hand =  game.getPlayer(0).getHand();
        // set up P1 hand
        p1Hand.add(new FoeCard(70));
        p1Hand.add(new FoeCard(50));
        p1Hand.add(new FoeCard(50));
        p1Hand.add(new FoeCard(40));
        p1Hand.add(new FoeCard(40));
        p1Hand.add(new FoeCard(35));
        p1Hand.add(new FoeCard(35));
        p1Hand.add(new FoeCard(35));
        p1Hand.add(new FoeCard(35));
        p1Hand.add(new FoeCard(30));
        p1Hand.add(new FoeCard(30));
        p1Hand.add(new FoeCard(30));

        for (AdventureCard card : p1Hand) {
            game.getAdventureDeck().getDeck().remove(card);
        }

        // all other players will only have low level weapons as much as possible while each having at least one dagger
        List<AdventureCard> p2Hand =  game.getPlayer(1).getHand();
        // set up P2 hand
        p2Hand.add(new WeaponCard('D', 5));
        p2Hand.add(new WeaponCard('D', 5));
        p2Hand.add(new WeaponCard('D', 5));
        p2Hand.add(new WeaponCard('D', 5));


        p2Hand.add(new WeaponCard('H', 10));
        p2Hand.add(new WeaponCard('H', 10));
        p2Hand.add(new WeaponCard('H', 10));
        p2Hand.add(new WeaponCard('H', 10));
        p2Hand.add(new WeaponCard('H', 10));
        p2Hand.add(new WeaponCard('H', 10));
        p2Hand.add(new WeaponCard('H', 10));
        p2Hand.add(new WeaponCard('H', 10));

        for (AdventureCard card : p2Hand) {
            game.getAdventureDeck().getDeck().remove(card);
        }

        List<AdventureCard> p3Hand =  game.getPlayer(2).getHand();
        // set up P3 hand
        p3Hand.add(new WeaponCard('D', 5));

        p3Hand.add(new WeaponCard('S', 10));
        p3Hand.add(new WeaponCard('S', 10));
        p3Hand.add(new WeaponCard('S', 10));
        p3Hand.add(new WeaponCard('S', 10));
        p3Hand.add(new WeaponCard('S', 10));
        p3Hand.add(new WeaponCard('S', 10));
        p3Hand.add(new WeaponCard('S', 10));
        p3Hand.add(new WeaponCard('S', 10));
        p3Hand.add(new WeaponCard('S', 10));
        p3Hand.add(new WeaponCard('S', 10));
        p3Hand.add(new WeaponCard('S', 10));

        for (AdventureCard card : p3Hand) {
            game.getAdventureDeck().getDeck().remove(card);
        }

        List<AdventureCard> p4Hand =  game.getPlayer(3).getHand();
        // set up P4 hand
        p4Hand.add(new WeaponCard('D', 5));
        p4Hand.add(new WeaponCard('S', 10));
        p4Hand.add(new WeaponCard('S', 10));
        p4Hand.add(new WeaponCard('S', 10));
        p4Hand.add(new WeaponCard('B', 15));
        p4Hand.add(new WeaponCard('B', 15));
        p4Hand.add(new WeaponCard('B', 15));
        p4Hand.add(new WeaponCard('B', 15));
        p4Hand.add(new WeaponCard('B', 15));
        p4Hand.add(new WeaponCard('B', 15));
        p4Hand.add(new WeaponCard('B', 15));
        p4Hand.add(new WeaponCard('B', 15));

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

        // remove one quest of size 2 from the deck somewhere
        QuestCard cardToRemove = null;
        for (EventDeckCard card : eventDeck) {
            if (card.getType() == EventCardType.QUESTTYPE) {
                QuestCard qCard = (QuestCard)card;
                if (qCard.getValue() == 2) {
                    cardToRemove = qCard;
                    break;
                }
            }
        }
        eventDeck.remove(cardToRemove);

        // ensure the top card is a quest of size 4
        eventDeck.push(new QuestCard(2));

        // build the top of the adventure deck how we need it
        Stack<AdventureCard> aDeck = game.getAdventureDeck().getDeck();

        Queue<AdventureCard> newCards = new LinkedList<AdventureCard>();

        // 3 lances will ensure no small foe or small weapon is drawn by p2, p3, or p4
        newCards.add(new WeaponCard('L', 20));
        newCards.add(new WeaponCard('L', 20));
        newCards.add(new WeaponCard('L', 20));

        // remove the cards we don't want duplicates of in the deck
        removeFromDeck(aDeck, newCards);

        // add the cards we do want on top
        aDeck.addAll(newCards);

        assertEquals(initialCards - (numPlayers * 12), game.getAdventureDeck().getDeckSize());
        assertEquals(17, eventDeck.size());
    }

    @When("p1 decides to sponsor")
    public void P1_Sponsor() {
        userInput.print("\n"); // p1 confirms the drawn card (Quest of 2 stages)

        userInput.print("y\n"); // p1 accepts sponsoring
    }

    @When("p1 builds the quest with two massive monsters")
    public void Build_Big_Quest() {
        userInput.print("10\nQuit\n"); // Stage 1 being a foe 70
        userInput.print("10\nQuit\n"); // Stage 2 being a foe 50
    }

    @When("all players discard their largest card")
    public void Discard_Biggest_Cards() {
        // p2's draw
        userInput.print("y\n"); // p2 confirms control
        userInput.print("12\n"); // p2 discards their best card

        // p3's draw
        userInput.print("y\n"); // p3 confirms control
        userInput.print("12\n"); // p3 discards their best card

        // p4's draw
        userInput.print("y\n"); // p4 confirms control
        userInput.print("12\n"); // p4 discards their best card
    }

    @When("Player {int} uses a dagger")
    public void Dagger_User(int playerNum) {
        userInput.print("\n"); // confirms control
        userInput.print("0\n");
        userInput.print("Quit\n"); // confirms attack
    }

    @When("all players lose")
    public void Confirm_Loss() {
        userInput.print("\n"); // confirm the defeat screen
    }

    @When("Player {int} discards {int} cards")
    public void Discard_cards_from_sponsorship_end(int playerNum, int numCards) {
        // trim the hand of the quest sponsor who now has to discard numCards cards
        userInput.print("\n"); // confirm player has control
        for (int i = 0; i < numCards; ++i) {
            userInput.print("0\n"); // we just discard the lowest foes in their hand randomly
        }
    }

    @When("all Players participate")
    public void All_Players_Participate() {
        for (int i = 0; i < 3; ++i) {
            userInput.print("y\n");
        }
    }

    // -------------------------------------------------------------------------------------------------------- //
    // -------------------------------------------------------------------------------------------------------- //
    // -------------------------------------------------------------------------------------------------------- //
    // -------------------------------------------------------------------------------------------------------- //
    // -------------------------------------------------------------------------------------------------------- //
    // -------------------------------------------------------------------------------------------------------- //
    // -------------------------------------------------------------------------------------------------------- //

    // 1 winner scenario with events steps

    @Given("The 1 Winner Scenario With Events Deck is setup")
    public void The_1_Winner_Scenario_With_Events_Deck_is_setup()  {
        try {
            pos = new PipedOutputStream();
            pis = new PipedInputStream(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        output = new StringWriter();
        writer = new PrintWriter(output);
        userInput = new PrintWriter(pos);
        scanner = new Scanner(pis);
        game = new Game(scanner, writer);
        game.initGame();



        // player hand shenanigans



        // return all the player hands to the adventure deck
        for (int i = 0; i < 4; ++i) {
            game.getAdventureDeck().getDeck().addAll(game.getPlayer(i).getHand());
            game.getPlayer(i).getHand().clear();
        }

        List<AdventureCard> p1Hand =  game.getPlayer(0).getHand();
        // set up P1 hand
        p1Hand.add(new FoeCard(5));
        p1Hand.add(new FoeCard(10));
        p1Hand.add(new FoeCard(15));
        p1Hand.add(new FoeCard(20));


        p1Hand.add(new FoeCard(5));
        p1Hand.add(new FoeCard(10));
        p1Hand.add(new FoeCard(15));


        p1Hand.add(new FoeCard(70));
        p1Hand.add(new FoeCard(50));
        p1Hand.add(new FoeCard(50));
        p1Hand.add(new FoeCard(40));
        p1Hand.add(new FoeCard(40));

        for (AdventureCard card : p1Hand) {
            game.getAdventureDeck().getDeck().remove(card);
        }


        List<AdventureCard> p2Hand =  game.getPlayer(1).getHand();
        // set up P2 hand
        p2Hand.add(new WeaponCard('S', 10));
        p2Hand.add(new WeaponCard('S', 10));
        p2Hand.add(new WeaponCard('B', 15));
        p2Hand.add(new WeaponCard('L', 20));

        p2Hand.add(new WeaponCard('D', 5));
        p2Hand.add(new WeaponCard('S', 10));
        p2Hand.add(new WeaponCard('B', 15));


        p2Hand.add(new FoeCard(5));
        p2Hand.add(new FoeCard(5));
        p2Hand.add(new FoeCard(5));
        p2Hand.add(new FoeCard(5));
        p2Hand.add(new FoeCard(5));

        for (AdventureCard card : p2Hand) {
            game.getAdventureDeck().getDeck().remove(card);
        }

        List<AdventureCard> p3Hand =  game.getPlayer(2).getHand();
        // set up P3 hand
        p3Hand.add(new WeaponCard('D', 5));
        p3Hand.add(new WeaponCard('S', 10));
        p3Hand.add(new WeaponCard('B', 15));
        p3Hand.add(new WeaponCard('L', 20));

        p3Hand.add(new WeaponCard('D', 5));
        p3Hand.add(new WeaponCard('S', 10));
        p3Hand.add(new WeaponCard('B', 15));

        p3Hand.add(new FoeCard(10));
        p3Hand.add(new FoeCard(10));
        p3Hand.add(new FoeCard(10));
        p3Hand.add(new FoeCard(10));
        p3Hand.add(new FoeCard(10));



        for (AdventureCard card : p3Hand) {
            game.getAdventureDeck().getDeck().remove(card);
        }

        List<AdventureCard> p4Hand =  game.getPlayer(3).getHand();
        // set up P4 hand
        p4Hand.add(new WeaponCard('D', 5));
        p4Hand.add(new WeaponCard('S', 10));
        p4Hand.add(new WeaponCard('B', 15));
        p4Hand.add(new WeaponCard('L', 20));

        p4Hand.add(new WeaponCard('D', 5));
        p4Hand.add(new WeaponCard('S', 10));
        p4Hand.add(new WeaponCard('D', 5));

        p4Hand.add(new FoeCard(15));
        p4Hand.add(new FoeCard(15));
        p4Hand.add(new FoeCard(15));
        p4Hand.add(new FoeCard(15));
        p4Hand.add(new FoeCard(15));


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



        // event deck shenanigans


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

        // remove one of each event type from the deck
        EventCard prosper = null;
        EventCard plague = null;
        EventCard queen = null;
        for (EventDeckCard card : eventDeck) {
            if (card.getType() == EventCardType.EVENTTYPE) {
                EventCard qCard = (EventCard) card;
                if (qCard.getEventType() == EventType.QUEENS_FAVOR) {
                    queen = qCard;
                }
                else if (qCard.getEventType() == EventType.PLAGUE) {
                    plague = qCard;
                }

                else if (qCard.getEventType() == EventType.PROSPERITY) {
                    prosper = qCard;
                }

            }
        }
        eventDeck.remove(prosper);
        eventDeck.remove(plague);
        eventDeck.remove(queen);


        // ensure the fifth from the top quest card is a quest of 3 stages
        eventDeck.push(new QuestCard(3));
        // ensure the fourth from the top quest card is a queens favor
        eventDeck.push(new EventCard(EventType.QUEENS_FAVOR));
        // ensure the third from the top quest card is a prosperity
        eventDeck.push(new EventCard(EventType.PROSPERITY));
        // ensure the second from the top quest card is a plague
        eventDeck.push(new EventCard(EventType.PLAGUE));
        // ensure the top card is a quest of size 4
        eventDeck.push(new QuestCard(4));




        // adventure deck shenanigans



        // build the top of the adventure deck how we need it
        Stack<AdventureCard> aDeck = game.getAdventureDeck().getDeck();

        Queue<AdventureCard> newCards = new LinkedList<AdventureCard>();


        // quest two
        // 2 draws for each player in stage 2 of quest one
        newCards.add(new WeaponCard('H', 10));
        newCards.add(new WeaponCard('H', 10));

        // 2 draws for each player in stage 2 of quest one
        newCards.add(new WeaponCard('H', 10));
        newCards.add(new WeaponCard('H', 10));

        // 3 draws for each player in stage 1 of quest one
        newCards.add(new WeaponCard('B', 15));
        newCards.add(new WeaponCard('H', 10));
        newCards.add(new FoeCard(30));


        // queen's favor card draw
        // p4 draws
        newCards.add(new FoeCard(30));
        newCards.add(new FoeCard(30));


        // propserity card draw
        // p4 draws
        newCards.add(new FoeCard(25));
        newCards.add(new WeaponCard('E', 30));

        // p3 draws
        newCards.add(new FoeCard(25));
        newCards.add(new WeaponCard('E', 30));

        // p2 draws
        newCards.add(new FoeCard(30));
        newCards.add(new WeaponCard('L', 20));

        // p1 draws
        newCards.add(new WeaponCard('S', 10));
        newCards.add(new WeaponCard('S', 10));


        // quest one

        // p1 cards from sponsoring
        newCards.add(new WeaponCard('H', 10));
        newCards.add(new WeaponCard('H', 10));
        newCards.add(new WeaponCard('H', 10));
        newCards.add(new WeaponCard('H', 10));
        newCards.add(new WeaponCard('H', 10));
        newCards.add(new WeaponCard('H', 10));
        newCards.add(new WeaponCard('H', 10));
        newCards.add(new WeaponCard('S', 10));

        // 3 draws for each player in stage 4 of quest one
        newCards.add(new FoeCard(25));
        newCards.add(new FoeCard(25));
        newCards.add(new FoeCard(25));

        // 3 draws for each player in stage 3 of quest one
        newCards.add(new FoeCard(20));
        newCards.add(new FoeCard(20));
        newCards.add(new FoeCard(25));

        // 3 draws for each player in stage 2 of quest one
        newCards.add(new FoeCard(20));
        newCards.add(new FoeCard(20));
        newCards.add(new FoeCard(20));

        // 3 draws for each player in stage 1 of quest one
        newCards.add(new FoeCard(5));
        newCards.add(new FoeCard(15));
        newCards.add(new FoeCard(20));



        // remove the cards we don't want duplicates of in the deck
        removeFromDeck(aDeck, newCards);

        // add the cards we do want on top
        aDeck.addAll(newCards);

        assertEquals(initialCards - (numPlayers * 12), game.getAdventureDeck().getDeckSize());
        assertEquals(17, eventDeck.size());
    }

    @When("The game runs")
    public void game_runs() {
        userInput.close(); // this confirms the user input we inputted
        game.gameLoop();
    }

    @When("Player {int} confirms it is their turn")
    public void player_confirms_turn(int playerNum) {
        userInput.print("\n");
    }

    @When("Player {int} draws from the event deck")
    public void player_draws_from_edeck(int playerNum) {
        userInput.print("\n");
    }

    @When("Player {int} confirms")
    public void player_confirms(int playerNum) {
        userInput.print("\n");
    }

    @When("p1 builds quest one")
    public void quest_one() {
        // p1 builds a easy quest
        userInput.print("0\nQuit\n"); // an f5
        userInput.print("1\nQuit\n"); // an f10
        userInput.print("3\nQuit\n"); // an f15
        userInput.print("3\nQuit\n"); // an f20
    }

    @When("players act in stage 1 of quest one")
    public void stage_one() {
        // p2's draw
        userInput.print("y\n"); // p2 confirms control
        userInput.print("0\n"); // p2 discards a foe

        // p3's draw
        userInput.print("y\n"); // p3 confirms control
        userInput.print("0\n"); // p3 discards a foe

        // p4's draw
        userInput.print("y\n"); // p4 confirms control
        userInput.print("0\n"); // p4 discards a foe

        // p2's attack
        userInput.print("\n"); // p2 confirms control
        userInput.print("5\n"); // p2 adds a sword to their attack
        userInput.print("Quit\n"); // p2 confirms attack

        // p3's attack
        userInput.print("\n"); // p3 confirms control
        userInput.print("5\n"); // p3 adds a dagger to their attack
        userInput.print("Quit\n"); // p3 confirms attack

        // p4's attack
        userInput.print("\n"); // p4 confirms control
        userInput.print("5\n"); // p4 adds a dagger to their attack
        userInput.print("Quit\n"); // p4 confirms attack

        // all attack are sufficient
        userInput.print("\n"); // confirm the victory screen
    }

    @When("players act in stage 2 of quest one")
    public void stage_two() {
        // p2's attack
        userInput.print("\n"); // p2 confirms control
        userInput.print("6\n"); // p2 adds a sword to their attack
        userInput.print("Quit\n"); // p2 confirms attack

        // p3's attack
        userInput.print("\n"); // p3 confirms control
        userInput.print("7\n"); // p3 adds a sword to their attack
        userInput.print("Quit\n"); // p3 confirms attack

        // p4's attack
        userInput.print("\n"); // p4 confirms control
        userInput.print("8\n"); // p4 adds a sword to their attack
        userInput.print("Quit\n"); // p4 confirms attack

        // all attack are sufficient
        userInput.print("\n"); // confirm the victory screen
    }

    @When("players act in stage 3 of quest one")
    public void stage_three() {
        // p2's attack
        userInput.print("\n"); // p2 confirms control
        userInput.print("9\n"); // p2 adds a battleaxe to their attack
        userInput.print("Quit\n"); // p2 confirms attack

        // p3's attack
        userInput.print("\n"); // p3 confirms control
        userInput.print("9\n"); // p3 adds a battleaxe to their attack
        userInput.print("Quit\n"); // p3 confirms attack

        // p4's attack
        userInput.print("\n"); // p4 confirms control
        userInput.print("10\n"); // p4 adds a battleaxe to their attack
        userInput.print("Quit\n"); // p4 confirms attack

        // all attack are sufficient
        userInput.print("\n"); // confirm the victory screen
    }

    @When("players act in stage 4 of quest one")
    public void stage_four() {
        // p2's attack
        userInput.print("\n"); // p2 confirms control
        userInput.print("11\n"); // p2 adds a lance to their attack
        userInput.print("Quit\n"); // p2 confirms attack

        // p3's attack
        userInput.print("\n"); // p3 confirms control
        userInput.print("11\n"); // p3 adds a lance to their attack
        userInput.print("Quit\n"); // p3 confirms attack

        // p4's attack
        userInput.print("\n"); // p4 confirms control
        userInput.print("11\n"); // p4 adds a lance to their attack
        userInput.print("Quit\n"); // p4 confirms attack

        // all attack are sufficient
        userInput.print("\n"); // confirm the victory screen
    }

    @When("players handle the prosperity event")
    public void handle_prosperity() {
        // p1's draw
        userInput.print("y\n"); // p4 confirms control
        userInput.print("11\n"); // p4 discards a big weapon
        userInput.print("11\n"); // p4 discards a big weapon

        // p2's draw
        userInput.print("y\n"); // p4 confirms control
        userInput.print("0\n"); // p4 discards a foe

        // p3's draw
        userInput.print("y\n"); // p2 confirms control
        userInput.print("0\n"); // p2 discards a foe

        // p4's draw
        userInput.print("y\n"); // p3 confirms control
        userInput.print("0\n"); // p3 discards a foe
    }

    @When("players handle the queen's favor event")
    public void handle_queen_favor() {
        // p4's draw
        userInput.print("y\n"); // p4 confirms control
        userInput.print("0\n"); // p4 discards a foe
        userInput.print("0\n"); // p4 discards a foe
    }

    @When("p1 builds quest two")
    public void build_quest_two() {
        // p1 builds a easy quest
        userInput.print("0\nQuit\n"); // an f5
        userInput.print("0\nQuit\n"); // an f10
        userInput.print("0\nQuit\n"); // an f15
    }

    @When("players act in stage 1 of quest two")
    public void stage_one_q2() {
        // p2's draw
        userInput.print("y\n"); // p2 confirms control
        userInput.print("0\n"); // p2 discards a foe

        // p3's draw
        userInput.print("y\n"); // p3 confirms control
        userInput.print("0\n"); // p3 discards a foe

        // p4's draw
        userInput.print("y\n"); // p4 confirms control
        userInput.print("0\n"); // p4 discards a foe

        // p2's attack
        userInput.print("\n"); // p2 confirms control
        userInput.print("8\n"); // p2 adds a sword to their attack
        userInput.print("Quit\n"); // p2 confirms attack

        // p3's attack
        userInput.print("\n"); // p3 confirms control
        userInput.print("7\n"); // p3 adds a dagger to their attack
        userInput.print("Quit\n"); // p3 confirms attack

        // p4's attack
        userInput.print("\n"); // p4 confirms control
        userInput.print("Quit\n"); // p4 confirms attack

        // all attack are sufficient except p4
        userInput.print("\n"); // confirm the victory screen
    }

    @When("players act in stage 2 of quest two")
    public void stage_two_q2() {
        // p2's attack
        userInput.print("\n"); // p2 confirms control
        userInput.print("8\n"); // p2 adds a sword to their attack
        userInput.print("Quit\n"); // p2 confirms attack

        // p3's attack
        userInput.print("\n"); // p3 confirms control
        userInput.print("7\n"); // p3 adds a dagger to their attack
        userInput.print("Quit\n"); // p3 confirms attack

        // all attack are sufficient
        userInput.print("\n"); // confirm the victory screen
    }

    @When("players act in stage 3 of quest two")
    public void stage_three_q2() {
        // p2's attack
        userInput.print("\n"); // p2 confirms control
        userInput.print("11\n"); // p2 adds a Battleaxe to their attack
        userInput.print("Quit\n"); // p2 confirms attack

        // p3's attack
        userInput.print("\n"); // p3 confirms control
        userInput.print("11\n"); // p3 adds a Battleaxe to their attack
        userInput.print("Quit\n"); // p3 confirms attack

        // all attack are sufficient
        userInput.print("\n"); // confirm the victory screen
    }

    @Then("player {int} should have won")
    public void check_output_for_winners(int winningPlayer) {
        String result = output.toString();

        String victoryString = "Congratulations! Player(s) " + 3 + " You are knighted and thus victorious!";

        assertTrue(result.contains(victoryString));
    }

    // -------------------------------------------------------------------------------------------------------- //
    // -------------------------------------------------------------------------------------------------------- //
    // -------------------------------------------------------------------------------------------------------- //
    // -------------------------------------------------------------------------------------------------------- //
    // -------------------------------------------------------------------------------------------------------- //
    // -------------------------------------------------------------------------------------------------------- //
    // -------------------------------------------------------------------------------------------------------- //

    // 2 winner scenario steps

    @Given("The 2 Winner Scenario Deck is setup")
    public void two_winner_scenario_deck_setup() {
        try {
            pos = new PipedOutputStream();
            pis = new PipedInputStream(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        output = new StringWriter();
        writer = new PrintWriter(output);
        userInput = new PrintWriter(pos);
        scanner = new Scanner(pis);
        game = new Game(scanner, writer);
        game.initGame();



        // player hand shenanigans



        // return all the player hands to the adventure deck
        for (int i = 0; i < 4; ++i) {
            game.getAdventureDeck().getDeck().addAll(game.getPlayer(i).getHand());
            game.getPlayer(i).getHand().clear();
        }

        List<AdventureCard> p1Hand =  game.getPlayer(0).getHand();
        // set up P1 hand
        p1Hand.add(new FoeCard(5));
        p1Hand.add(new FoeCard(10));
        p1Hand.add(new FoeCard(15));
        p1Hand.add(new FoeCard(20));


        p1Hand.add(new FoeCard(5));
        p1Hand.add(new FoeCard(10));
        p1Hand.add(new FoeCard(15));


        p1Hand.add(new FoeCard(70));
        p1Hand.add(new FoeCard(50));
        p1Hand.add(new FoeCard(50));
        p1Hand.add(new FoeCard(40));
        p1Hand.add(new FoeCard(40));

        for (AdventureCard card : p1Hand) {
            game.getAdventureDeck().getDeck().remove(card);
        }


        List<AdventureCard> p2Hand =  game.getPlayer(1).getHand();
        // set up P2 hand
        p2Hand.add(new WeaponCard('S', 10));
        p2Hand.add(new WeaponCard('S', 10));
        p2Hand.add(new WeaponCard('B', 15));
        p2Hand.add(new WeaponCard('L', 20));

        p2Hand.add(new WeaponCard('D', 5));
        p2Hand.add(new WeaponCard('S', 10));
        p2Hand.add(new WeaponCard('B', 15));


        p2Hand.add(new FoeCard(30));
        p2Hand.add(new FoeCard(10));
        p2Hand.add(new FoeCard(10));
        p2Hand.add(new FoeCard(5));
        p2Hand.add(new FoeCard(5));

        for (AdventureCard card : p2Hand) {
            game.getAdventureDeck().getDeck().remove(card);
        }

        List<AdventureCard> p3Hand =  game.getPlayer(2).getHand();
        // set up P3 hand
        p3Hand.add(new WeaponCard('D', 5));
        p3Hand.add(new WeaponCard('S', 10));
        p3Hand.add(new WeaponCard('B', 15));
        p3Hand.add(new WeaponCard('L', 20));

        p3Hand.add(new WeaponCard('D', 5));
        p3Hand.add(new WeaponCard('S', 10));
        p3Hand.add(new WeaponCard('B', 15));

        p3Hand.add(new FoeCard(5));
        p3Hand.add(new FoeCard(5));
        p3Hand.add(new FoeCard(10));
        p3Hand.add(new FoeCard(10));
        p3Hand.add(new FoeCard(10));



        for (AdventureCard card : p3Hand) {
            game.getAdventureDeck().getDeck().remove(card);
        }

        List<AdventureCard> p4Hand =  game.getPlayer(3).getHand();
        // set up P4 hand
        p4Hand.add(new WeaponCard('D', 5));
        p4Hand.add(new WeaponCard('S', 10));
        p4Hand.add(new WeaponCard('B', 15));
        p4Hand.add(new WeaponCard('L', 20));

        p4Hand.add(new WeaponCard('D', 5));
        p4Hand.add(new WeaponCard('S', 10));
        p4Hand.add(new WeaponCard('D', 5));

        p4Hand.add(new FoeCard(15));
        p4Hand.add(new FoeCard(15));
        p4Hand.add(new FoeCard(15));
        p4Hand.add(new FoeCard(15));
        p4Hand.add(new FoeCard(15));


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



        // event deck shenanigans


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
        Stack<AdventureCard> aDeck = game.getAdventureDeck().getDeck();

        Queue<AdventureCard> newCards = new LinkedList<AdventureCard>();


        // quest two
        // 2 draws for each player in stage 2 of quest one
        newCards.add(new WeaponCard('H', 10));
        newCards.add(new WeaponCard('H', 10));

        // 2 draws for each player in stage 2 of quest one
        newCards.add(new WeaponCard('H', 10));
        newCards.add(new WeaponCard('H', 10));

        // 2 draws for each player in stage 1 of quest one
        newCards.add(new WeaponCard('B', 15));
        newCards.add(new WeaponCard('H', 10));


        // quest one

        // p1 cards from sponsoring
        newCards.add(new WeaponCard('H', 10));
        newCards.add(new WeaponCard('H', 10));
        newCards.add(new WeaponCard('H', 10));
        newCards.add(new WeaponCard('H', 10));
        newCards.add(new WeaponCard('H', 10));
        newCards.add(new WeaponCard('H', 10));
        newCards.add(new WeaponCard('H', 10));
        newCards.add(new WeaponCard('S', 10));

        // 2 draws for each player in stage 4 of quest one
        newCards.add(new FoeCard(25));
        newCards.add(new FoeCard(25));

        // 2 draws for each player in stage 3 of quest one
        newCards.add(new FoeCard(20));
        newCards.add(new FoeCard(25));

        // 2 draws for each player in stage 2 of quest one
        newCards.add(new FoeCard(20));
        newCards.add(new FoeCard(20));

        // 3 draws for each player in stage 1 of quest one
        newCards.add(new FoeCard(5));
        newCards.add(new FoeCard(30));
        newCards.add(new FoeCard(20));



        // remove the cards we don't want duplicates of in the deck
        removeFromDeck(aDeck, newCards);

        // add the cards we do want on top
        aDeck.addAll(newCards);

        assertEquals(initialCards - (numPlayers * 12), game.getAdventureDeck().getDeckSize());
        assertEquals(17, eventDeck.size());
    }

    @When("players act in stage 1 of quest one of the 2 winner scenario")
    public void stage_1_quest_1_2_winners() {
        // p2's draw
        userInput.print("y\n"); // p2 confirms control
        userInput.print("0\n"); // p2 discards a foe

        // p3's draw
        userInput.print("y\n"); // p3 confirms control
        userInput.print("0\n"); // p3 discards a foe

        // p4's draw
        userInput.print("y\n"); // p4 confirms control
        userInput.print("0\n"); // p4 discards a foe

        // p2's attack
        userInput.print("\n"); // p2 confirms control
        userInput.print("5\n"); // p2 adds a sword to their attack
        userInput.print("Quit\n"); // p2 confirms attack

        // p3's attack
        userInput.print("\n"); // p3 confirms control
        userInput.print("Quit\n"); // p3 confirms attack

        // p4's attack
        userInput.print("\n"); // p4 confirms control
        userInput.print("5\n"); // p4 adds a dagger to their attack
        userInput.print("Quit\n"); // p4 confirms attack

        // all attack are sufficient except p3
        userInput.print("\n"); // confirm the victory screen
    }

    @When("players act in stage 2 of quest one of the 2 winner scenario")
    public void stage_2_quest_1_2_winners() {
        // p2's attack
        userInput.print("\n"); // p2 confirms control
        userInput.print("6\n"); // p2 adds a sword to their attack
        userInput.print("Quit\n"); // p2 confirms attack

        // p4's attack
        userInput.print("\n"); // p4 confirms control
        userInput.print("8\n"); // p4 adds a sword to their attack
        userInput.print("Quit\n"); // p4 confirms attack

        // all attack are sufficient
        userInput.print("\n"); // confirm the victory screen
    }

    @When("players act in stage 3 of quest one of the 2 winner scenario")
    public void stage_3_quest_1_2_winners() {
        // p2's attack
        userInput.print("\n"); // p2 confirms control
        userInput.print("9\n"); // p2 adds a battleaxe to their attack
        userInput.print("Quit\n"); // p2 confirms attack

        // p4's attack
        userInput.print("\n"); // p4 confirms control
        userInput.print("10\n"); // p4 adds a battleaxe to their attack
        userInput.print("Quit\n"); // p4 confirms attack

        // all attack are sufficient
        userInput.print("\n"); // confirm the victory screen
    }

    @When("players act in stage 4 of quest one of the 2 winner scenario")
    public void stage_4_quest_1_2_winners() {
        // p2's attack
        userInput.print("\n"); // p2 confirms control
        userInput.print("11\n"); // p2 adds a lance to their attack
        userInput.print("Quit\n"); // p2 confirms attack

        // p4's attack
        userInput.print("\n"); // p4 confirms control
        userInput.print("11\n"); // p4 adds a lance to their attack
        userInput.print("Quit\n"); // p4 confirms attack

        // all attack are sufficient
        userInput.print("\n"); // confirm the victory screen
    }

    @When("Player {int} declines to sponsor")
    public void decline_sponsorship(int playerNum) {
        userInput.print("n\n");
    }

    @When("Player {int} accepts sponsoring")
    public void accept_sponsoring(int playerNum) {
        userInput.print("y\n");
    }

    @When("p3 builds quest two")
    public void quest_2_p3() {
        // p3 builds an easy quest
        userInput.print("0\nQuit\n"); // an f5
        userInput.print("0\nQuit\n"); // an f10
        userInput.print("0\n2\nQuit\n"); // an F10 and a D5
    }

    @When("{int} players decline to join attack")
    public void decline_attack(int numPlayers) {
        for (int i = 0; i < numPlayers; ++i) {
            userInput.print("n\n");
        }
    }

    @When("players act in stage 1 of quest two of the 2 winner scenario")
    public void stage_1_quest_2_2_winners() {
        // p2's attack
        userInput.print("\n"); // p2 confirms control
        userInput.print("8\n"); // p2 adds a sword to their attack
        userInput.print("Quit\n"); // p2 confirms attack

        // p4's attack
        userInput.print("\n"); // p4 confirms control
        userInput.print("8\n"); // p4 adds a sword to their attack
        userInput.print("Quit\n"); // p4 confirms attack

        // all attack are sufficient
        userInput.print("\n"); // confirm the victory screen
    }

    @When("players act in stage 2 of quest two of the 2 winner scenario")
    public void stage_2_quest_2_2_winners() {
        // p2's attack
        userInput.print("\n"); // p2 confirms control
        userInput.print("8\n"); // p2 adds a battleaxe to their attack
        userInput.print("Quit\n"); // p2 confirms attack

        // p4's attack
        userInput.print("\n"); // p4 confirms control
        userInput.print("9\n"); // p4 adds a battleaxe to their attack
        userInput.print("Quit\n"); // p4 confirms attack

        // all attack are sufficient
        userInput.print("\n"); // confirm the victory screen
    }

    @When("players act in stage 3 of quest two of the 2 winner scenario")
    public void stage_3_quest_2_2_winners() {
        // p2's attack
        userInput.print("\n"); // p2 confirms control
        userInput.print("11\n"); // p2 adds a lance to their attack
        userInput.print("Quit\n"); // p2 confirms attack

        // p4's attack
        userInput.print("\n"); // p4 confirms control
        userInput.print("11\n"); // p4 adds a lance to their attack
        userInput.print("Quit\n"); // p4 confirms attack

        // all attack are sufficient
        userInput.print("\n"); // confirm the victory screen
    }

    @Then("players {int}, {int} should have won")
    public void two_winner_check(int firstPlayer, int secondPlayer) {
        String result = output.toString();

        String victoryString = "Congratulations! Player(s) " + firstPlayer + ", " + secondPlayer + " You are knighted and thus victorious!";

        assertTrue(result.contains(victoryString));
    }
}
