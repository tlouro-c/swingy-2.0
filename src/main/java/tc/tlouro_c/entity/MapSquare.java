package tc.tlouro_c.entity;

import tc.tlouro_c.config.Config;
import tc.tlouro_c.entity.character.enemy.Enemy;
import tc.tlouro_c.entity.character.enemy.EnemyFactory;

import java.util.Optional;

public class MapSquare {

    private Enemy enemy;
    private final Coordinates coordinates;

    public MapSquare(Coordinates coordinates, Coordinates spawnCoordinates) {
        this.coordinates = coordinates;

        var randomDouble = Math.random();

        if (randomDouble <= Config.getInstance().getEnemySpawnProbability() && !coordinates.equals(spawnCoordinates)) {
            var enemyFactory = EnemyFactory.getInstance();

            enemy = enemyFactory.createRandomEnemy();
        }
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }

    public Optional<Enemy> getEnemy() {
        return Optional.ofNullable(enemy);
    }

    public boolean hasEnemy() {
        return enemy != null;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }
}
