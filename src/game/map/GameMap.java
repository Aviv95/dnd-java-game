package game.map;

import game.characters.PlayerCharacter;
import game.core.GameEntity;
import game.items.GameItem;
import game.map.memento.GameMapMemento;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class GameMap {

    private static GameMap instance;

    private Map<Position, List<GameEntity>> grid;

    private final List<Runnable> listeners = new ArrayList<>();

    // === Singleton constructor ===
    private GameMap() {
        this.grid = new HashMap<>();
    }


    public void addListener(Runnable listener) {
        listeners.add(listener);
    }

    public void notifyListeners() {
        for (Runnable listener : listeners) {
            listener.run();
        }
    }
    // === Singleton accessor ===
    public static synchronized GameMap getInstance() {
        if (instance == null) {
            instance = new GameMap();
        }
        return instance;
    }

    public boolean addEntity(GameEntity entity) {
        if (entity == null || entity.getPosition() == null) return false;
        Position pos = entity.getPosition();
        grid.computeIfAbsent(pos, k -> new ArrayList<>()).add(entity);
        return true;
    }

    public boolean removeEntity(GameEntity entity) {
        if (entity == null || entity.getPosition() == null) return false;
        List<GameEntity> entities = grid.get(entity.getPosition());
        return entities != null && entities.remove(entity);
    }

    public List<GameEntity> getEntitiesAt(Position pos) {
        return grid.getOrDefault(pos, new ArrayList<>());
    }

    public boolean isOccupied(Position pos) {
        return grid.containsKey(pos) && !grid.get(pos).isEmpty();
    }

    public Set<Position> getAllOccupiedPositions() {
        return grid.keySet();
    }

    public Map<Position, List<GameEntity>> getGrid() {
        return grid;
    }

    public boolean canMoveTo(Position pos, PlayerCharacter player) {
        List<GameEntity> entities = getEntitiesAt(pos);

        for (GameEntity entity : entities) {
            if (entity instanceof GameItem item && item.isBlocking()) {
                return false;
            }

            if (entity instanceof game.combat.Combatant combatant && combatant != player) {
                return false;
            }
        }

        return true;
    }

    public boolean tryLockPosition(Position pos, long timeoutMillis) {
        return pos.tryLock(timeoutMillis);
    }

    public void unlockPosition(Position pos) {
        pos.unlock();
    }

    @Override
    public String toString() {
        return "GameMap with " + grid.size() + " active positions.";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof GameMap)) return false;
        GameMap other = (GameMap) obj;
        return Objects.equals(this.grid, other.grid);
    }

    public GameMapMemento saveState() {
        return new GameMapMemento(this.grid);
    }

    public void restoreState(GameMapMemento memento) {
        this.grid.clear(); // שלב 1: נקה את המפה הקודמת

        Map<Position, List<GameEntity>> restored = memento.getSavedGrid();

        // שלב 2: החזר את כל הישויות המשוחזרות
        for (Map.Entry<Position, List<GameEntity>> entry : restored.entrySet()) {
            Position pos = entry.getKey();
            for (GameEntity entity : entry.getValue()) {
                entity.setPosition(pos); // ודא שהאובייקט מכיר את המיקום שלו
                this.addEntity(entity); // הוסף מחדש למפה
            }
        }

        notifyListeners(); // שלב 3: עדכן תצוגה
    }


}
