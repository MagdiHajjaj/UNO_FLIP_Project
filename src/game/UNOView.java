package game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
/**
 * public class UNOView
 *
 * @author Magdi Hajjaj
 * @id 101299841
 * @version M2
 */
public class UNOView extends JFrame implements IView {
    private UNOModel model;
    private UNOController controller;
    private JPanel handPanel;
    private JPanel scoreBoardPanel;
    private TopPanel topPanel;
    private JButton nextPlayerButton;
    private JButton drawCardButton;
    private ArrayList<JLabel> playerScoreLabels;
    /**
     * Sets up the main UNO game window.
     *
     * This constructor initializes the model, registers this view so it receives
     * updates, creates the controller, and builds all UI sections (top panel,
     * draw/next buttons, scoreboard, and the player's hand display). It also
     * prompts the user to select the number of players before the game starts.
     */
    public UNOView() {
        super("Uno Flip Game");
        model = new UNOModel();
        model.addViewer(this);

        controller = new UNOController(model, this);
        playerSelectionMenu();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1400,900);
        this.setLayout(new BorderLayout());

        // add top part
        topPanel = new TopPanel();
        add(topPanel, BorderLayout.NORTH);
        topPanel.setTopCard(model.getTopCard().getImage(), model.getTopCard().toString());
        topPanel.setCurrentPlayer(model.getCurrentPlayer().getName());

        //left button
        nextPlayerButton = new JButton("NEXT PLAYER");
        nextPlayerButton.setFont(new Font("Arial", Font.BOLD, 14));
        nextPlayerButton.setActionCommand("NEXT_PLAYER");
        nextPlayerButton.addActionListener(controller);
        nextPlayerButton.setEnabled(false);
        this.add(nextPlayerButton, BorderLayout.WEST);

        //right button
        drawCardButton = new JButton("DRAW CARD");
        drawCardButton.setFont(new Font("Arial", Font.BOLD, 14));
        drawCardButton.setActionCommand("DRAW_CARD");
        drawCardButton.addActionListener(controller);
        drawCardButton.setEnabled(true);
        this.add(drawCardButton, BorderLayout.EAST);

        //bottom scoreboard
        playerScoreLabels = new ArrayList<>();
        scoreBoardPanel = ScoreboardPanel(model.getPlayers());
        this.add(scoreBoardPanel, BorderLayout.SOUTH);

        //Center cards
        refreshCenterCards();

