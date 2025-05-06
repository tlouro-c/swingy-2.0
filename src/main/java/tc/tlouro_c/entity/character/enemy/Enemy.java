package tc.tlouro_c.entity.character.enemy;

import tc.tlouro_c.entity.artifact.Artifact;
import tc.tlouro_c.entity.character.Character;
import tc.tlouro_c.entity.character.CharacterType;

public class Enemy extends Character {

    private final String spriteSrc;

    public Enemy(String name, CharacterType type, int attackDamageExtra, int armorExtra, int maxHitPointsExtra, Artifact artifact, String spriteSrc) {
        super(name, type, attackDamageExtra, armorExtra, maxHitPointsExtra, artifact);
        this.spriteSrc = spriteSrc;
    }

    public String getSpriteSrc() {
        return spriteSrc;
    }
}
