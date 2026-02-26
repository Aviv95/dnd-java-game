// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.items;

import game.characters.PlayerCharacter;

/**
 * Represents an interface for objects that can interact with a player character.
 * Classes implementing this interface define specific interaction behavior.
 */
public interface Interactable {

    /**
     * Defines the interaction behavior with a player character.
     *
     * @param c the player character interacting with the object.
     */
    void interact(PlayerCharacter c);

}