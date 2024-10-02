package Game;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    @DisplayName("Check that cards are distributed to players on game initialization")
    void RESP_02_TEST_01() {
        Game game = new Game();

        for (int i = 0; i < 4; i++) {
            assertEquals(12, game.getPlayer(i).getHand().size());
        }

        assertEquals(100 - 12 * 4, game.getAdventureDeck().getDeckSize());
    }
}