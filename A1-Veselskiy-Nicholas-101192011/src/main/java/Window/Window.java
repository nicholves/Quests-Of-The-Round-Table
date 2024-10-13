package Window;
import AdventureCard.AdventureCard;
import EventCard.EventDeckCard;
import EventCard.QuestCard;
import Player.Player;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class Window {
    public void promptToEndTurn(Scanner input, PrintWriter output) {
        output.println("Your Turn is Over Press <Enter>");
        input.nextLine();

        for (int i = 0; i < 100; i++) {
            output.println();
        }
    }

    public void promptForNewPlayerTurn(Scanner input, PrintWriter output, Player player) {
        output.println("It is now your turn Player: " + player.getPlayerId() + ", press <Enter> to continue:");
        input.nextLine();

        displayPlayerHand(input, output, player);
    }

    public void displayPlayerHand(Scanner input, PrintWriter output, Player player) {
        List<AdventureCard> hand =  player.getHand();

        output.print("Player " + player.getPlayerId() + ", your hand contains: ");

        for (int i = 0; i < hand.size(); ++i) {
            AdventureCard card = hand.get(i);

            output.print(card.asString());
            if (i != hand.size() - 1) {
                output.print(", ");
            }
        }

        output.println();
    }

    public void congratulateWinners(PrintWriter output, List<Player> players) {
        // some empty lines
        output.println();
        output.println();
        output.println();

        String winnersString = "";
        for (Player p : players) {
            if (!winnersString.isEmpty()) {
                winnersString = winnersString + ", ";
            }

            winnersString += p.getPlayerId();
        }

        output.println("Congratulations! Player(s) " + winnersString + " You are knighted and thus victorious!");
    }

    public void promptToTakeControl(Scanner input, PrintWriter output, Player player) {
        for (int i = 0; i < 100; i++) {
            output.println();
        }

        output.println("Player " + player.getPlayerId() + ", now needs to take an action which contains private information");
        output.println("Player: " + player.getPlayerId() + ", press <Enter> when you are in control ");
        input.nextLine();
    }

    public int discardCard(Scanner input, PrintWriter output, Player player) {
        promptToTakeControl(input, output, player);

        while(true) {
            displayPlayerHand(input, output, player);
            output.print("\nPlayer " + player.getPlayerId() + ", select a card to discard by its index and then press <Enter>: ");
            output.flush();

            String userInput = input.nextLine();

            int choice = Integer.parseInt(userInput);

            if (choice >= 0 && choice < player.getHandSize()) {
                return choice;
            }

            output.println("An invalid selection was made. Please choose a value between: " + 0 + " and " + (player.getHandSize() - 1));
        }
    }

    public void promptToDrawEventDeckCard(Scanner input, PrintWriter output, Player player) {
        output.println("Player " + player.getPlayerId() + ", press <Enter> when you are ready for an event deck " +
                "card to be drawn");

        input.nextLine();
    }

    public void displayEventDeckCard(Scanner input, PrintWriter output, EventDeckCard card) {
        output.println("The card drawn was a: " + card.display() + "!");
        output.print("Press <Enter> to continue");
        output.flush();

        input.nextLine();
    }

    public boolean askToSponsor(Scanner input, PrintWriter output, Player player, QuestCard quest) {
        for (int i = 0; i < 100; i++) {
            output.println();
        }

        output.println("Player " + player.getPlayerId() + " would you like to sponsor");
        output.println(quest.display());
        output.print("y for yes or n for no: ");
        output.flush();

        String result = input.nextLine();

        return result.startsWith("y");
    }

    public ArrayList<AdventureCard> buildQuestStage(Scanner input, PrintWriter output, Player player, int previousStageValue) {
        return null;
    }
}
