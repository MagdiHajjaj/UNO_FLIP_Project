/**
 * PileSystem class for the game of UNO
 *
 * @author Magdi Hajjaj
 * @version M1
 */
import java.util.*;

public class PileSystem {
    private Stack<Card> drawPile;
    private Stack<Card> discardPile;

    /**
     * Constructor for Card takes a Color and Type
     *
     * @paramc
     */
    public PileSystem() {
        drawPile = new Stack<>();
        discardPile = new Stack<>();
        createDrawPile();
        shuffleDrawPile();
    }

    /**
     * 
     * @paramc
     */
    private void createDrawPile(){
        for(CardColor color: CardColor.values()){
            if (color == CardColor.WILD) {
                // Add 4 of each wild type
                for (int i = 0; i < 4; i++) {
                    drawPile.push(new Card(CardColor.WILD, CardType.WILD));
                    drawPile.push(new Card(CardColor.WILD, CardType.WILD_DRAW_TWO));
                }
            }else {
                for (CardType cardType : CardType.values()) {
                    if (cardType != CardType.WILD_DRAW_TWO && cardType != CardType.WILD) {
                        drawPile.push(new Card(color, cardType));
                        drawPile.push(new Card(color, cardType));
                    }
                }
            }
        }
    }
    /**
     *
     *
     * @paramc
     */
    public void shuffleDrawPile(){
        Collections.shuffle(drawPile);
    }

    /**
     *
     * @paramc
     */
    public Card drawCard(){
        if(drawPile.isEmpty()){
            restoreDrawPile();
        }
        return drawPile.pop();
    }

    /**
     *
     *
     * @paramc
     */
    public void restoreDrawPile(){
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
     *
     *
     * @paramc
     */
    public void addToDiscardPile(Card c){
        discardPile.push(c);
    }

    /**
     *
     *
     * @paramc
     */
    public Card peekTopDiscard(){
        if (discardPile.isEmpty()) {
            return null;
        }
        return discardPile.peek();
    }
}

