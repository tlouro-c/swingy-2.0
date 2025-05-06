package tc.tlouro_c.model;

import tc.tlouro_c.entity.Coordinates;
import tc.tlouro_c.entity.MapSquare;
import tc.tlouro_c.util.Logger;

public class MapModel {

    private final MapSquare[][] mapGrid;
    private final int mapSize;

    public MapModel(PlayerModel playerModel) {

        Logger.debug("Generating a new map...");

        var heroLevel = playerModel.getSelectedHero().getLevelState().getLevel();

        mapSize = (heroLevel - 1) * 5 + 10 - (heroLevel % 2);
        var spawnCoordinates = new Coordinates(mapSize / 2, mapSize / 2);
        playerModel.setCoordinates(spawnCoordinates);

        mapGrid = new MapSquare[mapSize][mapSize];
        for (int row = 0; row < mapSize; row++) {
            for (int col = 0; col < mapSize; col++) {
                mapGrid[row][col] = new MapSquare(new Coordinates(col, row), spawnCoordinates);
            }
        }
    }

    public MapSquare[][] getMapGrid() {
        return mapGrid;
    }

    public MapSquare getMapSquare(Coordinates coordinates) {
        return mapGrid[coordinates.getY()][coordinates.getX()];
    }

    public boolean isOutOfBounds(Coordinates coordinates) {
        return coordinates.getX() >= mapSize || coordinates.getY() >= mapSize || coordinates.getX() < 0 || coordinates.getY() < 0;
    }
}
