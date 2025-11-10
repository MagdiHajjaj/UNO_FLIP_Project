package game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * public class UNOController, handles all the actions by invoking methods from the model.
 * It also implements the ActionListener interface to handle events from the view.
 *
 * @author Magdi Hajjaj
 * @id 101299841
 * @version M2
 */
public class UNOController implements ActionListener {
    private UNOModel model;
    private UNOView view;
    /**
     * constructor for UNOController
     * @param model, the model
     * @param view, the view
     */
    public UNOController(UNOModel model, UNOView view) {
        this.model = model;
        this.view = view;
    }
    /**
     * sets the number of players and starts a new game
     * @param numPlayers, the number of players
     */
    public void setNumPlayers(int numPlayers) {
        model.newGame(numPlayers);
    }

    /**
     * sets the wild color
     * @param wildColor
     */
    public void setWildColor(String wildColor) {
        model.setWildColor(CardColor.valueOf(wildColor));
    }

    /**
     * starts a new round
     */
    public void startNewRound() {
        model.startNewRound();
    }
    /**
     * resets the game when invoked
     */
    public void resetGame() {
        model.resetGame();
    }
    /**
     * handles all the action events from the view by invoking methods from the model
     * every action event has a unique command name
     * the action events commands are: DRAW_CARD, NEXT_PLAYER, PLAY_CARD
     * based on the command name, the appropriate method is called from the model.
     * @param e, the action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("DRAW_CARD")) {
            model.drawCard();
        }else if (command.equals("NEXT_PLAYER")) {
            model.nextPlayer();
        }else if (command.equals("PLAY_CARD")) {
            JButton clickedButton = (JButton) e.getSource();
            Card playedCard = (Card) clickedButton.getClientProperty("card");
            model.playCard(playedCard);
        }
    }
}
