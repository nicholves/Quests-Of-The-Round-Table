const apiBaseUrl = "http://localhost:8080";

const programOutput = document.getElementById("game-output-box");
async function startGame() {
    try {
        const response = await fetch(`${apiBaseUrl}/start`);
        const result = await response.text();
        console.log("Start Game Response:", result); 

        programOutput.innerHTML = "";

        let newP = document.createElement("p");
        let node = document.createTextNode("");
        newP.appendChild(node)

        newP.innerText += result;


        programOutput.appendChild(newP);


        programOutput.scrollTop = programOutput.scrollHeight

        updateUi();
    } catch (error) {
        console.error("Error in startGame:", error);
    }
}

async function updateUi() {
    try {
        for (let i = 1; i < 5; ++i) {
            // hand
            let response = await fetch(`${apiBaseUrl}/hand/${i}`);
            let result = await response.text();

            let node = document.getElementById(`player${i}_hand`);
            node.innerText = result;

            // shield count
            response = await fetch(`${apiBaseUrl}/shieldCount/${i}`);
            result = await response.text();

            node = document.getElementById(`player${i}_shields`);
            node.innerText = result;
        }
    }
    catch (error) {
        console.error("error when refreshing player info:", error);
    }
}


async function submitText() {
    try {
        const response = await fetch(`${apiBaseUrl}/send`, {
            method: "POST",
            body: document.getElementById("userInput").value ? document.getElementById("userInput").value : " "
        });

        let newP = document.createElement("p");
        let node = document.createTextNode("");
        newP.appendChild(node)

        newP.innerText += await response.text();


        programOutput.appendChild(newP);


        programOutput.scrollTop = programOutput.scrollHeight


        document.getElementById("userInput").value = "";

        updateUi();
    } catch (error) {
        console.error("error when sending text:", error);
    }
}

const inputBox = document.getElementById("userInput");
inputBox.addEventListener("keyup", function(event) {
    if (event.key == "Enter") {
        submitText()
    }
})