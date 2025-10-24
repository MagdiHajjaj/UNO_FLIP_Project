import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CardTest {

    @Test
    void cardTest(){
        Card c1 = new Card(CardColor.RED, CardType.FIVE);
        Card c2 = new Card(CardColor.YELLOW, CardType.FOUR);
        Card c3 = new Card(CardColor.BLUE, CardType.THREE);
        Card c4 = new Card(CardColor.GREEN, CardType.SIX);

        assertEquals(CardColor.RED, c1.getColor());
        assertEquals(CardType.FIVE, c1.getType());
        assertEquals(CardColor.YELLOW, c2.getColor());
        assertEquals(CardType.FOUR, c2.getType());
        assertEquals(CardColor.BLUE, c3.getColor());
        assertEquals(CardType.THREE, c3.getType());
        assertEquals(CardColor.GREEN, c4.getColor());
        assertEquals(CardType.SIX, c4.getType());

        assertThrows(IllegalArgumentException.class, () -> new Card(CardColor.RED, CardType.WILD));
        assertThrows(IllegalArgumentException.class, () -> new Card(CardColor.YELLOW, CardType.WILD_DRAW_TWO));
        assertDoesNotThrow(() -> new Card(CardColor.RED, CardType.FIVE));
        assertDoesNotThrow(() -> new Card(CardColor.WILD, CardType.WILD));
        assertThrows(IllegalArgumentException.class, () -> new Card(CardColor.WILD, CardType.FIVE));
        assertThrows(IllegalArgumentException.class, () -> new Card(CardColor.WILD, CardType.SKIP));
    }
}
