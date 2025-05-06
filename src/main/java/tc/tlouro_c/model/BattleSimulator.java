package tc.tlouro_c.model;

import tc.tlouro_c.entity.artifact.Artifact;
import tc.tlouro_c.entity.character.Character;
import tc.tlouro_c.entity.character.hero.Hero;
import tc.tlouro_c.entity.character.enemy.Enemy;

public class BattleSimulator {

    private BattleSimulator() {}

    public static BattleResult simulate(Hero player, Enemy enemy) {

        Character attacker = Math.random() > 0.5 ? player : enemy;
        Character target = (attacker == player) ? enemy : player;

        while (player.isAlive() && enemy.isAlive()) {
            attacker.attack(target);

            Character tmp = attacker;
            attacker = target;
            target = tmp;
        }
        Character winner = player.isAlive() ? player : enemy;
        Character loser = winner == player ? enemy : player;

        regenArtifact(winner.getArtifact());
        regenArtifact(loser.getArtifact());

        winner.getLevelState().updateLevel(loser.getLevelState().getLevel() * 400);
        winner.heal(winner.getMaxHitPoints() / 2);

        return winner == player ? BattleResult.PLAYER_WINS : BattleResult.PLAYER_DIES;
    }

    private static void regenArtifact(Artifact artifact) {
        if (artifact != null) {
            artifact.setHitPointsExtraAvailable(artifact.getHitPointsExtra());
        }
    }
}
