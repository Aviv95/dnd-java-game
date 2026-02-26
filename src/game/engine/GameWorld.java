// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.engine;

import game.characters.PlayerCharacter;
import game.characters.Enemy;
import game.combat.LootDropper;
import game.items.GameItem;
import game.items.Treasure;
import game.map.GameMap;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents the central world state of the game.
 * Stores all active players, enemies, items, and the game map.
 *
 * This class acts as the core model in the MVC architecture.
 * It notifies listeners whenever the state changes using the observer pattern.
 *
 * In addition, all significant changes to the world are logged to file using GameLogger.
 */
public class GameWorld extends GameObservable {

    private List<PlayerCharacter> players;
    private List<Enemy> enemies;
    private List<GameItem> items;
    private GameMap map;

    // Lock to synchronize enemy attacks on the player
    private final ReentrantLock playerLock = new ReentrantLock(true);

    public GameWorld(GameMap map) {
        this.players = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.items = new ArrayList<>();
        this.map = map;
        GameLogger.log("GameWorld initialized with empty state and map.");
    }

    public List<PlayerCharacter> getPlayers() {
        return players;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<GameItem> getItems() {
        return items;
    }

    public GameMap getMap() {
        return map;
    }

    public ReentrantLock getPlayerLock() {
        return playerLock;
    }

    public boolean addPlayer(PlayerCharacter p) {
        if (p == null) return false;
        boolean added = players.add(p);
        if (added) {
            GameLogger.log("Player added to world: " + p.getName());
            notifyGameStateChanged(this);
        }
        return added;
    }

    public boolean addEnemy(Enemy e) {
        if (e == null) return false;
        boolean added = enemies.add(e);
        if (added) {
            GameLogger.log("Enemy added to world at position: " + e.getPosition());
            notifyGameStateChanged(this);
        }
        return added;
    }

    public boolean addItem(GameItem item) {
        if (item == null) return false;
        boolean added = items.add(item);
        if (added) {
            GameLogger.log("Item added to world: " + item.getClass().getSimpleName());
            notifyGameStateChanged(this);
        }
        return added;
    }

    public boolean removeEnemy(Enemy e) {
        boolean removed = enemies.remove(e);
        if (removed) {
            GameLogger.log("Enemy removed from world at position: " + e.getPosition());
            notifyGameStateChanged(this);
        }
        return removed;
    }

    public boolean removeItem(GameItem item) {
        boolean removed = items.remove(item);
        if (removed) {
            GameLogger.log("Item removed from world: " + item.getClass().getSimpleName());
            notifyGameStateChanged(this);
        }
        return removed;
    }

    public boolean handleLootDrop(LootDropper source) {
        if (source == null) return false;
        Treasure t = source.generateLoot();
        if (t == null) return false;

        boolean added = addItem(t);
        map.addEntity(t);
        GameLogger.log("Treasure dropped at: " + t.getPosition());
        return added;
    }

    @Override
    public String toString() {
        return "GameWorld with " + players.size() + " player(s), " +
                enemies.size() + " enemy(ies), " +
                items.size() + " item(s).";
    }
}
