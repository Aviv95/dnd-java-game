// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.combat;

import game.core.GameEntity;
import game.items.Treasure;

/**
 * Represents any entity that can participate in combat.
 * This includes having health, power, evasion, and the ability to take or heal damage.
 */
public interface Combatant extends GameEntity {

    int getHealth();

    boolean setHealth(int health);

    void receiveDamage(int amount, Combatant source);

    void heal(int amount);

    boolean isDead();

    int getPower();

    Boolean tryEvade();

    /**
     * Returns the base evasion chance of this entity (e.g., used for evasion calculation).
     *
     * @return base evasion chance between 0.0 and 1.0
     */
    double getEvasionChance();

    /**
     * Called when a physical attacker attempts to hit this combatant.
     * Evasion is adjusted based on the attacker's accuracy.
     *
     * @param attacker the physical attacker
     * @return true if evasion succeeds, false otherwise
     */
    boolean evadePhysicalAttack(PhysicalAttacker attacker);

    double getElementalModifier(MagicElement attackerElement);

     void defeat();



}
