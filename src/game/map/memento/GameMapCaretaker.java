package game.map.memento;

import game.map.GameMap;

import java.util.ArrayDeque;
import java.util.Deque;

public class GameMapCaretaker {
    private static final int MAX_HISTORY = 10; // defines the maximum number of saved states
    private final Deque<GameMapMemento> history = new ArrayDeque<>();

    public void save(GameMap map) {
        if (history.size() >= MAX_HISTORY) {
            history.removeLast(); // deletes the oldest state if history is full
        }
        history.push(map.saveState()); // pushes the current state onto the stack
    }

    public void undo(GameMap map) {
        if (!history.isEmpty()) {
            GameMapMemento previous = history.pop(); // extracts the last saved state
            map.restoreState(previous); // restores the map to the previous state
        }
    }

    public boolean hasHistory() {
        return !history.isEmpty();
    }
}
