// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.items;
import game.characters.PlayerCharacter;
import game.map.Position;

import java.util.Random;

/**
 * Represents a treasure object in the game that may grant potions or treasure points.
 * Each treasure can be interacted with only once.
 */
public class Treasure extends GameItem implements Interactable{
    private int value;
    private boolean collected;

    /**
     * Constructs a Treasure object with a random value between 100 and 300.
     *
     * @param position the treasure's position on the map
     */
    public Treasure(Position position) {
        super(position, false, "Shiny Treasure");
        this.value = new Random().nextInt(201) + 100; // 100–300
        this.collected = false;
    }

    /**
     * Constructs a Treasure object with a predefined value.
     * This constructor is typically used when an enemy is defeated
     * and drops a treasure with a specific loot amount.
     *
     * @param position the location on the map where the treasure appears
     * @param value    the amount of treasure points this treasure holds
     */

    public Treasure(Position position, int value) {
        super(position, false, "Shiny Treasure");
        this.value = value;
        this.collected = false;
    }

    /**
     * Interacts with the treasure. If it hasn't been collected yet, it will apply one of the following:
     * - 1/3 chance: adds a normal potion to the inventory
     * - 1/2 chance: adds treasure points to the player
     * - 1/6 chance: adds a power potion to the inventory
     *
     * @param c the player character collecting the treasure
     */
    @Override
    public void interact(PlayerCharacter c) {
        if (collected || c == null) return;

        Random rand = new Random();
        int roll = rand.nextInt(6); // 0 to 5

        switch (roll) {
            case 0:
            case 1:
                Potion potion = new Potion(getPosition());
                c.addToInventory(potion);
                System.out.println("You found a healing potion! It was added to your inventory.");
                break;

            case 2:
            case 3:
            case 4:
                c.updateTreasurePoint(value);
                System.out.println("You gained " + value + " treasure points!");
                break;

            case 5:
                PowerPotion powerPotion = new PowerPotion(getPosition());
                c.addToInventory(powerPotion);
                System.out.println("You found a power potion! It was added to your inventory.");
                break;
        }

        collected = true;
    }



    public int getValue() {
        return value;
    }

    public boolean isCollected() {
        return collected;
    }

    @Override
    public String getDisplaySymbol() {
        return "T";
    }

    @Override
    public String toString() {
        return "Treasure at " + getPosition() +
                (collected ? " (collected)" : " (worth " + value + " points)");
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Treasure)) return false;
        Treasure other = (Treasure) obj;
        return super.equals(other) &&
                this.value == other.value &&
                this.collected == other.collected;
    }

}
