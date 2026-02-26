// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.characters;

import game.combat.LootDropper;
import game.items.Treasure;
import game.map.Position;

import java.util.Random;

/**
 * Represents a base class for enemy characters in the game.
 * Enemies have limited health and can drop treasure upon defeat.
 */
public class Enemy extends AbstractCharacter implements LootDropper {

    private int loot;

    /**
     * Constructs an Enemy with random health (0–50) and loot value (100–300).
     *
     * @param position the position of the enemy on the map
     */
    public Enemy(Position position) {
        super(position);
        if (!setHealth(new Random().nextInt(51))) {
            this.setHealth(30); // default health if random fails
        }
        // 0–50
        this.loot = new Random().nextInt(201) + 100; // 100–300
    }

    /**
     * Returns the amount of loot this enemy will drop if defeated.
     *
     * @return the loot value
     */
    public int getLoot() {
        return loot;
    }


    /**
     * Called when the enemy is defeated.
     */
    public void defeat() {
        System.out.println(getClass().getSimpleName() + " defeated at " + getPosition());
    }

    /**
     * Generates treasure containing the enemy's loot value.
     *
     * @return a new Treasure at the enemy's position
     */
    @Override
    public Treasure generateLoot() {
        return new Treasure(getPosition(), loot);
    }

    @Override
    public String getDisplaySymbol() {
        return "E";
    }

    @Override
    public String toString() {
        return super.toString() + " | Enemy | Loot: " + loot;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Enemy)) return false;
        Enemy other = (Enemy) obj;
        return super.equals(other) && this.loot == other.loot;
    }
    public boolean isDead() {
        return getHealth() <= 0;
    }


}
