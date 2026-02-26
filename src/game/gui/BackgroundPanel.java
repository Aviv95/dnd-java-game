// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.gui;

import javax.swing.*;
import java.awt.*;

/**
 * A custom JPanel that displays a background image.
 * The image is scaled to fit the panel's dimensions.
 */
public class BackgroundPanel extends JPanel {
    private final Image background;

    /**
     * Constructs a BackgroundPanel with the specified image.
     *
     * @param imagePath the path to the background image file, relative to the classpath.
     * @throws IllegalArgumentException if the image file is not found.
     */
    public BackgroundPanel(String imagePath) {
        var url = getClass().getClassLoader().getResource(imagePath);
        if (url == null) {
            throw new IllegalArgumentException("Image not found: " + imagePath);
        }
        background = new ImageIcon(url).getImage();
    }

    /**
     * Paints the background image onto the panel.
     * The image is scaled to fit the panel's width and height.
     *
     * @param g the Graphics object used for painting.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
    }
}