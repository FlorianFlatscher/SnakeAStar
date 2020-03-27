package sample.snake.game;

import javafx.scene.canvas.GraphicsContext;
import sample.snake.engine.Game;
import sample.snake.engine.GameCanvas;
import sample.snake.engine.GameEngine;
import sample.snake.game.colors.SnakeGameColors;
import sample.snake.game.navigation.GridTileState;
import sample.snake.game.navigation.Orientation;
import sample.snake.game.navigation.Vector2D;

import java.lang.reflect.Array;
import java.util.*;

public class SnakeGame implements Game {
    //Engine
    GameCanvas canvas;
    GraphicsContext gc;

    //Game
    int dimensionX, dimensionY;
    GridTileState[][] grid;
    ArrayList<Vector2D> snake;
    ArrayList<Orientation> snakePath;
    Vector2D fruit;
    HashSet<Vector2D>freeSpots = new HashSet<>();

    //Settings
    public static final double snakeSpeed = 0.1;

    public SnakeGame(GameCanvas canvas, int dimensionX, int dimensionY) {
        Objects.requireNonNull(canvas);
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();

        //Game
        this.dimensionX = dimensionX;
        this.dimensionY = dimensionY;
        grid = new GridTileState[dimensionX][dimensionY];

        freeSpots = new HashSet<>();
        for (int x = 0; x < dimensionX; x++) {
            for (int y = 0; y < dimensionY; y++) {
                freeSpots.add(new Vector2D(x, y));
            }
        }

        snake = new ArrayList<>();
        snakePath = new ArrayList<>();
        for (int i = 2; i >= 0; --i) {
            addSnakePart(i, 0);
            snakePath.add(Orientation.EAST);
        }
    }

    @Override
    public void update(GameEngine engine, double animationUnitScale) {
        //Prepare
        double scaleX = canvas.getWidth() / grid.length;
        double scaleY = canvas.getHeight() / grid[0].length;
        gc.save();
        gc.scale(scaleX, scaleY);
        gc.setFill(SnakeGameColors.backgroundColor);
        gc.clearRect(0, 0, dimensionX, dimensionY);

        //SnakeMovement
        for (int i = 0; i < snake.size(); i++) {
            Vector2D vel = Vector2D.fromOrientation(snakePath.get(i));
            System.out.println("animationUnitScale = " + animationUnitScale);
            vel.multiply(animationUnitScale * snakeSpeed);
            snake.get(i).add(vel);
        }

        //rendering
        gc.setFill(SnakeGameColors.snakeColor);
        for (Vector2D vector2D : snake) {
            gc.fillRect(vector2D.getX(), vector2D.getY(), 1, 1);
        }
        gc.restore();
    }

    private void setFruit() {
        Random random = new Random();
        int randomIndex = random.nextInt(freeSpots.size());
        for (Vector2D vector2D: freeSpots) {
            if (randomIndex <= 0) {
                fruit = vector2D.cloneVector2D();
                return;
            }
            randomIndex--;
        }
    }

    private void addSnakePart(int x, int y) {
        grid[x][y] = GridTileState.BLOCKED;
        snake.add(new Vector2D(x, y));
        freeSpots.remove(new Vector2D(x, y));
    }

    private void removeSnakePart(int indexInSnake) {
        if (indexInSnake < 0) {
            indexInSnake = snake.size() + indexInSnake;
        }
        Vector2D pos = snake.remove(indexInSnake);
        freeSpots.add(pos.cloneVector2D());
        grid[(int) Math.round(pos.getX())][(int) Math.round(pos.getY())] = GridTileState.EMPTY;
    }
}