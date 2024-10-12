package Player;

import AdventureCard.AdventureCard;
import AdventureCard.FoeCard;
import AdventureCard.WeaponCard;
import AdventureDeck.AdventureDeck;
import Game.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    @DisplayName("The game correctly removes the appropriate card from the user's hand and places it in the discard pile")
    public void RESP_08_TEST_02() {
        Player player = new Player(3);
        Game game = new Game();

        FoeCard first = new FoeCard(5);
        WeaponCard last = new WeaponCard('L', 20);

        player.addCardToHand(first);
        player.addCardToHand(new FoeCard(10));
        player.addCardToHand(new FoeCard(5));
        player.addCardToHand(new WeaponCard('D', 5));
        player.addCardToHand(new WeaponCard('L', 20));
        player.addCardToHand(new FoeCard(70));
        player.addCardToHand(new WeaponCard('D', 5));
        player.addCardToHand(new FoeCard(25));
        player.addCardToHand(new FoeCard(50));
        player.addCardToHand(new WeaponCard('H', 10));
        player.addCardToHand(new WeaponCard('S', 10));
        player.addCardToHand(new FoeCard(35));
        player.addCardToHand(new FoeCard(5));
        player.addCardToHand(last);

        AdventureDeck adventureDeck = game.getAdventureDeck();
        Stack<AdventureCard> discardPile = adventureDeck.getDiscardPile();

        player.discardCard(0, discardPile);
        player.discardCard(12, discardPile);

        assertEquals(12, player.getHandSize());
        assertEquals(2, discardPile.size());

        assertEquals(first, discardPile.firstElement());
        assertEquals(last, discardPile.elementAt(1));
    }

}