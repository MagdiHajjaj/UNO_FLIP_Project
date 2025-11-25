package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * UNOModel - Main model class for the UNO game.
 * Handles all game logic, player actions, and notifications to the view.
 *
 * @author Magdi Hajjaj
 * @id 101299841
 * @version M3
 */
public class UNOModel {
    private static final int WINNING_SCORE = 500;
    protected static final String[] playerOptionsDark = {"ORANGE", "PINK", "PURPLE", "TEAL"};
    protected static final String[] playerOptionsLight = {"RED", "YELLOW", "GREEN", "BLUE"};
    private static Random random;
    /* INSTANCE VARIABLES */
    private UNOEvent event;
    private ArrayList<Player> players; // contains list players and computers
    private List<IView> viewersList;
    private Player currentPlayer;
    private PileSystem pile;
    private CardColor wildColor;
    private Card playedCard;
    private Card lastDrawnCard;
    private boolean drawOne;
    private boolean wildDrawTwo;
    private boolean skip;
    private boolean skipEveryone;
    private boolean wildDrawColor;
    private boolean drawFive;
    private boolean exitGame;
    private boolean computerWild;

    private int direction;
    private int indexOfCurrentPlayer;
    private int roundNumber;
    private Status status;

    protected static boolean isDarkSide; // entire game state


