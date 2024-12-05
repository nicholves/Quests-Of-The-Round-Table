package quests;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import quests.AdventureCard.AdventureCard;
import quests.Game.Game;
import quests.Player.Player;

import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:8081")
public class QuestsController {
    private Game game;
    private PrintWriter writer;
    private BlockingQueue<String> userInput;
    private PipedOutputStream pos;
    private PipedInputStream pis;
    private StringWriter output;
    private boolean gameRunning = false;
    private boolean usingRiggedScenario = false;
    private int scenario;

    @GetMapping("/start")
    public String startGame() {
        resetGame();

        if (usingRiggedScenario) {
            switch (scenario) {
                case 1:
                    game.rigA1Scenario();
                    break;
                case 2:
                    game.rig2Winner2QuestScenario();
                    break;
                case 3:
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Invalid rigged scenario chosen defaulting to unrigged");
                    usingRiggedScenario = false;
            }
        }

        game.start();
        gameRunning = true;

        while (!Game.waiting_on_user) {
            // wait until the game is awaiting user input
        }

        String outputForUser;
        outputForUser = output.toString();
        output.getBuffer().setLength(0); // clear the output buffer
        Game.waiting_on_user = false;

        return outputForUser;
    }

    @GetMapping("/hand/{playerid}")
    public ResponseEntity<String> getHand(@PathVariable("playerid") int playerId) {
        if (playerId <= 0 || playerId > 4) {
            return new ResponseEntity<>("Player " + playerId + " is not a valid player", HttpStatus.FORBIDDEN);
        }

        String result = "";

        List<AdventureCard> hand = game.getPlayer(playerId - 1).getHand();

        for (int i = 0; i < hand.size(); ++i) {
            AdventureCard card = hand.get(i);

            result += (card.asString());
            if (i != hand.size() - 1) {
                result += ", ";
            }
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/shieldCount/{playerid}")
    public ResponseEntity<String> getShieldCount(@PathVariable("playerid") int playerId) {
        if (playerId <= 0 || playerId > 4) {
            return new ResponseEntity<>("Player " + playerId + " is not a valid player", HttpStatus.FORBIDDEN);
        }

        int shieldCount = game.getPlayer(playerId - 1).getNumShields();

        return new ResponseEntity<>(String.valueOf(shieldCount), HttpStatus.OK);
    }

    @PostMapping("/send")
    public ResponseEntity<String> recieveData(@RequestBody String text) {
        if (!gameRunning) {
            return new ResponseEntity<>("Please start a game first", HttpStatus.FORBIDDEN);
        }
        try {
            userInput.put(text);
        }
        catch (InterruptedException e) {
            System.err.println("ERR: Something has gone wrong with the input queue");
        }

        while (!Game.waiting_on_user) {
            // wait until the game is awaiting user input
        }
        Game.waiting_on_user = false;
        String outputForUser;
        outputForUser = output.toString();
        output.getBuffer().setLength(0); // clear the output buffer

        return new ResponseEntity<>(outputForUser, HttpStatus.OK);
    }

    @PostMapping("/rigScenario/{scenario}")
    public void rigScenario1(@PathVariable("scenario") int scenario) {
        usingRiggedScenario = true;
        this.scenario = scenario;
    }

    @PostMapping("/unrigScenario")
    public void unrigScenario() {
        usingRiggedScenario = false;
    }


    public QuestsController() {
        resetGame();
    }

    public void resetGame() {
        try {
            pos = new PipedOutputStream();
            pis = new PipedInputStream(pos);
        } catch (Exception e) {
            e.printStackTrace();
        }

        output = new StringWriter();
        writer = new PrintWriter(output);
        userInput = new ArrayBlockingQueue<String>(1);
        game = new Game(userInput, writer);
        game.initGame();
    }
}
