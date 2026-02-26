// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.combat;

/**
 * Represents a combatant capable of performing magical attacks.
 * Includes logic for casting spells, determining elemental affinity,
 * and calculating magic damage.
 */
public interface MagicAttacker {

    /**
     * Calculates and applies magic damage to the given target,
     * based on this attacker's magic power and elemental advantage.
     *
     * If this attacker's element is stronger than the target's,
     * the damage may be increased accordingly.
     *
     * @param target the combatant to receive the magic damage.
     */
    void calculateMagicDamage(Combatant target);

    /**
     * Casts a spell on the given target.
     * The behavior may include applying damage or status effects.
     *
     * @param target the target combatant.
     */
    void castSpell(Combatant target);

    /**
     * Returns the magical element of this attacker.
     * Used for elemental comparison and damage calculation.
     *
     * @return the attacker's MagicElement.
     */
    MagicElement getElement();

    /**
     * Determines whether this attacker's element is stronger than another's.
     *
     * Elemental hierarchy is defined in MagicElement.
     *
     * @param other the other MagicAttacker to compare against.
     * @return true if this attacker's element is stronger.
     */
    boolean isElementStrongerThan(MagicAttacker other);
}
