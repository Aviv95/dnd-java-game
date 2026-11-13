package game.combat;

import game.characters.Goblin;
import game.characters.PlayerCharacter;
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
 * Regression test #2 for Bug #2: on a fatal enemy hit the StatusPanel must be refreshed
 * (showing HP 0) BEFORE defeat() runs and shows the Game Over dialog.
 */
class CombatSystemFatalHitTest {

    private static final int START_HP = 5;
    private static final int ENEMY_POWER = 7;

    /** Every interesting event, in the order it happened. */
    private final List<String> events = new ArrayList<>();

    private GameMap map;
    private GameWorld world;
    private RecordingPlayer player;
    private Goblin enemy;
    private RecordingStatusPanel statusPanel;

    /** Real StatusPanel that also records each refresh and the label text it ends up with. */
    private class RecordingStatusPanel extends StatusPanel {
        @Override
        public void updateStatus(PlayerCharacter p) {
            super.updateStatus(p);
            events.add("StatusPanel refreshed: HP=" + p.getHealth() + ", label=\"" + healthLabelText(this) + "\"");
        }
    }

    /** Player whose defeat() only records the call (no dialog, no System.exit). */
    private class RecordingPlayer extends PlayerCharacter {
        RecordingPlayer(Position position) {
            super("TestPlayer", position);
        }

        @Override
        public Boolean tryEvade() {
            return false; // the hit always lands
        }

        @Override
        public void defeat() {
            events.add("defeat() called, label shows \"" + healthLabelText(statusPanel) + "\"");
        }
    }

    @BeforeEach
    void setUp() {
        // GameMap is a Singleton, so start every test from a clean grid.
        map = GameMap.getInstance();
        map.getGrid().clear();
        world = new GameWorld(map);

        player = new RecordingPlayer(new Position(5, 5));
        player.setHealth(START_HP);
        // Goblin.attack() deals exactly its power, which is more than the player's HP.
        enemy = new Goblin(1000, ENEMY_POWER, new Position(5, 6));

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
    void fatalEnemyHitRefreshesStatusPanelBeforeDefeat() {
        assertEquals("Health: " + START_HP, healthLabelText(statusPanel),
                "Precondition: the panel starts out showing the initial HP");

        // Real combat flow: an enemy attacks the player, no click involved.
        CombatSystem.resolveCombat(enemy, player, world);

        System.out.println("=== Event order on the fatal hit ===");
        for (int i = 0; i < events.size(); i++) {
            System.out.println((i + 1) + ". " + events.get(i));
        }

        assertEquals(0, player.getHealth(), "Model HP must reach 0");
        assertTrue(player.isDead());

        assertEquals(2, events.size(), "Expected exactly: one refresh, then defeat()");
        assertEquals("StatusPanel refreshed: HP=0, label=\"Health: 0\"", events.get(0),
                "The panel must be refreshed with HP=0 first");
        assertEquals("defeat() called, label shows \"Health: 0\"", events.get(1),
                "defeat() must run second, while the label already shows Health: 0");
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
