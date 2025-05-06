package tc.tlouro_c.entity.artifact;

import tc.tlouro_c.entity.character.enemy.EnemyType;

public class ArtifactFactory {

    private ArtifactFactory() {}

    private static class Holder {
        private static final ArtifactFactory INSTANCE = new ArtifactFactory();
    }

    public static ArtifactFactory getInstance() {
        return Holder.INSTANCE;
    }

    public Artifact createRandomArtifact(int level) {

        var type = generateRandomArtifactType();

        return switch (type) {
           case FLAME_SHARD -> new FlameShard(level);
           case IRON_SHELL -> new IronShell(level);
           case SEED_PENDANT -> new SeedPendant(level);
       };
    }

    public Artifact createArtifact(ArtifactType type, int level) {

        return switch (type) {
            case FLAME_SHARD -> new FlameShard(level);
            case IRON_SHELL -> new IronShell(level);
            case SEED_PENDANT -> new SeedPendant(level);
        };
    }

    private ArtifactType generateRandomArtifactType() {
        var differentArtifacts = ArtifactType.values();
        int randomIndex = (int)(Math.random() * differentArtifacts.length);
        return differentArtifacts[randomIndex];
    }
}
