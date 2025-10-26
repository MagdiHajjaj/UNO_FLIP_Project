package game;
import java.sql.SQLOutput;
import java.util.*;

/**
 * game.Game class for the game of UNO
 *
 * @author Mohammed Osman
 * @id 101311022
 * @version M1
 *
 *
 *
 * things to do:
 * - create players
 * - parse user input
 * - game loop
 * - check for winner
 *
 */


public class Game {
    private Scanner scanner;
    private final int numberOfPlayers = 0;
    private final LinkedList<Player> players;
    private Player currentPlayer;
    private PileSystem pile;

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

    public void welcomeMenu() {
        System.out.println("Welcome to UNO Flip!\n");
        System.out.println("------------------------------------");

    }
    public void displayTurn(Player currentPlayer) {

        System.out.println("--------"+currentPlayer.getName() + "'s turn"+"----------");
        for (Card card : currentPlayer.getHand()) {
            System.out.println(currentPlayer.getHand().indexOf(card)+1 + ". " + card);
        }
        System.out.println("---------Things to consider----------");
        System.out.println("top card of discard pile: " + pile.peekTopDiscard());
        System.out.println("draw pile size: " + pile.getDrawPileSize());
        System.out.println("------------------------------------");
        System.out.println("Enter number of card to play or enter 0 to draw a card from the draw pile");
        int cardNumber = parseUserIntInput();
        if (cardNumber > 0) {
            boolean valid = validatePlacedCard(currentPlayer.getHand().get(cardNumber-1), pile.peekTopDiscard());
        }


    }
    public boolean validatePlacedCard(Card placedCardFromHand, Card topCardOfDiscardPile) {

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
