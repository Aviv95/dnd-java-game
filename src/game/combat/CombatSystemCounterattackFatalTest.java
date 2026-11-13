package game.combat;

import game.characters.Goblin;
import game.characters.PlayerCharacter;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Regression test #3 for Bug #2: when the player attacks, the enemy survives and its
 * counterattack kills the player, the StatusPanel must show Health: 0 BEFORE defeat() runs.
 */
class CombatSystemCounterattackFatalTest {

    private static final int PLAYER_START_HP = 5;
    private static final int PLAYER_DAMAGE = 1;
    private static final int ENEMY_START_HP = 20;
    private static final int ENEMY_DAMAGE = 5;

    /** Every interesting event, in the order it happened. */
    private final List<String> events = new ArrayList<>();

    private GameMap map;
    private GameWorld world;
    private TestPlayer player;
    private TestEnemy enemy;
    private RecordingStatusPanel statusPanel;

    /** Real StatusPanel that also records each refresh and the label text it ends up with. */
    private class RecordingStatusPanel extends StatusPanel {
        @Override
        public void updateStatus(PlayerCharacter p) {
            super.updateStatus(p);
            events.add("StatusPanel refreshed: HP=" + p.getHealth() + ", label=\"" + healthLabelText(this) + "\"");
        }
    }

    /** Warrior with no randomness: never evades, never crits, deals exactly PLAYER_DAMAGE. */
    private class TestPlayer extends Warrior {
        TestPlayer(Position position) {
            super("TestPlayer", PLAYER_START_HP, PLAYER_DAMAGE, position);
        }

        @Override
        public Boolean tryEvade() {
            return false;
        }

        @Override
        public boolean isCriticalHit() {
            return false;
        }

        @Override
        public void attack(Combatant target) {
            events.add("Player attacks, dealing " + PLAYER_DAMAGE);
            super.attack(target);
        }

        /** Records the call instead of showing the dialog and calling System.exit(0). */
        @Override
        public void defeat() {
            events.add("defeat() called, label shows \"" + healthLabelText(statusPanel) + "\"");
        }
    }

    /** Goblin with no randomness: never evades, deals exactly ENEMY_DAMAGE. */
    private class TestEnemy extends Goblin {
        TestEnemy(Position position) {
            super(ENEMY_START_HP, ENEMY_DAMAGE, position);
        }

        @Override
        public Boolean tryEvade() {
            return false;
        }

        @Override
        public boolean isCriticalHit() {
            return false;
        }

        @Override
        public void attack(Combatant target) {
            events.add("Enemy counterattacks, dealing " + ENEMY_DAMAGE);
            super.attack(target);
        }
    }

    @BeforeEach
    void setUp() {
        // GameMap is a Singleton, so start every test from a clean grid.
        map = GameMap.getInstance();
        map.getGrid().clear();
        world = new GameWorld(map);

        player = new TestPlayer(new Position(5, 5));
        enemy = new TestEnemy(new Position(5, 6));

        world.addPlayer(player);
        map.addEntity(player);
        world.addEnemy(enemy);
        map.addEntity(enemy);

        // The real wiring: GameController registers the StatusPanel on the map.
        statusPanel = new RecordingStatusPanel();
        MapPanel mapPanel = new MapPanel(10, map);
        new GameController(world, map, player, mapPanel, statusPanel);

        // Ignore the refreshes done while the GUI was being built.
        events.clear();
    }

    @AfterEach
    void tearDown() {
        map.getGrid().clear();
    }

    @Test
    void fatalCounterattackRefreshesStatusPanelBeforeDefeat() {
        assertEquals("Health: " + PLAYER_START_HP, healthLabelText(statusPanel),
                "Precondition: the panel starts out showing the initial HP");

        // Real combat flow: the player attacks the adjacent enemy (what a click triggers),
        // the enemy survives and its counterattack kills the player.
        CombatSystem.resolveCombat(player, enemy, world);

        System.out.println("=== Event order on the fatal counterattack ===");
        for (int i = 0; i < events.size(); i++) {
            System.out.println((i + 1) + ". " + events.get(i));
        }

        // The scenario really is the counterattack path: the enemy survived and the player died.
        assertEquals(ENEMY_START_HP - PLAYER_DAMAGE, enemy.getHealth());
        assertFalse(enemy.isDead(), "The enemy must survive the player's hit");
        assertEquals(0, player.getHealth(), "Model HP must reach 0");
        assertTrue(player.isDead());

        assertEquals(List.of(
                "Player attacks, dealing " + PLAYER_DAMAGE,
                "Enemy counterattacks, dealing " + ENEMY_DAMAGE,
                "StatusPanel refreshed: HP=0, label=\"Health: 0\"",
                "defeat() called, label shows \"Health: 0\""
        ), events, "The panel must be refreshed with HP=0 before defeat() runs");
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
