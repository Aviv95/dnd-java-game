// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.engine;

import game.characters.*;
import game.items.*;
import game.map.*;

import java.util.Random;

/**
 * Populates the game map with enemies, walls, and potions
 * based on fixed probabilities:
 * <ul>
 *     <li>40% chance: empty</li>
 *     <li>30% chance: enemy (Goblin, Orc, Dragon)</li>
 *     <li>10% chance: wall</li>
 *     <li>15% chance: healing potion</li>
 *     <li>5% chance: power potion</li>
 * </ul>
 * The player's position is left untouched.
 */
public class MapPopulator {

    /**
     * Fills the game map grid with entities randomly based on probabilities.
     *
     * @param world     the GameWorld object containing the map, items, and enemies
     * @param size      the size of the square map (size x size)
     * @param playerPos the starting position of the player, which should remain empty
     */
    public static void populateMap(GameWorld world, int size, Position playerPos) {
        GameMap map = world.getMap();
        Random rand = new Random();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Position pos = new Position(row, col);

                if (map.isOccupied(pos)) continue; // skip if occupied


                // Do not place anything on the player's position
                if (pos.equals(playerPos)) continue;

                int roll = rand.nextInt(100); // roll between 0–99

                if (roll < 40) {
                    // 0–39: 40% chance → leave empty
                    continue;
                } else if (roll < 70) {
                    // 40–69: 30% chance → enemy
                    int enemyType = rand.nextInt(3); // 0–2
                    Enemy enemy = switch (enemyType) {
                        case 0 -> new Goblin(pos);
                        case 1 -> new Orc(pos);
                        case 2 -> new Dragon(pos);
                        default -> null;
                    };
                    if (enemy != null) {
                        world.addEnemy(enemy);
                        map.addEntity(enemy);
                    }
                } else if (roll < 80) {
                    // 70–79: 10% chance → wall
                    Wall wall = new Wall(pos);
                    world.addItem(wall);
                    map.addEntity(wall);
                } else if (roll < 95) {
                    // 80–94: 15% chance → healing potion
                    Potion potion = new Potion(pos);
                    world.addItem(potion);
                    map.addEntity(potion);
                } else {
                    // 95–99: 5% chance → power potion
                    PowerPotion powerPotion = new PowerPotion(pos);
                    world.addItem(powerPotion);
                    map.addEntity(powerPotion);
                }
            }
        }
    }
}
