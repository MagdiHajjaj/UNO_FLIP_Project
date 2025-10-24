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

    /**
     * Constructor for Card takes a Color and Type
     *
     * @param c CardColor enum used to construct Card
     * @param t CardType enum used to construct Card
     */
    public Card(CardColor c, CardType t){
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
