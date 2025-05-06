package tc.tlouro_c.entity.artifact;

public class FlameShard extends Artifact {
    public FlameShard(int level) {
        super("Flame Shard", 10 * level, 0, 0, level);
    }
}
