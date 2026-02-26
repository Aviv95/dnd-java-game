// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.characters;

import game.combat.*;
import game.map.Position;

import java.util.Random;

/**
 * Represents an Orc enemy character.
 * Orcs are melee fighters with natural resistance to magic damage.
 */
public class Orc extends Enemy implements PhysicalAttacker, MeleeFighter {

    private double resistance; // 0 to 0.5

    /**
     * Constructs an Orc with a random resistance between 0 and 0.5.
     *
     * @param position the orc's starting position
     */
    public Orc(Position position) {
        super(position);
        this.resistance = new Random().nextDouble() * 0.5;
    }

    public Orc(int hp, int power, Position position) {
        super(position);
        this.setHealth(hp);
        this.setPower(power);
    }

    /**
     * Returns whether the attack is a critical hit (10% chance).
     *
     * @return true if critical hit, false otherwise
     */
    @Override
    public boolean isCriticalHit() {
        return Math.random() < 0.10;
    }

    /**
     * Calculates the orc's base attack damage.
     *
     * @return the damage (equal to power)
     */
    public int calculateDamage() {
        return getPower();
    }

    /**
     * Performs a physical attack on the target.
     *
     * @param target the combatant being attacked
     */
    @Override
    public void attack(Combatant target) {
        int damage = calculateDamage();
        if (isCriticalHit()) {
            damage *= 2;
        }
        target.receiveDamage(damage, this);
    }

    /**
     * Handles incoming damage, reducing magic damage using resistance.
     *
     * @param amount the base damage amount
     * @param source the attacker
     */
    @Override
    public void receiveDamage(int amount, Combatant source) {
        if (tryEvade()) return;

        int finalDamage = amount;

        if (source instanceof MagicAttacker) {
            finalDamage = (int) Math.round(amount * (1 - resistance));
        }

        finalDamage = Math.max(1, finalDamage);
        int newHealth = getHealth() - finalDamage;
        setHealth(Math.max(0, newHealth));
        System.out.println("Orc received " + finalDamage + " damage. New HP: " + getHealth());    }


    /**
     * Performs a melee attack.
     *
     * @param target the combatant being attacked
     */
    @Override
    public void fightClose(Combatant target) {
        attack(target);
    }

    /**
     * Checks whether a target is within melee range.
     *
     * @param self   the orc's position
     * @param target the target's position
     * @return true if within range
     */
    @Override
    public boolean isInMeleeRange(Position self, Position target) {

        return self.distanceTo(target) == 1;
    }

    /**
     * Returns the orc's resistance to magic damage.
     *
     * @return resistance value (0–0.5)
     */
    public double getResistance() {
        return resistance;
    }

    @Override
    public String getDisplaySymbol() {
        return "Orc";
    }

    @Override
    public String toString() {
        return super.toString() + " | Orc | Resistance: " + String.format("%.2f", resistance);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Orc)) return false;
        Orc other = (Orc) obj;
        return super.equals(other) &&
                Double.compare(this.resistance, other.resistance) == 0;
    }
    @Override
    public double getElementalModifier(MagicElement attackerElement) {
        return 1.0 - resistance;
    }
    @Override
    public double getAccuracy() {
        return 0.0;
    }
}
