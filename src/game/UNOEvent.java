package game;



import java.util.ArrayList;
import java.util.EventObject;
/**
 * public class UNOEvent
 *
 * @author Magdi Hajjaj
 * @id 101299841
 * @version M3
 */
public class UNOEvent extends EventObject {
    private final Status status;
    private Card playedCard;
    private Card lastDrawnCard;
    private Player currentPlayer;
    private Player nextPlayer;
    private Card topCard;
    private CardColor wildColor;
    private ArrayList<Player> players;
    private int drawCounter;
    private boolean computerWild;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public UNOEvent(Object source, Status status,
                    Card playedCard,
                    Card lastDrawnCard,
                    Player currentPlayer,
                    Player nextPlayer,
                    Card topCard,
                    CardColor wildColor,
                    ArrayList<Player> players,
                    Boolean computerWild)
    {
        super(source);
        this.status = status;
        this.playedCard = playedCard;
        this.lastDrawnCard = lastDrawnCard;
        this.currentPlayer = currentPlayer;
        this.nextPlayer = nextPlayer;
        this.topCard = topCard;
        this.wildColor = wildColor;
        this.players = players;
        this.computerWild = computerWild;
    }

    public void updateWildColor(CardColor wildColor) {
        this.wildColor = wildColor;
    }
    public Status getStatus() {
        return status;
    }
    public CardColor getWildColor() {
        return wildColor;
    }
    public Card getPlayedCard() {
        return playedCard;
    }

    public Card getLastDrawnCard() {
        return lastDrawnCard;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getNextPlayer() {
        return nextPlayer;
    }

    public Card getTopCard() {
        return topCard;
    }
    public int getDrawCounter() {
        return drawCounter;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void updateCardDrawnCounter(int counter) {
        drawCounter = counter;
    }
    public boolean getComputerWild() {
        return computerWild;
    }
    @Override
    public String toString() {
        return "UNOEvent [status=" + status + ", playedCard=" + playedCard + ", lastDrawnCard=" + lastDrawnCard
                + ", currentPlayer=" + currentPlayer + ", nextPlayer=" + nextPlayer + ", topCard=" + topCard
                + ", wildColor=" + wildColor + ", players=" + players + ", drawCounter=" + drawCounter + "]";
    }
 }
