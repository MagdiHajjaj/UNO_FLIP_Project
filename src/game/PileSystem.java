package game;
/**
 * game.PileSystem class for the game of UNO
 *
 * @author Magdi Hajjaj
 * @id 101299841
 * @version M1
 */
import java.io.Serializable;
import java.util.*;

public class PileSystem implements Cloneable, Serializable {

    /**
     * Creates a deep copy of this pile system
     */
    @Override
    public PileSystem clone() {
        try {
            PileSystem cloned = (PileSystem) super.clone();

            // Deep copy draw pile
            cloned.drawPile = new Stack<>();
            for (Card card : this.drawPile) {
                cloned.drawPile.push(card.clone());
            }

            // Deep copy discard pile
            cloned.discardPile = new Stack<>();
            for (Card card : this.discardPile) {
                cloned.discardPile.push(card.clone());
            }

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported", e);
        }
    }
    private Stack<Card> drawPile;
    private Stack<Card> discardPile;
    private static final long serialVersionUID = 1L;
    /**
     * Constructor for game.Card takes a Color and Type
     *
     */
    public PileSystem() {
        drawPile = new Stack<>();
        discardPile = new Stack<>();
        createDrawPile();

        shuffleDrawPile();
    }

    /**
     * Creates and initializes the draw pile with all the cards needed for the game.
     *
     * The resulting draw pile represents a complete deck ready to be shuffled and used in the game.
     */
    private void createDrawPile(){

        for (CardColor color : CardColor.values()) {

            if (!color.isLightSide()) {
                continue; // skip dark colors
            }

            if (color == CardColor.WILD) {

                for (int i = 0; i < 4; i++) {
                    drawPile.push(new Card(CardColor.WILD, CardType.WILD));
                    drawPile.push(new Card(CardColor.WILD, CardType.WILD_DRAW_TWO));
                }
                continue;
            }

            for (CardType type : CardType.values()) {
                if (!type.isLightSide()) continue; // skip dark types

                if (type == CardType.WILD || type == CardType.WILD_DRAW_TWO) {
                    continue; // skip wilds during color loop
                }

                drawPile.push(new Card(color, type));
                drawPile.push(new Card(color, type));
            }
        }
    }
    /**
     * Shuffles the draw pile to randomize the order of the cards.
     *
     * This method ensures that the cards in the draw pile are in a random sequence
     * before the game begins or when the pile needs to be reshuffled.
     */
    private void shuffleDrawPile(){
        Collections.shuffle(drawPile);
    }

    /**
     * Draws a card from the top of the draw pile.
     *
     * If the draw pile is empty, it restores the pile before drawing a card.
     * @return the card drawn from the top of the draw pile
     */
    public Card drawCard(){
        if(isDrawPileEmpty()){
            restoreDrawPile();
        }
        return drawPile.pop();
    }

    /**
     * Restores the draw pile by transferring cards from the discard pile.
     *
     * The top card of the discard pile is kept on the discard pile, while all other cards
     * are moved to the draw pile and shuffled. This method ensures that the game can
     * continue when the draw pile becomes empty.
     *
     * @throws IllegalStateException if the discard pile is empty and the draw pile cannot be restored
     */
    private void restoreDrawPile(){
        if (discardPile.isEmpty()) {
            throw new IllegalStateException("Cannot restore draw pile — discard pile is empty!");
        }
        Card topCard = discardPile.pop();

        while(!discardPile.isEmpty()){
            drawPile.push(discardPile.pop());
        }
        shuffleDrawPile();
        discardPile.push(topCard);
    }

    /**
     * Adds a card to the top of the discard pile.
     *
     * @param c the card to be added to the discard pile
     */
    public void addToDiscardPile(Card c){
        discardPile.push(c);
    }

    /**
     * Returns the top card of the discard pile without removing it.
     *
     * If the discard pile is empty, this method returns {@code null}.
     *
     * @return the top card of the discard pile, or {@code null} if the discard pile is empty
     */
    public Card peekTopDiscard(){
        if (discardPile.isEmpty()) {
            return null;
        }
        return discardPile.peek();
    }

    /**
     * Returns the number of cards currently in the draw pile.
     *
     * @return the size of the draw pile
     */
    public int getDrawPileSize(){
        return drawPile.size();
    }

    /**
     * Checks whether the draw pile is empty.
     *
     * @return true if the draw pile is empty, false otherwise
     */
    public boolean isDrawPileEmpty(){
        return drawPile.isEmpty();
    }
    /**
     * Flips all cards in the draw pile and discard pile.
     * Calls flipCard() on each card.
     */
    public void flip() {
        for (Card card : drawPile) {
            card.flipCard();
        }
        for (Card card : discardPile) {
            card.flipCard();
        }
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        PileSystem other = (PileSystem) obj;

        // Compare draw pile
        if (drawPile == null) {
            if (other.drawPile != null) return false;
        } else if (!drawPile.equals(other.drawPile)) return false;

        // Compare discard pile
        if (discardPile == null) {
            return other.discardPile == null;
        } else {
            return discardPile.equals(other.discardPile);
        }
    }

}

