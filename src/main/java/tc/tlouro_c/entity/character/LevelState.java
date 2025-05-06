package tc.tlouro_c.entity.character;

import tc.tlouro_c.config.Config;
import tc.tlouro_c.util.Logger;

public class LevelState {

    private final Character character;
    private int level;
    private int experience;
    private int nextLevelExperience;
    private boolean levelUp;

    public LevelState(Character character) {
        this.character = character;
        this.level = 1;
        this.experience = 0;
        this.nextLevelExperience = calculateNextLevelExperience(1);
    }

    public LevelState(Character character, int level, int experience, int nextLevelExperience) {
        this.character = character;
        this.level = level;
        this.experience = experience;
        this.nextLevelExperience = nextLevelExperience;
    }

    public void updateLevel(int gainedExperience) {

        experience += gainedExperience;

        while (experience >= nextLevelExperience) {

            experience -= nextLevelExperience;
            level = Math.min(level + 1, Config.getInstance().getMaxLevel());
            nextLevelExperience = calculateNextLevelExperience(level);

            applyStatGains();
        }
    }

    public void seedLevel(int targetLevel) {

        targetLevel = Math.min(targetLevel, Config.getInstance().getMaxLevel());

        while (level < targetLevel) {
            level++;
            applyStatGains();
        }
        Logger.debug("After seeding level: " + level);
    }

    private int calculateNextLevelExperience(int level) {
        return level * 1000 + (int)Math.pow(level - 1, 2) * 1000;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public int getNextLevelExperience() {
        return nextLevelExperience;
    }

    public boolean isLevelUp() {
        var toReturn = levelUp;
        levelUp = false;
        return toReturn;
    }

    private void applyStatGains() {
        var characterType = character.getType();
        character.attackDamage += characterType.attackDamagePerLevel;
        character.armor += characterType.armorPerLevel;
        character.maxHitPoints += characterType.maxHitPointsPerLevel;
        character.hitPoints = character.maxHitPoints; // Character gets back to full health after leveling up
    }
}
