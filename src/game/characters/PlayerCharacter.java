package game.characters;

import game.combat.Combatant;
import game.core.Inventory;
import game.engine.GameLogger;
import game.items.GameItem;
import game.items.Potion;
import game.items.PowerPotion;
import game.map.Position;

import javax.swing.*;

/**
 * Represents a player-controlled character in the game.
 * Holds inventory and treasure points, and can use items.
 */
public class PlayerCharacter extends AbstractCharacter {

    private String name;
    private Inventory inventory;
    private int treasurePoints;

    /**
     * Constructs a new PlayerCharacter with the given name and position.
     *
     * @param name     the name of the player
     * @param position the starting position of the player
     */
    public PlayerCharacter(String name, Position position) {
        super(position);
        this.name = name;
        this.inventory = new Inventory();
        this.treasurePoints = 0;
    }

    /**
     * Returns the name of the player.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Adds an item to the player's inventory.
     *
     * @param item the item to add
     * @return true if the item was added, false if the item is null
     */
    public boolean addToInventory(GameItem item) {
        if (item == null) return false;
        inventory.addItem(item);
        GameLogger.log("Player picked up item: " + item.getClass().getSimpleName());
        return true;
    }

    /**
     * Uses a regular potion from the inventory if available.
     *
     * @return true if a potion was used, false otherwise
     */
    public boolean usePotion() {
        for (GameItem item : inventory.getItems()) {
            if (item instanceof Potion p && !(p instanceof PowerPotion)) {
                p.interact(this);
                inventory.removeItem(p);
                GameLogger.log("Player used Potion.");
                return true;
            }
        }
        return false;
    }

    /**
     * Uses a power potion from the inventory if available.
     *
     * @return true if a power potion was used, false otherwise
     */
    public boolean usePowerPotion() {
        for (GameItem item : inventory.getItems()) {
            if (item instanceof PowerPotion p) {
                p.interact(this);
                inventory.removeItem(p);
                GameLogger.log("Player used PowerPotion.");
                return true;
            }
        }
        return false;
    }

    /**
     * Updates the player's treasure points by the specified amount.
     *
     * @param amount the amount to add (can be negative)
     * @return true if the treasure was updated, false if amount is zero
     */
    public boolean updateTreasurePoint(int amount) {
        if (amount == 0) return false;
        this.treasurePoints += amount;
        GameLogger.log("Player treasure updated by " + amount + ". Total: " + treasurePoints);
        return true;
    }

    /**
     * Returns the current treasure points of the player.
     *
     * @return the treasure points
     */
    public int getTreasurePoints() {
        return treasurePoints;
    }

    /**
     * Increases the player's power by the specified amount.
     *
     * @param amount the amount to increase
     * @return true if power was increased, false if amount is not positive
     */
    public boolean increasePower(int amount) {
        if (amount <= 0) return false;
        GameLogger.log("Player increased power by " + amount);
        return setPower(getPower() + amount);
    }

    /**
     * Receives damage from a combat source, applying evasion if possible.
     *
     * @param amount the amount of damage
     * @param source the source of the damage
     */
    @Override
    public void receiveDamage(int amount, Combatant source) {
        if (tryEvade()) {
            GameLogger.log("Player evaded attack from " + source.getDisplaySymbol());
            return;
        }

        int finalDamage = Math.max(1, amount);
        int newHealth = getHealth() - finalDamage;
        newHealth = Math.max(0, newHealth);
        setHealth(newHealth);

        GameLogger.log("Player received " + finalDamage + " damage from " + source.getDisplaySymbol() + ". HP now: " + newHealth);
    }

    /**
     * Returns the display symbol for the player (first letter of name, uppercase).
     *
     * @return the display symbol
     */
    @Override
    public String getDisplaySymbol() {
        return name.substring(0, 1).toUpperCase();
    }

    /**
     * Returns a string representation of the player.
     *
     * @return string with player details
     */
    @Override
    public String toString() {
        return super.toString() + " | Player: " + name + " | Treasure: " + treasurePoints;
    }

    /**
     * Checks if this player is equal to another object.
     *
     * @param obj the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PlayerCharacter)) return false;
        PlayerCharacter other = (PlayerCharacter) obj;
        return super.equals(other) &&
                this.name.equals(other.name) &&
                this.treasurePoints == other.treasurePoints;
    }

    /**
     * Handles player defeat, shows a game over dialog, and exits the game.
     */
    @Override
    public void defeat() {
        GameLogger.log("Player defeated. Final treasure: " + getTreasurePoints());
        JOptionPane.showMessageDialog(null,
                "Game Over!\nYour treasure: " + getTreasurePoints(),
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE);

        System.exit(0);
    }

    /**
     * Returns the player's inventory.
     *
     * @return the inventory
     */
    public Inventory getInventory() {
        return inventory;
    }
}