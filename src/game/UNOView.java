package game;

import javax.swing.*;
import java.awt.*;
import java.util.Observer;

public class UNOView extends JFrame implements IView {
    private UNOModel model;
    private UNOController controller;
    private TopPanel topPanel;


    public  UNOView() {
        super("Uno Flip Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1400,900);
        this.setLayout(new BorderLayout());
        // add top part
        topPanel = new TopPanel();
        add(topPanel, BorderLayout.NORTH);

        // Create a WILD DRAW TWO card
        Card testCard = new Card(CardColor.WILD, CardType.WILD_DRAW_TWO);

        // Get the card’s image from your Card class
        ImageIcon icon = testCard.getImage();

        topPanel.setTopCard(icon, testCard.toString());
        model = new UNOModel();
        controller = new UNOController(model, this);
        playerSelectionMenu();
        this.setVisible(true);

    }

    private void playerSelectionMenu() {
        // options for player count
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
            System.out.println("Selected: " + numPlayers + " players");
        } else {
            System.out.println("User canceled");
            System.exit(0);
        }

    }
    @Override
    public void update() {

    }
    public static void main(String[] args) {
        UNOView unoView = new UNOView();
    }
}
