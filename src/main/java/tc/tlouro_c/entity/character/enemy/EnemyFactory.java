package tc.tlouro_c.entity.character.enemy;

import tc.tlouro_c.config.Config;
import tc.tlouro_c.entity.artifact.ArtifactFactory;
import tc.tlouro_c.util.Logger;

public class EnemyFactory {

    private static final EnemyFactory instance = new EnemyFactory();

    private EnemyFactory() {}

    public static EnemyFactory getInstance() {
        return instance;
    }

    public Enemy createRandomEnemy() {

        int pointsToDistribute = 30;
        int attackDamage = (int)(Math.random() * (pointsToDistribute + 1));
        int armor = (int)(Math.random() * (pointsToDistribute - attackDamage + 1));
        var maxHitPoints = pointsToDistribute - attackDamage - armor;

        int enemyLevel = generateRandomLevel();

        Logger.debug(String.valueOf(enemyLevel));

        var artifactFactory = ArtifactFactory.getInstance();
        var artifact = Math.random() >= 0.5 ? artifactFactory.createRandomArtifact(enemyLevel) : null;

        if (artifact != null) {
            Logger.debug("Artifact generated lv." + artifact.getLevel());
        }

        var enemy = switch (generateRandomEnemyType()) {
            case ARCANAINE -> new Arcanaine(attackDamage, armor, maxHitPoints, artifact);
            case GYARADOS -> new Gyarados(attackDamage, armor, maxHitPoints, artifact);
            case MACHAMP -> new Machamp(attackDamage, armor, maxHitPoints, artifact);
        };

        enemy.getLevelState().seedLevel(enemyLevel);
        return enemy;
    }

    public EnemyType generateRandomEnemyType() {
        var differentEnemies = EnemyType.values();
        int randomIndex = (int)(Math.random() * differentEnemies.length);
        return differentEnemies[randomIndex];
    }

    public int generateRandomLevel() {
        return (int) (Math.random() * Config.getInstance().getMaxLevel()) + 1;
    }

}
