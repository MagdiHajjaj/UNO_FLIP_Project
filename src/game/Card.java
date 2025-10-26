package game;
import java.util.HashMap;
import java.util.Map;
/**
 * game.Card class for the game of UNO
 *
 * @author Edward Savin
 * @version M1
 * @see CardColor
 * @see CardType
 */
public class Card {
    private final CardColor color; //game.Card has a Color
    private final CardType type; //game.Card has a Type

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
     * Constructor for game.Card takes a Color and Type
     *
     * @param c game.CardColor enum used to construct game.Card
     * @param t game.CardType enum used to construct game.Card
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
     * Getter for Color of game.Card
     *
     * @return the game.CardColor enum value of the game.Card
     */
    public CardColor getColor() {
        return color;
    }

    /**
     * Getter for Type of game.Card
     *
     * @return the game.CardType enum value of game.Card
     */
    public CardType getType() {
        return type;
    }

    /**
     * String representation of game.Card
     *
     * @return the game.CardColor and game.CardType enum values of game.Card as a String
     */
    @Override
    public String toString() {
        return color + " " + type;
    }


    /**
     * Compares this {@code Card} to the specified object for equality.
     * Two cards are considered equal if:
     *   They are the same instance (i.e., refer to the same object)
     *   The specified object is also a Card, and both have the same color and type.
     *
     *
     * @param o the object to compare this card against
     * @return {@code true} if the specified object is equal to this card;
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return this.color == card.color && this.type == card.type;
    }



}
