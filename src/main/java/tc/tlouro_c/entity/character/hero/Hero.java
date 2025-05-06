package tc.tlouro_c.entity.character.hero;

import tc.tlouro_c.entity.artifact.Artifact;
import tc.tlouro_c.entity.character.Character;
import tc.tlouro_c.entity.character.CharacterType;

public class Hero extends Character {

    private int id = -1;

    public Hero(String name, CharacterType type, int attackDamageExtra, int armorExtra, int maxHitPointsExtra, Artifact artifact) {
        super(name, type, attackDamageExtra, armorExtra, maxHitPointsExtra, artifact);
    }

    public Hero(int id, String name, CharacterType type, int attackDamage, int armor, int hitPoints, int maxHitPoints, Artifact artifact) {
        super(name, type, attackDamage, armor, hitPoints, maxHitPoints, artifact);
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
