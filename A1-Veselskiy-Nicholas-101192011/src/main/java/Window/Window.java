package Window;
import Player.Player;

import java.io.PrintWriter;
import java.util.Scanner;

public class Window {
    public void promptToEndTurn(Scanner input, PrintWriter output) {
        output.println("Your Turn is Over Press <Enter>");
        input.nextLine();

        for (int i = 0; i < 100; i++) {
            output.println();
        }
    }

    public void promptForNewPlayerTurn(Scanner input, PrintWriter output, Player player) {

    }

    public void displayPlayerHand(Scanner input, PrintWriter output, Player player) {

    }
}
