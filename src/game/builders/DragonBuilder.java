package game.builders;

import game.characters.Dragon;
import game.map.Position;

import java.util.Random;

public class DragonBuilder implements EnemyBuilder {

    private static final int BASE_HP = 20;
    private static final int BASE_POWER = 15;
    private static final int TOTAL_POINTS = BASE_HP + BASE_POWER;

    private final Random random = new Random();

    @Override
    public Dragon build(Position position) {
        int hpAdjust = getRandomAdjustment();
        int powerAdjust = -hpAdjust;

        int hp = BASE_HP + hpAdjust;
        int power = BASE_POWER + powerAdjust;

        return new Dragon(hp, power, position);
    }

    private int getRandomAdjustment() {
        int[] options = {-2, -1, 0, 1, 2, 3};
        return options[random.nextInt(options.length)];
    }
}
