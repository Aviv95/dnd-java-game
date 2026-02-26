// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.items;

import game.characters.PlayerCharacter;
import game.map.Position;

import java.util.Random;

/**
 * Represents a healing potion that can be used by a character to restore health.
 * A potion can be used once. After use, it becomes inactive.
 */
public class Potion extends GameItem implements Interactable {

    private int increaseAmount; // value between 10–50
    private boolean isUsed;

    /**
     * Constructs a new healing potion at the given position.
     * Healing amount is randomized between 10 and 50.
     *
     * @param position the location of the potion on the map
     */
    public Potion(Position position) {
        super(position, false, "Healing Potion");
        this.increaseAmount = new Random().nextInt(41) + 10; // 10–50
        this.isUsed = false;
    }

    /**
     * Heals the character by the potion's healing value, if not already used.
     * Once used, the potion becomes inactive and cannot be reused.
     *
     * @param c the character interacting with the potion
     */
    @Override
    public void interact(PlayerCharacter c) {
        if (!isUsed && c != null) {
            c.heal(increaseAmount);
            isUsed = true;
        }
    }

    /**
     * Gets the healing amount of this potion.
     *
     * @return the number of health points this potion restores
     */
    public int getIncreaseAmount() {
        return increaseAmount;
    }

    /**
     * Checks whether the potion has already been used.
     *
     * @return true if the potion is used, false otherwise
     */
    public boolean isUsed() {
        return isUsed;
    }

    /**
     * Returns the display symbol for the potion.
     *
     * @return the character 'P' as a symbolic representation
     */
    @Override
    public String getDisplaySymbol() {
        return "P";
    }

    /**
     * Returns a string representation of the potion,
     * including its usage state and healing amount.
     *
     * @return a human-readable string describing the potion
     */
    @Override
    public String toString() {
        return super.toString() + (isUsed ? " (used)" : " (heals " + increaseAmount + ")");
    }

    /**
     * Checks if this potion is equal to another object.
     * Two potions are equal if they have the same parent data,
     * healing amount, and usage state.
     *
     * @param obj the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Potion)) return false;
        Potion other = (Potion) obj;
        return super.equals(other) &&
                this.increaseAmount == other.increaseAmount &&
                this.isUsed == other.isUsed;
    }

    /**
     * Sets the used status of the potion.
     * Typically called by subclasses such as PowerPotion.
     *
     * @param used true if the potion was used
     * @return true (always succeeds)
     */
    protected boolean setUsed(boolean used) {
        this.isUsed = used;
        return true;
    }

    /**
     * Sets the healing or power-boost value of the potion.
     *
     * @param amount the new amount
     * @return true if the value is positive, false otherwise
     */
    protected boolean setIncreaseAmount(int amount) {
        if (amount <= 0) return false;
        this.increaseAmount = amount;
        return true;
    }

    /**
     * Updates the potion's description.
     *
     * @param desc the new description text
     * @return true if the description is non-null and not blank
     */
    protected boolean setDescription(String desc) {
        if (desc == null || desc.trim().isEmpty()) return false;
        super.setDescription(desc);
        return true;
    }
}
