package game;
/**
 * game.CardColor enum for the game of UNO
 *
 * @author Magdi Hajjaj
 * @id 101299841
 * @version M3
 */
public enum CardColor {
    //Light
    RED(true), BLUE(true), YELLOW(true), GREEN(true),
    //Dark
    ORANGE(false), PINK(false), PURPLE(false), TEAL(false),

    WILD(true);
    private final boolean isLight;

    /**
     * Constructor for game.CardColor
     *
     * @param isLight
     */
    CardColor(boolean isLight) {
        this.isLight = isLight;
    }
    /**
     * Returns true if this card is on the light side, false otherwise.
     *
     * @return true if this card is on the light side, false otherwise
     */
    public boolean isLightSide() {
        return isLight;
    }
}
