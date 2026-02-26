// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.core;
import game.map.Position;
/**
 * Represents any entity in the game world that has a position on the map
 * and can be displayed or hidden from the player's view.
 */
public interface GameEntity {
    /**
     * Returns the current position of the entity on the game map.
     *
     * @return the Position object representing the entity's location.
     */
    Position getPosition();
    /**
     * Sets a new position for the entity on the game map.
     *
     * @param newPos the new Position to move the entity to.
     */
    boolean setPosition(Position newPos);
    /**
     * Returns the display symbol representing the entity.
     * This can be used in a text-based view or symbolic representation.
     *
     * @return a String representing the entity's display symbol.
     */
    String getDisplaySymbol();
    /**
     * Sets whether the entity is visible to the player.
     *
     * @param visible true if the entity should be visible, false to hide it.
     */
    boolean setVisible(boolean visible);
    /**
     * Checks whether the entity is currently visible to the player.
     *
     * @return true if the entity is visible, false otherwise.
     */
    boolean isVisible();

}
