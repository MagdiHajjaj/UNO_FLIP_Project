package game;
import java.util.ArrayList;
import java.util.List;

/**
 * public class UnoModel
 *
 * @author Edward Savin
 * @id 101239342
 * @version M2
 */

public class UNOModel {

    private ArrayList<Player> players;
    private List<IView> viewersList;
    private Player currentPlayer;
    private PileSystem pile;
    private CardColor wildColor;
    private Card playedCard;
    private boolean drawOne;
    private boolean wildDrawTwo;
    private boolean skip;
    private boolean exitGame;
    private Card lastDrawnCard;
    private int direction;
    private int indexOfCurrentPlayer;
    private int roundNumber;
    private static final int WINNING_SCORE = 500;

    private Status status;

    public Player getPreviousPlayer() {
        int prevIndex;
        if (direction > 0) {
            prevIndex = (indexOfCurrentPlayer == 0) ? players.size() - 1 : indexOfCurrentPlayer - 1;
        } else {
            prevIndex = (indexOfCurrentPlayer == players.size() - 1) ? 0 : indexOfCurrentPlayer + 1;
        }
        return players.get(prevIndex);
    }

    /**
     * enum class for status for MVC pattern
     */
    public enum Status {
        INITIALIZING,
        IN_GAME,
        WILD_CARD,
        ROUND_OVER,
        GAME_OVER,
        CARD_DRAWN,
        NEXT_PLAYER,
        PLAYING_VALID_CARD,
        PLAYING_INVALID_CARD,

    }

    /**
     * Constructor for uno model class
     * set status to INITIALIZING
     */
    public UNOModel() {
        this.players = new ArrayList<Player>();
        this.viewersList = new ArrayList<>();
        this.currentPlayer = null;
        this.lastDrawnCard = null;
        this.pile = new PileSystem();
        this.direction = 1;
        this.indexOfCurrentPlayer = 0;
        this.roundNumber = 1;
        this.status = Status.INITIALIZING;

    }


    public void addViewer(IView view) {
        this.viewersList.add(view);
    }
    public void removeViewer(IView view) {
        this.viewersList.remove(view);
    }
    public void notifyViewers() {
        for (IView viewer : this.viewersList) {
            viewer.update();
        }

    }

    /**
     * Starts a new game with a list of player names
     * set status to IN_GAME
     * @param numPlayers number of playerws
     */
    public void newGame(int numPlayers) {
//        if (playerNames == null || playerNames.size() < 2 || playerNames.size() > 4) {
//            throw new IllegalArgumentException("must have 2-4 players");
//        }
        players.clear();
        for(int i = 1; i <= numPlayers; i++) {

            players.add(new Player("Player " + i));
        }
        resetFields();
        currentPlayer = players.getFirst();
        status = Status.IN_GAME;
        notifyViewers();
    }

    /**
     * resetFields method re-used from M1 Game class
     */
    public void resetFields() {
        indexOfCurrentPlayer = 0;
        drawOne = false;
        wildDrawTwo = false;
        direction = 1;
        skip = false;
        wildColor = null;
        currentPlayer = null;
        pile = new PileSystem();
        exitGame = false;

        pile.addToDiscardPile(pile.drawCard());
        while(pile.peekTopDiscard().getColor() == CardColor.WILD){
            pile.addToDiscardPile(pile.drawCard());
        }

        for (Player player : players) {
            player.clearHand();
            this.generatePlayerHand(player);
        }
    }

    /**
     * method used to play a card

     * @return true if the card was able to play, otherwise false
     */
    public void playCard(Card playedCard){
        if (currentPlayer == null || playedCard == null) {
            return;
        }
        Card topCard = pile.peekTopDiscard();
        if (validatePlacedCard(playedCard, topCard, wildColor)){
            Card toBePlayed = currentPlayer.removeCardAt(playedCard);
            pile.addToDiscardPile(toBePlayed);
            this.playedCard = toBePlayed;
            processCard(toBePlayed);

            if (toBePlayed.getType() == CardType.WILD || toBePlayed.getType() == CardType.WILD_DRAW_TWO) {
                this.status = Status.WILD_CARD;
                notifyViewers();
                return;
            }

            if (toBePlayed.getType() == CardType.DRAW_ONE) {
                lastDrawnCard = pile.drawCard();
                getNextPlayer().addCard(lastDrawnCard);
                drawOne = false;
                this.status = Status.CARD_DRAWN;
                notifyViewers();
                       return;
            }

            if (currentPlayer.getHand().isEmpty()) {
                roundEnd();
                return;
            }

            this.status = Status.PLAYING_VALID_CARD;
            notifyViewers();
            return;
        }
        this.status = Status.PLAYING_INVALID_CARD;
        notifyViewers();
    }

    /**
     * method used to draw a card
     */
    public void drawCard(){
        if (currentPlayer != null){
            lastDrawnCard = pile.drawCard();
            currentPlayer.addCard(lastDrawnCard);
            status = Status.CARD_DRAWN;
            notifyViewers();
        }
    }
    public Card getLastDrawnCard(){
        return lastDrawnCard;
    }

    /**
     * method used to set color if wild card
     * @param color is the chosen wild card color to be set
     */
    public void setWildColor(CardColor color){
        this.wildColor = color;
        if (wildDrawTwo){
            getNextPlayer().addCard(pile.drawCard());
            getNextPlayer().addCard(pile.drawCard());
            wildDrawTwo = false;
        }
    }

    /**
     * method to start a new Round
     */
    public void startNewRound(){
        resetFields();
        roundNumber++;
        indexOfCurrentPlayer = 0;
        currentPlayer = players.get(indexOfCurrentPlayer);
        status = Status.IN_GAME;
        notifyViewers();
    }

