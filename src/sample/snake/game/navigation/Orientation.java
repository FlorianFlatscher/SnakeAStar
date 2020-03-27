package sample.snake.game.navigation;

import java.util.HashMap;
import java.util.Map;

public enum Orientation {
    NORTH(3), EAST(0), SOUTH(1), WEST(2);

    private static Map map = new HashMap<>();
    private int value;

    private Orientation(int value){
        this.value = value;
    }

    public double toRadians() {
        return value * 0.5 * Math.PI;
    }
}
