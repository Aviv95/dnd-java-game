// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.core;

import game.items.GameItem;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player's inventory for storing game items.
 * Provides methods for adding, removing, and retrieving items.
 */
public class Inventory {

    private List<GameItem> items;

    /**
     * Constructs an empty inventory.
     */
    public Inventory() {
        this.items = new ArrayList<>();
    }

    /**
     * Adds an item to the inventory.
     *
     * @param item the item to add
     */
    public boolean addItem(GameItem item) {
        if (item == null) return false;
        return items.add(item);
    }

    /**
     * Removes an item from the inventory.
     *
     * @param item the item to remove
     */

    public boolean removeItem(GameItem item) {
        if (item == null) return false;
        return items.remove(item);
    }

    /**
     * Returns the list of items currently in the inventory.
     *
     * @return the list of GameItems
     */
    public List<GameItem> getItems() {
        return new ArrayList<>(items); // return a copy to preserve encapsulation
    }

    @Override
    public String toString() {
        return "Inventory: " + items;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Inventory)) return false;
        Inventory other = (Inventory) obj;
        return items.equals(other.items);
    }
}
