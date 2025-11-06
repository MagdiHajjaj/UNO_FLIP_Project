package game;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Constructs a new UNO Flip game instance.
 * Initializes piles, scanner, player list and round control flags,
 * then enters the main menu/setup loop to start or continue play.
 *
 * @author Mohammed Osman, Magdi Hajjaj
 * @id 101311022, 101299841
 * @version M1
 */
public class Game {
    private Scanner scanner;
    private ArrayList<Player> players;
    private Player currentPlayer;
    private PileSystem pile;
    private Card wildColor;
    private boolean drawOne;
    private boolean wildDrawTwo;
    private boolean skip;
    private boolean exitGame;
    private boolean isWinner;
    private int direction;
    private int indexOfCurrentPlayer;
    private int roundNumber;
    private static final int WINNING_SCORE = 500;

    public Game() {
        pile = new PileSystem();
        scanner = new Scanner(System.in);
        players = new ArrayList<Player>();
        drawOne = false;
        wildDrawTwo = false;
        currentPlayer = null;
        direction = 1;
        wildColor = null;
        skip = false;
        indexOfCurrentPlayer = 0;
        exitGame = false;
        roundNumber = 1;

        // adds the first card to the discard pile
        pile.addToDiscardPile(pile.drawCard());
        while(pile.peekTopDiscard().getColor() == CardColor.WILD){
            pile.addToDiscardPile(pile.drawCard());
        }

        while (!exitGame) {
            welcomeMenu();
            System.out.println("Enter number of players (2-4):");
            int numberOfPlayers = parseUserIntInput();
            currentPlayer = createPlayers(numberOfPlayers);

            while (indexOfCurrentPlayer < players.size()){
                if (checkIfAnyHandIsEmpty()) {
                    currentPlayer.addScore(calculateScore());
                    isWinner = checkWinner(currentPlayer);
                    if (isWinner == true) {
                        System.out.println(currentPlayer.getName() + " has 0 cards left! they win this round.");
                        System.out.println(currentPlayer.getName() + " has earned " + currentPlayer.getScore() + " points.");
                        System.out.println("FINAL SCORES:");
                        displayScores();
                        System.out.print("There is a winner! "+ currentPlayer.getName() + " is the winner of the game. ");
                        restartGame();
                        break;
                    } else {
                        System.out.println(currentPlayer.getName() + " has 0 cards left! they win this round.");
                        System.out.println(currentPlayer.getName() + " has earned " + currentPlayer.getScore() + " points.");
                        System.out.println("CURRENT SCORES:");
                        displayScores();
                        newRound();
                    }
                }
                if (skip) {
                    System.out.println(players.get(indexOfCurrentPlayer).getName() + "'s turn is skipped");
                    if (direction > 0) {
                        indexOfCurrentPlayer = (indexOfCurrentPlayer == players.size() - 1) ? 0 : indexOfCurrentPlayer + 1;
                    } else {
                        indexOfCurrentPlayer = (indexOfCurrentPlayer == 0) ? players.size() - 1 : indexOfCurrentPlayer - 1;
                    }
                    skip = false;
                    continue;
                }
                // Show current player's turn
                displayTurn(players.get(indexOfCurrentPlayer));
                indexOfCurrentPlayer = ((indexOfCurrentPlayer + direction) % players.size() + players.size()) % players.size();

            }

        }
    }

    /**
     * Displays the welcome message and introductory menu header
     * for the UNO Flip game.
     * This method prints a formatted welcome message to the console,
     * including the game title and a separator line. It does not
     * take any input or return any value.
     */
    public void welcomeMenu() {
        System.out.println("Welcome to UNO Flip!");
        System.out.println("------------------------------------");
    }

