// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.combat;

/**
 * Represents a combatant capable of performing physical attacks.
 * Includes logic for attacking and determining critical hits.
 */
public interface PhysicalAttacker {

    /**
     * Performs a physical attack on the given target.
     * Implementation may involve damage calculation and critical hit check.
     *
     * @param target the combatant being attacked.
     */
    void attack(Combatant target);

    /**
     * Determines whether the current attack is a critical hit.
     * A critical hit typically deals double damage.
     *
     * @return true if the attack is a critical hit, false otherwise.
     */
    boolean isCriticalHit();

    /**
     * Returns the attacker's accuracy, which lowers the target's evasion chance.
     * Default value is 0.0 (no bonus).
     *
     * @return accuracy value between 0.0 and 1.0
     */
    double getAccuracy();

}
