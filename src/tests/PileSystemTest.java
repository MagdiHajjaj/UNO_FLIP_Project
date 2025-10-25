package tests;
/**
 * Test class for game.PileSystem
 *
 * @author Magdi Hajjaj
 * @version M1
 */


import game.Card;
import game.CardColor;
import game.CardType;
import game.PileSystem;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class PileSystemTest {

    private PileSystem pileSystem;

    @BeforeEach
    public void setUp() {
        pileSystem = new PileSystem();
    }

    /**
     * Test that the draw pile is initialized correctly
     */


    /**
     * Test drawing a card reduces the pile size by one
     */
    @Test
    public void testDrawCardRemovesFromDrawPile() {
        int initialSize = pileSystem.getDrawPileSize();
        Card drawnCard = pileSystem.drawCard();
        assertNotNull(drawnCard, "Drawn card should not be null.");
        assertEquals(initialSize - 1, pileSystem.getDrawPileSize(), "Draw pile size should decrease by one.");
    }

    /**
     * Test adding a card to the discard pile
     */
    @Test
    public void testAddToDiscardPile() {
        Card testCard = new Card(CardColor.RED, CardType.SKIP);
        pileSystem.addToDiscardPile(testCard);
        assertEquals(testCard, pileSystem.peekTopDiscard(), "Top of discard pile should be the card just added.");
    }

    /**
     * Test restoring draw pile keeps top discard card
     */
    @Test
    public void testRestoreDrawPileKeepsTopDiscard() {
        while (!pileSystem.isDrawPileEmpty()) {
            pileSystem.drawCard();
        }

        Card topCard = new Card(CardColor.BLUE, CardType.REVERSE);
        pileSystem.addToDiscardPile(new Card(CardColor.GREEN, CardType.ONE));
        pileSystem.addToDiscardPile(topCard);

        pileSystem.restoreDrawPile();

        assertEquals(topCard, pileSystem.peekTopDiscard(), "Top card should remain after restore.");
        assertFalse(pileSystem.isDrawPileEmpty(), "Draw pile should be refilled after restore.");
    }

    /**
     * Test restoreDrawPile throws exception when discard pile is empty
     */
    @Test
    public void testRestoreDrawPileEmptyDiscardThrowsException() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            pileSystem.restoreDrawPile();
        });
        assertTrue(exception.getMessage().contains("empty"), "Exception should mention discard pile is empty.");
    }

    /**
     * Test shuffling changes card order (probabilistically)
     */
    @Test
    public void testShuffleChangesOrder() {
        PileSystem newPile = new PileSystem();
        PileSystem newPile2 = new PileSystem();


        assertFalse(newPile.drawCard() == newPile2.drawCard(), "Shuffle should change the order of at least one card.");
    }
}
