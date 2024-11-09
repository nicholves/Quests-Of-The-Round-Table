import AdventureCard.AdventureCard;
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

public class StepDefinitions {
    private Game game;
    private PrintWriter writer;
    private Scanner scanner;
    private PrintWriter userInput;
    private PipedOutputStream pos;
    private PipedInputStream pis;


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
}