    /**
     * Displays the current player's turn, including any special card effects.
     * If the player has a DRAW_ONE card, they must draw one card.
     * If the player has a WILD_DRAW_TWO card, they must draw two cards.
     * Otherwise, the player can play any valid card from their hand.
     *
     * @param currentPlayer the player whose turn it is
     */
    public void displayTurn(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        System.out.println("--------"+currentPlayer.getName() + "'s turn"+"----------");
        System.out.println("Top card: " + pile.peekTopDiscard() + "\n");
        if (drawOne) {
            System.out.println(currentPlayer.getName()+" has to draw one card due to the DRAW_ONE card.");
            Card card1 = pile.drawCard();
            currentPlayer.addCard(card1);
            System.out.println(currentPlayer.getName()+" drew a " + card1);
            drawOne = false;
        } else if (wildDrawTwo) {
            System.out.println(currentPlayer.getName()+" has to draw two cards due to the DRAW_TWO card.");
            Card card1 = pile.drawCard();
            Card card2 = pile.drawCard();
            currentPlayer.addCard(card1);
            currentPlayer.addCard(card2);
            System.out.println(currentPlayer.getName()+" drew a " + card1 + " and a " + card2);
            wildDrawTwo = false;
        }
        for (int j = 0; j < currentPlayer.getHand().size(); j++) {
            System.out.println((j + 1) + ". " + currentPlayer.getHand().get(j));
        }
        while (true) {
            System.out.println("Enter a card index to play a card, or 0 to draw a card:");
            int cardNumber = parseUserIntInput();
            if (cardNumber > 0 && cardNumber <= currentPlayer.handSize()) {
                boolean valid = validatePlacedCard(currentPlayer.getHand().get(cardNumber - 1), pile.peekTopDiscard(), wildColor);
                if (valid) {
                    Card cardPlaced = currentPlayer.removeCardAt(cardNumber - 1);
                    proccessCard(cardPlaced);
                    System.out.println("");
                    break;
                } else {
                    System.out.println("Invalid card. Please try again.");

                }
            } else if (cardNumber == 0) {
                Card drawnCard = pile.drawCard();
                currentPlayer.addCard(drawnCard);
                System.out.println("Player " + currentPlayer.getName() + " drew a " + drawnCard + "!") ;
                System.out.println("---------------------------------------------\n");
                break;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    /**
     * Starts a new round when no player has won the game yet.
     * Prompts the user to type "new" and, upon confirmation,
     * resets per-round fields and redeals cards.
     */
    public void newRound() {
        while (true) {
            System.out.println("No winner yet. type 'new' to start a new round");
            String input = parseUserStringInput();
            if (input.equals("new")) {
                resetFeilds();
                roundNumber++;
                System.out.println("A new round begins! Hands have been reset, a new pile is ready. Current round: #" + roundNumber);
                break;
            }
        }
    }
    /**
     * Checks if any player's hand is empty.
     *
     * @return true if any player's hand is empty, false otherwise
     */
    public boolean checkIfAnyHandIsEmpty() {
        for (Player player : players) {
            if (player.getHand().isEmpty()) {
                currentPlayer = player;
                return true;
            }
        }
        return false;
    }

    /**
     * Resets per-round game fields to their initial state and redeals hands.
     * Does not reset player scores.
     * Recreates the draw/discard piles and reinitializes control flags.
     */
    public void resetFeilds() {
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
     * Resets per-round game fields to their initial state and redeals hands,
     * with an option to also reset player scores to zero.
     *
     * @param resetScore if true, all players' scores are set to 0 in addition to round reset
     */
    public void resetFeilds(boolean resetScore) {
        resetFeilds();
        roundNumber = 1;
        if (resetScore) {
            for (Player player : players) {
                player.setScore(0);
            }
        }
    }

    /**
     * Checks if current player has reached the winning score threshold (>= 500).
     *
     * @return true if current player is a winner otherwise false.
     */
    public boolean checkWinner(Player current_player) {
        return current_player.getScore()  >= WINNING_SCORE;
    }

    /**
     * Prints all players' current scores to the console.
     * Output format: "<name>: <score>" per line.
     */
    public void displayScores() {
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getScore());
        }
    }

    /**
     * Prompts to start a brand new game (fresh scores and hands).
     * If the user types "new", resets all fields and scores; otherwise sets a flag to exit.
     */
    public void restartGame() {
        System.out.println("Type 'new' to start a new game.");
        String input = parseUserStringInput();
        if (input.equals("new")) {
            resetFeilds(true);

        } else {
            exitGame = true;
        }
    }

    /**
     * Processes a card placement from the current player's hand.
     * Adds the card to the discard pile and handles special card effects.
     *
     * @param placedCardFromHand the card being placed from the player's hand
     */
    public void proccessCard(Card placedCardFromHand) {
        String chosenColor;
        pile.addToDiscardPile(placedCardFromHand);
        switch (placedCardFromHand.getType()) {
            case ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE:
                System.out.println(currentPlayer.getName() + " placed a " + placedCardFromHand);
                break;
            case DRAW_ONE:
                System.out.println(currentPlayer.getName() + " placed a " + placedCardFromHand);
                drawOne = true;
                break;
            case REVERSE:
                System.out.print(currentPlayer.getName()+" played a Reverse Card, direction is set to ");
                direction *= -1;
                if(direction > 0){
                    System.out.println("clock wise");
                }else{
                    System.out.println("counter clock wise");
                }
                break;
            case SKIP:
                skip = true;
                System.out.println(currentPlayer.getName()+" played a Skip Card.");
                break;
            case WILD:
                System.out.println("Enter color (RED, YELLOW, BLUE, GREEN)");
                chosenColor = parseUserStringInput().toUpperCase();
                while (true) {
                    if (chosenColor.equals("RED") || chosenColor.equals("YELLOW") || chosenColor.equals("BLUE") || chosenColor.equals("GREEN")) {
                        wildColor = new Card(CardColor.valueOf(chosenColor), null);
                        System.out.println(currentPlayer.getName()+" played a Wild Card, color is set to " + chosenColor);
                        break;
                    } else {
                        System.out.println("Invalid color. Please try again.");
                        chosenColor = parseUserStringInput();
                    }
                }
                break;
            case WILD_DRAW_TWO:
                System.out.println("Enter color (RED, YELLOW, BLUE, GREEN)");
                chosenColor = parseUserStringInput().toUpperCase();
                while (true) {
                    if (chosenColor.equals("RED") || chosenColor.equals("YELLOW") || chosenColor.equals("BLUE") || chosenColor.equals("GREEN")) {
                        wildColor = new Card(CardColor.valueOf(chosenColor), null);
                        System.out.println(currentPlayer.getName()+" played a Wild Draw Two Card, color is set to " + chosenColor);
                        break;
                    } else {
                        System.out.println("Invalid color. Please try again.");
                        chosenColor = parseUserStringInput();
                    }
                }
                wildDrawTwo = true;
                break;
        }
    }

    /**
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
     * Validates a card placement from the current player's hand.
     * Checks if the card matches the top card of the discard pile,
     * or if it is a WILD or WILD_DRAW_TWO card that matches the specified color.
     *
     * @param placedCardFromHand the card being placed from the player's hand
     * @param topCardOfDiscardPile the top card of the discard pile
     * @param wildColor the color specified by the previous player using a WILD or WILD_DRAW_TWO card
     * @return true if the placement is valid; otherwise {@code false}
     */
    public boolean validatePlacedCard(Card placedCardFromHand, Card topCardOfDiscardPile, Card wildColor) {
        if (topCardOfDiscardPile.getType()==CardType.WILD || topCardOfDiscardPile.getType()==CardType.WILD_DRAW_TWO) { // if true, must check for the specifed color from previous player using wildColor
            return wildColor != null && (placedCardFromHand.getColor() == wildColor.getColor() || placedCardFromHand.getColor() == CardColor.WILD);

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

    /**
     * Creates a specified number of players and assigns them hands.
     * Validates the number of players (between 2 and 4).
     * Prompts for each player's name and generates their hand.
     *
     * @param numberOfPlayers the number of players to create
     */
    public Player createPlayers(int numberOfPlayers) {
        while (numberOfPlayers > 4 || numberOfPlayers < 2) {
            System.out.println("Invalid number of players. Please enter a number between 2 and 4.");
            numberOfPlayers = parseUserIntInput();
        }
        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.println("Enter name of player " + (i+1));
            String name = parseUserStringInput();
            players.add(new Player(name));
            generatePlayerHand(players.get(i)); // o(1)
        }
        System.out.println("");
        return players.get(0); // returns the current player (first one when game starts)

    }
    /**
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
     * Parses user input as an integer.
     * Skips any remaining input on the current line.
     *
     * @return the parsed integer value
     */
    public int parseUserIntInput() {
        int integer = -1;
        boolean valid = false;

        while (!valid) {
            try {
                integer = scanner.nextInt();
                scanner.nextLine();
                valid = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a number:");
                scanner.nextLine();
            }
        }
        return integer;
    }
    /**
     * Parses user input as a string.
     *
     * @return the parsed string value
     */
    public String parseUserStringInput() {
        return scanner.nextLine();

    }

    // Test-mode constructor: init fields, NO game loop.
    public Game(Scanner scanner, boolean testMode) {
        this.scanner = scanner != null ? scanner : new Scanner(System.in);
        this.players = new ArrayList<>();
        this.pile = new PileSystem();
        this.wildColor = null;
        this.drawOne = false;
        this.wildDrawTwo = false;
        this.skip = false;
        this.exitGame = false;
        this.isWinner = false;
        this.direction = 1;
        this.indexOfCurrentPlayer = 0;
        this.roundNumber = 1;
    }

    /**
     * Returns the current play direction.
     * @return 1 for normal direction, -1 for reverse
     */
    public int getDirection() {
        return direction;
    }

    /**
     * Checks if the Draw One effect is active.
     * @return true if a Draw One card was played, false otherwise
     */
    public boolean getDrawOne() {
        return drawOne;
    }

    /**
     * Checks if the Wild Draw Two effect is active.
     * @return true if a Wild Draw Two card was played, false otherwise
     */
    public boolean getWildDrawTwo() {
        return wildDrawTwo;
    }

    /**
     * Adds a player to the game.
     * @param player the player to add
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Checks if the player list is empty.
     * @return true if there are no players, false otherwise
     */
    public boolean isPlayerListEmpty() {
        return players.isEmpty();}
}
