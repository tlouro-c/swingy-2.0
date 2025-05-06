package tc.tlouro_c.entity.character;

public enum CharacterType {
    BRUISER(10, 10, 10),
    TANK(0, 15, 15),
    ASSASSIN(30, 0, 0);

    final int attackDamagePerLevel;
    final int armorPerLevel;
    final int maxHitPointsPerLevel;

    CharacterType(int attackDamagePerLevel, int armorPerLevel, int maxHitPointsPerLevel) {
        this.attackDamagePerLevel = attackDamagePerLevel;
        this.armorPerLevel = armorPerLevel;
        this.maxHitPointsPerLevel = maxHitPointsPerLevel;
    }

    public static CharacterType fromString(String type) {
        return CharacterType.valueOf(type.toUpperCase());
    }
}
