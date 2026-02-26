package game.gui;

import game.characters.*;
import game.combat.CombatSystem;
import game.combat.MeleeFighter;
import game.combat.RangedFighter;
import game.core.GameEntity;
import game.engine.GameLogger;
import game.engine.GameWorld;
import game.items.*;
import game.map.GameMap;
import game.map.Position;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import java.awt.*;

/**
 * The GameController class manages the interaction between the player, the game world,
 * and the graphical user interface. It handles player actions, updates the game state,
 * and notifies listeners of changes. Also logs significant player actions.
 */
public class GameController implements MapPanel.MapClickListener {

    private final GameWorld world;
    private final GameMap gameMap;
    private final PlayerCharacter player;
    private final MapPanel mapPanel;
    private final StatusPanel statusPanel;
    private final List<GameEventListener> listeners = new ArrayList<>();

    /**
     * Constructs a GameController to manage the game state and UI.
     *
     * @param world the game world model
     * @param gameMap the map of the game
     * @param player the player character
     * @param mapPanel the panel displaying the map
     * @param statusPanel the panel displaying player status
     */
    public GameController(GameWorld world, GameMap gameMap, PlayerCharacter player,
                          MapPanel mapPanel, StatusPanel statusPanel) {
        this.world = world;
        this.gameMap = gameMap;
        this.player = player;
        this.mapPanel = mapPanel;
        this.statusPanel = statusPanel;

        this.mapPanel.setClickListener(this);
        mapPanel.refresh(player.getPosition());
        statusPanel.updateStatus(player);

        listeners.add(mapPanel);
        listeners.add(statusPanel);

        notifyListeners();
    }

    /**
     * Notifies all registered listeners that the game state has changed.
     */
    private void notifyListeners() {
        for (GameEventListener listener : listeners) {
            listener.onGameStateChanged(world);
        }
    }

    /**
     * Handles left mouse clicks on the map panel.
     * Moves the player, picks up items, or initiates combat depending on the clicked cell.
     *
     * @param row the row of the clicked cell
     * @param col the column of the clicked cell
     */
    @Override
    public void onLeftClick(int row, int col) {
        Position target = new Position(row, col);
        boolean isAdjacent = isWithinMoveRange(player.getPosition(), target);

        for (GameEntity entity : gameMap.getEntitiesAt(target)) {
            if (entity instanceof GameItem item && item.isBlocking()) {
                mapPanel.highlightCell(target, Color.ORANGE);
                return;
            }
        }

        for (GameEntity entity : gameMap.getEntitiesAt(target)) {
            if (entity instanceof Enemy enemy) {
                boolean inMelee = player instanceof MeleeFighter mf && mf.isInMeleeRange(player.getPosition(), target);
                boolean inRange = player instanceof RangedFighter rf && rf.isInRange(player.getPosition(), target);

                if (inMelee || inRange) {
                    CombatSystem.resolveCombat(player, enemy, world);
                    if (!enemy.isDead()) {
                        CombatSystem.resolveCombat(enemy, player, world);
                    }
                    if (enemy.isDead()) {
                        world.removeEnemy(enemy);
                        gameMap.removeEntity(enemy);
                        mapPanel.highlightCell(target, Color.RED);
                        mapPanel.setPlayerPosition(player.getPosition());
                        notifyListeners();
                    }
                    notifyListeners();
                    return;
                } else {
                    mapPanel.highlightCell(target, Color.ORANGE);
                    return;
                }
            }

            if (entity instanceof Potion || entity instanceof PowerPotion) {
                if (player.addToInventory((GameItem) entity)) {
                    world.removeItem((GameItem) entity);
                    gameMap.removeEntity(entity);
                    mapPanel.highlightCell(target, Color.GREEN);
                }
                mapPanel.setPlayerPosition(player.getPosition());
                notifyListeners();
                return;
            }

            if (entity instanceof GameItem item && item instanceof Interactable interactable) {
                interactable.interact(player);
                world.removeItem(item);
                gameMap.removeEntity(item);
                mapPanel.highlightCell(target, Color.GREEN);
                notifyListeners();
                return;
            }
        }

        if (isAdjacent) {
            Position from = player.getPosition();
            gameMap.removeEntity(player);
            player.setPosition(target);
            gameMap.addEntity(player);
            mapPanel.setPlayerPosition(target);
            GameLogger.log("Player moved from " + from + " to " + target);
            notifyListeners();
        }
    }

    /**
     * Handles right mouse clicks on the map panel.
     * Shows a popup with information about entities at the clicked cell.
     *
     * @param row the row of the clicked cell
     * @param col the column of the clicked cell
     * @param invoker the component that triggered the popup
     * @param x the x coordinate for the popup
     * @param y the y coordinate for the popup
     */
    @Override
    public void onRightClick(int row, int col, Component invoker, int x, int y) {
        Position pos = new Position(row, col);
        StringBuilder info = new StringBuilder("<html>");

        for (GameEntity entity : gameMap.getEntitiesAt(pos)) {
            info.append(entity.toString()).append("<br>");
        }

        if (info.toString().equals("<html>")) info.append("Nothing here.");
        info.append("</html>");

        JPopupMenu popup = new JPopupMenu();
        JLabel label = new JLabel(info.toString());
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        popup.add(label);
        popup.show(invoker, x, y);
    }

    /**
     * Checks if the target position is within move range (adjacent) of the source position.
     *
     * @param from the starting position
     * @param to the target position
     * @return true if the target is adjacent, false otherwise
     */
    private boolean isWithinMoveRange(Position from, Position to) {
        return from.distanceTo(to) <= 1;
    }
}