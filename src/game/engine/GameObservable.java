package game.engine;

import game.gui.GameEventListener;
import java.util.ArrayList;
import java.util.List;

/**
 * A base class that allows listeners (views/controllers) to be notified of game state changes.
 */
public class GameObservable {

    private final List<GameEventListener> listeners = new ArrayList<>();

    public void addGameEventListener(GameEventListener listener) {
        listeners.add(listener);
    }

    public void removeGameEventListener(GameEventListener listener) {
        listeners.remove(listener);
    }

    protected void notifyGameStateChanged(GameWorld world) {
        for (GameEventListener listener : listeners) {
            listener.onGameStateChanged(world);
        }
    }
}
