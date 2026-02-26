// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.combat;

import game.map.Position;

/**
 * Represents a fighter capable of performing ranged attacks.
 * Includes logic for executing ranged attacks and checking if a target is within range.
 */
public interface RangedFighter {

    /**
     * Executes a ranged attack on the given target.
     * This should only be used if the target is within ranged attack distance.
     *
     * @param target the combatant being attacked.
     */
    void fightRanged(Combatant target);

    /**
     * Returns the attack range of the fighter.
     * This determines how far the fighter can strike from their current position.
     *
     * @return the range value as an integer (default expected to be 2).
     */
    int getRange();

    /**
     * Checks whether the target is within ranged attack distance.
     * Typically calculated using Manhattan distance.
     *
     * @param self the position of the attacker.
     * @param target the position of the target.
     * @return true if the target is within range, false otherwise.
     */
    boolean isInRange(Position self, Position target);
}
