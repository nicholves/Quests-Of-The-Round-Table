const apiBaseUrl = "http://localhost:8080";
const { Builder, By, until } = require('selenium-webdriver');
let sleepAmount = 1000

async function scenario1() {
    let driver = await new Builder().forBrowser("chrome").build();
    const timeout = 100000;

    try {
        await driver.get("http://127.0.0.1:8081");

        // choose the rigged scenario
        const response = await fetch(`${apiBaseUrl}/rigScenario/1`, {
            method: "POST"
        });

        if (!await response.ok) {
            console.error("Test failed to rig");
            await driver.quit();
        }

        // begin test
        let startButton = await driver.wait(until.elementLocated(By.id("start-button")), timeout);
        let outputBox = await driver.wait(until.elementLocated(By.id("game-output-box")), timeout);
        let inputBox = await driver.wait(until.elementLocated(By.id("userInput")), timeout);
        let submitButton = await driver.wait(until.elementLocated(By.id("submit-button")), timeout);

        await startButton.click();

        await driver.wait(async function () {
            let children = await outputBox.findElements(By.xpath("./*"));
            return children.length > 0;
        }, timeout);

        let userInput = getA1ScenarioInputs();

        while (userInput) {
            if (userInput[0] === "\n") {
                await submitButton.click()
                userInput = userInput.replace(/^\n/, "");
                await driver.sleep(sleepAmount);
                continue;
            }

            inputBox.sendKeys(userInput[0]);
            userInput = userInput.substring(1);
        }

        let p1Valid = false;
        let p2Valid = false;
        let p3Valid = false;
        let p4Valid = false;

        // P1 assertions
        let p1Hand = await driver.wait(until.elementLocated(By.id("player1_hand")), timeout).getText();
        let p1Shields = await driver.wait(until.elementLocated(By.id("player1_shields")), timeout).getText();
        console.log("Player 1 has the following hand: ");
        console.log(p1Hand)
        console.log(`\nPlayer 1 has ${p1Shields} shields`)
        
        console.assert(p1Hand.includes("F5, F10, F15, F15, F30, H10, B15, B15, L20") && p1Shields == 0, "Player 1 has the correct values\n\n");
        if (p1Hand.includes("F5, F10, F15, F15, F30, H10, B15, B15, L20") && p1Shields == 0) {
            p1Valid = true;
        }

        // P2 assertions
        let p2Hand = await driver.wait(until.elementLocated(By.id("player2_hand")), timeout).getText();
        let p2Shields = await driver.wait(until.elementLocated(By.id("player2_shields")), timeout).getText();
        console.log("Player 2 has the following hand: ");
        console.log(p2Hand)
        console.log(`\nPlayer 2 has ${p2Shields} shields`)


        let cardsInHandp2 = p2Hand.split(', ').length;
        console.log("Player 2 hand count = " + cardsInHandp2);



        console.assert(cardsInHandp2 == 12 && p2Shields == 0, "Player 2 has the correct values\n\n");
        if (cardsInHandp2 == 12 && p2Shields == 0) {
            p2Valid = true;
        }

        // P3 assertions
        let p3Hand = await driver.wait(until.elementLocated(By.id("player3_hand")), timeout).getText();
        let p3Shields = await driver.wait(until.elementLocated(By.id("player3_shields")), timeout).getText();
        console.log("Player 3 has the following hand: ");
        console.log(p3Hand)
        console.log(`\nPlayer 3 has ${p3Shields} shields`)
        
        console.assert(p3Hand.includes("F5, F5, F15, F30, S10") && p3Shields == 0, "Player 3 has the correct values\n\n");
        if (p3Hand.includes("F5, F5, F15, F30, S10") && p3Shields == 0) {
            p3Valid = true;
        }

        // P4 assertions
        let p4Hand = await driver.wait(until.elementLocated(By.id("player4_hand")), timeout).getText();
        let p4Shields = await driver.wait(until.elementLocated(By.id("player4_shields")), timeout).getText();
        console.log("Player 4 has the following hand: ");
        console.log(p4Hand)
        console.log(`\nPlayer 4 has ${p4Shields} shields`)
        
        console.assert(p4Hand.includes("F15, F15, F40, L20") && p4Shields == 4, "Player 4 has the correct values\n\n");
        if (p4Hand.includes("F15, F15, F40, L20") && p4Shields == 4) {
            p4Valid = true;
        }


        console.assert(p1Valid && p2Valid && p3Valid && p4Valid, "All players should have correct hands and shield counts for the A1 Scenario");

        if (p1Valid && p2Valid && p3Valid && p4Valid) {
            console.log("\n\n\nText Passed: A1 Scenario\n\n\n");
            return true;
        } else {
            return false;
        }

    }
    catch (error) {
        console.error("Test encountered and error: ", error);
        return false;
    }
    finally {
        await driver.quit();
    }
}

