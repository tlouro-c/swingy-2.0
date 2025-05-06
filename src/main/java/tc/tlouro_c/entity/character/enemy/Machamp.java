package tc.tlouro_c.entity.character.enemy;

import tc.tlouro_c.entity.artifact.Artifact;
import tc.tlouro_c.entity.character.CharacterType;

public class Machamp extends Enemy {
    public Machamp(int attackDamage, int armor, int maxHitPoints, Artifact artifact) {
        super("Machamp",
                CharacterType.BRUISER,
                attackDamage, armor, maxHitPoints, artifact, "/sprites/enemies/machamp.png");
    }
}
