// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.items;

import game.map.Position;

/**
 * Represents an impassable wall on the game map.
 * Walls block movement and do not support interaction.
 */
public class Wall extends GameItem {

    /**
     * Constructs a wall at the specified position.
     *
     * @param position the wall's location on the map
     */
    public Wall(Position position) {
        super(position, true, "Wall");
    }

    /**
     * Returns the display symbol for a wall.
     *
     * @return the symbol 'W'
     */
    @Override
    public String getDisplaySymbol() {
        return "W";
    }

    /**
     * Returns a string representation of the wall.
     *
     * @return string with class name and position
     */
    @Override
    public String toString() {
        return "Wall at " + getPosition();
    }

    /**
     * Compares this wall with another object for equality.
     *
     * @param obj the object to compare
     * @return true if both are walls at the same position
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Wall)) return false;
        return super.equals(obj);
    }
}
