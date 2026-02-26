// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.characters;

import game.combat.Combatant;
import game.combat.MagicAttacker;
import game.combat.MeleeFighter;
import game.combat.PhysicalAttacker;
import game.combat.RangedFighter;
import game.combat.MagicElement;
import game.map.Position;

import java.util.Random;

/**
 * Represents the Dragon boss enemy.
 * The Dragon can attack with both magic and physical damage, at melee or ranged distance.
 */
public class Dragon extends Enemy implements PhysicalAttacker, MagicAttacker, MeleeFighter, RangedFighter {

    private MagicElement element;

    /**
     * Constructs a Dragon with a random magic element.
     *
     * @param position the dragon's starting position
     */
    public Dragon(Position position) {
        super(position);
        this.element = MagicElement.values()[new Random().nextInt(MagicElement.values().length)];
    }

    public Dragon(int hp, int power, Position position) {
        super(position); // Initialize the Dragon with specific health and power
        this.setHealth(hp);
        this.setPower(power);
    }


    /**
     * Gets the magic element of the Dragon.
     *
     * @return the magic element
     */
    @Override
    public MagicElement getElement() {
        return element;
    }
    /**
     * Determines if the Dragon's element is stronger than another magic attacker's element.
     *
     * @param other the other magic attacker
     * @return true if the Dragon's element is stronger, false otherwise
     */
    @Override
    public boolean isElementStrongerThan(MagicAttacker other) {
        return this.element.isStrongerThan(other.getElement());
    }
    /**
     * Calculates and applies magic damage to a target.
     *
     * @param target the target to receive magic damage
     */
    @Override
    public void calculateMagicDamage(Combatant target) {
        double baseDamage = getPower() * 1.5;
        double modifier = target.getElementalModifier(this.element);
        int finalDamage = (int)Math.round(baseDamage * modifier);
        finalDamage = Math.max(1, finalDamage); // Ensure at least 1 damage
        target.receiveDamage(finalDamage, this);
    }
    /**
     * Casts a spell on a target, dealing magic damage.
     *
     * @param target the target to cast the spell on
     */
    @Override
    public void castSpell(Combatant target) {
        calculateMagicDamage(target);
    }
    /**
     * Attacks a target, choosing between melee or ranged combat based on distance.
     *
     * @param target the target to attack
     */
    @Override
    public void attack(Combatant target) {
        if (isInMeleeRange(getPosition(), target.getPosition())) {
            fightClose(target);
        } else if (isInRange(getPosition(), target.getPosition())) {
            fightRanged(target);
        }
    }
    /**
     * Performs a melee attack on a target.
     *
     * @param target the target to attack
     */
    @Override
    public void fightClose(Combatant target) {
        int damage = getPower();
        if (isCriticalHit()) damage *= 2;
        if (damage <= 0) damage = 1;
        target.receiveDamage(damage, this);
    }

    @Override
    public void fightRanged(Combatant target) {
        castSpell(target);
    }

    @Override
    public boolean isInMeleeRange(Position self, Position target) {
        return self.distanceTo(target) == 1;
    }

    @Override
    public boolean isInRange(Position self, Position target) {
        return self.distanceTo(target) <= getRange();
    }

    @Override
    public int getRange() {
        return 2;
    }

    @Override
    public boolean isCriticalHit() {
        return Math.random() < 0.10;
    }

    @Override
    public String getDisplaySymbol() {
        return "Dragon";
    }

    @Override
    public String toString() {
        return super.toString() + " | Dragon | Element: " + element;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Dragon)) return false;
        Dragon other = (Dragon) obj;
        return super.equals(other) && this.element == other.element;
    }

    /**
     * Gets the Dragon's accuracy for physical attacks.
     *
     * @return the accuracy value
     */
    @Override
    public double getAccuracy() {
        return 0.0;
    }

}
