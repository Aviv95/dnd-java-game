package game.gui;

import game.characters.PlayerCharacter;
import game.map.GameMap;
import game.map.memento.GameMapCaretaker;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    private final GameMapCaretaker caretaker = new GameMapCaretaker();
    private final GameMap map;
    private final MapPanel mapPanel;
    private final PlayerCharacter player;

    public GameFrame(MapPanel mapPanel, StatusPanel statusPanel, GameMap map, PlayerCharacter player) {
        super("Dungeons & Dragons");

        this.map = map;
        this.mapPanel = mapPanel;
        this.player = player;

        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);

        add(mapPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        setJMenuBar(createMenuBar());

        pack();
        setLocationRelativeTo(null);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");

        JMenuItem saveItem = new JMenuItem("Save State");
        JMenuItem restoreItem = new JMenuItem("Restore State");

        saveItem.addActionListener(e -> {
            caretaker.save(map);
            JOptionPane.showMessageDialog(this, "Game state saved!");
        });

        restoreItem.addActionListener(e -> {
            if (caretaker.hasHistory()) {
                caretaker.undo(map);
                mapPanel.refresh(player.getPosition()); // ← עדכון נכון של התצוגה
                JOptionPane.showMessageDialog(this, "Game state restored!");
            } else {
                JOptionPane.showMessageDialog(this, "No saved state to restore.");
            }
        });

        gameMenu.add(saveItem);
        gameMenu.add(restoreItem);
        menuBar.add(gameMenu);

        return menuBar;
    }
}
