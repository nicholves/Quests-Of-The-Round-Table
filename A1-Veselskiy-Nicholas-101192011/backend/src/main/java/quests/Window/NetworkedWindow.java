package quests.Window;
import quests.AdventureCard.AdventureCard;
import quests.EventCard.EventDeckCard;
import quests.EventCard.QuestCard;
import quests.Player.Player;
import quests.Game.Game;
import quests.Quest.Quest;
import quests.AdventureCard.WeaponCard;
import quests.Attack.Attack;
import quests.Game.Game;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class NetworkedWindow {
    public void promptToEndTurn(BlockingQueue<String> input, PrintWriter output) throws InterruptedException {
        output.println("Your Turn is Over Press <Enter>");
        Game.waiting_on_user = true;
        input.take();

        for (int i = 0; i < 100; i++) {
            output.println();
        }
    }

    public void promptForNewPlayerTurn(BlockingQueue<String> input, PrintWriter output, Player player) throws InterruptedException {
        output.println("It is now your turn Player: " + (player.getPlayerId() + 1) + ", press <Enter> to continue:");
        Game.waiting_on_user = true;
        input.take();

        displayPlayerHand(input, output, player);
    }

    public void displayPlayerHand(BlockingQueue<String> input, PrintWriter output, Player player) throws InterruptedException {
        List<AdventureCard> hand =  player.getHand();

        output.print("Player " + (player.getPlayerId() + 1) + ", your hand contains: ");

        for (int i = 0; i < hand.size(); ++i) {
            AdventureCard card = hand.get(i);

            output.print(card.asString());
            if (i != hand.size() - 1) {
                output.print(", ");
            }
        }

        output.println();
    }

    public void displayQuestStage(PrintWriter output, List<AdventureCard> stage) throws InterruptedException {
        output.print("The quest stage currently contains: ");

        for (int i = 0; i < stage.size(); ++i) {
            AdventureCard card = stage.get(i);

            output.print(card.asString());
            if (i != stage.size() - 1) {
                output.print(", ");
            }
        }

        output.println();

        output.println("The value of this quest stage so far is: " + Quest.getQuestStageValue(stage));

        output.println();
        output.println();
    }

    public void congratulateWinners(PrintWriter output, List<Player> players) throws InterruptedException {
        // some empty lines
        output.println();
        output.println();
        output.println();

        String winnersString = "";
        for (Player p : players) {
            if (!winnersString.isEmpty()) {
                winnersString = winnersString + ", ";
            }

            winnersString += (p.getPlayerId() + 1);
        }

        output.println("Congratulations! Player(s) " + winnersString + " You are knighted and thus victorious!");
    }

    public void promptToTakeControl(BlockingQueue<String> input, PrintWriter output, Player player) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            output.println();
        }

        output.println("Player " + (player.getPlayerId() + 1) + ", now needs to take an action which contains private information");
        output.println("Player: " + (player.getPlayerId() + 1) + ", press <Enter> when you are in control ");
        Game.waiting_on_user = true;
        input.take();
    }

    public int discardCard(BlockingQueue<String> input, PrintWriter output, Player player) throws InterruptedException {
        while(true) {
            displayPlayerHand(input, output, player);
            output.print("\nPlayer " + (player.getPlayerId() + 1) + ", select a card to discard by its index and then press <Enter>: ");
            output.flush();

            Game.waiting_on_user = true;
            String userInput = input.take();

            int choice = Integer.parseInt(userInput);

            if (choice >= 0 && choice < player.getHandSize()) {
                return choice;
            }

            output.println("An invalid selection was made. Please choose a value between: " + 0 + " and " + (player.getHandSize() - 1));
        }
    }

    public void promptToDrawEventDeckCard(BlockingQueue<String> input, PrintWriter output, Player player) throws InterruptedException {
        output.println("Player " + (player.getPlayerId() + 1) + ", press <Enter> when you are ready for an event deck " +
                "card to be drawn");

        Game.waiting_on_user = true;
        input.take();
    }

    public void displayEventDeckCard(BlockingQueue<String> input, PrintWriter output, EventDeckCard card) throws InterruptedException {
        output.println("The card drawn was a: " + card.display() + "!");
        output.print("Press <Enter> to continue");
        output.flush();

        Game.waiting_on_user = true;
        input.take();
    }

    public boolean askToSponsor(BlockingQueue<String> input, PrintWriter output, Player player, QuestCard quest) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            output.println();
        }

        output.println("Player " + (player.getPlayerId() + 1) + " would you like to sponsor");
        output.println(quest.display());
        output.print("y for yes or n for no: ");
        output.flush();

        Game.waiting_on_user = true;
        String result = input.take();

        return result.startsWith("y");
    }

    public ArrayList<AdventureCard> buildQuestStage(BlockingQueue<String> input, PrintWriter output, Player player, int previousStageValue) throws InterruptedException {
        ArrayList<AdventureCard> result = new ArrayList<AdventureCard>();

        output.println();
        output.println("You must now create a quest stage with value greater than: " + previousStageValue);
        while (true) {
            displayPlayerHand(input, output, player);

            output.print("Enter a valid card index: ");
            output.flush();

            Game.waiting_on_user = true;
            String answer = input.take();

            // Quit with a valid quest
            if (answer.equals("Quit") && Game.validateQuestStage(result, previousStageValue)) {
                return result;
            }

            if (answer.equals("Quit")) { // Quit with an invalid quest
                displayErrorWithStage(output, result, previousStageValue);
                continue;
            }


            int indexChoice = Integer.parseInt(answer);
            if (player.getHandSize() == 0) { // if the player has no cards in hand they must quit
                output.println("You have no more Adventure Cards in hand you must Quit");
                continue;
            }

            if (indexChoice >= player.getHandSize() || indexChoice < 0) { // invalid index entered
                output.println("Invalid index chosen. Valid index is >= 0 or <= " + (player.getHandSize() - 1));
                continue;
            }

            AdventureCard cardChosenToAdd = player.getHand().remove(indexChoice);

            if (validateCardAddedToStage(output, result, cardChosenToAdd)) {
                result.add(cardChosenToAdd);
                displayQuestStage(output, result);
            }
            else {
                player.addCardToHand(cardChosenToAdd);
            }
        }
    }

    private void displayErrorWithStage(PrintWriter output, ArrayList<AdventureCard> stage, int previousStageValue) throws InterruptedException {
        boolean containsFoe = false;
        for (AdventureCard card : stage) {
            if (card.getLetter() == 'F') {
                containsFoe = true;
            }
        }

        if (!containsFoe) {
            output.println("Your Quest stage cannot contain 0 foes");
            return;
        }

        // calculate the sum of this quest stage
        int sum = 0;
        for (AdventureCard card : stage) {
            sum += card.getValue();
        }

        if (sum <= previousStageValue) {
            output.println("Your quest stage must be greater than the previous stage's value which was: " + previousStageValue + ". Your value was: " + sum);
        }
    }

    private boolean validateCardAddedToStage(PrintWriter output, ArrayList<AdventureCard> stage, AdventureCard cardToAdd) throws InterruptedException {
        if (cardToAdd.getLetter() == 'F') { // Trying to add a foe
            boolean containsFoe = false;
            for (AdventureCard card : stage) {
                if (card.getLetter() == 'F') {
                    containsFoe = true;
                    break;
                }
            }

            if (containsFoe) {
                output.println("a maximum of 1 foe is allowed per quest stage");
                return false;
            }
        }

        else {
            for (AdventureCard card : stage) {
                if (card.getLetter() == cardToAdd.getLetter()) {
                    output.println("A maximum of 1 copy of a weapon is allowed per quest stage");
                    return false;
                }
            }
        }

        return true;
    }

    // returns null if quit is selected
    public AdventureCard selectWeaponToAddToAttack(BlockingQueue<String> input, PrintWriter output, Player player) throws InterruptedException {
        output.println("Please construct your attack for this stage");

        while (true) {
            displayPlayerHand(input, output, player);

            output.println();
            output.print("Select an index for a card to add to your attack or 'Quit' to confirm your attack. (The attack must consist of unique weapons): ");
            output.flush();

            Game.waiting_on_user = true;
            String in = input.take();

            if (in.equals("Quit")) {
                return null;
            }

            int index = Integer.parseInt(in);

            if (player.getHandSize() == 0) {
                output.println("Your hand is empty you must confirm your attack");
                continue;
            }

            if (index < 0 || index > (player.getHandSize() - 1)) {
                output.println("An invalid index was selected. Please choose a value between 0 and " + (player.getHandSize() - 1));
                continue;
            }

            return player.getHand().remove(index);
        }
    }

    public void reportDuplicateWeaponAdding(PrintWriter output) {
        output.println("You cannot add a duplicate weapon to an attack");
    }

    public void reportAddingFoeToAttack(PrintWriter output) {
        output.println("Foe cards cannot be added to an attack");
    }


    public List<Player> askForParticipation(BlockingQueue<String> input, PrintWriter output, List<Player> players) throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            output.println();
        }

        ArrayList<Player> participants = new ArrayList<Player>();

        for (int i = 0; i < players.size(); ++i) {
            output.print("Player: " + (players.get(i).getPlayerId() + 1) + " would you like to tackle the current stage of the quest (y/n): ");
            output.flush();

            Game.waiting_on_user = true;
            String in = input.take();

            if (in.equals("y")) {
                participants.add(players.get(i));
            }
        }
        return participants;
    }

    public void displayWinnersOfQuestStage(BlockingQueue<String> input, PrintWriter output, List<Player> players) throws InterruptedException {
        output.println("Congratulations!");

        output.print("Player(s)");
        for (Player player : players) {
            output.print(" " + (player.getPlayerId() + 1));
        }

        output.print(" are victorious and can proceed to the next stage!");
        output.println();
        output.println();

        Game.waiting_on_user = true;
        input.take();
    }

    public void questFailedMessage(BlockingQueue<String> input, PrintWriter output) throws InterruptedException {
        output.println("The current quest has failed as there are no more eligible players");
        output.println();

        Game.waiting_on_user = true;
        input.take();
    }

    public void printAttack(BlockingQueue<String> input, PrintWriter output, Attack attack) throws InterruptedException {
        output.println();
        output.print("The attack currently contains:");
        for (WeaponCard weaponCard : attack.getCardsInAttack()) {
            output.print(" " + weaponCard.asString());
        }

        output.flush();
        output.println();
    }

    public void displayParticipants(PrintWriter output, List<Player> players) {
        output.println("The remaining eligible participants in the quest are:");

        for (Player player : players) {
            output.print(" " + (player.getPlayerId() + 1));
        }

        output.println();
    }
}
