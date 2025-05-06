package tc.tlouro_c;

import tc.tlouro_c.entity.artifact.Artifact;
import tc.tlouro_c.model.MapModel;
import tc.tlouro_c.model.PlayerModel;

import javax.swing.*;

public class GameContext {

    private MapModel mapModel;
    private PlayerModel playerModel;
    private Artifact droppedArtifact;

    public Artifact getDroppedArtifact() {
        return droppedArtifact;
    }

    public void setDroppedArtifact(Artifact droppedArtifact) {
        this.droppedArtifact = droppedArtifact;
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(MapModel mapModel) {
        this.mapModel = mapModel;
    }

    public PlayerModel getPlayerModel() {
        return playerModel;
    }

    public void setPlayerModel(PlayerModel playerModel) {
        this.playerModel = playerModel;
    }

    // Singleton
    private GameContext() {
        this.playerModel = new PlayerModel();
    }

    private static class Holder {
        private static final GameContext INSTANCE = new GameContext();
    }

    public static GameContext getInstance() {
        return Holder.INSTANCE;
    }
}
