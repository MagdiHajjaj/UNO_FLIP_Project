package game;

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




}