async function scenario2() {
    let driver = await new Builder().forBrowser("chrome").build();
    const timeout = 100000;

    try {
        await driver.get("http://127.0.0.1:8081");

        // choose the rigged scenario
        const response = await fetch(`${apiBaseUrl}/rigScenario/2`, {
            method: "POST"
        });

        if (!await response.ok) {
            console.error("Test failed to rig");
            await driver.quit();
        }

        // begin test
        let startButton = await driver.wait(until.elementLocated(By.id("start-button")), timeout);
        let outputBox = await driver.wait(until.elementLocated(By.id("game-output-box")), timeout);
        let inputBox = await driver.wait(until.elementLocated(By.id("userInput")), timeout);
        let submitButton = await driver.wait(until.elementLocated(By.id("submit-button")), timeout);

        await startButton.click();

        await driver.wait(async function () {
            let children = await outputBox.findElements(By.xpath("./*"));
            return children.length > 0;
        }, timeout);


        let userInput = getTwoWinnerScenarioInputs();

        while (userInput) {
            if (userInput[0] === "\n") {
                await submitButton.click()
                userInput = userInput.replace(/^\n/, "");
                await driver.sleep(sleepAmount);
                continue;
            }

            inputBox.sendKeys(userInput[0]);
            userInput = userInput.substring(1);
        }

        const output = await outputBox.getText();
        let winnerValid = false;
        console.assert(output.includes("Congratulations! Player(s) 2, 4 You are knighted and thus victorious!"), "victory is indicated to the user");
        winnerValid = output.includes("Congratulations! Player(s) 2, 4 You are knighted and thus victorious!");

        let p1Valid = false;
        let p2Valid = false;
        let p3Valid = false;
        let p4Valid = false;

        // P1 assertions
        let p1Hand = await driver.wait(until.elementLocated(By.id("player1_hand")), timeout).getText();
        let p1Shields = await driver.wait(until.elementLocated(By.id("player1_shields")), timeout).getText();
        console.log("Player 1 has the following hand: ");
        console.log(p1Hand)
        console.log(`\nPlayer 1 has ${p1Shields} shields`)
        
        console.assert(p1Hand.includes("F15, F15, F20, F20, F20, F20, F25, F25, F30, H10, B15, L20") && p1Shields == 0, "Player 1 has the correct values\n\n");
        if (p1Hand.includes("F15, F15, F20, F20, F20, F20, F25, F25, F30, H10, B15, L20") && p1Shields == 0) {
            p1Valid = true;
        }

        // P2 assertions
        let p2Hand = await driver.wait(until.elementLocated(By.id("player2_hand")), timeout).getText();
        let p2Shields = await driver.wait(until.elementLocated(By.id("player2_shields")), timeout).getText();
        console.log("Player 2 has the following hand: ");
        console.log(p2Hand)
        console.log(`\nPlayer 2 has ${p2Shields} shields`)



        console.assert(p2Hand.includes("F10, F15, F15, F25, F30, F40, F50, L20, L20") && p2Shields == 7, "Player 2 has the correct values\n\n");
        if (p2Hand.includes("F10, F15, F15, F25, F30, F40, F50, L20, L20") && p2Shields == 7) {
            p2Valid = true;
        }

        // P3 assertions
        let p3Hand = await driver.wait(until.elementLocated(By.id("player3_hand")), timeout).getText();
        let p3Shields = await driver.wait(until.elementLocated(By.id("player3_shields")), timeout).getText();
        console.log("Player 3 has the following hand: ");
        console.log(p3Hand)
        console.log(`\nPlayer 3 has ${p3Shields} shields`)
        
        console.assert(p3Hand.includes("F20, F40, D5, D5, S10, H10, H10, H10, H10, B15, B15, L20") && p3Shields == 0, "Player 3 has the correct values\n\n");
        if (p3Hand.includes("F20, F40, D5, D5, S10, H10, H10, H10, H10, B15, B15, L20") && p3Shields == 0) {
            p3Valid = true;
        }

        // P4 assertions
        let p4Hand = await driver.wait(until.elementLocated(By.id("player4_hand")), timeout).getText();
        let p4Shields = await driver.wait(until.elementLocated(By.id("player4_shields")), timeout).getText();
        console.log("Player 4 has the following hand: ");
        console.log(p4Hand)
        console.log(`\nPlayer 4 has ${p4Shields} shields`)
        
        console.assert(p4Hand.includes("F15, F15, F20, F25, F30, F50, F70, L20, L20") && p4Shields == 7, "Player 4 has the correct values\n\n");
        if (p4Hand.includes("F15, F15, F20, F25, F30, F50, F70, L20, L20") && p4Shields == 7) {
            p4Valid = true;
        }

        console.assert(p1Valid && p2Valid && p3Valid && p4Valid && winnerValid, "All players should have correct hands and shield counts for the 2 winner 2 quest scenario");

        if (p1Valid && p2Valid && p3Valid && p4Valid) {
            console.log("\n\n\nText Passed: 2 winner scenario\n\n\n");
            return true;
        } else {
            return false;
        }
    }
    catch (error) {
        console.error("Test encountered and error: ", error);
        return false;
    }
    finally {
        await driver.quit();
    }
}

