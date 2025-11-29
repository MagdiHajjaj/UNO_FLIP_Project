package game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * public class UNOController, handles all the actions by invoking methods from the model.
 * It also implements the ActionListener interface to handle events from the view.
 *
 * @author Magdi Hajjaj
 * @id 101299841
 * @version M3
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
    public void setNumPlayers(int numPlayers, int numComputers) {
        model.newGame(numPlayers, numComputers);
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

    public void saveGame() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose a file");
        chooser.setApproveButtonText("Save");

        int input = chooser.showSaveDialog(this.view);
        if (input == JFileChooser.APPROVE_OPTION) {
            File saveFile = chooser.getSelectedFile();
            if (!saveFile.getName().toLowerCase().endsWith("ser")) {
                saveFile = new File(saveFile.getAbsolutePath() + ".ser");
            }
            try {
                model.saveGame(saveFile.getAbsolutePath());
                JOptionPane.showMessageDialog(this.view, "Game saved to " + saveFile.getAbsolutePath());
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this.view, "Error saving game to " + saveFile.getAbsolutePath());
                e.printStackTrace();
            }
        }
    }

    public void loadGame() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose a file");
        chooser.setApproveButtonText("Load");

        int input = chooser.showOpenDialog(this.view);
        if (input == JFileChooser.APPROVE_OPTION) {
            File loadFile = chooser.getSelectedFile();
            if (!loadFile.exists()) {
                JOptionPane.showMessageDialog(this.view, "Error File not found or does not exist " + loadFile.getAbsolutePath());
                return;
            }
            if (!loadFile.getName().toLowerCase().endsWith("ser")) {
                JOptionPane.showMessageDialog(this.view, "Error loading non '.Ser' File " + loadFile.getAbsolutePath());
                return;
            }
            try {
                model.loadGame(loadFile.getAbsolutePath());
                JOptionPane.showMessageDialog(this.view, "Game loaded from " + loadFile.getAbsolutePath());
            } catch (IOException | ClassNotFoundException e) {
                JOptionPane.showMessageDialog(this.view, "Error loading File " + loadFile.getAbsolutePath());
                e.printStackTrace();
            }
        }
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
        }else if (command.equals("UNDO")) {
            model.undo();
        } else if (command.equals("REDO")) {
            model.redo();
        }
    }

}
