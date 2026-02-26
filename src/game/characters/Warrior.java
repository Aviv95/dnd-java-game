// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.characters;

import game.combat.Combatant;
import game.combat.MeleeFighter;
import game.combat.PhysicalAttacker;
import game.map.Position;

import java.util.Random;

/**
 * Represents a Warrior player character.
 * Warriors are melee fighters that rely on physical attacks
 * and use their defense stat to reduce incoming damage.
 */
public class Warrior extends PlayerCharacter implements MeleeFighter, PhysicalAttacker {

    private int defense;

    /**
     * Constructs a new Warrior with the given name and position.
     * Defense is initialized randomly between 0 and 120.
     *
     * @param name     the warrior's name
     * @param position the starting position on the map
     */
    public Warrior(String name, Position position) {
        super(name, position);
        this.defense = new Random().nextInt(121); // 0–120
    }

    public Warrior(String name, int hp, int power, Position position) {
        super(name, position); // ✅ תואם לקונסטרקטור של PlayerCharacter
        this.setHealth(hp);
        this.setPower(power);
    }



    /**
     * Calculates the damage after applying the warrior's defense.
     * The reduction factor is: min(0.6, defense / 200.0), up to 60%.
     *
     * @param rawDamage the incoming damage before defense
     * @return the adjusted damage after defense is applied
     */
    private int calculateReducedDamage(int rawDamage) {
        double reductionFactor = Math.min(0.6, defense / 200.0);
        return (int) Math.round(rawDamage * (1 - reductionFactor));
    }

    /**
     * Handles receiving damage, factoring in evasion and defense.
     * If evasion fails, the damage is reduced based on defense.
     *
     * @param amount the raw damage received
     * @param source the attacker
     */
    @Override
    public void receiveDamage(int amount, Combatant source) {
        if (tryEvade()) return;
        int finalDamage = calculateReducedDamage(amount);
        setHealth(getHealth() - finalDamage);
    }

    /**
     * Performs a physical attack on a target.
     * If a critical hit occurs (10% chance), damage is doubled.
     *
     * @param target the combatant being attacked
     */
    @Override
    public void attack(Combatant target) {
        int damage = getPower();
        if (isCriticalHit()) {
            damage *= 2;
        }
        damage = Math.max(1, damage); // Ensure at least 1 damage
        target.receiveDamage(damage, this);
    }

    /**
     * Determines whether the attack is a critical hit.
     * 10% chance to return true.
     *
     * @return true if critical hit, false otherwise
     */
    @Override
    public boolean isCriticalHit() {
        return Math.random() < 0.10;
    }

    /**
     * Executes a melee attack on the target using the attack() method.
     *
     * @param target the combatant being attacked
     */
    @Override
    public void fightClose(Combatant target) {
        attack(target);
    }

    /**
     * Checks whether the target is within melee range.
     * Melee range is defined as a Manhattan distance of exactly 1.
     *
     * @param self   the position of this character
     * @param target the position of the target
     * @return true if within melee range, false otherwise
     */
    @Override
    public boolean isInMeleeRange(Position self, Position target) {
        return self.distanceTo(target) == 1;
    }

    /**
     * Returns the warrior's defense value.
     *
     * @return the defense stat
     */
    public int getDefense() {
        return defense;
    }

    /**
     * Returns the display symbol for this character on the map.
     *
     * @return the character "W" representing Warrior
     */
    @Override
    public String getDisplaySymbol() {
        return "Warrior";
    }

    /**
     * Returns a string representation of the Warrior, including name, health, power, and defense.
     *
     * @return a string representing the warrior's state
     */
    @Override
    public String toString() {
        return super.toString() + " | Class: Warrior | DEF: " + defense;
    }

    /**
     * Checks if this Warrior is equal to another object.
     * Two warriors are equal if they share the same name, stats, and defense.
     *
     * @param obj the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Warrior)) return false;
        Warrior other = (Warrior) obj;
        return super.equals(other) && this.defense == other.defense;
    }
    @Override
    public double getAccuracy() {
        return 0.0;
    }
}
