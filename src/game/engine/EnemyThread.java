package game.engine;

import game.characters.Enemy;
import game.characters.PlayerCharacter;
import game.combat.CombatSystem;
import game.map.GameMap;
import game.map.Position;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * EnemyThread: Represents the AI logic of a single enemy.
 * Runs in a loop, performing periodic actions such as movement or attack.
 */
public class EnemyThread implements Runnable {

    private final Enemy enemy;
    private final PlayerCharacter player;
    private final GameMap map;
    private final GameWorld world;
    private final AtomicBoolean isRunning;
    private final Random random = new Random();

    private static final int VISION_RANGE = 5;
    private static final int MELEE_RANGE = 1;

    public EnemyThread(Enemy enemy, PlayerCharacter player, GameMap map, GameWorld world, AtomicBoolean isRunning) {
        this.enemy = enemy;
        this.player = player;
        this.map = map;
        this.world = world;
        this.isRunning = isRunning;
    }

    @Override
    public void run() {
        while (isRunning.get() && !enemy.isDead()) {
            try {
                long delay = 500 + random.nextInt(1000);
                Thread.sleep(delay);

                Position enemyPos = enemy.getPosition();
                Position playerPos = player.getPosition();

                int distance = enemyPos.distanceTo(playerPos);

                if (distance <= VISION_RANGE) {
                    if (distance <= MELEE_RANGE) {
                        ReentrantLock playerLock = world.getPlayerLock();
                        if (playerLock.tryLock()) {
                            try {
                                GameLogger.log("Enemy at " + enemyPos + " attacks player at " + playerPos);
                                CombatSystem.resolveCombat(enemy, player, world);
                            } finally {
                                playerLock.unlock();
                            }
                        } else {
                            GameLogger.log("Enemy at " + enemyPos + " wanted to attack but player is busy.");
                        }
                    } else {
                        Position nextStep = calculateStepTowards(enemyPos, playerPos);
                        if (nextStep != null && tryMoveWithLock(nextStep)) {
                            GameLogger.log("Enemy at " + enemyPos + " moved toward player to " + nextStep);
                        }
                    }
                } else {
                    if (random.nextInt(100) < 20) {
                        Position randomMove = getRandomAdjacentPosition(enemyPos);
                        if (randomMove != null && tryMoveWithLock(randomMove)) {
                            GameLogger.log("Enemy at " + enemyPos + " moved randomly to " + randomMove);
                        }
                    } else {
                        GameLogger.log("Enemy at " + enemyPos + " idles.");
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                GameLogger.log("Enemy thread interrupted.");
            }
        }

        GameLogger.log("Enemy thread stopped for enemy at " + enemy.getPosition());
    }

    private boolean tryMoveWithLock(Position targetPos) {
        boolean locked = map.tryLockPosition(targetPos, 50);
        if (!locked) return false;

        try {
            synchronized (map) {
                if (!map.isOccupied(targetPos)) {
                    map.removeEntity(enemy);
                    enemy.setPosition(targetPos);
                    map.addEntity(enemy);
                    world.notifyGameStateChanged(world);
                    return true;
                }
            }
        } finally {
            map.unlockPosition(targetPos);
        }
        return false;
    }

    private Position calculateStepTowards(Position from, Position to) {
        int dRow = Integer.compare(to.getRow(), from.getRow());
        int dCol = Integer.compare(to.getCol(), from.getCol());
        return new Position(from.getRow() + dRow, from.getCol() + dCol);
    }

    private Position getRandomAdjacentPosition(Position pos) {
        List<Position> options = List.of(
                new Position(pos.getRow() + 1, pos.getCol()),
                new Position(pos.getRow() - 1, pos.getCol()),
                new Position(pos.getRow(), pos.getCol() + 1),
                new Position(pos.getRow(), pos.getCol() - 1)
        );
        return options.get(random.nextInt(options.size()));
    }
}
