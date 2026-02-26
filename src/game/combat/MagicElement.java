// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.combat;

/**
 * Represents a magical element used in combat.
 * Each element has strengths and weaknesses against others.
 */
public enum MagicElement {
    FIRE,
    ICE,
    LIGHTNING,
    ACID;

    /**
     * Determines if this element is stronger than the other.
     *
     * <ul>
     *     <li>FIRE > ICE</li>
     *     <li>ICE > LIGHTNING</li>
     *     <li>LIGHTNING > ACID</li>
     *     <li>ACID > FIRE</li>
     * </ul>
     *
     * @param other the other element to compare against
     * @return true if this element is stronger, false otherwise
     */
    public boolean isStrongerThan(MagicElement other) {
        return switch (this) {
            case FIRE -> other == ICE;
            case ICE -> other == LIGHTNING;
            case LIGHTNING -> other == ACID;
            case ACID -> other == FIRE;
        };
    }
}
