package tc.tlouro_c.entity.character.enemy;

import tc.tlouro_c.entity.artifact.Artifact;
import tc.tlouro_c.entity.character.CharacterType;

public class Arcanaine extends Enemy {
    public Arcanaine(int attackDamage, int armor, int maxHitPoints, Artifact artifact) {
        super("Arcanaine",
                CharacterType.ASSASSIN,
                attackDamage, armor, maxHitPoints, artifact, "/sprites/enemies/arcanaine.png");
    }
}
