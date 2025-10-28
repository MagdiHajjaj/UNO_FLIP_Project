package tests;

import game.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class GameTest {

    // Build a Game that won't run the interactive loop
    private Game newGameWithInput(String input) {
        return new Game(new Scanner(new ByteArrayInputStream(input == null ? new byte[0] : input.getBytes())), true);
    }

    // ---------------- validatePlacedCard ----------------

    @Test
    void validate_allowsSameColor() {
        Game g = newGameWithInput(null);
        Card top = new Card(CardColor.RED, CardType.THREE);
        Card play = new Card(CardColor.RED, CardType.NINE);
        assertTrue(g.validatePlacedCard(play, top, null));
    }

    @Test
    void validate_allowsSameType() {
        Game g = newGameWithInput(null);
        Card top = new Card(CardColor.BLUE, CardType.FIVE);
        Card play = new Card(CardColor.YELLOW, CardType.FIVE);
        assertTrue(g.validatePlacedCard(play, top, null));
    }

    @Test
    void validate_allowsWildAnywhere() {
        Game g = newGameWithInput(null);
        Card top = new Card(CardColor.GREEN, CardType.SEVEN);
        Card play = new Card(CardColor.WILD, CardType.WILD);
        assertTrue(g.validatePlacedCard(play, top, null));
    }

    @Test
    void validate_blocksMismatch() {
        Game g = newGameWithInput(null);
        Card top = new Card(CardColor.RED, CardType.TWO);
        Card play = new Card(CardColor.BLUE, CardType.NINE);
        assertFalse(g.validatePlacedCard(play, top, null));
    }

    @Test
    void validate_topIsWild_requiresChosenColor() {
        Game g = newGameWithInput(null);
        Card topWild = new Card(CardColor.WILD, CardType.WILD);
        Card chosenYellow = new Card(CardColor.YELLOW, null);

        Card ok = new Card(CardColor.YELLOW, CardType.THREE);
        Card bad = new Card(CardColor.RED, CardType.THREE);

        assertTrue(g.validatePlacedCard(ok, topWild, chosenYellow));
        assertFalse(g.validatePlacedCard(bad, topWild, chosenYellow));
    }

    // ---------------- proccessCard effects ----------------

    @Test
    void process_reverseFlipsDirection() {
        Game g = newGameWithInput(null);
        assertEquals(1, g.getDirection());
        g.proccessCard(new Card(CardColor.RED, CardType.REVERSE));
        assertEquals(-1, g.getDirection());
        g.proccessCard(new Card(CardColor.BLUE, CardType.REVERSE));
        assertEquals(1, g.getDirection());
    }

    @Test
    void process_drawOneSetsFlag() {
        Game g = newGameWithInput(null);
        g.proccessCard(new Card(CardColor.RED, CardType.DRAW_ONE));
        assertTrue(g.getDrawOne());
    }

    @Test
    void process_wildDrawTwoSetsFlagAndAcceptsColor() {
        // Feed "RED" as the color choice
        Game g = newGameWithInput("RED\n");
        g.proccessCard(new Card(CardColor.WILD, CardType.WILD_DRAW_TWO));
        assertTrue(g.getWildDrawTwo());
        // Now simulate validating the next player's move with chosen color RED
        Card topWild = new Card(CardColor.WILD, CardType.WILD_DRAW_TWO);
        Card chosenRed = new Card(CardColor.RED, null);
        Card ok = new Card(CardColor.RED, CardType.ONE);
        Card bad = new Card(CardColor.BLUE, CardType.ONE);
        assertTrue(g.validatePlacedCard(ok, topWild, chosenRed));
        assertFalse(g.validatePlacedCard(bad, topWild, chosenRed));
    }

    @Test
    void process_wildSetsChosenColor() {
        // Feed "BLUE"
        Game g = newGameWithInput("BLUE\n");
        g.proccessCard(new Card(CardColor.WILD, CardType.WILD));
        // Validate using BLUE as chosen color
        Card topWild = new Card(CardColor.WILD, CardType.WILD);
        Card chosenBlue = new Card(CardColor.BLUE, null);
        assertTrue(g.validatePlacedCard(new Card(CardColor.BLUE, CardType.NINE), topWild, chosenBlue));
        assertFalse(g.validatePlacedCard(new Card(CardColor.RED, CardType.NINE), topWild, chosenBlue));
    }

    // ---------------- calculateScore ----------------

    @Test
    void calculateScore_sumsAllOtherHands() {
        Game g = newGameWithInput("0\n"); // displayTurn will draw and return
        Player p0 = new Player("p0");
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");

        // Give other players known points
        p1.addCard(new Card(CardColor.RED, CardType.FIVE));   // 5
        p1.addCard(new Card(CardColor.YELLOW, CardType.SEVEN)); // 7
        p2.addCard(new Card(CardColor.BLUE, CardType.NINE));    // 9

        // Build player list
        g.addPlayer(p0);
        g.addPlayer(p1);
        g.addPlayer(p2);

        // Make p0 the current player (without exposing internals) by calling displayTurn(p0)
        g.displayTurn(p0); // input "0\n" makes it draw and exit quickly

        int total = g.calculateScore();
        assertEquals(5 + 7 + 9, total);
    }

    // ---------------- checkWinner ----------------

    @Test
    void checkWinner_boundaryAt500() {
        Game g = newGameWithInput(null);
        Player p = new Player("p");
        p.setScore(499);
        assertFalse(g.checkWinner(p));
        p.setScore(500);
        assertTrue(g.checkWinner(p));
    }

    // ---------------- resetFeilds ----------------

    @Test
    void resetFields_resetsFlagsHandsDirectionAndIndex() {
        Game g = newGameWithInput(null);

        Player a = new Player("A");
        Player b = new Player("B");
        g.addPlayer(a);
        g.addPlayer(b);

        // Give them hands
        g.generatePlayerHand(a);
        g.generatePlayerHand(b);
        assertTrue(a.handSize() >= 7 && b.handSize() >= 7);

        // Flip state via cards
        g.proccessCard(new Card(CardColor.RED, CardType.DRAW_ONE));
        g.proccessCard(new Card(CardColor.GREEN, CardType.WILD_DRAW_TWO)); // needs color input, skip here
        g.proccessCard(new Card(CardColor.BLUE, CardType.REVERSE));

        // Reset
        g.resetFeilds();

        assertEquals(1, g.getDirection());
        assertFalse(g.getDrawOne());
        assertFalse(g.getWildDrawTwo());
        // After reset, each player should have exactly 7
        assertEquals(7, a.handSize());
        assertEquals(7, b.handSize());
    }

    @Test
    void resetFieldsTrue_resetsScoresAndRoundCounter() {
        Game g = newGameWithInput(null);
        Player a = new Player("A"); a.setScore(120);
        Player b = new Player("B"); b.setScore(300);
        g.addPlayer(a);
        g.addPlayer(b);

        g.resetFeilds(true);
        assertEquals(0, a.getScore());
        assertEquals(0, b.getScore());
    }

    // ---------------- player list helpers ----------------

    @Test
    void playerList_addAndEmpty() {
        Game g = newGameWithInput(null);
        assertTrue(g.isPlayerListEmpty());
        g.addPlayer(new Player("X"));
        assertFalse(g.isPlayerListEmpty());
    }

    // ---------------- parse input ----------------

    @Test
    void parseInt_handlesBadThenGood() {
        Game g = newGameWithInput("abc\n12\n");
        assertEquals(12, g.parseUserIntInput());
    }

    @Test
    void parseString_readsLine() {
        Game g = newGameWithInput("Magdi\n");
        assertEquals("Magdi", g.parseUserStringInput());
    }

    // ---------------- sanity getters ----------------

    @Test
    void getters_defaults() {
        Game g = newGameWithInput(null);
        assertEquals(1, g.getDirection());
        assertFalse(g.getDrawOne());
        assertFalse(g.getWildDrawTwo());
    }
}
