package game.builders;

import game.characters.Orc;
import game.map.Position;

import java.util.Random;

public class OrcBuilder implements EnemyBuilder {

    private static final int BASE_HP = 10;
    private static final int BASE_POWER = 5;
    private static final int TOTAL_POINTS = BASE_HP + BASE_POWER;

    private final Random random = new Random();

    @Override
    public Orc build(Position position) {
        int hpAdjust = getRandomAdjustment();
        int powerAdjust = -hpAdjust; // שומר על סכום קבוע

        int hp = BASE_HP + hpAdjust;
        int power = BASE_POWER + powerAdjust;

        return new Orc(hp, power, position);
    }

    private int getRandomAdjustment() {
        int[] options = {-2, -1, 0, 1, 2, 3};
        return options[random.nextInt(options.length)];
    }
}
