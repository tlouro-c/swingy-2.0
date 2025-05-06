package tc.tlouro_c.entity.character.enemy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import tc.tlouro_c.config.Config;

public class EnemyFactoryTest {


    @Test
    void createRandomEnemy() {

        var enemyFactory = EnemyFactory.getInstance();

        var newEnemy = enemyFactory.createRandomEnemy();

        Assertions.assertTrue(
                newEnemy.getClass() == Arcanaine.class ||
                newEnemy.getClass() == Gyarados.class ||
                newEnemy.getClass() == Machamp.class
        );

        Assertions.assertTrue(
                newEnemy.getLevelState().getLevel() > 0 &&
                newEnemy.getLevelState().getLevel() <= Config.getInstance().getMaxLevel()
        );

        Assertions.assertTrue(
                newEnemy.getAttackDamage() > 0 &&
                newEnemy.getArmor() > 0 &&
                newEnemy.getMaxHitPoints() > 0 &&
                newEnemy.getMaxHitPoints() == newEnemy.getHitPoints()
        );

    }
}