async function scenario3() {
    let driver = await new Builder().forBrowser("chrome").build();
    const timeout = 100000;

    try {
        await driver.get("http://127.0.0.1:8081");

        // choose the rigged scenario
        const response = await fetch(`${apiBaseUrl}/rigScenario/3`, {
            method: "POST"
        });

        if (!await response.ok) {
            console.error("Test failed to rig");
            await driver.quit();
        }

        // begin test
        let startButton = await driver.wait(until.elementLocated(By.id("start-button")), timeout);
        let outputBox = await driver.wait(until.elementLocated(By.id("game-output-box")), timeout);
        let inputBox = await driver.wait(until.elementLocated(By.id("userInput")), timeout);
        let submitButton = await driver.wait(until.elementLocated(By.id("submit-button")), timeout);

        await startButton.click();

        await driver.wait(async function () {
            let children = await outputBox.findElements(By.xpath("./*"));
            return children.length > 0;
        }, timeout);


    }
    catch (error) {
        console.error("Test encountered and error: ", error);
    }
    finally {
        await driver.quit();
    }
}

async function scenario4() {
    let driver = await new Builder().forBrowser("chrome").build();
    const timeout = 100000;

    try {
        await driver.get("http://127.0.0.1:8081");

        // choose the rigged scenario
        const response = await fetch(`${apiBaseUrl}/rigScenario/4`, {
            method: "POST"
        });

        if (!await response.ok) {
            console.error("Test failed to rig");
            await driver.quit();
        }

        // begin test
        let startButton = await driver.wait(until.elementLocated(By.id("start-button")), timeout);
        let outputBox = await driver.wait(until.elementLocated(By.id("game-output-box")), timeout);
        let inputBox = await driver.wait(until.elementLocated(By.id("userInput")), timeout);
        let submitButton = await driver.wait(until.elementLocated(By.id("submit-button")), timeout);

        await startButton.click();

        await driver.wait(async function () {
            let children = await outputBox.findElements(By.xpath("./*"));
            return children.length > 0;
        }, timeout);


    }
    catch (error) {
        console.error("Test encountered and error: ", error);
    }
    finally {
        await driver.quit();
    }
}

async function runTest() {
    await scenario1();
    await scenario12();
    await scenario3();
    await scenario4();
}

if (process.argv.length > 2) {
    sleepAmount = process.argv[2];
}
//runTest()
scenario2();









// inputs
function getA1ScenarioInputs() {
    userInput = "";
    userInput += "\n" // p1 confirms it is their turn

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
    userInput += "0\n0\n0\n0\n\n"; // we just discard the four lowest foes in their hand randomly

    return userInput;
}

