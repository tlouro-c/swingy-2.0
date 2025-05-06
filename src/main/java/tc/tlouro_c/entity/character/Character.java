package tc.tlouro_c.entity.character;

import tc.tlouro_c.entity.artifact.Artifact;


public abstract class Character {

    protected final String name;
    protected final CharacterType type;
    protected LevelState levelState;
    protected int attackDamage;
    protected int armor;
    protected int hitPoints;
    protected int maxHitPoints;
    protected Artifact artifact;

    public void attack(Character target) {
        target.takeDamage(getTotalAttackDamage());
    }

    private void takeDamage(int damage) {

        boolean dodged = Math.random() > 0.85;
        if (dodged) {
            return;
        }

        double armorReduction = 1.0 + (this.getTotalArmor() / 100.0);
        int trueDamage = (int)(damage / armorReduction);

        int extraHP = artifact != null ? artifact.getHitPointsExtraAvailable() : 0;

        if (extraHP > 0) {
            int damageToArtifact = Math.min(trueDamage, extraHP);
            artifact.setHitPointsExtraAvailable(extraHP - damageToArtifact);
            trueDamage -= damageToArtifact;
        }

        if (trueDamage > 0) {
            this.hitPoints -= trueDamage;
            this.hitPoints = Math.max(this.hitPoints, 0);
        }
    }

    public Character(String name, CharacterType type, int attackDamageExtra, int armorExtra, int maxHitPointsExtra, Artifact artifact) {
        this.name = name;
        this.type = type;
        this.levelState = new LevelState(this);
        this.attackDamage = 50 + type.attackDamagePerLevel + attackDamageExtra;
        this.armor = 50 + type.armorPerLevel + armorExtra;
        this.maxHitPoints = 200 + type.maxHitPointsPerLevel + maxHitPointsExtra;
        this.hitPoints = this.maxHitPoints;
        this.artifact = artifact;
    }

    public Character(String name, CharacterType type, int attackDamage, int armor, int hitPoints, int maxHitPoints, Artifact artifact) {
        this.name = name;
        this.type = type;
        this.levelState = new LevelState(this);
        this.attackDamage = attackDamage;
        this.armor = armor;
        this.hitPoints = hitPoints;
        this.maxHitPoints = maxHitPoints;
        this.artifact = artifact;
    }

    public String getName() {
        return name;
    }

    public CharacterType getType() {
        return type;
    }

    public LevelState getLevelState() {
        return levelState;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getArmor() {
        return armor;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    public Artifact getArtifact() {
        return artifact;
    }

    public boolean hasArtifact() {
        return artifact != null;
    }

    public void setLevelState(LevelState levelState) {
        this.levelState = levelState;
    }

    public void setArtifact(Artifact artifact) {
        this.artifact = artifact;
    }

    public boolean isAlive() {
        return hitPoints > 0;
    }

    public int getTotalArmor() {
        return artifact != null ? armor + artifact.getArmorExtra() : armor;
    }

    public int getTotalAttackDamage() {
        return artifact != null ? attackDamage + artifact.getAttackDamageExtra() : attackDamage;
    }

    public void heal(int hitPoints) {
        this.hitPoints = Math.min(this.hitPoints + hitPoints, maxHitPoints);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(name).append(" [").append(type).append("] ");
        sb.append("Lvl ").append(levelState.getLevel()).append(" ");
        sb.append("XP ").append(levelState.getExperience()).append("/").append(levelState.getNextLevelExperience()).append(" | ");
        sb.append("ATK ").append(attackDamage).append(" ARM ").append(armor).append(" HP ").append(hitPoints).append("/").append(maxHitPoints);

        if (artifact != null) {
            sb.append(" | Artifact: ").append(artifact.getName());
            sb.append(" (+ATK ").append(artifact.getAttackDamageExtra());
            sb.append(" +ARM ").append(artifact.getArmorExtra());
            sb.append(" +HP ").append(artifact.getHitPointsExtra()).append(")");
        }

        return sb.toString();
    }
}
