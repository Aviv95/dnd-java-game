// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.combat;

import game.map.Position;

/**
 * Represents a fighter capable of engaging in close-range (melee) combat.
 * Includes logic for initiating melee attacks and checking distance to the target.
 */
public interface MeleeFighter {

    /**
     * Executes a melee attack on the given target.
     * This should only be used if the target is within melee range.
     *
     * @param target the combatant being attacked.
     */
    void fightClose(Combatant target);

    /**
     * Checks whether the target is within melee attack range.
     * By default, melee range is considered to be a Manhattan distance of 1.
     *
     * @param self the position of the attacker.
     * @param target the position of the target.
     * @return true if the target is within melee range, false otherwise.
     */
    boolean isInMeleeRange(Position self, Position target);
}
