package game.engine;

import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.factories.EnemyFactory;
import game.map.GameMap;
import game.map.Position;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class EnemyThreadPoolManager {

    private static final int MIN_THREADS = 1;
    private static final int MAX_THREADS = 10;
    private static final double THREAD_RATIO = 0.03;

    private final ExecutorService threadPool;
    private final Set<Enemy> activeEnemies = ConcurrentHashMap.newKeySet();
    private final PlayerCharacter player;
    private final GameMap map;
    private final GameWorld world;
    private final AtomicBoolean isRunning = new AtomicBoolean(true);
    private final Random random = new Random();

    public EnemyThreadPoolManager(PlayerCharacter player, GameMap map, GameWorld world) {
        this.player = player;
        this.map = map;
        this.world = world;

        int poolSize = calculatePoolSize(map);
        this.threadPool = Executors.newFixedThreadPool(poolSize);
    }

    public void start() {
        for (int i = 0; i < ((ThreadPoolExecutor) threadPool).getCorePoolSize(); i++) {
            spawnNewEnemy();
        }
    }

    public void stop() {
        isRunning.set(false);
        threadPool.shutdownNow();
    }

    private void spawnNewEnemy() {
        Position pos = getRandomFreePosition();
        if (pos == null) return;

        Enemy enemy = EnemyFactory.createEnemy(pos, map);
        map.addEntity(enemy);
        activeEnemies.add(enemy);

        EnemyThread task = new EnemyThread(enemy, player, map, world, isRunning) {
            @Override
            public void run() {
                super.run(); // הפעולה המקורית
                activeEnemies.remove(enemy);

                if (isRunning.get() && !threadPool.isShutdown()) {
                    spawnNewEnemy(); // ברגע שאויב מת – תוולד משימה חדשה
                }
            }
        };

        threadPool.submit(task);
    }

    private int calculatePoolSize(GameMap map) {
        int mapSize = map.getGrid().size();
        return Math.max(MIN_THREADS, Math.min(MAX_THREADS, (int) Math.floor(mapSize * THREAD_RATIO)));
    }

    private Position getRandomFreePosition() {
        // לוגיקה פשוטה למציאת מיקום פנוי (אפשר לשפר לפי הצורך)
        for (int i = 0; i < 100; i++) {
            int row = random.nextInt(10);
            int col = random.nextInt(10);
            Position pos = new Position(row, col);
            if (!map.isOccupied(pos)) {
                return pos;
            }
        }
        return null;
    }
}
