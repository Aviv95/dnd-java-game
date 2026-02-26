// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.characters;

import game.combat.Combatant;
import game.combat.PhysicalAttacker;
import game.combat.RangedFighter;
import game.map.Position;

import java.util.Random;

/**
 * Represents an Archer player character.
 * Archers deal physical ranged damage and have an accuracy stat
 * that lowers the target's chance to evade.
 */
public class Archer extends PlayerCharacter implements PhysicalAttacker, RangedFighter {

    private double accuracy; // 0 to 0.8

    /**
     * Constructs an Archer with a given name and position.
     * Accuracy is randomized between 0 and 0.8.
     *
     * @param name     the archer's name
     * @param position starting position
     */
    public Archer(String name, Position position) {
        super(name, position);
        this.accuracy = new Random().nextDouble() * 0.8;
    }

    public Archer(String name, int hp, int power, Position position) {
        super(name, position);
        this.setHealth(hp);
        this.setPower(power);
    }


    /**
     * Performs a ranged physical attack on the target.
     * If the target evades based on its evasion logic, no damage is dealt.
     * Accuracy lowers the target's evasion chance.
     *
     * @param target the target being attacked
     */
    @Override
    public void attack(Combatant target) {
        if (target.evadePhysicalAttack(this)) {
            return; // evaded
        }

        int damage = getPower();
        if (isCriticalHit()) {
            damage *= 2;
        }
        target.receiveDamage(damage, this);
    }

    @Override
    public void fightRanged(Combatant target) {
        attack(target);
    }

    @Override
    public int getRange() {
        return 2;
    }

    @Override
    public boolean isInRange(Position self, Position target) {
        return self.distanceTo(target) <= 2;
    }

    @Override
    public boolean isCriticalHit() {
        return Math.random() < 0.10;
    }

    /**
     * Returns the archer's accuracy value (0–0.8).
     *
     * @return accuracy level
     */
    @Override
    public double getAccuracy() {
        return accuracy;
    }

    @Override
    public String getDisplaySymbol() {
        return "Archer";
    }

    @Override
    public String toString() {
        return super.toString() + " | Class: Archer | Accuracy: " + String.format("%.2f", accuracy);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Archer)) return false;
        Archer other = (Archer) obj;
        return super.equals(other) && Double.compare(this.accuracy, other.accuracy) == 0;
    }
}
