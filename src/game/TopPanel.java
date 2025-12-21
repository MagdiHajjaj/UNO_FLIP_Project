package game;

import javax.swing.*;
import java.awt.*;

/**
 * class for the top panel of the UNO game
 *
 * @author Magdi Hajjaj
 * @id 101299841
 * @version M5
 */
public class TopPanel extends JPanel {
    private JLabel topCardLabel;
    private JLabel topCardImage;
    private JLabel currentPlayerLabel;
    private JLabel statusMessageLabel;
    private JLabel currentWildColorLabel;

    /**
     * Constructor for TopPanel.
     */
    public TopPanel(Color textColor) {
        setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        setOpaque(false);
        // Section title
        JLabel title = new JLabel("Top Card");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        title.setForeground(textColor);
        add(title, BorderLayout.NORTH);

        JPanel cardPanel = new JPanel();
        cardPanel.setOpaque(false);
        topCardImage = new JLabel();
        topCardLabel = new JLabel();
        topCardLabel.setFont(new Font("Arial", Font.BOLD, 15));
        topCardLabel.setForeground(textColor);

        cardPanel.add(topCardImage);
        cardPanel.add(topCardLabel);
        add(cardPanel, BorderLayout.CENTER);

        // Bottom status section
        JPanel bottomPanel = new JPanel(new GridLayout(3, 1));
        bottomPanel.setOpaque(false);
        currentPlayerLabel = new JLabel("Current Player: ");
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 15));
        currentPlayerLabel.setForeground(textColor);

        statusMessageLabel = new JLabel("Status Message:");
        statusMessageLabel.setFont(new Font("Arial", Font.BOLD, 15));
        statusMessageLabel.setForeground(textColor);

        currentWildColorLabel = new JLabel("Current Wild Color:");
        currentWildColorLabel.setFont(new Font("Arial", Font.BOLD, 15));
        currentWildColorLabel.setForeground(textColor);

        bottomPanel.add(currentPlayerLabel);
        bottomPanel.add(statusMessageLabel);
        bottomPanel.add(currentWildColorLabel);

        add(bottomPanel, BorderLayout.SOUTH);
    }
    /**
     * Sets the top card image and label based on the given card.
     *
     * @param cardImage ImageIcon representing the card's image
     * @param cardName String representing the card's name
     */
    public void setTopCard(ImageIcon cardImage, String cardName) {
        topCardImage.setIcon(cardImage);
        topCardLabel.setText(cardName);
        currentWildColorLabel.setVisible(cardName.equals("WILD WILD_DRAW_TWO") || cardName.equals("WILD WILD") || cardName.equals("WILD WILD_DRAW_COLOR"));
        this.revalidate();
        this.repaint();

        // Force parent to update too
        if(this.getParent() != null){
            this.getParent().revalidate();
            this.getParent().repaint();
        }
    }
    /**
     * sets the current color of the wild color for the next player on the top panel
     *
     * @param wildColor, the current color, null otherwise
     */
    public void setCurrentWildColorLabel(String wildColor) {
        currentWildColorLabel.setText("Current WILD Colour: "+ wildColor);

    }
    /**
     * sets the current player on the top panel
     * @param player, the current player
     */
    public void setCurrentPlayer(Player player) {

        currentPlayerLabel.setText("Current Player: " + player.getName());
        this.repaint();
        this.revalidate();
    }
    /**
     * sets a message to be displayed on the top panel regarding info on what the card placed does.
     * @param message, the message to be displayed
     */
    public void setStatusMessage(String message) {
        statusMessageLabel.setText("Status Message: " + message);
    }
}