    /**
     * Initializes a new UNOModel object.
     */
    public UNOModel() {
        this.players = new ArrayList<>();
        this.viewersList = new ArrayList<>();
        this.currentPlayer = null;
        this.lastDrawnCard = null;
        this.pile = new PileSystem();
        this.direction = 1;
        this.indexOfCurrentPlayer = 0;
        this.roundNumber = 1;
        this.status = Status.INITIALIZING;
        this.computerWild = false;
    }
    /**
     * Registers a view so it will be notified when the model updates.
     *
     * @param view the view to add
     */
    public void addViewer(IView view) {
        viewersList.add(view);
    }
    /**
     * Removes a previously registered view.
     *
     * @param view the view to remove
     */
    public void removeViewer(IView view) {
        viewersList.remove(view);
    }
    /**
     * Notifies all registered views that the model has changed.
     * Each view's update() method will be called.
     */
    public void notifyViewers() {
        event = new UNOEvent(
                this,
                status,
                playedCard,
                lastDrawnCard,
                currentPlayer,
                getNextPlayer(),
                pile.peekTopDiscard(),
                wildColor,
                players,
                computerWild
        );
        System.out.println(event);
        for (IView viewer : viewersList) {
            viewer.update(event);
        }
    }
    /**
     * Starts a new game with the given number of players.
     * Initializes the player list and prepares the first round.
     *
     * @param numPlayers number of players in the game
     */
    public void newGame(int numPlayers, int numComputers) {
        players.clear();
        for (int i = 1; i <= numPlayers; i++) {
            players.add(new Player("Player " + i));
        }
        for (int i = 1; i <= numComputers; i++) {
            players.add(new ComputerPlayer("Computer " + i));
        }
        resetFields();
        currentPlayer = players.getFirst();
        isDarkSide  = false;
        status = Status.IN_GAME;
        notifyViewers();
    }
    /**
     * Resets round-specific fields and reinitializes the draw and discard piles.
     * Also deals new hands to all players.
     */
    public void resetFields() {
        indexOfCurrentPlayer = 0;
        drawOne = false;
        drawFive = false;
        wildDrawColor = false;
        wildDrawTwo = false;
        direction = 1;
        skip = false;
        wildColor = null;
        currentPlayer = null;
        exitGame = false;
        pile = new PileSystem();

        // Initialize discard pile with non-wild card
        pile.addToDiscardPile(pile.drawCard());
        while (pile.peekTopDiscard().getColor() == CardColor.WILD) {
            pile.addToDiscardPile(pile.drawCard());
        }

        // Deal cards to players
        for (Player player : players) {
            player.clearHand();
            generatePlayerHand(player);
        }
    }
    /**
     * Attempts to play the given card from the current player's hand.
     * Validates the move, applies effects, updates game status, and notifies views.
     *
     * @param playedCard the card the player is trying to play
     */
    public void playCard(Card playedCard) {
        if (currentPlayer == null || playedCard == null) return;

        Card topCard = pile.peekTopDiscard();

        if (!validatePlacedCard(playedCard, topCard, wildColor)) {

            System.out.println("DID THIS RUN");
            status = Status.PLAYING_INVALID_CARD;
            notifyViewers();
            return;
        }

        // Play card
        Card toBePlayed = currentPlayer.removeCardAt(playedCard);
        pile.addToDiscardPile(toBePlayed);
        this.playedCard = toBePlayed;
        processCard(toBePlayed);

        if (currentPlayer instanceof ComputerPlayer && (toBePlayed.getType() == CardType.WILD || toBePlayed.getType() == CardType.WILD_DRAW_TWO || toBePlayed.getType() == CardType.WILD_DRAW_COLOR)) {
            currentPlayer.removeCardAt(toBePlayed);
            computerWild = true;
            if (isDarkSide) {
                wildColor = CardColor.valueOf(playerOptionsDark[random.nextInt(playerOptionsDark.length)]);

            } else {
                wildColor = CardColor.valueOf(playerOptionsLight[random.nextInt(playerOptionsLight.length)]);
            }
            notifyViewers();
        }
        if ((toBePlayed.getType() == CardType.SKIP_EVERYONE )
                && !currentPlayer.getHand().isEmpty()) {
            status = Status.SKIP_EVERYONE;
            notifyViewers();
            return;
        }
        // Handle special card logic
        if ((toBePlayed.getType() == CardType.FLIP )
                && !currentPlayer.getHand().isEmpty()) {
            status = Status.FLIP_CARD;
            isDarkSide = !isDarkSide;
            notifyViewers();
            return;
        }

        if ((toBePlayed.getType() == CardType.WILD || toBePlayed.getType() == CardType.WILD_DRAW_TWO || toBePlayed.getType() == CardType.WILD_DRAW_COLOR) && !currentPlayer.getHand().isEmpty()) {
            status = Status.WILD_CARD;
            notifyViewers();
            return;
        }

        if (toBePlayed.getType() == CardType.DRAW_ONE && !currentPlayer.getHand().isEmpty()) {
            lastDrawnCard = pile.drawCard();
            getNextPlayer().addCard(lastDrawnCard);
            drawOne = false;
            status = Status.CARD_DRAWN;
            notifyViewers();
            return;
        }
        if (toBePlayed.getType() == CardType.DRAW_FIVE && !currentPlayer.getHand().isEmpty()) {

            getNextPlayer().addCard(pile.drawCard());
            getNextPlayer().addCard(pile.drawCard());
            getNextPlayer().addCard(pile.drawCard());
            getNextPlayer().addCard(pile.drawCard());
            getNextPlayer().addCard(pile.drawCard());
            drawFive = false;
            status = Status.DRAW_FIVE;
            notifyViewers();
            return;
        }

        if (currentPlayer.getHand().isEmpty()) {
            roundEnd();
            return;
        }

        status = Status.PLAYING_VALID_CARD;
        notifyViewers();
    }
    /**
     * Draws one card from the pile and adds it to the current player's hand.
     * Updates the game status and notifies views.
     */
    public void drawCard() {
        if (currentPlayer != null) {
            lastDrawnCard = pile.drawCard();
            currentPlayer.addCard(lastDrawnCard);
            status = Status.CARD_DRAWN;
            notifyViewers();
        }
    }
    /**
     * Sets the color selected after playing a wild card.
     * If the card was WILD DRAW TWO, applies the penalty cards to the next player.
     *
     * @param color the selected wild color
     */
    public void setWildColor(CardColor color) {
        this.wildColor = color;
        event.updateWildColor(wildColor);
        if (wildDrawTwo) {
            getNextPlayer().addCard(pile.drawCard());
            getNextPlayer().addCard(pile.drawCard());
            wildDrawTwo = false;
        }
        if (wildDrawColor) {
            getNextPlayer().addCard(pile.drawCard());
            int counter = 1;
            Card lastDrawn = getNextPlayer().getHand().getLast();
            while(lastDrawn.getColor() != wildColor && lastDrawn.getColor() != CardColor.WILD) {
                getNextPlayer().addCard(pile.drawCard());
                lastDrawn = getNextPlayer().getHand().getLast();
                counter++;
            }
            event.updateCardDrawnCounter(counter);
            wildDrawColor = false;
        }
    }
    /**
     * Begins a new round while keeping players' scores.
     * Resets hands, piles, and turn order, then updates the view.
     */
    public void startNewRound() {
        resetFields();
        roundNumber++;
        indexOfCurrentPlayer = 0;
        currentPlayer = players.get(indexOfCurrentPlayer);
        status = Status.IN_GAME;
        playedCard = pile.peekTopDiscard();
        notifyViewers();
    }
    /**
     * Resets the entire game, including all player scores and round count.
     * Starts again from round one.
     */
    public void resetGame() {
        for (Player player : players) player.setScore(0);
        resetFields();
        roundNumber = 1;
        currentPlayer = players.get(indexOfCurrentPlayer);
        status = Status.IN_GAME;
        playedCard = pile.peekTopDiscard();
        notifyViewers();
    }
    /**
     * Handles the end of a round by awarding points to the winning player
     * and determining whether the game is over.
     */
    private void roundEnd() {
        int roundScore = calculateScore();
        currentPlayer.addScore(roundScore);
        status = checkWinner(currentPlayer) ? Status.GAME_OVER : Status.ROUND_OVER;
        notifyViewers();
    }
    /**
     * Applies the effects of the given card (skip, reverse, draw, wild, etc.).
     *
     * @param card the card just played
     */
    private void processCard(Card card) {
        switch (card.getType()) {
            case DRAW_ONE -> drawOne = true;
            case REVERSE -> {
                if (players.size() == 2) skip = true;
                else direction *= -1;
            }
            case SKIP -> skip = true;
            case SKIP_EVERYONE -> skipEveryone = true;
            case WILD -> status = Status.WILD_CARD;
            case WILD_DRAW_TWO -> {
                wildDrawTwo = true;
                status = Status.WILD_CARD;
            }
            case WILD_DRAW_COLOR -> {
                wildDrawColor = true;

            }
            case DRAW_FIVE -> {
                    drawFive = true;
                    skip = true;
            }
        }
    }
    /**
     * Moves the turn to the next player, accounting for skip and reverse effects.
     * Updates the game status and notifies views.
     */
    public void nextPlayer() {
        advanceIndex();
        if (skip) {
            advanceIndex();
            skip = false;
        }
        if(skipEveryone){
            for(int i = 0 ; i < players.size() - 1; i++){
                advanceIndex();
            }
            skipEveryone = false;
        }
        currentPlayer = players.get(indexOfCurrentPlayer);
        //check if ai
        if (currentPlayer instanceof ComputerPlayer) {
            status = Status.NEXT_PLAYER;
            playCard(computerCardChoice());
            return;
        }
        status = Status.NEXT_PLAYER;
        notifyViewers();
    }
    /**
     * Updates indexOfCurrentPlayer based on the current turn direction.
     */
    private void advanceIndex() {
        if (direction > 0)
            indexOfCurrentPlayer = (indexOfCurrentPlayer == players.size() - 1) ? 0 : indexOfCurrentPlayer + 1;
        else
            indexOfCurrentPlayer = (indexOfCurrentPlayer == 0) ? players.size() - 1 : indexOfCurrentPlayer - 1;
    }
    /**
     * Checks whether the played card is a legal move based on the top discard and wild color.
     *
     * @param handCard the card being played
     * @param topCard the current card on top of the discard pile
     * @param wildColor the active wild color if one is set
     * @return true if the move is valid, false otherwise
     */
    public boolean validatePlacedCard(Card handCard, Card topCard, CardColor wildColor) {

        if (topCard.getType() == CardType.WILD || topCard.getType() == CardType.WILD_DRAW_TWO || topCard.getType() == CardType.WILD_DRAW_COLOR) {
            return wildColor != null && (handCard.getColor() == wildColor || handCard.getColor() == CardColor.WILD);
        }
        return handCard == topCard ||
                handCard.getColor() == topCard.getColor() ||
                handCard.getType() == topCard.getType() ||
                handCard.getType() == CardType.WILD ||
                handCard.getType() == CardType.WILD_DRAW_TWO ||
                handCard.getType() == CardType.WILD_DRAW_COLOR;
    }
    /**
     * Calculates the total points earned for the round by the current player.
     *
     * @return total points from other players' remaining cards
     */
    public int calculateScore() {
        int total = 0;
        for (Player player : players) {
            if (currentPlayer == player) continue;
            for (Card card : player.getHand()) total += card.getPoint();
        }
        return total;
    }
    /**
     * Checks if the given player has reached the winning score threshold.
     *
     * @param p the player to check
     * @return true if the player has won the game
     */
    public boolean checkWinner(Player p) {
        return p.getScore() >= WINNING_SCORE;
    }
    /**
     * Deals the standard starting hand of cards to the given player.
     *
     * @param p the player to receive cards
     */
    public void generatePlayerHand(Player p) {
        for (int i = 0; i < 7; i++) {
            p.addCard(pile.drawCard());
        }
    }
    /* GETTERS */
    /**
     * Returns the current player.
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    /**
     * Returns the next player in the turn order.
     *
     * @return the next player
     */
    public Player getNextPlayer() {
        int next = (direction > 0)
                ? (indexOfCurrentPlayer == players.size() - 1 ? 0 : indexOfCurrentPlayer + 1)
                : (indexOfCurrentPlayer == 0 ? players.size() - 1 : indexOfCurrentPlayer - 1);
        return players.get(next);
    }
    /**
     * Returns the previous player in the turn order.
     *
     * @return the previous player
     */
    public Player getPreviousPlayer() {
        int prev = (direction > 0)
                ? (indexOfCurrentPlayer == 0 ? players.size() - 1 : indexOfCurrentPlayer - 1)
                : (indexOfCurrentPlayer == players.size() - 1 ? 0 : indexOfCurrentPlayer + 1);
        return players.get(prev);
    }
    /**
     * @return the current player's hand of cards
     */
    public List<Card> getCurrentPlayerHand() {
        return currentPlayer.getHand();
    }

