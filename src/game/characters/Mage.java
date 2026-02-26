// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.characters;

import game.combat.Combatant;
import game.combat.MagicAttacker;
import game.combat.RangedFighter;
import game.combat.MagicElement;
import game.map.Position;

/**
 * Represents a Mage player character that casts elemental spells from range.
 */
public class Mage extends PlayerCharacter implements MagicAttacker, RangedFighter {

    private MagicElement element;

    /**
     * Constructs a Mage with the given name, position, and random element.
     *
     * @param name     the mage's name
     * @param position starting position on the map
     */
    public Mage(String name, Position position) {
        super(name, position);
        this.element = MagicElement.values()[(int)(Math.random() * MagicElement.values().length)];
    }

    public Mage(String name, int hp, int power, Position position) {
        super(name, position);
        this.setHealth(hp);
        this.setPower(power);
    }


    /**
     * Returns the mage's element type.
     *
     * @return the magic element of the mage
     */
    @Override
    public MagicElement getElement() {
        return element;
    }

    /**
     * Determines if this mage's element is stronger than the opponent's.
     *
     * @param other the opponent mage
     * @return true if this element is stronger, false otherwise
     */
    @Override
    public boolean isElementStrongerThan(MagicAttacker other) {
        return this.element.isStrongerThan(other.getElement());
    }

    /**
     * Calculates the magic damage to inflict on the target.
     * Uses element comparison to adjust damage.
     *
     * @param target the target to attack
     */
    @Override
    public void calculateMagicDamage(Combatant target) {
        double baseDamage = getPower() * 1.5;

        if (target instanceof MagicAttacker) {
            MagicAttacker enemy = (MagicAttacker) target;

            if (this.isElementStrongerThan(enemy)) {
                baseDamage *= 1.2;
            } else if (enemy.isElementStrongerThan(this)) {
                baseDamage *= 0.8;
            }
        }

        target.receiveDamage((int) baseDamage, this);
    }

    /**
     * Casts a spell on the target.
     *
     * @param target the target to attack
     */
    @Override
    public void castSpell(Combatant target) {
        calculateMagicDamage(target);
    }

    /**
     * Executes a ranged magic attack using castSpell().
     *
     * @param target the target to attack
     */
    @Override
    public void fightRanged(Combatant target) {
        castSpell(target);
    }

    /**
     * Returns the range of the mage's attacks.
     *
     * @return fixed range value of 2
     */
    @Override
    public int getRange() {
        return 2;
    }

    /**
     * Checks whether a target is within magic attack range.
     *
     * @param self   the position of the mage
     * @param target the position of the enemy
     * @return true if within range, false otherwise
     */
    @Override
    public boolean isInRange(Position self, Position target) {
        return self.distanceTo(target) <= 2;
    }

    /**
     * Returns the mage's symbol for display on the map.
     *
     * @return the character 'M'
     */
    @Override
    public String getDisplaySymbol() {
        return "Mage";
    }

    /**
     * Returns a string representation of the mage.
     *
     * @return string with name, stats, and element
     */
    @Override
    public String toString() {
        return super.toString() + " | Class: Mage | Element: " + element;
    }

    /**
     * Checks equality with another object.
     *
     * @param obj the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Mage)) return false;
        Mage other = (Mage) obj;
        return super.equals(other) && this.element == other.element;
    }

    @Override
    public void receiveDamage(int amount, Combatant source) {
        if (tryEvade()) return;

        int finalDamage = amount;

        if (source instanceof MagicAttacker attacker) {
            double modifier = getElementalModifier(attacker.getElement());
            finalDamage = (int) Math.round(amount * modifier);
        }

        finalDamage = Math.max(1, finalDamage);
        setHealth(getHealth() - finalDamage);
    }


    @Override
    public double getElementalModifier(MagicElement attackerElement) {
        if (attackerElement.isStrongerThan(this.element)) return 1.2;
        if (this.element.isStrongerThan(attackerElement)) return 0.8;
        return 1.0;
    }

}
