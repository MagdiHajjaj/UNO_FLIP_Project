import java.util.HashMap;
import java.util.Map;

/**
 * Card class for the game of UNO
 *
 * @author Edward Savin
 * @version M1
 * @see CardColor
 * @see CardType
 */
public class Card {
    private final CardColor color; //Card has a Color
    private final CardType type; //Card has a Type

    //Point System
    private static final Map<CardType, Integer> Point_System = new HashMap<>();
    static {
        Point_System.put(CardType.ONE, 1);
        Point_System.put(CardType.TWO, 2);
        Point_System.put(CardType.THREE, 3);
        Point_System.put(CardType.FOUR, 4);
        Point_System.put(CardType.FIVE, 5);
        Point_System.put(CardType.SIX, 6);
        Point_System.put(CardType.SEVEN, 7);
        Point_System.put(CardType.EIGHT, 8);
        Point_System.put(CardType.NINE, 9);

        Point_System.put(CardType.DRAW_ONE, 10);

        Point_System.put(CardType.SKIP, 20);
        Point_System.put(CardType.REVERSE, 20);

        Point_System.put(CardType.WILD, 40);
        Point_System.put(CardType.WILD_DRAW_TWO, 50);
    }

    /**
     * getter for points associated to card
     *
     * @return integer of Cards point value
     */
    public int getPoint() {
        return Point_System.get(this.type);
    }

    /**
     * Constructor for Card takes a Color and Type
     *
     * @param c CardColor enum used to construct Card
     * @param t CardType enum used to construct Card
     */
    public Card(CardColor c, CardType t){
        if (t == CardType.WILD || t == CardType.WILD_DRAW_TWO) {
            if (c != CardColor.WILD) {
                throw new IllegalArgumentException("Wild cards can only be given the color: wild");
            }
        } else {
            if (c == CardColor.WILD){
                throw new IllegalArgumentException("Wild cards can only be given the color: wild");
            }
        }
        color = c;
        type = t;

    }

    /**
     * Getter for Color of Card
     *
     * @return the CardColor enum value of the Card
     */
    public CardColor getColor() {
        return color;
    }

    /**
     * Getter for Type of Card
     *
     * @return the CardType enum value of Card
     */
    public CardType getType() {
        return type;
    }

    /**
     * String representation of Card
     *
     * @return the CardColor and CardType enum values of Card as a String
     */
    @Override
    public String toString() {
        return color + " " + type;
    }




}
