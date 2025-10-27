package tests;

import game.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PileSystemTest {
    /**
     * Tests that the draw pile is correctly initialized with 104 cards.
     */
    @Test
    public void testDrawPileInitialization() {
        PileSystem piles = new PileSystem();
        int expectedCards = (4 * ((9 + 3) * 2)) + (2 * 4 * 1);
        assertEquals(expectedCards, piles.getDrawPileSize(), "Draw pile should contain 104 cards initially");
    }
    /**
     * Tests that drawing a card reduces the draw pile size by one.
     */
    @Test
    public void testDrawCardReducesPileSize() {
        PileSystem piles = new PileSystem();
        int initialSize = piles.getDrawPileSize();
        piles.drawCard();
        assertEquals(initialSize - 1, piles.getDrawPileSize(), "Drawing a card should reduce pile size by one");
    }
    /**
     * Tests adding a card to the discard pile and retrieving it using  peekTopDiscard().
     */
    @Test
    public void testAddToDiscardPileAndPeek() {
        PileSystem piles = new PileSystem();
        Card card = new Card(CardColor.RED, CardType.ONE);
        piles.addToDiscardPile(card);

        assertEquals(card, piles.peekTopDiscard(), "Top discard should match the card added");
    }
    /**
     * Tests that the draw pile can be restored from the discard pile when empty.
     * Ensures no exception is thrown during the restore process.
     */
    @Test
    public void testRestoreDrawPileWhenEmpty() {
        PileSystem piles = new PileSystem();

        Card lastCard = piles.drawCard();
        piles.addToDiscardPile(lastCard);

        while (!piles.isDrawPileEmpty()) {
            piles.addToDiscardPile(piles.drawCard());
        }
        assertDoesNotThrow(() -> {
            piles.drawCard(); // This triggers restoreDrawPile() internally
        });
    }
    /**
     * Tests that peekTopDiscard() returns null when the discard pile is empty.
     */

    @Test
    public void testPeekTopDiscardReturnsNullWhenEmpty() {
        PileSystem piles = new PileSystem();
        assertNull(piles.peekTopDiscard(), "Should return null if discard pile is empty");
    }
}
