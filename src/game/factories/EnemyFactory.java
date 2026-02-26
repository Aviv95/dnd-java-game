package game.factories;

import game.builders.*;
import game.characters.Enemy;
import game.map.GameMap;
import game.map.Position;

import java.util.*;
import java.util.function.Supplier;

public class EnemyFactory {

    private static final Map<String, Supplier<EnemyBuilder>> enemySuppliers = new HashMap<>();
    private static final Random random = new Random();

    static {
        enemySuppliers.put("Orc", OrcBuilder::new);
        enemySuppliers.put("Goblin", GoblinBuilder::new);
        enemySuppliers.put("Dragon", DragonBuilder::new);
    }

    public static Enemy createEnemy(Position position, GameMap map) {
        Set<String> existingTypes = getExistingEnemyTypes(map);

        // שלב 1: חפש אויבים שעדיין לא קיימים על הלוח
        List<String> missingTypes = new ArrayList<>();
        for (String type : enemySuppliers.keySet()) {
            if (!existingTypes.contains(type)) {
                missingTypes.add(type);
            }
        }

        String selectedType;

        if (!missingTypes.isEmpty()) {
            // עדיפות לסוגים שלא קיימים
            selectedType = missingTypes.get(random.nextInt(missingTypes.size()));
        } else {
            // אם כולם קיימים – בחירה אקראית
            List<String> allTypes = new ArrayList<>(enemySuppliers.keySet());
            selectedType = allTypes.get(random.nextInt(allTypes.size()));
        }

        EnemyBuilder builder = enemySuppliers.get(selectedType).get();
        return builder.build(position);
    }

    private static Set<String> getExistingEnemyTypes(GameMap map) {
        Set<String> types = new HashSet<>();
        for (List<?> entityList : map.getGrid().values()) {
            for (Object obj : entityList) {
                if (obj instanceof Enemy enemy) {
                    types.add(enemy.getClass().getSimpleName());
                }
            }
        }
        return types;
    }
}
