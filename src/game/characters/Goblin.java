// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.characters;

import game.combat.Combatant;
import game.combat.MeleeFighter;
import game.combat.PhysicalAttacker;
import game.map.Position;

import java.util.Random;

/**
 * Represents a Goblin enemy character.
 * Goblins fight with melee attacks and rely on agility to evade damage.
 */
public class Goblin extends Enemy implements PhysicalAttacker, MeleeFighter {

    private int agility;

    /**
     * Constructs a Goblin with a random agility value (0–80).
     *
     * @param position the goblin's starting position
     */
    public Goblin(Position position) {
        super(position);
        this.agility = new Random().nextInt(81); // 0–80
    }
    public Goblin(int hp, int power, Position position) {
        super(position);
        this.setHealth(hp);
        this.setPower(power);
    }


    /**
     * Returns the goblin's chance to evade based on agility.
     * Formula: min(0.8, agility / 100.0)
     *
     * @return true if evasion is successful
     */
    @Override
    public Boolean tryEvade() {
        double evasionChance = Math.min(0.8, agility / 100.0);
        return Math.random() < evasionChance;
    }

    /**
     * Calculates the goblin's physical attack damage.
     *
     * @return the damage value (equal to power)
     */
    public int calculateDamage() {
        return getPower();
    }

    /**
     * Attacks the target with a melee strike.
     * Damage is equal to the goblin's power.
     *
     * @param target the combatant being attacked
     */
    @Override
    public void attack(Combatant target) {
        int damage = Math.max(1, calculateDamage()); // Ensure minimum 1 damage
        target.receiveDamage(damage, this);
    }


    /**
     * Performs a melee attack on the target.
     *
     * @param target the combatant being attacked
     */
    @Override
    public void fightClose(Combatant target) {
        attack(target);
    }

    /**
     * Checks if a target is in melee range (distance == 1).
     *
     * @param self   the goblin's position
     * @param target the enemy's position
     * @return true if within melee range
     */
    @Override
    public boolean isInMeleeRange(Position self, Position target) {

        return self.distanceTo(target) == 1;
    }

    /**
     * Determines whether the goblin scores a critical hit.
     *
     * @return true if critical hit (10% chance)
     */
    @Override
    public boolean isCriticalHit() {
        return Math.random() < 0.10;
    }

    /**
     * Gets the goblin's agility value.
     *
     * @return agility (0–80)
     */
    public int getAgility() {
        return agility;
    }

    @Override
    public String getDisplaySymbol() {
        return "Goblin";
    }

    @Override
    public String toString() {
        return super.toString() + " | Goblin | Agility: " + agility;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Goblin)) return false;
        Goblin other = (Goblin) obj;
        return super.equals(other) && this.agility == other.agility;
    }
    @Override
    public double getAccuracy() {
        return 0.0;
    }
}
