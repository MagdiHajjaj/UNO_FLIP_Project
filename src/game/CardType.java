package game;

/**
 * game.CardType enum for the game of UNO
 *
 * @author Magdi Hajjaj
 * @id 101299841
 * @version M5
 */
public enum CardType {
    // Light
    ONE(true), TWO(true), THREE(true), FOUR(true), FIVE(true),
    SIX(true), SEVEN(true), EIGHT(true), NINE(true),

    REVERSE(true),
    WILD(true),
    FLIP(true),
    DRAW_ONE(true),
    SKIP(true),
    WILD_DRAW_TWO(true),

    // Dark
    SKIP_EVERYONE(false),
    DRAW_FIVE(false),
    WILD_DRAW_COLOR(false);

    private final boolean isLight;

    CardType(boolean isLight) {
        this.isLight = isLight;
    }

    public boolean isLightSide() {
        return isLight;
    }
}

