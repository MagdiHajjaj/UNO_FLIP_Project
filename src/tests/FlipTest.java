//package tests;
//import game.*;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//
//import static org.junit.jupiter.api.Assertions.*;
///**
// * Test Class for new flip implementation
// *
// * @author Edward Savin
// * @version M3
// */
//public class FlipTest {
//    private UNOModel game;
//    private Player p1, p2, p3;
//
//    /**
//     * setup before each test a new game with 3 players
//     */
//    @BeforeEach
//    public void setUp() {
//        game = new UNOModel();
//        game.newGame(3);
//
//        ArrayList<Player> players = game.getPlayers();
//        p1 = players.get(0);
//        p2 = players.get(1);
//        p3 = players.get(2);
//    }
//
//    /**
//     * test the initial state is lightside and cards are lightside
//     */
//    @Test
//    void testInitialState(){
//        assertFalse(game.getIsDarkSide(), "No Flip card in initial state should be light side" );
//        for (Player p : game.getPlayers()) {
//            for (Card card : p.getHand()){
//                assertTrue(card.getColor().isLightSide(), "Card should be light side (true)" );
//            }
//        }
//    }
//
//    /**
//     * test after first flip card is played
//     */
//    @Test
//    void initialFLip(){
//        Card flipCard = new Card(game.getTopCard().getColor(), CardType.FLIP);
//
//        p1.addCard(flipCard);
//        game.playCard(flipCard);
//
//
//        assertFalse(game.getTopCard().getColor().isLightSide(), "Card should be dark side (false)" );
//        assertTrue(game.getIsDarkSide(), "Flip card should dark side");
//        assertEquals(Status.FLIP_CARD, game.getStatus());
//
//    }
//
//    /**
//     * test multiple flips ensure states changes accordingly
//     */
//    @Test
//    void testFlipCard(){
//        assertFalse(game.getIsDarkSide(), "No Flip card in initial state should be light side" );
//        Card flipCard = new Card(game.getTopCard().getColor(), CardType.FLIP);
//        Card flipCard2 = new Card(game.getTopCard().getColor(), CardType.FLIP);
//        p1.addCard(flipCard);
//        game.playCard(flipCard);
//        assertFalse(game.getTopCard().getColor().isLightSide(), "Card should be dark side (false)" );
//        assertTrue(game.getIsDarkSide(), "Flip card should dark side");
//        assertEquals(Status.FLIP_CARD, game.getStatus());
//        game.nextPlayer();
//        p2.addCard(flipCard2);
//        game.playCard(flipCard2);
//        assertTrue(game.getTopCard().getColor().isLightSide(), "Card should be light side (true)" );
//        assertFalse(game.getIsDarkSide(), "Flip card 2 should light side");
//        assertEquals(Status.FLIP_CARD, game.getStatus());
//    }
//
//    /**
//     * test new draw five card
//     */
//    @Test
//    void testDrawFive(){
//        Card flipCard = new Card(game.getTopCard().getColor(), CardType.FLIP);
//        p1.addCard(flipCard);
//        Card draw1Card = new Card(game.getTopCard().getColor(), CardType.DRAW_ONE);
//        p2.addCard(draw1Card);
//        int initialHandSize = p3.getHand().size();
//        game.playCard(flipCard);
//        assertTrue(game.getIsDarkSide(), "Flip card should dark side");
//
//        game.nextPlayer();
//        game.playCard(draw1Card);
//        assertEquals(Status.DRAW_FIVE, game.getStatus());
//        assertEquals(initialHandSize + 5 , p3.getHand().size());
//        //advance next player to check for Draw 5 skip
//        game.nextPlayer();
//        assertEquals(p1, game.getCurrentPlayer());
//
//    }
//
//    /**
//     * test new skip everyone card
//     */
//    @Test
//    void testSkipEveryone(){
//        Card flipCard = new Card(game.getTopCard().getColor(), CardType.FLIP);
//        p1.addCard(flipCard);
//
//        Card skipCard = new Card(game.getTopCard().getColor(), CardType.SKIP);
//        p2.addCard(skipCard);
//
//        game.playCard(flipCard);
//        assertTrue(game.getIsDarkSide(), "Flip card should dark side");
//
//        game.nextPlayer();
//        game.playCard(skipCard);
//        assertEquals(Status.SKIP_EVERYONE, game.getStatus());
//        //advance to skip all other players
//        game.nextPlayer();
//        assertEquals(p2, game.getCurrentPlayer());
//    }
//
//    /**
//     * test new wild draw color card
//     */
//    @Test
//    void testWildDrawColor(){
//        Card flipCard = new Card(game.getTopCard().getColor(), CardType.FLIP);
//        p1.addCard(flipCard);
//
//        Card wildCard = new Card(game.getTopCard().getColor(), CardType.WILD_DRAW_TWO);
//        p2.addCard(wildCard);
//
//        game.playCard(flipCard);
//        game.nextPlayer();
//        int initialHandSize = p3.getHand().size();
//        game.playCard(wildCard);
//        game.setWildColor(CardColor.GREEN);
//
//
//        assertTrue(p3.getHand().size() > initialHandSize, "p3 has to draw cards till they get teal");
//        Card drawSelectedColorCard = p3.getHand().get(p3.getHand().size()-1);
//        assertTrue(CardColor.TEAL == drawSelectedColorCard.getColor() || CardColor.WILD == drawSelectedColorCard.getColor());
//    }
//
//    //the following tests ensure the points of the new cards
//
//    @Test
//    void testFlipPoints(){
//        Card flipCard = new Card(game.getTopCard().getColor(), CardType.FLIP);
//
//        p1.clearHand();
//        p2.clearHand();
//        p3.clearHand();
//        p2.addCard(flipCard);
//
//        //only a flip in rotation, score should be 20
//        assertEquals(20, game.calculateScore());
//
//    }
//    @Test
//    void testDrawFivePoints() {
//        Card flipCard = new Card(game.getTopCard().getColor(), CardType.FLIP);
//        p1.addCard(flipCard);
//        game.playCard(flipCard);
//
//        p2.clearHand();
//        p3.clearHand();
//        p1.clearHand();
//
//        p2.addCard(new Card(CardColor.RED, CardType.DRAW_ONE));
//        //only a draw 5 in rotation, score should be 20
//        assertEquals(20, game.calculateScore());
//    }
//
//    @Test
//    void testSkipEveryonePoints() {
//        Card flipCard = new Card(game.getTopCard().getColor(), CardType.FLIP);
//        p1.addCard(flipCard);
//        game.playCard(flipCard);
//
//        p2.clearHand();
//        p3.clearHand();
//        p1.clearHand();
//
//        p2.addCard(new Card(CardColor.RED, CardType.SKIP));
//        //only a skip everyone in rotation, score should be 30
//        assertEquals(30, game.calculateScore());
//    }
//
//    @Test
//    void testWildDrawColorPoints(){
//        Card flipCard = new Card(game.getTopCard().getColor(), CardType.FLIP);
//        p1.addCard(flipCard);
//        game.playCard(flipCard);
//
//        p2.clearHand();
//        p3.clearHand();
//        p1.clearHand();
//
//        p2.addCard(new Card(CardColor.RED, CardType.WILD_DRAW_TWO));
//        //only a wild draw color in rotation, score should be 60
//        assertEquals(60, game.calculateScore());
//    }
//}
