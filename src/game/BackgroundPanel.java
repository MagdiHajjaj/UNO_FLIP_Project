package game;

import javax.swing.*;
import java.awt.*;
/**
 * A panel that shows either the normal background or the flip background.
 * Used in UNO Flip to change the board when a flip card is played.
 */
public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    private Image flipBackgroundImage;
    private boolean flipped;
    /**
     * Creates the panel and loads both background images.
     *
     * @param imagePath path to the normal background image
     * @param panelWidth width to scale the images
     * @param panelHeight height to scale the images
     */
    public BackgroundPanel(String flipImagePath,String imagePath, int panelWidth, int panelHeight) {
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        backgroundImage = icon.getImage().getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);

        ImageIcon flipIcon = new ImageIcon(getClass().getResource(flipImagePath));
        flipBackgroundImage = flipIcon.getImage().getScaledInstance(panelWidth, panelHeight, Image.SCALE_SMOOTH);

        flipped = false;
        setLayout(new BorderLayout());
    }
    /**
     * Draws the correct background depending on whether the panel is flipped.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (flipped) {
            g.drawImage(flipBackgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    /**
     * Switches between the normal and flip backgrounds.
     */
    public void flip() {
        flipped = !flipped;
        repaint();
    }
}

