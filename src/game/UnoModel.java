package game;
import java.util.ArrayList;
import java.util.List;

/**
 * UNOModel - Main model class for the UNO game.
 * Handles all game logic, player actions, and notifications to the view.
 *
 * @author Edward Savin, Magdi Hajjaj
 * @id 101239342, 101299841
 * @version M2
 */
public class UNOModel {

    /* ENUMS AND CONSTANTS */
    public enum Status {
        INITIALIZING,
        IN_GAME,
        WILD_CARD,
        ROUND_OVER,
        GAME_OVER,
        CARD_DRAWN,
        NEXT_PLAYER,
        PLAYING_VALID_CARD,
        PLAYING_INVALID_CARD
    }

    private static final int WINNING_SCORE = 500;

    /* INSTANCE VARIABLES */
    private ArrayList<Player> players;
    private List<IView> viewersList;
    private Player currentPlayer;
    private PileSystem pile;
    private CardColor wildColor;
    private Card playedCard;
    private Card lastDrawnCard;

    private boolean drawOne;
    private boolean wildDrawTwo;
    private boolean skip;
    private boolean exitGame;

    private int direction;
    private int indexOfCurrentPlayer;
    private int roundNumber;

    private Status status;

    /* CONSTRUCTOR */
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
    }

    /* OBSERVER MANAGEMENT */
    public void addViewer(IView view) { viewersList.add(view); }
    public void removeViewer(IView view) { viewersList.remove(view); }
    public void notifyViewers() {
        for (IView viewer : viewersList) {
            viewer.update();
        }
    }

    /* GAME INITIALIZATION METHODS */

    /** Starts a new game with the given number of players. */
    public void newGame(int numPlayers) {
        players.clear();
        for (int i = 1; i <= numPlayers; i++) {
            players.add(new Player("Player " + i));
        }
        resetFields();
        currentPlayer = players.getFirst();
        status = Status.IN_GAME;
        notifyViewers();
    }

    /** Resets fields and prepares for a new round or game. */
    public void resetFields() {
        indexOfCurrentPlayer = 0;
        drawOne = false;
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

    /* PLAYER ACTION METHODS */

    /** Handles playing a card from the current player's hand. */
    public void playCard(Card playedCard) {
        if (currentPlayer == null || playedCard == null) return;

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

        // Handle special card logic
        if ((toBePlayed.getType() == CardType.WILD || toBePlayed.getType() == CardType.WILD_DRAW_TWO)
                && !currentPlayer.getHand().isEmpty()) {
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

        if (currentPlayer.getHand().isEmpty()) {
            roundEnd();
            return;
        }

        status = Status.PLAYING_VALID_CARD;
        notifyViewers();
    }

    /** Handles drawing a card. */
    public void drawCard() {
        if (currentPlayer != null) {
            lastDrawnCard = pile.drawCard();
            currentPlayer.addCard(lastDrawnCard);
            status = Status.CARD_DRAWN;
            notifyViewers();
        }
    }

    /** Called when a wild color is selected. */
    public void setWildColor(CardColor color) {
        this.wildColor = color;
        if (wildDrawTwo) {
            getNextPlayer().addCard(pile.drawCard());
            getNextPlayer().addCard(pile.drawCard());
            wildDrawTwo = false;
        }
    }

    /* ROUND AND GAME MANAGEMENT */

    public void startNewRound() {
        resetFields();
        roundNumber++;
        indexOfCurrentPlayer = 0;
        currentPlayer = players.get(indexOfCurrentPlayer);
        status = Status.IN_GAME;
        playedCard = pile.peekTopDiscard();
        notifyViewers();
    }

    public void resetGame() {
        for (Player player : players) player.setScore(0);
        resetFields();
        roundNumber = 1;
        currentPlayer = players.get(indexOfCurrentPlayer);
        status = Status.IN_GAME;
        playedCard = pile.peekTopDiscard();
        notifyViewers();
    }

    private void roundEnd() {
        int roundScore = calculateScore();
        currentPlayer.addScore(roundScore);
        status = checkWinner(currentPlayer) ? Status.GAME_OVER : Status.ROUND_OVER;
        notifyViewers();
    }

    /* GAME LOGIC HELPERS */

    private void processCard(Card card) {
        switch (card.getType()) {
            case DRAW_ONE -> drawOne = true;
            case REVERSE -> {
                if (players.size() == 2) skip = true;
                else direction *= -1;
            }
            case SKIP -> skip = true;
            case WILD -> status = Status.WILD_CARD;
            case WILD_DRAW_TWO -> {
                wildDrawTwo = true;
                status = Status.WILD_CARD;
            }
        }
    }

    public void nextPlayer() {
        advanceIndex();
        if (skip) {
            advanceIndex();
            skip = false;
        }
        currentPlayer = players.get(indexOfCurrentPlayer);
        status = Status.NEXT_PLAYER;
        notifyViewers();
    }

    private void advanceIndex() {
        if (direction > 0)
            indexOfCurrentPlayer = (indexOfCurrentPlayer == players.size() - 1) ? 0 : indexOfCurrentPlayer + 1;
        else
            indexOfCurrentPlayer = (indexOfCurrentPlayer == 0) ? players.size() - 1 : indexOfCurrentPlayer - 1;
    }

    public boolean validatePlacedCard(Card handCard, Card topCard, CardColor wildColor) {
        if (topCard.getType() == CardType.WILD || topCard.getType() == CardType.WILD_DRAW_TWO) {
            return wildColor != null && (handCard.getColor() == wildColor || handCard.getColor() == CardColor.WILD);
        }
        return handCard == topCard ||
                handCard.getColor() == topCard.getColor() ||
                handCard.getType() == topCard.getType() ||
                handCard.getType() == CardType.WILD ||
                handCard.getType() == CardType.WILD_DRAW_TWO;
    }


    public int calculateScore() {
        int total = 0;
        for (Player player : players) {
            if (currentPlayer == player) continue;
            for (Card card : player.getHand()) total += card.getPoint();
        }
        return total;
    }

    public boolean checkWinner(Player p) { return p.getScore() >= WINNING_SCORE; }

    public void generatePlayerHand(Player p) {
        for (int i = 0; i < 7; i++) p.addCard(pile.drawCard());
    }

    /* GETTERS */
    public Player getCurrentPlayer() { return currentPlayer; }
    public Player getNextPlayer() {
        int next = (direction > 0)
                ? (indexOfCurrentPlayer == players.size() - 1 ? 0 : indexOfCurrentPlayer + 1)
                : (indexOfCurrentPlayer == 0 ? players.size() - 1 : indexOfCurrentPlayer - 1);
        return players.get(next);
    }
    public Player getPreviousPlayer() {
        int prev = (direction > 0)
                ? (indexOfCurrentPlayer == 0 ? players.size() - 1 : indexOfCurrentPlayer - 1)
                : (indexOfCurrentPlayer == players.size() - 1 ? 0 : indexOfCurrentPlayer + 1);
        return players.get(prev);
    }
    public List<Card> getCurrentPlayerHand() { return currentPlayer.getHand(); }
    public ArrayList<Player> getPlayers() { return new ArrayList<>(players); }
    public PileSystem getPileSystem() { return pile; }
    public Card getTopCard() { return pile.peekTopDiscard(); }
    public Card getPlayedCard() { return playedCard; }
    public Card getLastDrawnCard() { return lastDrawnCard; }
    public CardColor getWildColor() { return wildColor; }
    public Status getStatus() { return status; }
    public int getDirection() { return direction; }
    public int getRoundNumber() { return roundNumber; }
    public int getIndexOfCurrentPlayer() { return indexOfCurrentPlayer; }
    public boolean isGameOver() { return status == Status.GAME_OVER; }
    public boolean isRoundOver() { return status == Status.ROUND_OVER; }
}
