package tc.tlouro_c.model;

import org.hibernate.validator.internal.util.logging.Log;
import tc.tlouro_c.entity.Coordinates;
import tc.tlouro_c.entity.Direction;
import tc.tlouro_c.entity.MapSquare;
import tc.tlouro_c.entity.character.hero.Hero;
import tc.tlouro_c.exception.EnemyAppearedException;
import tc.tlouro_c.exception.MapFinishedException;
import tc.tlouro_c.util.Logger;

public class PlayerModel {

    private Hero selectedHero;
    private Coordinates coordinates;
    private Coordinates previousCoordinates;

    public Hero getSelectedHero() {
        return selectedHero;
    }

    public void setSelectedHero(Hero selectedHero) {
        this.selectedHero = selectedHero;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public Coordinates getPreviousCoordinates() { return previousCoordinates; }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void attemptMove(Direction direction, MapModel mapModel) throws EnemyAppearedException, MapFinishedException {

        previousCoordinates = new Coordinates(coordinates);

        switch (direction) {
            case NORTH -> coordinates.decreaseY();
            case SOUTH -> coordinates.increaseY();
            case WEST -> coordinates.decreaseX();
            case EAST -> coordinates.increaseX();
        }

        if (mapModel.isOutOfBounds(coordinates)) {
            throw new MapFinishedException();
        }

        if (mapModel.getMapSquare(coordinates).hasEnemy()) {
            throw new EnemyAppearedException();
        }
    }


}