    /**
     * method to reset game
     */
    public void resetGame(){
        for (Player player : players){
            player.setScore(0);
        }
        resetFields();
        roundNumber = 1;
        status = Status.INITIALIZING;
        notifyViewers();
    }

    /**
     * method to process card effects
     * @param card is the card being processed for effects
     */
    private void processCard(Card card){
        switch (card.getType()){
            case DRAW_ONE:
                drawOne = true;
                break;
            case REVERSE:
                //in 2-player games of Uno reverse acts as skip
                if (players.size() == 2){
                    skip = true;
                } else {
                    direction *= -1;
                }
                break;

            case SKIP:
                skip = true;
                break;
            case WILD:
                status = Status.WILD_CARD;
                break;

            case WILD_DRAW_TWO:
                wildDrawTwo = true;
                status = Status.WILD_CARD;
                break;
        }
    }

    /**
     * method to end current Round and check for winner
     */
    private void roundEnd(){
        int roundScore = calculateScore();
        currentPlayer.addScore(roundScore);

        if (checkWinner(currentPlayer)){
            status = Status.GAME_OVER;
        } else {
            status = Status.ROUND_OVER;
        }
        notifyViewers();
    }

    /**
     * method used to change turns
     * deals with special cases
     */
    public void nextPlayer(){
        advanceIndex();
        if (skip){
            advanceIndex();
            skip = false;
        }
        currentPlayer = players.get(indexOfCurrentPlayer);


        this.status = Status.NEXT_PLAYER;
        notifyViewers();
    }

    /**
     * method used to adjust index to next player
     */
    private void advanceIndex(){
        if (direction > 0) {
            indexOfCurrentPlayer = (indexOfCurrentPlayer == players.size() - 1) ? 0 : indexOfCurrentPlayer + 1;
        } else {
            indexOfCurrentPlayer = (indexOfCurrentPlayer == 0) ? players.size() - 1 : indexOfCurrentPlayer - 1;
        }

    }

    /**
     * calculateScore method used from M1 Game class
     * Calculates the score for a player by summing the point values of all cards in other players' hands.
     *
     * @return the total score for the player
     */
    public int calculateScore() {
        int total = 0;
        for (Player player: players) {
            if (currentPlayer == player) {
                continue;
            }
            for (Card card: player.getHand()){
                total += card.getPoint() ;
            }
        }
        return total;
    }

    /**
     * checkWinner method used from M1 Game class
     * @param current_player player to check
     * Checks if current player has reached the winning score threshold (>= 500).
     *
     * @return true if current player is a winner otherwise false.
     */
    public boolean checkWinner(Player current_player) {
        return current_player.getScore()  >= WINNING_SCORE;
    }

    /**
     * generatePlayerHand used from M1 Game class
     * Generates a hand of 7 cards for a player by drawing from the draw pile.
     *
     * @param player the player to generate a hand for
     */
    public void generatePlayerHand(Player player) {
        for (int i = 0; i < 7; i++) { // each player starts with 7 cards
            player.addCard(pile.drawCard()); // pile returns a card, player adds that card to its hand.
        }
    }

    /**
     * validatePlacedCard from M1 Game class
     * Validates a card placement from the current player's hand.
     * Checks if the card matches the top card of the discard pile,
     * or if it is a WILD or WILD_DRAW_TWO card that matches the specified color.
     *
     * @param placedCardFromHand the card being placed from the player's hand
     * @param topCardOfDiscardPile the top card of the discard pile
     * @param wildColor the color specified by the previous player using a WILD or WILD_DRAW_TWO card
     * @return true if the placement is valid; otherwise {@code false}
     */
    public boolean validatePlacedCard(Card placedCardFromHand, Card topCardOfDiscardPile, CardColor wildColor) {
        if (topCardOfDiscardPile.getType()==CardType.WILD || topCardOfDiscardPile.getType()==CardType.WILD_DRAW_TWO) { // if true, must check for the specifed color from previous player using wildColor
            return wildColor != null && (placedCardFromHand.getColor() == wildColor || placedCardFromHand.getColor() == CardColor.WILD);

        } else if (placedCardFromHand == topCardOfDiscardPile ||
                placedCardFromHand.getColor() == topCardOfDiscardPile.getColor() ||
                placedCardFromHand.getType() == topCardOfDiscardPile.getType() ||
                placedCardFromHand.getType() == CardType.WILD ||
                placedCardFromHand.getType() == CardType.WILD_DRAW_TWO
        ) {
            return true;
        }
        return false;
    }
    //GETTERS
    public Player getNextPlayer() {
        int temp;
        if (direction > 0) {
            temp=  (indexOfCurrentPlayer == players.size() - 1) ? 0 : indexOfCurrentPlayer + 1;
        } else {
            temp =  (indexOfCurrentPlayer == 0) ? players.size() - 1 : indexOfCurrentPlayer - 1;
        }
        return players.get(temp);
    }

    public int getDirection(){
        return direction;
    }
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    public List<Card> getCurrentPlayerHand() {
        return currentPlayer.getHand();
    }
    public ArrayList<Player> getPlayers(){
        return new ArrayList<>(players);
    }
    public PileSystem getPileSystem() {
        return pile;
    }
    public Card getTopCard() {
        return pile.peekTopDiscard();
    }
    public Card getPlayedCard() {
        return playedCard;
    }


    public CardColor getWildColor() {
        return wildColor;
    }
    public Status getStatus() {
        return status;
    }
    public int getRoundNumber() {
        return roundNumber;
    }
    public int getIndexOfCurrentPlayer() {
        return indexOfCurrentPlayer;
    }
    public boolean isGameOver() {
        return status == Status.GAME_OVER;
    }
    public boolean isRoundOver() {
        return status == Status.ROUND_OVER;
    }

}
