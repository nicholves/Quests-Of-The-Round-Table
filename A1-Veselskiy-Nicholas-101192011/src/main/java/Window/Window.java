package Window;
import AdventureCard.AdventureCard;
import Player.Player;

import java.io.PrintWriter;
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
}
