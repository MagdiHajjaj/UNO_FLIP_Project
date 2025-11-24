//package tests;
///**
// * Test class for game.Player
// *
// * @author Edward Savin
// * @version M1
// */
//
//
//import game.Card;
//import game.CardColor;
//import game.CardType;
//import game.Player;
//import org.junit.jupiter.api.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//public class PlayerTest {
//    private Player p1;
//    private Player p2;
//    private Player p3;
//    private Player p4;
//
//    @BeforeEach
//    public void setUp(){
//        p1 = new Player("p1");
//        p2 = new Player("p2");
//        p3 = new Player("p3");
//        p4 = new Player("p4");
//    }
//
//    /**
//     * Test Constructor and getName for Player
//     */
//    @Test
//    public void playerTest(){
//        assertEquals("p1", p1.getName());
//        assertEquals("p2", p2.getName());
//        assertEquals("p3", p3.getName());
//        assertEquals("p4", p4.getName());
//    }
//
//    /**
//     * Test Player Score methods
//     */
//    @Test
//    public void scoreTest(){
//        assertEquals(0,p1.getScore());
//        assertEquals(0,p2.getScore());
//
//        p3.addScore(1);
//        assertEquals(1,p3.getScore());
//
//        p4.setScore(2);
//        assertEquals(2,p4.getScore());
//    }
//
//    /**
//     * Test Player Hand Methods
//     */
//    @Test
//    public void handTest(){
//        Card c1 = new Card(CardColor.YELLOW, CardType.FIVE);
//        Card c2 = new Card(CardColor.RED, CardType.SKIP);
//        Card c3 = new Card(CardColor.WILD, CardType.WILD_DRAW_TWO);
//
//        assertEquals(0,p1.handSize());
//
//        p2.addCard(c1);
//        assertEquals(1,p2.handSize());
//        p2.removeCardAt(0);
//        assertEquals(0,p2.handSize());
//        p3.addCard(c2);
//        p4.addCard(c2);
//        assertEquals(p4.getHand(), p3.getHand());
//        assertNotEquals(p3.getHand(), p2.getHand());
//        p4.addCard(c3);
//        p4.addCard(c1);
//        assertEquals(3,p4.handSize());
//        p4.clearHand();
//        assertEquals(0,p4.handSize());
//    }
//
//    /**
//     * Test Player toString method
//     */
//    @Test
//    public void toStringPLayerTest(){
//        Card c1 = new Card(CardColor.YELLOW, CardType.FIVE);
//        Card c2 = new Card(CardColor.RED, CardType.SKIP);
//        Card c3 = new Card(CardColor.WILD, CardType.WILD_DRAW_TWO);
//
//        p1.addCard(c1);
//        assertEquals("Player{p1, hand=1, score=0}", p1.toString());
//
//        p2.addCard(c2);
//        p2.addCard(c3);
//        assertEquals("Player{p2, hand=2, score=0}", p2.toString());
//
//        p3.addCard(c1);
//        p3.addScore(5);
//        assertEquals("Player{p3, hand=1, score=5}", p3.toString());
//
//        p1.removeCardAt(0);
//        assertNotEquals(p4.toString(), p1.toString());
//    }
//
//}
