package game.engine;

import game.characters.Enemy;
import game.characters.Goblin;
import game.characters.PlayerCharacter;
import game.core.GameEntity;
import game.map.GameMap;
import game.map.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Regression test for bug #1: enemies could move outside the map boundaries.
 */
class EnemyThreadTest {

    private static final int MAP_SIZE = 10;

    private GameMap map;
    private GameWorld world;
    private Enemy enemy;
    private EnemyThread enemyThread;

    @BeforeEach
    void setUp() {
        // GameMap is a Singleton, so start every test from a clean grid.
        map = GameMap.getInstance();
        map.getGrid().clear();

        world = new GameWorld(map);

        // Player is far from the enemy; it is only a constructor dependency here.
        PlayerCharacter player = new PlayerCharacter("TestPlayer", new Position(0, 0));

        enemy = new Goblin(10, 5, new Position(9, 5));
        map.addEntity(enemy);

        // Never started as a thread - the test calls the movement method directly.
        enemyThread = new EnemyThread(enemy, player, map, world, new AtomicBoolean(true), MAP_SIZE);
    }

    @AfterEach
    void tearDown() {
        map.getGrid().clear();
    }

    @Test
    void moveOutsideMapIsRejectedAndEnemyStaysInPlace() {
        Position outside = new Position(10, 5);

        boolean moved = enemyThread.tryMoveWithLock(outside);

        assertFalse(moved, "Movement outside the map must be rejected");

        // The enemy itself did not move.
        assertEquals(new Position(9, 5), enemy.getPosition());

        // The map still holds the enemy at (9,5), exactly once.
        List<GameEntity> atStart = map.getEntitiesAt(new Position(9, 5));
        assertEquals(1, atStart.size());
        assertSame(enemy, atStart.get(0));

        // Nothing was placed outside the map.
        assertTrue(map.getEntitiesAt(outside).isEmpty());
        for (Map.Entry<Position, List<GameEntity>> entry : map.getGrid().entrySet()) {
            if (entry.getValue().isEmpty()) continue;
            Position p = entry.getKey();
            assertTrue(p.getRow() >= 0 && p.getRow() < MAP_SIZE
                            && p.getCol() >= 0 && p.getCol() < MAP_SIZE,
                    "Entity found outside the map at " + p);
        }
    }

    @Test
    void moveInsideMapStillWorks() {
        // Control: proves the test above does not pass just because every move is rejected.
        Position inside = new Position(8, 5);

        assertTrue(enemyThread.tryMoveWithLock(inside));
        assertEquals(inside, enemy.getPosition());
    }
}
