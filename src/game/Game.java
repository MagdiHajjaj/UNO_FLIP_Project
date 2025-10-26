package game;
import java.util.*;

/**
 * game.Game class for the game of UNO
 *
 * @author Mohammed Osman
 * @id 101311022
 * @version M1
 *
 */


public class Game {
    private Scanner scanner;
    private final int numberOfPlayers = 0;
    private final LinkedList<Player> players;
    private Player currentPlayer;
    private PileSystem pile;
    private boolean drawTwo = false;
    private boolean wildDrawTwo = false;

    public Game() {
        PileSystem pile = new PileSystem(); // creates and shuffles draw pile. Also creates discard pile
        players = new LinkedList<Player>();
        currentPlayer = null;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            welcomeMenu();
            System.out.println("Enter number of players (2-4");
            int numberOfPlayers = parseUserIntInput();
            currentPlayer = createPlayers(numberOfPlayers);
            for (Player player : players) {
                displayTurn(player);
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
        System.out.println("Welcome to UNO Flip!\n");
        System.out.println("------------------------------------");
    }
    public void displayTurn(Player currentPlayer) {

        System.out.println("--------"+currentPlayer.getName() + "'s turn"+"----------");
        if (drawTwo) {
            System.out.println(currentPlayer.getName()+" has to draw two cards due to the DRAW_TWO card.");
            currentPlayer.addCard(pile.drawCard());
            currentPlayer.addCard(pile.drawCard());
            drawTwo = false;
        } else if (wildDrawTwo) {

            System.out.println("Wild draw two!");
        }
        for (Card card : currentPlayer.getHand()) {
            System.out.println(currentPlayer.getHand().indexOf(card)+1 + ". " + card);
        }
        System.out.println("---------Things to consider----------");
        System.out.println("top card of discard pile: " + pile.peekTopDiscard());
        System.out.println("draw pile size: " + pile.getDrawPileSize());
        System.out.println("------------------------------------");
        while (true) {
            System.out.println("Enter number of card to play or enter 0 to draw a card from the draw pile");
            int cardNumber = parseUserIntInput();
            if (cardNumber > 0 && cardNumber <= currentPlayer.handSize()) {
                boolean valid = validatePlacedCard(currentPlayer.getHand().get(cardNumber - 1), pile.peekTopDiscard());
                if (valid) {
                    currentPlayer.removeCardAt(cardNumber - 1);
                    proccessCard(currentPlayer.getHand().get(cardNumber-1), cardNumber);
                    System.out.println("---------------------------------------------");
                    break;
                } else {
                    System.out.println("Invalid card. Please try again.");
                    continue;
                }
            } else if (cardNumber == 0) {
                Card drawnCard = pile.drawCard();
                currentPlayer.addCard(drawnCard);
                System.out.println("Player " + currentPlayer.getName() + " drew a " + drawnCard + " !") ;
                System.out.println("---------------------------------------------");
                break;
            } else {
                System.out.println("Invalid input. Please try again.");
            }
        }


    }


    public void proccessCard(Card placedCardFromHand, int cardNumber) {
        switch (placedCardFromHand.getType()) {
            case CardType.ONE, CardType.TWO, CardType.THREE, CardType.FOUR, CardType.FIVE, CardType.SIX, CardType.SEVEN, CardType.EIGHT, CardType.NINE:
                pile.addToDiscardPile(currentPlayer.getHand().get(cardNumber - 1));
                System.out.println("Card removed from hand and added to discard pile!");
                break;
            case CardType.DRAW_ONE:

                break;
            case CardType.REVERSE:
                break;
            case CardType.SKIP:
                break;
            case CardType.WILD:
                break;
            case CardType.WILD_DRAW_TWO:
                break;
        }
    }

    public void calculateScore() {

    }




    public boolean validatePlacedCard(Card placedCardFromHand, Card topCardOfDiscardPile) {
        if (placedCardFromHand.getColor() == topCardOfDiscardPile.getColor() || placedCardFromHand.getType() == topCardOfDiscardPile.getType()) {
            return true;
        }
        return false;
    }
    public Player createPlayers(int numberOfPlayers) {
        while (numberOfPlayers > 4 || numberOfPlayers < 2) {
            System.out.println("Invalid number of players. Please enter a number between 2 and 4.");
            numberOfPlayers = parseUserIntInput();
        }
        for (int i = 0; i < numberOfPlayers; i++) {
            System.out.println("Enter name of player " + i);
            String name = parseUserStringInput();
            players.add(new Player(name));
            generatePlayerHand(players.get(i)); // o(1)
        }
        return players.get(0); // returns the current player (first one when game starts)
        
    }
    public void generatePlayerHand(Player player) {
        for (int i = 0; i < 7; i++) { // each player starts with 7 cards
            player.addCard(pile.drawCard()); // pile returns a card, player adds that card to its hand.
        }
    }

    public int parseUserIntInput() {
        int integer =  scanner.nextInt();
        return integer;
    }
    public String parseUserStringInput() {
        String string =  scanner.nextLine();
        return string;
    }
}
