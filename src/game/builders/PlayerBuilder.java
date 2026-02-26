package game.builders;

import game.characters.*;
import game.map.Position;

public class PlayerBuilder {

    private int hp;
    private int power;
    private Position position;
    private String classType;
    private String name;

    public PlayerBuilder setHP(int hp) {
        this.hp = hp;
        return this;
    }

    public PlayerBuilder setPower(int power) {
        this.power = power;
        return this;
    }

    public PlayerBuilder setPosition(Position position) {
        this.position = position;
        return this;
    }

    public PlayerBuilder setClassType(String classType) {
        this.classType = classType;
        return this;
    }

    public PlayerBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PlayerCharacter build() {
        return switch (classType) {
            case "Warrior" -> new Warrior(name, hp, power, position);
            case "Mage" -> new Mage(name, hp, power, position);
            case "Archer" -> new Archer(name, hp, power, position);
            default -> throw new IllegalArgumentException("Unknown class type: " + classType);
        };
    }
}
