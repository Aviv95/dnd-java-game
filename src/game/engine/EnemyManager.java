package game.engine;

import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.map.GameMap;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * EnemyManager: Responsible for managing and launching EnemyThreads.
 * Holds the isRunning flag to control all enemy threads globally.
 */
public class EnemyManager {

    private final List<Enemy> enemies;
    private final ExecutorService executor;
    private final AtomicBoolean isRunning;
    private final PlayerCharacter player;
    private final GameMap map;
    private final GameWorld world;

    public EnemyManager(List<Enemy> enemies, PlayerCharacter player, GameMap map, GameWorld world) {
        this.enemies = enemies;
        this.player = player;
        this.map = map;
        this.world = world;
        this.executor = Executors.newCachedThreadPool();
        this.isRunning = new AtomicBoolean(true);
    }

    /**
     * Starts all enemy threads.
     */
    public void startAll() {
        for (Enemy enemy : enemies) {
            executor.submit(new EnemyThread(enemy, player, map, world, isRunning));
        }
    }

    /**
     * Stops all enemy threads by flipping the running flag and shutting down the executor.
     */
    public void stopAll() {
        isRunning.set(false);
        executor.shutdownNow();
    }

    /**
     * Returns the shared isRunning flag (optional accessor).
     */
    public AtomicBoolean getIsRunning() {
        return isRunning;
    }
}
