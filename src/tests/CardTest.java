package tests;
import game.Card;
import game.CardColor;
import game.CardType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    private Card c1;
    private Card c2;
    private Card c3;
    private Card c4;
    private Card c5;
    private Card c6;
    private Card c7;
    private Card c8;
    @BeforeEach
    void setUp() {
         c1 = new Card(CardColor.RED, CardType.FIVE);
         c2 = new Card(CardColor.YELLOW, CardType.FOUR);
         c3 = new Card(CardColor.BLUE, CardType.THREE);
         c4 = new Card(CardColor.GREEN, CardType.SIX);
         c5 = new Card(CardColor.RED, CardType.REVERSE);
         c6 = new Card(CardColor.WILD, CardType.WILD_DRAW_TWO);
         c7 = new Card(CardColor.YELLOW, CardType.DRAW_ONE);
         c8 = new Card(CardColor.WILD, CardType.WILD);
    }

    /**
     * Test for Card class
     * Testing Constructor and Getters
     */
    @Test
    void cardTest(){

        assertEquals(CardColor.RED, c1.getColor());
        assertEquals(CardType.FIVE, c1.getType());
        assertEquals(CardColor.YELLOW, c2.getColor());
        assertEquals(CardType.FOUR, c2.getType());
        assertEquals(CardColor.BLUE, c3.getColor());
        assertEquals(CardType.THREE, c3.getType());
        assertEquals(CardColor.GREEN, c4.getColor());
        assertEquals(CardType.SIX, c4.getType());
        assertEquals(CardColor.RED, c5.getColor());
        assertEquals(CardType.REVERSE, c5.getType());
        assertEquals(CardType.WILD_DRAW_TWO, c6.getType());
        assertEquals(CardColor.WILD, c6.getColor());

        assertThrows(IllegalArgumentException.class, () -> new Card(CardColor.RED, CardType.WILD));
        assertThrows(IllegalArgumentException.class, () -> new Card(CardColor.YELLOW, CardType.WILD_DRAW_TWO));
        assertDoesNotThrow(() -> new Card(CardColor.RED, CardType.FIVE));
        assertDoesNotThrow(() -> new Card(CardColor.WILD, CardType.WILD));
        assertThrows(IllegalArgumentException.class, () -> new Card(CardColor.WILD, CardType.FIVE));
        assertThrows(IllegalArgumentException.class, () -> new Card(CardColor.WILD, CardType.SKIP));
    }

    @Test
    void pointTest(){
        assertEquals(5, c1.getPoint());
        assertEquals(4, c2.getPoint());
        assertEquals(3, c3.getPoint());
        assertEquals(6, c4.getPoint());
        assertEquals(20, c5.getPoint());
        assertEquals(50, c6.getPoint());
        assertEquals(10, c7.getPoint());
        assertEquals(40, c8.getPoint());
    }

    @Test
    void toStringTest(){
        assertEquals("RED FIVE", c1.toString());
        assertEquals("YELLOW FOUR", c2.toString());
        assertEquals("BLUE THREE", c3.toString());
        assertEquals("GREEN SIX", c4.toString());
        assertEquals("RED REVERSE", c5.toString());
        assertEquals("WILD WILD_DRAW_TWO", c6.toString());
        assertEquals("YELLOW DRAW_ONE", c7.toString());
        assertEquals("WILD WILD", c8.toString());
    }

    @Test
    void equalsTest(){
        assertNotEquals(c1, c2);
        Card c1x = new Card(CardColor.RED, CardType.FIVE);
        assertEquals(c1, c1x);
        assertEquals(c1x, c1);
        assertNotEquals(null, c8);
        assertNotEquals(c8, c1);
        Card c1y = new Card(CardColor.YELLOW, CardType.FIVE);
        assertNotEquals(c1, c1y);
        assertNotEquals(c1, c5);
    }
}
