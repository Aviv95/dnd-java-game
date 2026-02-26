package game.builders;
import game.characters.Enemy;
import game.map.Position;
public interface EnemyBuilder {
    Enemy build(Position position);

}
