// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.items;

import game.characters.PlayerCharacter;
import game.map.Position;

import java.util.Random;

/**
 * A special potion that increases a character's power instead of healing.
 * PowerPotions are single-use and cannot be reused once consumed.
 */
public class PowerPotion extends Potion {

    /**
     * Constructs a PowerPotion with a random power boost between 1 and 5.
     *
     * @param position the location of the power potion on the map
     */
    public PowerPotion(Position position) {
        super(position);
        super.setIncreaseAmount(new Random().nextInt(5) + 1); // range: 1–5
        super.setDescription("Power Potion");
    }

    /**
     * Increases the character's power by the potion's amount, if it hasn't been used yet.
     *
     * @param c the character using the potion
     */

    @Override
    public void interact(PlayerCharacter c) {
        if (!isUsed() && c != null) {
            if (c.increasePower(getIncreaseAmount())) {
                setUsed(true);
            }
        }
    }



    /**
     * Returns the display symbol for this item on the map.
     *
     * @return the symbol representing a power potion
     */
    @Override
    public String getDisplaySymbol() {
        return "⚡"; // Or "PP" if ASCII only
    }

    /**
     * Returns a string representation of the PowerPotion.
     *
     * @return a string including description, position, and boost amount
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + " at " + getPosition() +
                (isUsed() ? " (used)" : " (power +" + getIncreaseAmount() + ")");
    }

    /**
     * Checks equality with another object.
     *
     * @param obj the object to compare to
     * @return true if both objects are equal
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PowerPotion)) return false;
        return super.equals(obj);
    }
}
