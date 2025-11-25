package game;
import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
/**
 * game.Card class for the game of UNO
 *
 * @author Magdi Hajjaj
 * @id 101299841
 * @version M3
 * @see CardType
 * @see CardColor
 */
public class Card {
    private final CardColor lightColor;
    private final CardType lightType;

    private final CardColor darkColor;
    private final CardType darkType;

    private ImageIcon lightImage;
    private ImageIcon darkImage;


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

        Point_System.put(CardType.DRAW_ONE, 10); //light
        Point_System.put(CardType.DRAW_FIVE, 20); //dark

        Point_System.put(CardType.SKIP, 20); //light
        Point_System.put(CardType.SKIP_EVERYONE, 30); //dark

        Point_System.put(CardType.WILD_DRAW_TWO, 50); //light
        Point_System.put(CardType.WILD_DRAW_COLOR, 60); //dark

        Point_System.put(CardType.FLIP, 20);

        Point_System.put(CardType.REVERSE, 20);
        Point_System.put(CardType.WILD, 40);

    }

    /**
     * getter for points associated to card
     *
     * @return integer of Cards point value
     */
    public int getPoint() {
        return Point_System.get(getType());
    }

    /**
     * Constructor for game.Card takes a Color and Type
     *

     */
    public Card(CardColor lightColor, CardType lightType) {
        if ((lightType == CardType.WILD
                || lightType == CardType.WILD_DRAW_TWO
                || lightType == CardType.WILD_DRAW_COLOR)
                && lightColor != CardColor.WILD) {
            throw new IllegalArgumentException(lightType + " cards must have color WILD");
        }

        if (!(lightType == CardType.WILD
                || lightType == CardType.WILD_DRAW_TWO
                || lightType == CardType.WILD_DRAW_COLOR)
                && lightColor == CardColor.WILD) {
            throw new IllegalArgumentException("Non-wild cards cannot have color WILD");
        }
        this.lightColor = lightColor;
        this.lightType = lightType;

        this.darkColor = mapDarkColor(lightColor);
        this.darkType = mapDarkType(lightType);

        // Load images
        this.lightImage = loadImage(lightColor, lightType);
        this.darkImage = loadImage(darkColor, darkType);

    }
    /**
     * Maps a light-side card colour to its corresponding dark-side colour
     * used in UNO Flip mode.
     *
     * Light → Dark mapping:
     *  RED    → ORANGE
     *  BLUE   → PINK
     *  GREEN  → TEAL
     *  YELLOW → PURPLE
     *  WILD   → WILD (wild cards stay the same colour)
     *
     * @param lightColor the light-side CardColor to convert
     * @return the corresponding dark-side CardColor
     * @throws IllegalArgumentException if the provided colour does not match any known light-side value
     */
    private CardColor mapDarkColor(CardColor lightColor) {
        switch (lightColor) {
            case RED:
                return CardColor.ORANGE;
            case BLUE:
                return CardColor.PINK;
            case GREEN:
                return CardColor.TEAL;
            case YELLOW:
                return CardColor.PURPLE;
            case WILD:
                return CardColor.WILD;
            default:
                throw new IllegalArgumentException("Unknown light color: " + lightColor);
        }
    }
    /**
     * Maps a light-side card type to its corresponding dark-side type
     * for UNO Flip mode.
     *
     * Number cards map directly (1→1, 2→2, ..., 9→9).
     * Action cards follow these mappings:
     *
     * Light → Dark:
     *  DRAW_ONE        → DRAW_FIVE
     *  SKIP            → SKIP_EVERYONE
     *  REVERSE         → REVERSE (same)
     *  WILD            → WILD (same)
     *  WILD_DRAW_TWO   → WILD_DRAW_COLOR
     *  FLIP            → FLIP (same)
     *
     * @param lightType the light-side CardType to convert
     * @return the corresponding dark-side CardType
     * @throws IllegalArgumentException if an unknown type is provided
     */
    private CardType mapDarkType(CardType lightType) {
        switch (lightType) {
            case ONE:
                return CardType.ONE;
            case TWO:
                return CardType.TWO;
            case THREE:
                return CardType.THREE;
            case FOUR:
                return CardType.FOUR;
            case FIVE:
                return CardType.FIVE;
            case SIX:
                return CardType.SIX;
            case SEVEN:
                return CardType.SEVEN;
            case EIGHT:
                return CardType.EIGHT;
            case NINE:
                return CardType.NINE;

            // Light → Dark action cards
            case DRAW_ONE:
                return CardType.DRAW_FIVE;
            case SKIP:
                return CardType.SKIP_EVERYONE;
            case REVERSE:
                return CardType.REVERSE;

            // Wilds
            case WILD:
                return CardType.WILD;
            case WILD_DRAW_TWO:
                return CardType.WILD_DRAW_COLOR;

            // Flip stays Flip
            case FLIP:
                return CardType.FLIP;

            default:
                throw new IllegalArgumentException("Unknown light type: " + lightType);
        }
    }
    /**
     * Loads the image for the card based on its color and type.
     *
     * @return ImageIcon representing the card's image
     */
    private ImageIcon loadImage(CardColor color, CardType type) {

        String imageName;

        if (color == CardColor.WILD) {
            imageName = "wild_" + type.name().toLowerCase() + ".png";
        } else {
            imageName = color.name().toLowerCase() + "_" + type.name().toLowerCase() + ".png";
        }

        java.net.URL imageUrl = getClass().getResource("/images/" + imageName);

        if (imageUrl != null) {
            ImageIcon original = new ImageIcon(imageUrl);
            Image scaled = original.getImage().getScaledInstance(70, 100, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } else {
            System.err.println("Missing image: " + imageName);
            return new ImageIcon();
        }
    }
    /**
     * Returns the image of the card.
     *
     * @return ImageIcon representing the card's image
     */
    public ImageIcon getImage() {
        return UNOModel.isDarkSide ? darkImage : lightImage;
    }
    /**
     * Getter for Color of game.Card
     *
     * @return the game.CardColor enum value of the game.Card
     */
    public CardColor getColor() {
        return UNOModel.isDarkSide ? darkColor : lightColor;

    }
    /**
     * Getter for Type of game.Card
     *
     * @return the game.CardType enum value of game.Card
     */
    public CardType getType() {
        return UNOModel.isDarkSide ? darkType : lightType;

    }
    /**
     * String representation of game.Card
     *
     * @return the game.CardColor and game.CardType enum values of game.Card as a String
     */
    @Override
    public String toString() {

        return UNOModel.isDarkSide ? darkColor + " " + darkType : lightColor + " " + lightType;
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
        if (o == null || getClass() != o.getClass()) return false;

        Card other = (Card) o;

        // Compare ACTIVE (visible) side
        if (UNOModel.isDarkSide) {
            return this.darkColor == other.darkColor &&
                    this.darkType == other.darkType;
        } else {
            return this.lightColor == other.lightColor &&
                    this.lightType == other.lightType;
        }
    }
    /**
     * Returns the light-side colour of this card.
     *
     * @return the light-side CardColor
     */
    public CardColor getLightColor(){
        return lightColor;
    }
    /**
     * Returns the light-side type of this card.
     *
     * @return the light-side CardType
     */
    public CardType getLightType(){
        return lightType;
    }
    /**
     * Returns a string describing the light-side of this card
     * in the format "COLOR TYPE".
     *
     * @return a string containing the light-side colour and type
     */
    public String getLightToString() {
        return lightColor + " " + lightType;
    }

}
