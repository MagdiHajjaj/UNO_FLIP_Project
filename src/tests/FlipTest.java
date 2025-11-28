package tests;

import game.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Class for UNO Flip implementation
 */
public class FlipTest {

    private UNOModel game;
    private ArrayList<Player> players;

    @BeforeEach
    public void setUp() {
        game = new UNOModel();
        game.newGame(3, 0); // 3 players, no AI
        players = game.getPlayers();

        // Clear hands for controlled tests
        for (Player p : players) {
            p.clearHand();
            p.addCard(new Card(CardColor.GREEN, CardType.ONE));
        }

    }

    @Test
    void testInitialState() {
        assertEquals(Side.LIGHT, game.getSide(), "Initial side should be LIGHT");
        for (Player p : players) {
            for (Card c : p.getHand()) {
                assertTrue(c.getColor().isLightSide(), "All cards should start on light side");
            }
        }
    }

    @Test
    void testInitialFlip() {
        Card flipCard = new Card(game.getTopCard().getColor(), CardType.FLIP);
        players.get(0).addCard(flipCard);

        game.playCard(players.get(0).getHand().get(1));

        assertFalse(game.getTopCard().getColor().isLightSide(), "Top card should be dark side after flip");
        assertEquals(Side.DARK, game.getSide(), "Game side should be DARK after first flip");
        assertEquals(Status.FLIP_CARD, game.getStatus(), "Status should be FLIP_CARD");
    }

    @Test
    void testFlipCard() {
        // Prepare two FLIP cards for first two players
        Card flip1 = new Card(game.getTopCard().getColor(), CardType.FLIP);
        Card flip2 = new Card(game.getTopCard().getColor(), CardType.FLIP);

        players.get(0).addCard(flip1);
        players.get(1).addCard(flip2);

        // First player plays FLIP
        game.playCard(players.get(0).getHand().get(1));

        assertEquals(Side.DARK, game.getSide(), "Side should be DARK after first flip");
        assertFalse(game.getTopCard().getColor().isLightSide(), "Top card should be dark side");
        assertEquals(Status.FLIP_CARD, game.getStatus(), "Status should be FLIP_CARD");
        game.nextPlayer();
        // Second player plays FLIP
        game.playCard(players.get(1).getHand().get(1));

        assertEquals(Side.LIGHT, game.getSide(), "Side should return to LIGHT after second flip");
        assertTrue(game.getTopCard().getColor().isLightSide(), "Top card should be light side");
        assertEquals(Status.FLIP_CARD, game.getStatus(), "Status should still be FLIP_CARD");
    }

    @Test
    void testDrawFive() {
        Card flipCard = new Card(game.getTopCard().getColor(), CardType.FLIP);
        players.get(0).addCard(flipCard);
        Card drawCard = new Card(game.getTopCard().getColor(), CardType.DRAW_ONE);
        players.get(1).addCard(drawCard);

        int initialHandSize = players.get(2).getHand().size();

        // First player plays FLIP
        game.playCard(players.get(0).getHand().get(1));
        assertEquals(Side.DARK, game.getSide(), "Side should be DARK after flip");
        game.nextPlayer();

        // Second player plays DRAW_ONE

        game.playCard(players.get(1).getHand().get(1));

        assertEquals(initialHandSize + 5, players.get(2).getHand().size(), "Third player should draw 5 cards");

        // After DRAW_FIVE, turn should return to first player
        game.nextPlayer();
        assertEquals(players.get(0), game.getCurrentPlayer(), "Next player should be first player after DRAW_FIVE");
    }

    @Test
    void testSkipEveryone() {
        Card flipCard = new Card(game.getTopCard().getColor(), CardType.FLIP);
        players.get(0).addCard(flipCard);
        Card skipCard = new Card(game.getTopCard().getColor(), CardType.SKIP);
        players.get(1).addCard(skipCard);

        game.playCard(players.get(0).getHand().get(1));
        assertEquals(Side.DARK, game.getSide(), "Side should be DARK after flip");
        game.nextPlayer();

        assertEquals(players.get(1).getHand().get(1).getType(), CardType.SKIP_EVERYONE, "Status should be SKIP_EVERYONE");
        game.playCard(players.get(1).getHand().get(1));
        assertEquals(players.get(1), game.getCurrentPlayer(), "Current player should be second player after skip everyone");
    }

    @Test
    void testWildDrawColor() {
        Card flipCard = new Card(game.getTopCard().getColor(), CardType.FLIP);
        players.get(0).addCard(flipCard);
        Card wildCard = new Card(CardColor.WILD, CardType.WILD_DRAW_TWO);
        players.get(1).addCard(wildCard);

        game.playCard(players.get(0).getHand().get(1));
        game.nextPlayer();

        int initialHandSize = players.get(2).getHand().size();
        game.playCard(players.get(1).getHand().get(1));
        game.setWildColor(CardColor.GREEN);

        assertTrue(players.get(2).getHand().size() > initialHandSize, "Third player should draw cards until matching color");
        Card lastCard = players.get(2).getHand().get(players.get(2).getHand().size() - 1);
        assertTrue(lastCard.getColor() == CardColor.TEAL || lastCard.getColor() == CardColor.WILD,
                "Last drawn card should be TEAL or WILD");
    }

    @Test
    void testFlipPoints() {
        Card flipCard = new Card(CardColor.BLUE, CardType.FLIP);
        players.get(0).clearHand();
        players.get(1).addCard(flipCard);
        players.get(2).clearHand();

        assertEquals(21, game.calculateScore(), "FLIP card should be worth 20 points");
    }

    @Test
    void testDrawFivePoints() {
        Card flipCard = new Card(game.getTopCard().getColor(), CardType.FLIP);
        players.get(0).addCard(flipCard);
        players.get(1).addCard(new Card(CardColor.RED, CardType.DRAW_ONE));
        game.playCard(players.get(0).getHand().get(1));

        players.get(0).clearHand();
        players.get(2).clearHand();

        assertEquals(21, game.calculateScore(), "DRAW_FIVE should be worth 20 points");
    }

    @Test
    void testSkipEveryonePoints() {
        Card flipCard = new Card(game.getTopCard().getColor(), CardType.FLIP);
        players.get(0).addCard(flipCard);
        players.get(1).addCard(new Card(CardColor.RED, CardType.SKIP));
        game.playCard(players.get(0).getHand().get(1));
        players.get(0).clearHand();
        players.get(2).clearHand();


        assertEquals(31, game.calculateScore(), "SKIP_EVERYONE should be worth 30 points");
    }

    @Test
    void testWildDrawColorPoints() {
        Card flipCard = new Card(game.getTopCard().getColor(), CardType.FLIP);
        players.get(0).addCard(flipCard);
        players.get(1).addCard(new Card(CardColor.WILD, CardType.WILD_DRAW_TWO));

        game.playCard(players.get(0).getHand().get(1));

        players.get(0).clearHand();
        players.get(2).clearHand();

        assertEquals(61, game.calculateScore(), "WILD_DRAW_COLOR should be worth 60 points");
    }
}
