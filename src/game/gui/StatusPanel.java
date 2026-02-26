// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.gui;

import game.characters.PlayerCharacter;
import game.engine.GameWorld;
import game.items.GameItem;
import game.map.Position;

import javax.swing.*;
import java.awt.*;

/**
 * The StatusPanel class displays the player's status, including health, power,
 * treasure points, and inventory details. It also provides buttons for using
 * potions and power potions.
 */
public class StatusPanel extends JPanel implements GameEventListener {
    private final JLabel nameLabel;
    private final JLabel healthLabel;
    private final JLabel powerLabel;
    private final JLabel treasureLabel;
    private final JLabel potionsLabel;
    private final JButton usePotionButton;
    private final JButton usePowerPotionButton;
    private PlayerCharacter player;

    /**
     * Constructs a StatusPanel with labels and buttons to display and interact
     * with the player's status.
     */
    public StatusPanel() {
        this.setLayout(new GridLayout(0, 1));
        this.setPreferredSize(new Dimension(200, 100));
        this.setBackground(Color.LIGHT_GRAY);

        nameLabel = new JLabel();
        healthLabel = new JLabel();
        powerLabel = new JLabel();
        treasureLabel = new JLabel();
        potionsLabel = new JLabel();

        usePotionButton = new JButton("Use Potion");
        usePowerPotionButton = new JButton("Use Power Potion");

        usePotionButton.addActionListener(e -> {
            if (player != null && player.usePotion()) {
                updateStatus(player);
            }
        });

        usePowerPotionButton.addActionListener(e -> {
            if (player != null && player.usePowerPotion()) {
                updateStatus(player);
            }
        });

        this.add(nameLabel);
        this.add(healthLabel);
        this.add(powerLabel);
        this.add(treasureLabel);
        this.add(potionsLabel);
        this.add(usePotionButton);
        this.add(usePowerPotionButton);
    }

    /**
     * Updates the status panel with the player's current status.
     *
     * @param p the PlayerCharacter whose status is to be displayed.
     */
    public void updateStatus(PlayerCharacter p) {
        this.player = p;
        nameLabel.setText("Name: " + p.getName());
        healthLabel.setText("Health: " + p.getHealth());
        powerLabel.setText("Power: " + p.getPower());
        treasureLabel.setText("Treasure: " + p.getTreasurePoints());

        long potions = p.getInventory().getItems().stream()
                .filter(i -> i.getClass().getSimpleName().equals("Potion")).count();
        long powerPotions = p.getInventory().getItems().stream()
                .filter(i -> i.getClass().getSimpleName().equals("PowerPotion")).count();
        potionsLabel.setText("Potions: " + potions + " | Power: " + powerPotions);
    }

    /**
     * Called when the game state changes. Updates the player's status if the
     * player is not null.
     *
     * @param world the current game world.
     */
    @Override
    public void onGameStateChanged(GameWorld world) {
        if (player != null) {
            updateStatus(player);
        }
    }
}