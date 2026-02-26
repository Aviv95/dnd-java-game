// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.characters;

import game.combat.Combatant;
import game.combat.MagicElement;
import game.combat.PhysicalAttacker;
import game.core.GameEntity;
import game.map.Position;

import java.util.Random;

/**
 * An abstract base class for all characters in the game.
 * Implements basic combat and map behaviors common to both players and enemies.
 */
public abstract class AbstractCharacter implements Combatant, GameEntity {

    private Position position;
    private int health;
    private int power;
    private double evasionChance = 0.25;
    private boolean visible;


    /**
     * Constructs a character with default health and randomized power.
     *
     * @param position the starting position of the character
     */
    public AbstractCharacter(Position position) {
        this.position = position;
        this.health = 100; // default
        this.power = new Random().nextInt(11) + 4; // 4–14
        this.visible = false;
    }

    @Override
    public Position getPosition() {
        return position;
    }
    /**
     * Sets the character's position to a new value.
     *
     * @param newPos the new position
     * @return true if the position was successfully set
     */
    @Override
    public boolean setPosition(Position newPos) {
        if (newPos == null) return false;
        this.position = newPos;
        return true;
    }
    /**
     * Gets the display symbol for the character.
     *
     * @return the display symbol as a string
     */
    @Override
    public String getDisplaySymbol() {
        return "@"; // generic symbol, subclasses should override
    }
    /**
     * Sets the visibility of the character.
     *
     * @param visible true to make the character visible, false otherwise
     * @return true if the visibility was successfully set
     */
    @Override
    public boolean setVisible(boolean visible) {
        this.visible = visible;
        return true;
    }
    /**
     * Checks if the character is visible.
     *
     * @return true if the character is visible, false otherwise
     */
    @Override
    public boolean isVisible() {
        return visible;
    }
    /**
     * Attempts to evade an attack based on the character's evasion chance.
     *
     * @return true if the attack is evaded, false otherwise
     */
    @Override
    public Boolean tryEvade() {
        return Math.random() < evasionChance;
    }
    /**
     * Handles receiving damage from an attacker.
     * If the character successfully evades, no damage is taken.
     *
     * @param amount the amount of damage to be received
     * @param source the source of the attack
     */
    @Override
    public void receiveDamage(int amount, Combatant source) {
        int finalDamage = Math.max(0, amount); // Ensure at least 1 damage
        this.health -= finalDamage;
        if (this.health < 0) this.health = 0;
    }

    /**
     * Heals the character by a specified amount.
     *
     * @param amount the amount of health to restore
     */
    @Override
    public void heal(int amount) {
        if (amount < 0) return;
        this.health = Math.min(100, this.health + amount);
    }
    /**
     * Checks if the character is dead.
     *
     * @return true if the character's health is 0 or less, false otherwise
     */
    @Override
    public boolean isDead() {
        return health <= 0; 
    }
    /**
     * Gets the character's power value.
     *
     * @return the power value
     */
    @Override
    public int getPower() {
        return power;
    }
    /**
     * Gets the character's current health.
     *
     * @return the health value
     */
    @Override
    public int getHealth() {
        return health;
    }
    /**
     * Gets the character's evasion chance.
     *
     * @return the evasion chance as a double
     */
    public double getEvasionChance() {
        return evasionChance;
    }

    /**
     * Sets the character's health to a new value if it's between 0 and 100.
     *
     * @param health the new health value
     * @return true if the value is valid and updated
     */
    @Override
    public boolean setHealth(int health) {
        this.health = Math.max(0, Math.min(health, 100));
        return true;
    }


    /**
     * Sets the character's power.
     *
     * @param power the new power value
     * @return true if the value is valid (positive)
     */
    protected boolean setPower(int power) {
        if (power < 0) return false;
        this.power = power;
        return true;
    }

    /**
     * Sets the evasion chance.
     *
     * @param chance the new evasion chance (between 0 and 1)
     * @return true if value is valid
     */
    protected boolean setEvasionChance(double chance) {
        if (chance < 0 || chance > 1) return false;
        this.evasionChance = chance;
        return true;
    }
    /**
     * Returns a string representation of the character, including its class, position, health, and power.
     *
     * @return a string representation of the character
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + " at " + position + " | HP: " + health + " | Power: " + power;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AbstractCharacter)) return false;
        AbstractCharacter other = (AbstractCharacter) obj;
        return this.health == other.health &&
                this.power == other.power &&
                this.position.equals(other.position);
    }
    /**
     * Determines if the character evades a physical attack from an attacker.
     * The evasion chance is adjusted based on the attacker's accuracy.
     *
     * @param attacker the physical attacker
     * @return true if the attack is evaded, false otherwise
     */
    @Override
    public boolean evadePhysicalAttack(PhysicalAttacker attacker) {
        double adjustedChance = evasionChance * (1 - attacker.getAccuracy());
        return Math.random() < adjustedChance;
    }
    /**
     * Gets the elemental modifier for an attack based on the attacker's element.
     * Default behavior is to return 1.0 (no modifier).
     *
     * @param attackerElement the element of the attack
     * @return the elemental modifier
     */
    @Override
    public double getElementalModifier(MagicElement attackerElement) {
        return 1.0; // default behavior – unaffected by element
    }
}
