// Students: Aviv Nahum, ID : 206291163 -- Bar Swisa, ID: 211631551

import game.characters.*;
import game.combat.CombatSystem;
import game.core.GameEntity;
import game.engine.EnemyManager;
import game.engine.GameLogger;
import game.engine.GameWorld;
import game.engine.MapPopulator;
import game.gui.*;
import game.items.GameItem;
import game.map.*;
import game.builders.*;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

public class Main {
    private static void clearLogFile() {
        try (FileWriter fw = new FileWriter("log.txt", false)) {
            fw.write(""); // ריקון תוכן הקובץ
        } catch (IOException e) {
            System.err.println("Failed to clear log file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

        clearLogFile();
        GameLogger.log("=== Game Started ===");


        SwingUtilities.invokeLater(() -> {
            // choose name
            String name = promptForName();

            // choose character
            PlayerCharacter player = promptForCharacter(name);

            // choose map size
            int mapSize = promptForMapSize();

            // create game Map and World
            GameMap map = GameMap.getInstance(); // Singleton

            GameWorld world = new GameWorld(map);

            // randomize player position
            Random rand = new Random();
            Position playerPos = new Position(rand.nextInt(mapSize), rand.nextInt(mapSize));
            player.setPosition(playerPos);
            world.addPlayer(player);
            map.addEntity(player);

            // create enemies
            MapPopulator.populateMap(world, mapSize, playerPos);
            EnemyManager enemyManager = new EnemyManager(world.getEnemies(), player, map, world);
            enemyManager.startAll();


            // 7. GUI
            MapPanel mapPanel = new MapPanel(mapSize, map);
            StatusPanel statusPanel = new StatusPanel();
            GameController controller = new GameController(world, map, player, mapPanel, statusPanel);
            GameFrame frame = new GameFrame(mapPanel, statusPanel, map, player);
            frame.setVisible(true);

        });

    }
    private static String promptForName() {
        JDialog dialog = new JDialog();
        BackgroundPanel panel = new BackgroundPanel("images/choose_name_character.png");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));


        JLabel label = new JLabel("Enter your name:");
        label.setForeground(Color.WHITE);
        JTextField field = new JTextField(15);
        JButton ok = new JButton("OK");

        final String[] nameHolder = {null};

        ok.addActionListener(e -> {
            String input = field.getText().trim();
            if (!input.isEmpty()) {
                nameHolder[0] = input;
                dialog.dispose();
            }
        });

        panel.add(label);
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(ok);

        dialog.setContentPane(panel);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        dialog.setVisible(true);

        return nameHolder[0];
    }

    private static PlayerCharacter promptForCharacter(String name) {
        JDialog dialog = new JDialog();
        BackgroundPanel panel = new BackgroundPanel("images/choose_name_character.png");
        panel.setLayout(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel classLabel = new JLabel("Choose class:");
        classLabel.setForeground(Color.WHITE);
        JComboBox<String> classBox = new JComboBox<>(new String[]{"Warrior", "Mage", "Archer"});

        JLabel hpLabel = new JLabel("HP:");
        hpLabel.setForeground(Color.WHITE);
        JSpinner hpSpinner = new JSpinner();

        JLabel powerLabel = new JLabel("Power:");
        powerLabel.setForeground(Color.WHITE);
        JSpinner powerSpinner = new JSpinner();

        JLabel infoLabel = new JLabel("Total points must equal 110 (base: HP=100, Power=10)");
        infoLabel.setForeground(Color.WHITE);

        JButton okButton = new JButton("Create");

        final PlayerCharacter[] result = {null};

        final int BASE_HP = 100;
        final int BASE_POWER = 10;
        final int TOTAL = BASE_HP + BASE_POWER;

        classBox.addActionListener(e -> {
            hpSpinner.setModel(new SpinnerNumberModel(BASE_HP, BASE_HP - 2, BASE_HP + 3, 1));
            powerSpinner.setModel(new SpinnerNumberModel(BASE_POWER, BASE_POWER - 2, BASE_POWER + 3, 1));
        });
        classBox.setSelectedIndex(0); // מפעיל את actionListener פעם ראשונה

        okButton.addActionListener(e -> {
            int hp = (Integer) hpSpinner.getValue();
            int power = (Integer) powerSpinner.getValue();

            if (hp + power == TOTAL) {
                Position dummy = new Position(0, 0);
                PlayerBuilder builder = new PlayerBuilder()
                        .setName(name)
                        .setClassType((String) classBox.getSelectedItem())
                        .setHP(hp)
                        .setPower(power)
                        .setPosition(dummy);
                result[0] = builder.build();
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog,
                        "Total points must remain " + TOTAL + " (current: " + (hp + power) + ")");
            }
        });

        panel.add(classLabel); panel.add(classBox);
        panel.add(hpLabel); panel.add(hpSpinner);
        panel.add(powerLabel); panel.add(powerSpinner);
        panel.add(new JLabel("")); panel.add(infoLabel);
        panel.add(new JLabel("")); panel.add(okButton);

        dialog.setContentPane(panel);
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);

        return result[0];
    }




    private static int promptForMapSize() {
        JDialog dialog = new JDialog();
        BackgroundPanel panel = new BackgroundPanel("images/choose_map.png");
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel label = new JLabel("Enter map size (min 10):");
        label.setForeground(Color.WHITE);
        JTextField field = new JTextField(5);
        JButton ok = new JButton("Start");

        final int[] mapSize = {0};

        ok.addActionListener(e -> {
            try {
                int size = Integer.parseInt(field.getText());
                if (size >= 10) {
                    mapSize[0] = size;
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Minimum is 10.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Enter a valid number.");
            }
        });

        panel.add(label);
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(ok);

        dialog.setContentPane(panel);
        dialog.setSize(300, 200);
        dialog.setLocationRelativeTo(null);
        dialog.setModal(true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        dialog.setVisible(true);

        return mapSize[0];
    }
}
