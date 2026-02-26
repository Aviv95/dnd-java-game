// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.items;

import game.core.GameEntity;
import game.map.Position;

import java.util.Objects;

/**
 * Represents a basic item in the game world.
 * Implements {@link GameEntity} and supports position, description,
 * movement blocking behavior, and visibility status.
 * <p>
 * This class serves as a base class for all items, including potions, walls, and treasures.
 */
public class GameItem implements GameEntity {

    private Position position;
    private boolean blocksMovement;
    private String description;
    private boolean visible;

    /**
     * Constructs a GameItem with the given position, movement blocking flag, and description.
     *
     * @param position       the item's location on the map
     * @param blocksMovement true if the item blocks movement on the map
     * @param description    a short textual description of the item
     */
    public GameItem(Position position, boolean blocksMovement, String description) {
        this.position = position;
        this.blocksMovement = blocksMovement;
        this.description = description;
        this.visible = false; // default state: hidden
    }

    /**
     * Returns the item's current position on the map.
     *
     * @return the item's position
     */
    @Override
    public Position getPosition() {
        return position;
    }

    /**
     * Updates the item's position on the map, if the new position is not null.
     *
     * @param newPos the new position to set
     * @return true if the position was updated, false if newPos is null
     */
    @Override
    public boolean setPosition(Position newPos) {
        if (newPos == null) return false;
        this.position = newPos;
        return true;
    }

    /**
     * Returns the symbol that represents the item when drawn on the map.
     * Subclasses may override this to provide custom symbols.
     *
     * @return a string representing the item (default: "?")
     */
    @Override
    public String getDisplaySymbol() {
        return "?";
    }

    /**
     * Sets whether the item is visible to the player.
     *
     * @param visible true to make the item visible, false to hide it
     * @return true (always succeeds)
     */
    @Override
    public boolean setVisible(boolean visible) {
        this.visible = visible;
        return true;
    }

    /**
     * Returns whether the item is currently visible to the player.
     *
     * @return true if the item is visible, false otherwise
     */
    @Override
    public boolean isVisible() {
        return visible;
    }

    /**
     * Indicates whether this item blocks player movement.
     *
     * @return true if movement into the item's cell is blocked
     */
    public boolean isBlocking() {
        return blocksMovement;
    }

    /**
     * Returns the textual description of the item.
     *
     * @return the item's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Updates the description of the item.
     * Used primarily by subclasses such as PowerPotion.
     *
     * @param description the new description to assign
     * @return true if the description is valid (non-null and not blank), false otherwise
     */
    protected boolean setDescription(String description) {
        if (description == null || description.trim().isEmpty()) return false;
        this.description = description;
        return true;
    }

    /**
     * Returns a string representation of the item,
     * including its type, position, and description.
     *
     * @return a string describing the item
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + " at " + position + " - " + description;
    }

    /**
     * Checks equality with another object.
     * Two GameItems are equal if they have the same position, description,
     * and blocking behavior.
     *
     * @param obj the object to compare to
     * @return true if the items are considered equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof GameItem)) return false;
        GameItem other = (GameItem) obj;
        return Objects.equals(position, other.position) &&
                Objects.equals(description, other.description) &&
                blocksMovement == other.blocksMovement;
    }
}
