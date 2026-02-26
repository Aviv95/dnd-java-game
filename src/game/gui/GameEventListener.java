// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.gui;

import game.engine.GameWorld;

/**
 * An interface for listening to game state changes.
 * Classes implementing this interface can respond to updates in the game world.
 */
public interface GameEventListener {

    /**
     * Called when the game state changes.
     *
     * @param world the current state of the game world.
     */
    void onGameStateChanged(GameWorld world);
}