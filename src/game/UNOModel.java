package game;

import javax.swing.*;
import java.io.*;
import java.util.*;

/**
 * UNOModel - Main model class for the UNO game.
 * Handles all game logic, player actions, and notifications to the view.
 *
 * @author Magdi Hajjaj
 * @id 101299841
 * @version M5
 */
public class UNOModel implements Cloneable, Serializable{
    private static final long serialVersionUID = 1L;
    private static final int WINNING_SCORE = 500;
    protected static final String[] playerOptionsDark = {"ORANGE", "PINK", "PURPLE", "TEAL"};
    protected static final String[] playerOptionsLight = {"RED", "YELLOW", "GREEN", "BLUE"};
    private static Random random;

    /* INSTANCE VARIABLES */
    private UNOEvent event;
    private ArrayList<Player> players; // contains list players and computers
    private transient List<IView> viewersList;
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
    private Side side;
    private GameHistoryManager gameHistoryManager;



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
        this.random = new Random();
        gameHistoryManager = new GameHistoryManager();
    }

    public Side getSide() {
        return side;
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
        for (IView viewer : viewersList) {
            viewer.update(this.event);
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
        side = Side.LIGHT;

        status = Status.IN_GAME;
        saveToUndoStack();
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
        if(side == Side.DARK) {
            side = Side.LIGHT;
            pile.flip();
        }
        wildColor = null;
        exitGame = false;
        pile = new PileSystem();
        gameHistoryManager.resetUndoRedoHistory();

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

        saveToUndoStack();
        Card topCard = pile.peekTopDiscard();
        if (!validatePlacedCard(playedCard, topCard, wildColor)) {

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
            if (side == Side.DARK) {
                setWildColor(CardColor.valueOf(playerOptionsDark[random.nextInt(playerOptionsDark.length)]));

            } else {
                setWildColor(CardColor.valueOf(playerOptionsLight[random.nextInt(playerOptionsLight.length)]));
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
        if (toBePlayed.getType() == CardType.FLIP && !currentPlayer.getHand().isEmpty()) {
            for (Player player : players) {
                player.flipHand();
            }
            pile.flip();
            side = side == Side.LIGHT ? Side.DARK : Side.LIGHT;
            status = Status.FLIP_CARD;

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
            saveToUndoStack();
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
        saveToUndoStack();

    }
    /**
     * Resets the entire game, including all player scores and round count.
     * Starts again from round one.
     */
    public void resetGame() {
        for (Player player : players) player.setScore(0);
        resetFields();
        roundNumber = 1;
        indexOfCurrentPlayer = 0;
        currentPlayer = players.get(indexOfCurrentPlayer);
        status = Status.IN_GAME;
        playedCard = null;
        notifyViewers();
    }
    /**
     * Handles the end of a round by awarding points to the winning player
     * and determining whether the game is over.
     */
    private void roundEnd() {
        int roundScore = calculateScore();
        currentPlayer.addScore(roundScore);

        if (checkWinner(currentPlayer)) {
            status = Status.GAME_OVER;
            notifyViewers();   // Final game over event
            return;
        }

        // Round is over but game still continuing
        status = Status.ROUND_OVER;
        notifyViewers();


        resetFields();

        status = Status.IN_GAME;
        playedCard = null;
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
        saveToUndoStack();
        computerWild = false;
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
            notifyViewers();
            playCard(computerCardChoice());
            gameHistoryManager.removeUndo();
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
    /**
     * Creates a deep copy of this model
     */
    @Override
    public UNOModel clone() {
        try {
            UNOModel cloned = (UNOModel) super.clone();

            // Deep copy players
            cloned.players = new ArrayList<>();
            for (Player p : this.players) {
                cloned.players.add(p.clone());
            }

            // Deep copy pile system
            cloned.pile = this.pile.clone();

            // Clone cards if they exist
            cloned.playedCard = this.playedCard != null ? this.playedCard.clone() : null;
            cloned.lastDrawnCard = this.lastDrawnCard != null ? this.lastDrawnCard.clone() : null;

            // Deep copy viewers list
            cloned.viewersList = new ArrayList<>(this.viewersList);

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Clone not supported", e);
        }
    }
    /**
     * Restores this model's state from another model
     */
    public void setState(UNOModel other) {
        this.players = new ArrayList<>();
        for (Player p : other.players) {
            this.players.add(p.clone());
        }
        this.currentPlayer = this.players.get(other.indexOfCurrentPlayer); // preserve current player
        this.pile = other.pile.clone();
        this.wildColor = other.wildColor;
        this.playedCard = other.playedCard != null ? other.playedCard.clone() : null;
        this.lastDrawnCard = other.lastDrawnCard != null ? other.lastDrawnCard.clone() : null;
        this.drawOne = other.drawOne;
        this.wildDrawTwo = other.wildDrawTwo;
        this.skip = other.skip;
        this.skipEveryone = other.skipEveryone;
        this.wildDrawColor = other.wildDrawColor;
        this.drawFive = other.drawFive;
        this.direction = other.direction;
        this.indexOfCurrentPlayer = other.indexOfCurrentPlayer;
        this.status = other.status;
        this.side = other.side;
        this.computerWild = other.computerWild;
    }

    /**
     * Saves the current state for undo functionality
     */
    public void saveToUndoStack() {
        gameHistoryManager.saveState(this);
    }

    /**
     * Undoes the last action
     */
    public void undo() {
        UNOModel previousState = gameHistoryManager.undo(this);
        if (previousState != null) {
            if (previousState.playedCard == null && previousState.lastDrawnCard == null) {
                previousState.status = Status.EDGE_CASE_UNDO;
            }
            if (previousState.status == Status.WILD_CARD && previousState.wildColor != null) {

                previousState.status = Status.PLAYING_VALID_CARD;
            }
            this.setState(previousState);
            notifyViewers();
        }
    }

    /**
     * Redoes the last undone action
     */
    public void redo() {
        UNOModel nextState = gameHistoryManager.redo(this);
        if (nextState != null) {
            this.setState(nextState);
            notifyViewers();
        }
    }

    public void saveGame(String file) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(this);

        }
    }

    public void loadGame(String file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            UNOModel loadModel = (UNOModel) in.readObject();
            this.players = loadModel.players;
            this.currentPlayer = loadModel.currentPlayer;
            this.pile = loadModel.pile;
            this.wildColor = loadModel.wildColor;
            this.playedCard = loadModel.playedCard;
            this.lastDrawnCard = loadModel.lastDrawnCard;
            this.roundNumber = loadModel.roundNumber;
            this.drawOne =  loadModel.drawOne;
            this.wildDrawTwo = loadModel.wildDrawTwo;
            this.skip = loadModel.skip;
            this.skipEveryone = loadModel.skipEveryone;
            this.wildDrawColor = loadModel.wildDrawColor;
            this.drawFive = loadModel.drawFive;
            this.exitGame = loadModel.exitGame;
            this.computerWild = loadModel.computerWild;
            this.direction = loadModel.direction;
            this.indexOfCurrentPlayer = loadModel.indexOfCurrentPlayer;
            this.side = loadModel.side;
            this.status = Status.LOAD;
            this.gameHistoryManager = loadModel.gameHistoryManager;
            if (this.viewersList == null) {
                this.viewersList = new ArrayList<>();
            }
            notifyViewers();
        }
    }
    /**
     * Chooses the next card for the computer player to play.
     *
     * Checks the computer player's hand for valid cards to play.
     * If no valid card is found, the computer draws a card and returns null.
     *
     * @return the first valid card the computer can play, or null if none is playable
     */
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


    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        UNOModel other = (UNOModel) obj;

        return indexOfCurrentPlayer == other.indexOfCurrentPlayer
                && roundNumber == other.roundNumber
                && direction == other.direction
                && drawOne == other.drawOne
                && drawFive == other.drawFive
                && wildDrawTwo == other.wildDrawTwo
                && wildDrawColor == other.wildDrawColor
                && skip == other.skip
                && skipEveryone == other.skipEveryone
                && computerWild == other.computerWild
                && Objects.equals(players, other.players)
                && Objects.equals(playedCard, other.playedCard)
                && Objects.equals(lastDrawnCard, other.lastDrawnCard)
                && wildColor == other.wildColor
                && Objects.equals(pile, other.pile);
    }



}
