package game;

import javax.swing.*;
import java.awt.*;
public class TopPanel extends JPanel {
    private JLabel topCardLabel;
    private JLabel topCardImage;
    private JLabel currentPlayerLabel;
    private JLabel statusMessageLabel;

    public TopPanel() {
        setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        // Section title
        JLabel title = new JLabel("Top Card");
        title.setFont(new Font("Arial", Font.BOLD, 14));
        add(title, BorderLayout.NORTH);

        JPanel cardPanel = new JPanel();

        topCardImage = new JLabel();
        topCardLabel = new JLabel();
        topCardLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        cardPanel.add(topCardImage);
        cardPanel.add(topCardLabel);
        add(cardPanel, BorderLayout.CENTER);

        // Bottom status section
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));
        currentPlayerLabel = new JLabel("Current Player: ");
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 12));

        statusMessageLabel = new JLabel("Status Message:");
        statusMessageLabel.setFont(new Font("Arial", Font.BOLD, 12));

        bottomPanel.add(currentPlayerLabel);
        bottomPanel.add(statusMessageLabel);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    public void setTopCard(ImageIcon cardImage, String cardName) {
        topCardImage.setIcon(cardImage);
        topCardLabel.setText(cardName);
    }

    public void setCurrentPlayer(String name) {
        currentPlayerLabel.setText("Current Player: " + name);
    }

    public void setStatusMessage(String message) {
        statusMessageLabel.setText("Status Message: " + message);
    }
}
