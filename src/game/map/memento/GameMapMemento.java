package game.map.memento;

import game.core.GameEntity;
import game.map.Position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMapMemento {
    private final Map<Position, List<GameEntity>> savedGrid;

    public GameMapMemento(Map<Position, List<GameEntity>> currentGrid) {
        // העתק עמוק של המפה למניעת שינויים חיצוניים
        this.savedGrid = deepCopy(currentGrid);
    }

    public Map<Position, List<GameEntity>> getSavedGrid() {
        return deepCopy(savedGrid);
    }

    private Map<Position, List<GameEntity>> deepCopy(Map<Position, List<GameEntity>> original) {
        Map<Position, List<GameEntity>> copy = new HashMap<>();
        for (Map.Entry<Position, List<GameEntity>> entry : original.entrySet()) {
            List<GameEntity> copiedEntities = new ArrayList<>(entry.getValue()); // אם צריך – לשכפל אובייקטים עצמם
            copy.put(entry.getKey().copy(), copiedEntities);
        }
        return copy;
    }
}
