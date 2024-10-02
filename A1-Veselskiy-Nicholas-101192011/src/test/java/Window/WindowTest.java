package Window;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Window.Window;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class WindowTest {
    @Test
    @DisplayName("The interface prompts the user to press enter to end their turn and then flushes the screen")
    void RESP_03_TEST_01() {
        Window window = new Window();

        String input = "\n";
        StringWriter output = new StringWriter();

        window.promptToEndTurn(new Scanner(input), new PrintWriter(output));


        boolean assertion = output.toString().contains("<Enter>");
        assertion = assertion && output.toString().contains("\n");

        assertTrue(assertion);
    }
}