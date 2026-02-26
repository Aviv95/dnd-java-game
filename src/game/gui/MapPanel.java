// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

package game.gui;

import game.core.GameEntity;
import game.engine.GameWorld;
import game.map.GameMap;
import game.map.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The MapPanel class represents the graphical grid-based map of the game.
 * It displays game entities, handles user interactions, and updates the map
 * based on the game state.
 */
public class MapPanel extends JPanel implements GameEventListener {

    private final int size;
    private final JLabel[][] cells;
    private final GameMap gameMap;
    private final Map<String, ImageIcon> iconMap = new HashMap<>();
    private final Map<Position, Color> highlights = new HashMap<>();
    private final int cellSize = 64;
    private MapClickListener clickListener;
    private Position currentPlayerPosition;
    private Position lastPlayerPosition = null;

    /**
     * Constructs a MapPanel with the specified size and game map.
     *
     * @param size    the size of the grid (number of rows and columns).
     * @param gameMap the game map containing entities and their positions.
     */
    public MapPanel(int size, GameMap gameMap) {
        this.size = size;
        this.gameMap = gameMap;
        this.setLayout(new GridLayout(size, size));
        this.cells = new JLabel[size][size];
        loadIcons();
        initializeGrid();

    }

    /**
     * Sets the player's current position on the map.
     *
     * @param pos the new position of the player.
     */
    public void setPlayerPosition(Position pos) {
        this.currentPlayerPosition = pos;
        this.lastPlayerPosition = null; // Ensures refresh is not skipped
    }

    /**
     * Sets the click listener for handling user interactions on the map.
     *
     * @param listener the MapClickListener to handle clicks.
     */
    public void setClickListener(MapClickListener listener) {
        this.clickListener = listener;
    }

    /**
     * Updates the map when the game state changes.
     *
     * @param world the current game world.
     */
    @Override
    public void onGameStateChanged(GameWorld world) {
        if (!world.getPlayers().isEmpty()) {
            Position pos = world.getPlayers().get(0).getPosition(); // First player's position
            refresh(pos); // Update based on the current model position
        }
    }

    /**
     * Loads icons for various game entities.
     */
    private void loadIcons() {
        iconMap.put("Warrior", loadIcon("figther.png"));
        iconMap.put("Mage", loadIcon("Mage.png"));
        iconMap.put("Archer", loadIcon("archer.png"));
        iconMap.put("Goblin", loadIcon("goblin.png"));
        iconMap.put("Orc", loadIcon("orc.png"));
        iconMap.put("Dragon", loadIcon("dragon.png"));
        iconMap.put("Potion", loadIcon("life_potion.png"));
        iconMap.put("PowerPotion", loadIcon("power_potion.png"));
        iconMap.put("Wall", loadIcon("wall.png"));
        iconMap.put("Treasure", loadIcon("treasure.png"));
        iconMap.put("GameBackground", loadIcon("game_background.png"));
        iconMap.put("Unknown", loadIcon("unknown.png"));
        iconMap.put("Empty", loadIcon("game_background.png"));
    }

    /**
     * Loads an icon from the resources folder.
     *
     * @param filename the name of the icon file.
     * @return the loaded ImageIcon.
     */
    private ImageIcon loadIcon(String filename) {
        var url = getClass().getClassLoader().getResource("images/" + filename);
        if (url == null) {
            System.err.println("Missing icon: " + filename);
            return new ImageIcon();
        }
        Image img = new ImageIcon(url).getImage().getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    /**
     * Initializes the grid of cells for the map.
     */
    private void initializeGrid() {
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                JLabel cell = new JLabel();
                cell.setOpaque(true);
                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setVerticalAlignment(SwingConstants.CENTER);
                cell.setBackground(Color.BLACK);

                final int r = row, c = col;
                cell.addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        if (clickListener == null) return;
                        if (SwingUtilities.isLeftMouseButton(e))
                            clickListener.onLeftClick(r, c);
                        else if (SwingUtilities.isRightMouseButton(e))
                            clickListener.onRightClick(r, c, e.getComponent(), e.getX(), e.getY());
                    }
                });

                cells[row][col] = cell;
                this.add(cell);
            }
        }
    }

    /**
     * Refreshes the map display based on the player's position.
     *
     * @param playerPos the current position of the player.
     */
    public void refresh(Position playerPos) {
        if (playerPos == null) return;
        this.currentPlayerPosition = playerPos;

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                Position pos = new Position(row, col);
                JLabel cell = cells[row][col];

                cell.setIcon(null);

                int distance = playerPos.distanceTo(pos);
                List<GameEntity> entities = gameMap.getEntitiesAt(pos);

                GameEntity visible = null;
                if (distance <= 2) {
                    for (GameEntity e : entities) {
                        if (e instanceof game.characters.Enemy en && en.isDead()) continue;
                        e.setVisible(true);
                        visible = e;
                        break;
                    }
                }

                if (distance > 2) {
                    cell.setIcon(iconMap.get("Unknown"));
                } else if (visible != null) {
                    cell.setIcon(iconMap.getOrDefault(visible.getClass().getSimpleName(), iconMap.get("Unknown")));
                } else if (!highlights.containsKey(pos)) {
                    cell.setIcon(iconMap.get("Empty"));
                }

                if (highlights.containsKey(pos)) {
                    cell.setBackground(highlights.get(pos));
                } else {
                    cell.setBackground(Color.BLACK);
                }
            }
        }
    }


    /**
     * Highlights a specific cell on the map with a given color.
     *
     * @param pos   the position of the cell to highlight.
     * @param color the color to use for highlighting.
     */
    public void highlightCell(Position pos, Color color) {
        if (pos == null) return;
        highlights.put(pos, color);
        refresh(currentPlayerPosition);

        Timer t = new Timer(3000, e -> {
            highlights.remove(pos);
            refresh(currentPlayerPosition);
        });
        t.setRepeats(false);
        t.start();
    }

    /**
     * Returns the size of each cell in the grid.
     *
     * @return the cell size in pixels.
     */
    public int getCellSize() {
        return cellSize;
    }

    /**
     * Interface for handling map click events.
     */
    public interface MapClickListener {
        /**
         * Called when the user left-clicks on a cell.
         *
         * @param row the row index of the clicked cell.
         * @param col the column index of the clicked cell.
         */
        void onLeftClick(int row, int col);

        /**
         * Called when the user right-clicks on a cell.
         *
         * @param row     the row index of the clicked cell.
         * @param col     the column index of the clicked cell.
         * @param invoker the component that triggered the event.
         * @param mouseX  the x-coordinate of the mouse click.
         * @param mouseY  the y-coordinate of the mouse click.
         */
        void onRightClick(int row, int col, Component invoker, int mouseX, int mouseY);
    }
}