    /**
     * @return a copy of the list of players in the game
     */
    public ArrayList<Player> getPlayers() {
        return new ArrayList<>(players);
    }

    /**
     * @return the pile system that manages the draw and discard piles
     */
    public PileSystem getPileSystem() {
        return pile;
    }

    /**
     * @return the card currently on top of the discard pile
     */
    public Card getTopCard() {
        return pile.peekTopDiscard();
    }

    /**
     * @return the most recently played card
     */
    public Card getPlayedCard() {
        return playedCard;
    }

    /**
     * @return the last card that was drawn
     */
    public Card getLastDrawnCard() {
        return lastDrawnCard;
    }

    /**
     * @return the currently selected wild color, if any
     */
    public CardColor getWildColor() {
        return wildColor;
    }

    /**
     * @return the current state of the game
     */
    public Status getStatus() {
        return status;
    }

    /**
     * @return the current direction of play (1 for forward, -1 for reverse)
     */
    public int getDirection() {
        return direction;
    }

    /**
     * @return the current round number
     */
    public int getRoundNumber() {
        return roundNumber;
    }

    /**
     * @return the index of the player whose turn it is
     */
    public int getIndexOfCurrentPlayer() {
        return indexOfCurrentPlayer;
    }

    /**
     * @return true if a player has reached the winning score and the game has ended
     */
    public boolean isGameOver() {
        return status == Status.GAME_OVER;
    }

    /**
     * @return true if the current round has ended
     */
    public boolean isRoundOver() {
        return status == Status.ROUND_OVER;
    }

    public static boolean getIsDarkSide() {
        return isDarkSide;
    }

    public Card computerCardChoice() {
        List<Card> listOfplayableCard = new ArrayList<>();
        for (Card card : currentPlayer.getHand()) {
            if (validatePlacedCard(card, getTopCard(), getWildColor())) {
                listOfplayableCard.add(card);
            }
        }
        if (listOfplayableCard.isEmpty()) {
            this.drawCard();
            return null;

        }
        return listOfplayableCard.get(0);
    }

}
