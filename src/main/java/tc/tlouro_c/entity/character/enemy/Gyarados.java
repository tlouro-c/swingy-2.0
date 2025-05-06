package tc.tlouro_c.entity.character.enemy;

import tc.tlouro_c.entity.artifact.Artifact;
import tc.tlouro_c.entity.character.CharacterType;

public class Gyarados extends Enemy {
    public Gyarados(int attackDamage, int armor, int maxHitPoints, Artifact artifact) {
        super("Gyarados",
                CharacterType.TANK,
                attackDamage, armor, maxHitPoints, artifact, "/sprites/enemies/gyarados.png");
    }
}
