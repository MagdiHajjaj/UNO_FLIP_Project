public class Card {
    private final CardColor color;
    private final CardType type;

    public Card(CardColor c, CardType t){
        color = c;
        type = t;
    }

    public CardColor getColor() {
        return color;
    }

    public CardType getType() {
        return type;
    }

    @Override
    public String toString() {
        return color + " " + type;
    }
}
