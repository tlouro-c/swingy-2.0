package tc.tlouro_c.entity.artifact;

public abstract class Artifact {

    protected final String name;
    protected final int level;
    protected final int attackDamageExtra;
    protected final int armorExtra;
    protected final int hitPointsExtra;
    protected int hitPointsExtraAvailable;

    public Artifact(String name, int attackDamageExtra, int armorExtra, int hitPointsExtra, int level) {
        this.name = name;
        this.level = level;
        this.attackDamageExtra = attackDamageExtra * level;
        this.armorExtra = armorExtra * level;
        this.hitPointsExtra = hitPointsExtra * level;
        this.hitPointsExtraAvailable = hitPointsExtra;
    }

    public String getName() {
        return name;
    }

    public int getAttackDamageExtra() {
        return attackDamageExtra;
    }

    public int getArmorExtra() {
        return armorExtra;
    }

    public int getHitPointsExtra() {
        return hitPointsExtra;
    }

    public int getLevel() {
        return level;
    }

    public int getHitPointsExtraAvailable() {
        return hitPointsExtraAvailable;
    }

    public void setHitPointsExtraAvailable(int hitPointsExtraAvailable) {
        this.hitPointsExtraAvailable = hitPointsExtraAvailable;
    }
}
