package tests;
import game.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test Class for Game.UNOModel
 *
 * @author Edward Savin
 * @version M2
 */
public class UNOModelTest {
    private UNOModel model;

    @BeforeEach
    public void setUp() {
        model = new UNOModel();
    }
    @Test
    public void testStatusStartGame(){
        assertEquals(UNOModel.Status.INITIALIZING,model.getStatus());
        assertFalse(UNOModel.Status.GAME_OVER == model.getStatus());
        assertFalse(UNOModel.Status.ROUND_OVER == model.getStatus());
        model.newGame(2);
        assertEquals(UNOModel.Status.IN_GAME, model.getStatus());
        assertFalse(UNOModel.Status.GAME_OVER == model.getStatus());
        assertFalse(UNOModel.Status.ROUND_OVER == model.getStatus());
    }

    @Test
    public void testPlayerGetters(){
        assertTrue(model.getPlayers().isEmpty());
        assertNull(model.getCurrentPlayer());
        model.newGame(3);
        assertNotNull(model.getCurrentPlayer());
        assertEquals(3, model.getPlayers().size());
        assertEquals(0, model.getIndexOfCurrentPlayer());

        assertNotNull(model.getCurrentPlayerHand());
        assertEquals(7, model.getCurrentPlayerHand().size());
    }

    @Test
    public void testCardGetters(){
        model.newGame(2);
        assertNotNull(model.getTopCard());
        assertNotNull(model.getPileSystem());

        assertNull(model.getPlayedCard());
        assertNull(model.getLastDrawnCard());

        assertNull(model.getWildColor());
    }

    @Test
    public void testNextAndPreviousPlayerGetters(){

        assertThrows(IndexOutOfBoundsException.class, () -> {
            model.getNextPlayer();
        });
        assertThrows(IndexOutOfBoundsException.class, () -> {
            model.getPreviousPlayer();
        });
        model.newGame(3);
        Player pX = model.getCurrentPlayer();
        assertNotNull(model.getNextPlayer());
        assertNotNull(model.getPreviousPlayer());

        assertNotEquals(pX, model.getPreviousPlayer());
        assertNotEquals(pX, model.getNextPlayer());
    }

    @Test
    public void testDirectionGetter(){
        assertEquals(1, model.getDirection());

        model.newGame(3);
        assertEquals(1, model.getDirection());
    }

    @Test
    public void testRoundNumberGetter(){
        assertEquals(1, model.getRoundNumber());
        model.newGame(3);
        assertEquals(1, model.getRoundNumber());
        model.startNewRound();
        assertEquals(2, model.getRoundNumber());
    }

    @Test
    public void testIndexOfCurrentPlayer(){
        assertEquals(0, model.getIndexOfCurrentPlayer());
        model.newGame(3);
        assertEquals(0, model.getIndexOfCurrentPlayer());

        model.nextPlayer();
        assertEquals(1, model.getIndexOfCurrentPlayer());
    }

    @Test
    public void testNewGame() {
        model.newGame(3);
        assertNotNull(model.getCurrentPlayer());
        assertEquals(3, model.getPlayers().size());
        assertEquals("Player 1", model.getCurrentPlayer().getName());
        assertEquals(UNOModel.Status.IN_GAME, model.getStatus());
        assertNotNull(model.getCurrentPlayerHand());
        assertNotNull(model.getTopCard());
    }

    @Test
    public void testPlayerHands(){
        model.newGame(2);

        assertEquals(7, model.getCurrentPlayer().getHand().size());
        assertEquals(7, model.getNextPlayer().getHand().size());
    }

    @Test
    public void testDrawCard(){
        model.newGame(3);
        int initial = model.getCurrentPlayer().getHand().size();

        model.drawCard();
        assertNotEquals(initial, model.getCurrentPlayer().getHand().size());
        assertEquals(initial+1, model.getCurrentPlayer().getHand().size());
        assertEquals(UNOModel.Status.CARD_DRAWN, model.getStatus());
    }

    @Test
    public void testNextPlayer(){
        model.newGame(3);
        Player p1 = model.getCurrentPlayer();

        model.nextPlayer();
        assertNotEquals(p1, model.getCurrentPlayer());
    }

    @Test
    public void testValidateCard() {
        model.newGame(2);
        Card topCard = model.getTopCard();
        Card playCard = new Card(topCard.getColor(), CardType.ONE);
        assertTrue(model.validatePlacedCard(playCard,topCard, null));

        Card wildTestCard = new Card(CardColor.WILD, CardType.WILD);
        assertTrue(model.validatePlacedCard(wildTestCard,topCard, null));

        Card card1 = new Card(CardColor.BLUE, CardType.ONE);
        Card card2 = new Card(CardColor.RED, CardType.TWO);
        assertFalse(model.validatePlacedCard(card1,card2,null));
        Card card3 = new Card(CardColor.RED, CardType.ONE);
        assertTrue(model.validatePlacedCard(card1,card3,null));
    }

    @Test
    public void testWildColor(){
        model.newGame(3);
        model.setWildColor(CardColor.GREEN);
        assertEquals(CardColor.GREEN, model.getWildColor());
    }

    @Test
    public void testScoreCalculator() {
        assertEquals(0, model.calculateScore());
        model.newGame(2);
        int score =  model.calculateScore();
        assertTrue(score > 0);


    }
    @Test
    public void testGetAndSetScore() {
        model.newGame(2);
        Player p1 = model.getCurrentPlayer();
        assertEquals(0, p1.getScore());
        p1.setScore(1);
        assertEquals(1, p1.getScore());

    }

    @Test
    public void testGameWinner() {
        model.newGame(3);
        Player p1 = model.getCurrentPlayer();
        p1.setScore(500);
        assertTrue(model.checkWinner(p1));

        p1.setScore(499);
        assertFalse(model.checkWinner(p1));

    }

    @Test
    public void testNewRound() {
        model.newGame(3);
        int initial = model.getRoundNumber();
        assertEquals(1, initial);
        model.startNewRound();
        assertNotEquals(initial, model.getRoundNumber());
        assertEquals(2,  model.getRoundNumber());
        assertEquals(UNOModel.Status.IN_GAME, model.getStatus());

    }

    @Test
    public void testResetGame() {
        model.newGame(2);

        model.getCurrentPlayer().setScore(1);
        model.resetGame();
        assertEquals(UNOModel.Status.IN_GAME, model.getStatus());
        assertEquals(0, model.getCurrentPlayer().getScore());
        assertEquals(1, model.getRoundNumber());

    }
}
