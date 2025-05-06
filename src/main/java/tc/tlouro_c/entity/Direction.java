package tc.tlouro_c.entity;

import java.util.Optional;

public enum Direction {
    NORTH('w'),
    SOUTH('s'),
    WEST('a'),
    EAST('d');

    private final char key;

    Direction(char key) {
        this.key = key;
    }

    public char getKey() {
        return key;
    }

    public static Optional<Direction> fromKey(char input) {
        input = Character.toLowerCase(input); // Handle both uppercase and lowercase
        for (Direction direction : values()) {
            if (direction.key == input) {
                return Optional.of(direction);
            }
        }
        return Optional.empty();
    }
}

