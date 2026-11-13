package game.combat;

import game.characters.Goblin;
import game.characters.Warrior;
import game.engine.GameWorld;
import game.gui.GameController;
import game.gui.MapPanel;
import game.gui.StatusPanel;
import game.map.GameMap;
import game.map.Position;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Regression test for Bug #2: the player's health display stayed stale after an
 * enemy hit until the player clicked something.
 */
class CombatSystemTest {

    private static final int START_HP = 50;
    private static final int ENEMY_POWER = 7;

    /** Warrior that never evades, so the hit always lands (no randomness). */
    private static class NonEvadingWarrior extends Warrior {
        NonEvadingWarrior(String name, int hp, int power, Position position) {
            super(name, hp, power, position);
        }

        @Override
        public Boolean tryEvade() {
            return false;
        }
    }

    private GameMap map;
    private GameWorld world;
    private NonEvadingWarrior player;
    private Goblin enemy;
    private StatusPanel statusPanel;

    @BeforeEach
    void setUp() {
        // GameMap is a Singleton, so start every test from a clean grid.
        map = GameMap.getInstance();
        map.getGrid().clear();
        world = new GameWorld(map);

        // Defense is 0 for this constructor, so the damage taken equals the enemy's power.
        player = new NonEvadingWarrior("TestWarrior", START_HP, 10, new Position(5, 5));
        // Goblin.attack() deals exactly its power; high HP keeps it alive after any counterattack.
        enemy = new Goblin(1000, ENEMY_POWER, new Position(5, 6));

        world.addPlayer(player);
        map.addEntity(player);
        world.addEnemy(enemy);
        map.addEntity(enemy);

        // The real wiring: GameController registers the StatusPanel on the map.
        statusPanel = new StatusPanel();
        MapPanel mapPanel = new MapPanel(10, map);
        new GameController(world, map, player, mapPanel, statusPanel);
    }

    @AfterEach
    void tearDown() {
        map.getGrid().clear();
    }

    @Test
    void nonFatalEnemyHitUpdatesStatusPanelWithoutPlayerClick() {
        assertEquals("Health: " + START_HP, healthLabelText(statusPanel),
                "Precondition: the panel starts out showing the initial HP");

        // An enemy attacks the player; no click, no controller call.
        CombatSystem.resolveCombat(enemy, player, world);

        int expectedHp = START_HP - ENEMY_POWER;
        assertEquals(expectedHp, player.getHealth(), "Model HP must drop by the enemy's power");
        assertFalse(player.isDead(), "This must be a non-fatal hit");
        assertEquals("Health: " + expectedHp, healthLabelText(statusPanel),
                "The StatusPanel must show the new HP right after the hit");
    }

    private static String healthLabelText(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JLabel label && label.getText() != null && label.getText().startsWith("Health:")) {
                return label.getText();
            }
            if (c instanceof Container inner) {
                String found = healthLabelText(inner);
                if (found != null) return found;
            }
        }
        return null;
    }
}