function getTwoWinnerScenarioInputs() {
    userInput = "";
    userInput += "\n" // p1 confirms it is their turn

    userInput += "\n"; // p1 confirms the drawn card (Quest of 4 stages)

    userInput += "y\n"; // p1 accepts sponsoring

    // p1 builds the quest
    userInput += "0\nQuit\n"; // Stage 1 being an f5
    userInput += "0\n4\nQuit\n"; // Stage 2 being a f5 and dagger

    userInput += "0\n3\nQuit\n"; // Stage 3

    userInput += "0\n3\nQuit\n"; // Stage 4 f10 plus axe


    // 6) Stage 1:
    // all players participate
    userInput += "y\n";
    userInput += "y\n";
    userInput += "y\n";

    // p2's draw
    userInput += "\n"; // p2 confirms control
    userInput += "0\n"; // p2 discards an F5 to trimming

    // p3's draw
    userInput += "\n"; // p3 confirms control
    userInput += "0\n"; // p3 discards an F5 to trimming

    // p4's draw
    userInput += "\n"; // p4 confirms control
    userInput += "0\n"; // p4 discards an F5 to trimming

    // p2's attack
    userInput += "\n"; // p2 confirms control
    userInput += "5\n"; // p2 adds a horse to their attack
    userInput += "Quit\n"; // p2 confirms attack

    // p3's attack
    userInput += "\n"; // p3 confirms control
    userInput += "Quit\n"; // p3 confirms attack

    // p4's attack
    userInput += "\n"; // p4 confirms control
    userInput += "5\n"; // p4 adds a horse to their attack
    userInput += "Quit\n"; // p4 confirms attack

    // all attack are sufficient
    userInput += "\n"; // confirm the victory screen

    // 7) Stage 2:
    // all players participate
    userInput += "y\n";
    userInput += "y\n";


    // p2's attack
    userInput += "\n"; // p2 confirms control
    userInput += "3\n"; // p2 adds a sword to their attack
    userInput += "Quit\n"; // p2 confirms attack

    // p4's attack
    userInput += "\n"; // p4 confirms control
    userInput += "3\n"; // p4 adds a sword to their attack
    userInput += "Quit\n"; // p4 confirms attack


    userInput += "\n"; // confirm the victory screen

    // 8) Stage 3:
    // all 2 players participate
    userInput += "y\n";
    userInput += "y\n";

    // p2's attack
    userInput += "\n"; // p2 confirms control
    userInput += "4\n"; // p2 adds a sword to their attack
    userInput += "5\n"; // p2 adds a horse to their attack
    userInput += "Quit\n"; // p2 confirms attack

    // p4's attack
    userInput += "\n"; // p4 confirms control
    userInput += "4\n"; // p4 adds a sword to their attack
    userInput += "5\n"; // p4 adds a horse to their attack
    userInput += "Quit\n"; // p4 confirms attack

    // Confirm the winners of this stage, p2 and p4
    userInput += "\n";

    // 8) Stage 4:
    // all 2 players participate
    userInput += "y\n";
    userInput += "y\n";

    // p2's attack
    userInput += "\n"; // p2 confirms control
    userInput += "5\n"; // p2 adds a sword to their attack
    userInput += "5\n"; // p2 adds an axe to their attack
    userInput += "Quit\n"; // p2 confirms attack

    // p4's attack
    userInput += "\n"; // p4 confirms control
    userInput += "5\n"; // p4 adds a sword to their attack
    userInput += "5\n"; // p4 adds an axe to their attack
    userInput += "Quit\n"; // p4 confirms attack

    // Confirm the winners of this stage, just p4 who is victorious in the entire quest
    userInput += "\n";


    // trim the hand of the quest sponsor (p2)
    userInput += "\n"; // confirm p1 has control
    userInput += "0\n0\n0\n0\n\n"; // we just discard the four lowest foes in their hand randomly

    // confirm turn end
    userInput += "\n";

    userInput += "\n" // p2 confirms it is their turn\

    userInput += "\n"; // p2 confirms the drawn card (Quest of 3 stages)



    userInput += "n\n"; // p2 declines to sponsor
    userInput += "y\n"; // p3 sponsors

    // building quest 2
    // stage 1
    userInput += "0\n";
    userInput += "Quit\n";

    // stage 2
    userInput += "0\n"; // f5
    userInput += "2\n"; // dagger
    userInput += "Quit\n";

    // stage 3
    userInput += "0\n"; // f5
    userInput += "3\n"; // horse
    userInput += "Quit\n";


    // all players participate except p1 participate
    userInput += "n\n";
    userInput += "y\n";
    userInput += "y\n";


    // stage (1)
    userInput += "\n"; // p3 confirms control
    userInput += "5\n"; // p3 attacks with dagger
    userInput += "Quit\n";

    userInput += "\n"; // p4 confirms control
    userInput += "5\n"; // p4 attacks with dagger
    userInput += "Quit\n";

    userInput += "\n"; // Confirms congratulations

    // all players participate
    userInput += "y\n";
    userInput += "y\n";


    // stage (2)
    userInput += "\n"; // p2 confirms control
    userInput += "6\n"; // p2 attacks with axe
    userInput += "Quit\n";

    userInput += "\n"; // p4 confirms control
    userInput += "6\n"; // p4 attacks with dagger
    userInput += "Quit\n";



    userInput += "\n"; // Confirms congratulations

    // all players participate
    userInput += "y\n";
    userInput += "y\n";

    // stage (3)
    userInput += "\n"; // p2 confirms control
    userInput += "9\n"; // p2 attacks with excal
    userInput += "Quit\n";

    userInput += "\n"; // p4 confirms control
    userInput += "9\n"; // p4 attacks with excal
    userInput += "Quit\n";

    userInput += "\n"; // Confirms congratulations

    userInput += "\n"; // p3 confirms control

    // p3 discrads
    userInput += "0\n";
    userInput += "1\n";
    userInput += "1\n";

    userInput += "\n"; // confirm quest end

    userInput += "\n"; // confirm turn end


    return userInput;
}