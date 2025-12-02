package game;

import java.util.ArrayList;
import java.util.EventObject;

/**
 * Represents an event occurring within the UNO game.
 * This class acts as a data container used by the model to notify the view
 * and controller about state changes (e.g., card played, card drawn, wild color chosen, etc.).
 *
 * Each UNOEvent contains:
 *  - The current game status
 *  - Information about the played/drawn card
 *  - The active and next player
 *  - The top card on the discard pile
 *  - The chosen wild color (if applicable)
 *  - The full list of players
 *  - Whether a computer chose a wild color automatically
 *  - A draw counter used for stacking actions
 *
 * @author Magdi
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
     * Creates a new UNOEvent carrying all relevant state information for
     * the observers (view/controller).
     *
     * @param source        the source object firing the event (usually the model)
     * @param status        the current game status
     * @param playedCard    the card that was played (null if none)
     * @param lastDrawnCard the most recently drawn card (null if none)
     * @param currentPlayer the player whose turn it currently is
     * @param nextPlayer    the next player in turn order
     * @param topCard       the current top card on the discard pile
     * @param wildColor     the selected wild color (null unless a wild was played)
     * @param players       list of all players in the game
     * @param computerWild  true if the computer chose a wild color automatically
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
                    Boolean computerWild) {

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

    /**
     * Updates the active wild color after a wild card is played.
     *
     * @param wildColor the new chosen wild color
     */
    public void updateWildColor(CardColor wildColor) {
        this.wildColor = wildColor;
    }

    /**
     * Returns the current game status associated with the event.
     *
     * @return the game status
     */
    public Status getStatus() {
        return status;
    }


    /**
     * Returns the wild color chosen for this event (if applicable).
     *
     * @return the chosen CardColor or null if no wild was involved
     */
    public CardColor getWildColor() {
        return wildColor;
    }

    /**
     * Returns the card played during this event.
     *
     * @return the played card (may be null)
     */
    public Card getPlayedCard() {
        return playedCard;
    }

    /**
     * Returns the last card drawn by a player.
     *
     * @return the drawn card (may be null)
     */
    public Card getLastDrawnCard() {
        return lastDrawnCard;
    }

    /**
     * Returns the player whose turn this event relates to.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Returns the player who will take the next turn.
     *
     * @return the next player
     */
    public Player getNextPlayer() {
        return nextPlayer;
    }

    /**
     * Returns the top card on the discard pile when this event occurred.
     *
     * @return the top card
     */
    public Card getTopCard() {
        return topCard;
    }

    /**
     * Returns the accumulated draw counter used for stacking draw actions.
     *
     * @return the total draw count
     */
    public int getDrawCounter() {
        return drawCounter;
    }

    /**
     * Returns the list of players involved in this event.
     *
     * @return the list of all players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Updates the internal draw counter,
     * used for "Draw One", "Draw Five", and stacking wild draw effects.
     *
     * @param counter the new draw count
     */
    public void updateCardDrawnCounter(int counter) {
        drawCounter = counter;
    }

    /**
     * Indicates whether a computer player automatically selected the wild color.
     *
     * @return true if a computer chose the color, false otherwise
     */
    public boolean getComputerWild() {
        return computerWild;
    }

    /**
     * Returns a formatted string containing all event data.
     *
     * @return string representation of this event
     */
    @Override
    public String toString() {
        return "UNOEvent [status=" + status +
                ", playedCard=" + playedCard +
                ", lastDrawnCard=" + lastDrawnCard +
                ", currentPlayer=" + currentPlayer +
                ", nextPlayer=" + nextPlayer +
                ", topCard=" + topCard +
                ", wildColor=" + wildColor +
                ", players=" + players +
                ", drawCounter=" + drawCounter + "]";
    }
}