        this.setVisible(true);
    }
    /**
     * Creates a scoreboard panel for the given players.
     *
     * @param players, list of players
     * @return returns a JPanel containing the scoreboard
     */
    public JPanel ScoreboardPanel(ArrayList<Player> players) {
        JPanel scoreboardPanel = new JPanel();
        scoreboardPanel.setLayout(new BoxLayout(scoreboardPanel, BoxLayout.Y_AXIS));
        scoreboardPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        JLabel title = new JLabel("Scoreboard:");
        title.setFont(new Font("Arial", Font.BOLD, 12));
        scoreboardPanel.add(title);

        for (int i = 0; i < players.size(); i++) {
            JLabel label = new JLabel("Player " + (i + 1) + ": " + players.get(i).getScore());
            playerScoreLabels.add(label);
            scoreboardPanel.add(label);
        }
        return scoreboardPanel;
    }
    /**
     * Asks the user to select how many players will be in the game.
     *
     * The selected value is sent to the controller to set up the game.
     * If the user cancels the dialog, the program exits.
     */
    private void playerSelectionMenu() {
        String[] playerOptions = {"2", "3", "4"};

        String selection = (String) JOptionPane.showInputDialog(
                null,
                "Select the total number of players:",
                "Player Selection",
                JOptionPane.QUESTION_MESSAGE,
                null,
                playerOptions,
                playerOptions[0]
        );

        if (selection != null) {
            int numPlayers = Integer.parseInt(selection);
            controller.setNumPlayers(numPlayers);
        } else {
            System.out.println("User canceled");
            System.exit(0);
        }
    }
    /**
     * Builds the main UNO game window and initializes the game.
     *
     * This constructor creates the model and controller, registers this view
     * so it receives model updates, sets up the UI panels and buttons, and
     * asks the user how many players will be in the game.
     */
    public void gameOverMenu() {
        String message = model.getCurrentPlayer().getName() + " has reached 500+ points! They win the game! "
                + "Points have been reset for a new game.";

        // Show information message
        JOptionPane.showMessageDialog(
                null,
                message,
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE
        );

        controller.resetGame();
    }
    /**
     * Displays a menu when a round ends and lets the player choose to start
     * a new round or quit the game.
     */
    public void roundOverMenu() {
        String[] playerOptions = {"NEW ROUND", "QUIT"};
        String[] options = {"NEW ROUND", "QUIT"};
        String message = model.getCurrentPlayer().getName() + " has 0 cards left! They win this round! "
                + model.getCurrentPlayer().getName() + " earned " + model.getCurrentPlayer().getScore() + " points.\n\n"
                + "Select NEW ROUND to continue playing.";

        // Show the input dialog with combo box
        String selection = (String) JOptionPane.showInputDialog(
                null,
                message,
                "Round Over",
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );
        // Handle selection
        if (selection != null) {
            if (selection.equals("NEW ROUND")) {
                controller.startNewRound();
            } else if (selection.equals("QUIT")) {
                System.exit(0);
            }
        } else {
            System.out.println("Dialog closed. Exiting game.");
            System.exit(0);
        }
    }
    /**
     * Opens a dialog allowing the player to choose a color after playing a wild card.
     *
     * The method blocks until the player selects a valid color. Once selected,
     * the chosen color is passed to the controller to update the game state.
     */
    private void colorSelectionMenu() {
        String[] playerOptions = {"RED", "YELLOW", "GREEN", "BLUE"};
        String selection = null;

        while (selection == null) {
            selection = (String) JOptionPane.showInputDialog(
                    null,
                    "Choose a colour for the WILD CARD:",
                    "WILD Colour Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    playerOptions,
                    playerOptions[0]
            );

            if (selection == null) {
                JOptionPane.showMessageDialog(
                        null,
                        "You must select a color to continue!",
                        "Selection Required",
                        JOptionPane.WARNING_MESSAGE
                );
            }
        }
        if (selection != null) { controller.setWildColor(selection); }
    }
    /**
     * Rebuilds and displays the current player's hand in the center area.
     *
     * This removes the previous hand panel and creates a new one based on the
     * player's updated cards. Each card is shown as a button with its image and
     * is wired to allow the player to select and play it.
     */
    public void refreshCenterCards() {
        List<Card> playerHand = model.getCurrentPlayerHand();
        if (handPanel != null) {
            this.remove(handPanel);
        }
        handPanel = new JPanel(new GridLayout(2, playerHand.size() / 2));
        for (int i = 0; i < playerHand.size(); i++) {
            Card card = playerHand.get(i);
            JButton cardButton = new JButton(card.toString());
            cardButton.setIcon(card.getImage());
            cardButton.setVerticalTextPosition(SwingConstants.TOP);
            cardButton.setHorizontalTextPosition(SwingConstants.CENTER);
            cardButton.setFont(new Font("Arial", Font.BOLD, 14));
            cardButton.putClientProperty("card", card);
            cardButton.setActionCommand("PLAY_CARD");
            cardButton.addActionListener(controller);
            handPanel.add(cardButton);
        }
        this.add(handPanel, BorderLayout.CENTER);
        this.revalidate();
        this.repaint();
    }
    /**
     * Updates the scoreboard to show each player's current score.
     *
     * The labels in the scoreboard are matched with the players in the list
     * by index. If the UI has not been initialized yet, the method returns
     * without doing anything.
     *
     * @param players list of players whose scores should be displayed
     */
    public void updateScoreboard(ArrayList<Player> players) {
        if (playerScoreLabels == null || playerScoreLabels.isEmpty()) {
            return; // nothing to update yet
        }

        for (int i = 0; i < players.size() && i < playerScoreLabels.size(); i++) {
            Player player = players.get(i);
            JLabel label = playerScoreLabels.get(i);
            label.setText("Player " + (i + 1) + ": " + player.getScore());
        }

        scoreBoardPanel.revalidate();
        scoreBoardPanel.repaint();
    }
    /**
     * Helper method to refresh the main display and control button states.
     *
     * @param canDraw     whether the "Draw Card" button should be enabled
     * @param canPlay     whether the player can click card buttons
     * @param canNext     whether the "Next Player" button should be enabled
     * @param statusMsg   message to display on the top panel
     */
    private void updateUIState(boolean canDraw, boolean canPlay, boolean canNext, String statusMsg) {
        refreshCenterCards();
        drawCardButton.setEnabled(canDraw);
        nextPlayerButton.setEnabled(canNext);

        if (!canPlay) {
            disableAllCardButtons();
        }

        if (statusMsg != null) {
            topPanel.setStatusMessage(statusMsg);
        }
    }
    /**
     * Disables every card button in the player's hand.
     *
     * This is used when the player should not be able to play a card
     * (for example, after drawing a card or while waiting for another player).
     */
    public void disableAllCardButtons() {
        for (Component comp : handPanel.getComponents()) {
            comp.setEnabled(false);
        }
    }
    /**
     * Updates the view when the model changes.
     *
     * This class implements the View interface, so the model calls this method
     * whenever its state is updated. The method checks the model's current
     * Status and adjusts the user interface to match (for example: updating
     * displayed cards, showing messages, enabling/disabling buttons).
     *
     * Each case in the switch corresponds to a different part of gameplay,
     * and the UI is updated so the player always sees the current state of
     * the game.
     */
    @Override
    public void update() {
        Status status = model.getStatus();
        switch (status) {
            case Status.CARD_DRAWN:
                String drawMsg;
                if (model.getPlayedCard() != null && model.getPlayedCard().getType() == CardType.DRAW_ONE) {
                    drawMsg = "A DRAW_ONE card has been played. " + model.getNextPlayer().getName() + " drew a card: "+ model.getLastDrawnCard().toString();
                } else {
                    drawMsg = model.getCurrentPlayer().getName() + " drew a card: " + model.getLastDrawnCard();
                }
                updateUIState(false, false, true, drawMsg);
                break;
            case Status.NEXT_PLAYER:
                updateUIState(true, true, false, "");
                refreshCenterCards();
                topPanel.setCurrentPlayer(model.getCurrentPlayer().getName());
                break;
            case Status.WILD_CARD:
                colorSelectionMenu();
                topPanel.setCurrentWildColorLabel(model.getWildColor().name());
                String wildMsg;
                if(model.getPlayedCard().getType()==CardType.WILD_DRAW_TWO) {
                    int sizeOfHand = model.getNextPlayer().getHand().size();
                    wildMsg = "A WILD DRAW TWO card has been played. " + model.getNextPlayer().getName() + " draws 2 cards: " +
                            model.getNextPlayer().getHand().get(sizeOfHand - 1) + ", " +  model.getNextPlayer().getHand().get(sizeOfHand - 2);

                }else{
                    wildMsg = "WILD color selected: " + model.getWildColor().name();
                }
                updateUIState(false, false, true, wildMsg);
                break;
            case Status.PLAYING_VALID_CARD:
                String playMsg;
                if (model.getTopCard().getType() == CardType.SKIP) {
                    playMsg = "A SKIP card has been played. " + model.getNextPlayer().getName() + " turn has been skipped.";
                } else if (model.getTopCard().getType() == CardType.REVERSE) {
                    playMsg = "A REVERSE card has been played. The direction of player rotation changes.";
                } else {
                    playMsg = model.getCurrentPlayer().getName() + " played a card: " + model.getTopCard();
                }
                updateUIState(false, false, true, playMsg);
                break;
            case Status.PLAYING_INVALID_CARD:
                updateUIState(true, true, false, "Placing that card is not a valid move. Try again.");
                break;
            case Status.ROUND_OVER:
                refreshCenterCards();
                roundOverMenu();
                String roundOver = model.getCurrentPlayer().getName() + " has 0 cards left! They win this round! "
                        + model.getCurrentPlayer().getName() + " earned " + model.getCurrentPlayer().getScore() + " points.\n\n";
                updateUIState(true, true, false,roundOver);
                updateScoreboard(model.getPlayers());
                break;
            case Status.GAME_OVER:
                refreshCenterCards();
                gameOverMenu();
                String gameOver = model.getCurrentPlayer().getName() + " has reached 500+ points! They win the game! "
                        + "Points have been reset for a new game.";
                updateUIState(true, true, false,gameOver);
                updateScoreboard(model.getPlayers());
                break;
        }
        if(model.getPlayedCard() != null){
            topPanel.setTopCard(model.getPlayedCard().getImage(), model.getPlayedCard().toString());
        }

    }

}
