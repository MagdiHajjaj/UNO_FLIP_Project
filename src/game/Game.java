package game;
import game.*;
/*
* to do:
* welcome menu.
* player creation.
* user input
*
*
 */
public class Game {
    private PileSystem pile;
    private ArrayList<Player> players;
    private Player currentPlayer;

    public Game() {
        welcomeMenu();
        PileSystem pile = new PileSystem(); // creates and shuffles draw pile. Also creates discard pile
        //game loop
        //display every player's hand

    }
    public void welcomeMenu() {
        System.out.println("Welcome to UNO Flip!");
        //get number of players and their names then inilize them.
        //scanf......
        Player currentPlayer = createPlayers(null);

    }
    public Player createPlayers(int numberOfPlayers) {
        //generate every player's hand
    }
}
