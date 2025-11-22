package game;

public enum CardColor {
    //Light
    RED(true), BLUE(true), YELLOW(true), GREEN(true),
    //Dark
    ORANGE(false), PINK(false), PURPLE(false), TEAL(false),

    WILD(true);
    private final boolean isLight;

    CardColor(boolean isLight) {
        this.isLight = isLight;
    }

    public boolean isLightSide() {
        return isLight;
    }
}
