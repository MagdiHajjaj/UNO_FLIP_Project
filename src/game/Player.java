package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Represents a single player.
 *
 * @author Hamza Mohammed Salih
 * @id 101311021
 * @version M1
 */
public class Player {

    //Display name shown
    private final String name;

    //Cards currently in players hand
    private List<Card> hand = new ArrayList<>();

    //Cumulative score
    private int totalScore = 0;

    /**
     * Constructs a new player.
     * If the provided name is null or becomes empty after trimming,
     * the name defaults to "Player".
     *
     * @param name display name for this player; may be null
     */
    public Player(String name) {

        if (name == null || name.isBlank()) {
            this.name = "Player";
        } else {
            this.name = name.trim();
        }
    }

    //Getters
    /**
     * Returns the player's display name.
     *
     * @return non-null display name
     */
    public String getName() {
        return name;
    }


    /**
     * Returns a read-only view of the player's current hand.
     * Use addCard and removeCardAt to modify the hand.
     *
     * @return unmodifiable list view of the hand
     */
    public List<Card> getHand() {
        return Collections.unmodifiableList(hand);
    }

    /**
     * Returns the current number of cards in the player's hand.
     *
     * @return number of cards
     */
    public int handSize() { return hand.size(); }

    /**
     * Returns the player's cumulative score.
     *
     * @return total score
     */
    public int getScore() { return totalScore; }

    /**
     * Adds non-negative points to the player's cumulative score.
     * Negative values are ignored to keep this method strictly additive.
     *
     * @param  points to add; ignored if points is less than or equal to zero
     */
    public void addScore(int points) {
        if (points > 0)
            totalScore += points;
    }

    /**
     * Removes all cards from the player's hand.
     * Typically called when starting a new round.
     */
    public void clearHand() { hand.clear(); }

    /**
     * Adds a card to the player's hand, for example when drawing.
     * If the card is null, nothing happens.
     *
     * @param c card to add; may be null
     */
    public void addCard(Card c) {
        if (c != null)
            hand.add(c);
    }
    /**
     * Sets the player's cumulative score.
     *
     * @param score new total score
     */
    public void setScore(int score) {
        totalScore = score;
    }

    /**
     * Removes and returns the card at the given index, for example when playing.
     * Callers should validate the index and the legality of the move elsewhere.
     *
     * @param index zero-based position of the card in the hand
     * @return the removed card
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public Card removeCardAt(int index) {
        return hand.remove(index);
    }

    /**
     * Returns a concise textual summary for logging or debugging.
     * Includes name, hand size, and score.
     *
     * @return summary string
     */
    @Override
    public String toString() {
        return "Player{" + name + ", hand=" + hand.size() + ", score=" + totalScore + "}";
    }
}
