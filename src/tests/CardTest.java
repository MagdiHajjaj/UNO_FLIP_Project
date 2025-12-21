package tests;
import game.Card;
import game.CardColor;
import game.CardType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for game.Card
 *
 * @author Edward Savin
 * @version M1
 */
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

        assertEquals(CardColor.RED, c1.getLightColor());
        assertEquals(CardType.FIVE, c1.getLightType());
        assertEquals(CardColor.YELLOW, c2.getLightColor());
        assertEquals(CardType.FOUR, c2.getLightType());
        assertEquals(CardColor.BLUE, c3.getLightColor());
        assertEquals(CardType.THREE, c3.getLightType());
        assertEquals(CardColor.GREEN, c4.getLightColor());
        assertEquals(CardType.SIX, c4.getLightType());
        assertEquals(CardColor.RED, c5.getLightColor());
        assertEquals(CardType.REVERSE, c5.getLightType());
        assertEquals(CardType.WILD_DRAW_TWO, c6.getLightType());
        assertEquals(CardColor.WILD, c6.getLightColor());

        assertThrows(IllegalArgumentException.class, () -> new Card(CardColor.RED, CardType.WILD));
        assertThrows(IllegalArgumentException.class, () -> new Card(CardColor.YELLOW, CardType.WILD_DRAW_TWO));
        assertDoesNotThrow(() -> new Card(CardColor.RED, CardType.FIVE));
        assertDoesNotThrow(() -> new Card(CardColor.WILD, CardType.WILD));
        assertThrows(IllegalArgumentException.class, () -> new Card(CardColor.WILD, CardType.FIVE));
        assertThrows(IllegalArgumentException.class, () -> new Card(CardColor.WILD, CardType.SKIP));
    }


    /**
     * Test toString method
     */
    @Test
    void toStringTest(){
        assertEquals("RED FIVE", c1.getLightToString());
        assertEquals("YELLOW FOUR", c2.getLightToString());
        assertEquals("BLUE THREE", c3.getLightToString());
        assertEquals("GREEN SIX", c4.getLightToString());
        assertEquals("RED REVERSE", c5.getLightToString());
        assertEquals("WILD WILD_DRAW_TWO", c6.getLightToString());
        assertEquals("YELLOW DRAW_ONE", c7.getLightToString());
        assertEquals("WILD WILD", c8.getLightToString());
    }

    /**
     * Test Equals method
     */
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
