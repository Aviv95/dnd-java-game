// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.combat;
import game.items.Treasure;

public interface LootDropper {

    /**
     * Generates a treasure to be dropped.
     *
     * @return a Treasure object with the appropriate value and position
     */
    Treasure generateLoot();
}
