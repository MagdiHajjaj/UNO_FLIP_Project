package game;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UNOView extends JFrame implements IView {
    private UNOModel model;
    private UNOController controller;
    private JPanel handPanel;
    private TopPanel topPanel;
    private JButton nextPlayerButton;
    private JButton drawCardButton;
    private ArrayList<JLabel> playerScoreLabels;

    public  UNOView() {
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
        this.add(ScoreboardPanel(model.getPlayers()), BorderLayout.SOUTH);

        //Center cards
        refreshCenterCards();

        this.setVisible(true);

    }

    public void refreshCenterCards(){
        List<Card> playerHand = model.getCurrentPlayerHand();
        if (handPanel != null) {
            this.remove(handPanel);
        }
        handPanel = new JPanel(new GridLayout(2, playerHand.size()/2));
        for(int i = 0; i < playerHand.size();i++){
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
        // refresh layout visually
        this.revalidate();
        this.repaint();
    }
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
            System.out.println("Selected: " + numPlayers + " players");
        } else {
            System.out.println("User canceled");
            System.exit(0);
        }

    }

    @Override
    public void update() {
        UNOModel.Status status = model.getStatus();
        switch (status) {
            case CARD_DRAWN:
                refreshCenterCards();
                drawCardButton.setEnabled(false);
                disableAllCardButtons();
                nextPlayerButton.setEnabled(true);
                topPanel.setStatusMessage(model.getCurrentPlayer().getName() + " drew a card: " + model.getLastDrawnCard().toString());

                if (model.getPlayedCard() != null && model.getPlayedCard().getType() == CardType.DRAW_ONE) {
                    topPanel.setStatusMessage("A DRAW_ONE card has been played. "
                            + model.getNextPlayer().getName() + " drew a card: "+ model.getLastDrawnCard().toString());
                } else {
                    topPanel.setStatusMessage(model.getCurrentPlayer().getName() + " drew a card: "
                            + model.getLastDrawnCard());
                }
                break;
            case NEXT_PLAYER:
                refreshCenterCards();
                topPanel.setCurrentPlayer(model.getCurrentPlayer().getName());
                topPanel.setStatusMessage("");
                drawCardButton.setEnabled(true);
                nextPlayerButton.setEnabled(false);
                break;
            case WILD_CARD:
                colorSelectionMenu();
                topPanel.setCurrentWildColorLabel(model.getWildColor().name());
                if(model.getPlayedCard().getType()==CardType.WILD_DRAW_TWO) {
                    int sizeOfHand = model.getNextPlayer().getHand().size();
                    topPanel.setStatusMessage("A WILD DRAW TWO card has been played. " + model.getNextPlayer().getName() + " draws 2 cards: " +
                            model.getNextPlayer().getHand().get(sizeOfHand - 1) + ", " +  model.getNextPlayer().getHand().get(sizeOfHand - 2));

                }else{
                    topPanel.setStatusMessage("WILD color selected: " + model.getWildColor().name());
                }
                refreshCenterCards();
                drawCardButton.setEnabled(false);
                disableAllCardButtons();
                nextPlayerButton.setEnabled(true);
                break;
            case PLAYING_VALID_CARD:
                refreshCenterCards();
                drawCardButton.setEnabled(false);
                disableAllCardButtons();
                nextPlayerButton.setEnabled(true);
                if(model.getTopCard().getType() != CardType.SKIP && model.getTopCard().getType() != CardType.REVERSE)
                {
                    topPanel.setStatusMessage(model.getCurrentPlayer().getName() + " played a card: " + model.getTopCard().toString());
                }else if(model.getTopCard().getType() == CardType.SKIP){
                    topPanel.setStatusMessage("A "+ model.getTopCard().getType() + "card has been played. " + model.getNextPlayer().getName() +" turn has been skipped");
                }else{
                    topPanel.setStatusMessage("A " + model.getTopCard().getType() + " card has been played. " + "The direction of player rotation changes.");
                }
                break;
            case PLAYING_INVALID_CARD:
                topPanel.setStatusMessage("Placing that card is not a valid move. Try Again.");
                break;
        }
        if(model.getPlayedCard()!= null){
            topPanel.setTopCard(model.getPlayedCard().getImage(), model.getPlayedCard().toString());
        }


    }
    private void colorSelectionMenu() {
        String[] playerOptions = {"RED", "YELLOW", "GREEN", "BLUE"};
        String selection = null;
        while(selection == null) {
             selection = (String) JOptionPane.showInputDialog(
                    null,
                    "Choose a colour for the WILD CARD:",
                    "WILD Colour Selection",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    playerOptions,
                    playerOptions[0]
            );
            if(selection == null)
            {
                JOptionPane.showMessageDialog(
                        null,
                        "You must select a color to continue!",
                        "Selection Required",
                        JOptionPane.WARNING_MESSAGE
                );
            }

        }
        if (selection != null) {
            controller.setWildColor(selection);
        }
    }

    public void disableAllCardButtons(){
        for (Component comp : handPanel.getComponents()) {
            comp.setEnabled(false);
        }
    }
    public static void main(String[] args) {
        UNOView unoView = new UNOView();
    }
}
