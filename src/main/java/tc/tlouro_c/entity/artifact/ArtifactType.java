package tc.tlouro_c.entity.artifact;

import tc.tlouro_c.entity.character.CharacterType;

public enum ArtifactType {
    FLAME_SHARD,
    IRON_SHELL,
    SEED_PENDANT;

    public static ArtifactType fromString(String type) {
        return ArtifactType.valueOf(type.toUpperCase());
    }
}
