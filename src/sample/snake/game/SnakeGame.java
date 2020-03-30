package sample.snake.game;

import javafx.concurrent.Task;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.StrokeType;
import sample.snake.engine.Game;
import sample.snake.engine.GameCanvas;
import sample.snake.engine.GameEngine;
import sample.snake.engine.RedrawTask;
import sample.snake.game.colors.SnakeGameColors;
import sample.snake.game.navigation.GridTileState;
import sample.snake.game.navigation.Orientation;
import sample.snake.game.navigation.Vector2D;

import java.util.*;

public class SnakeGame implements Game {


    //Game
    private int dimensionX, dimensionY;
    private GridTileState[][] grid;
    private ArrayList<Vector2D> snake;
    private ArrayList<Orientation> snakePath;
    private Vector2D fruit;
    private HashSet<Vector2D> freeSpots = new HashSet<>();
    private Orientation currentDirection = Orientation.EAST;

    //Settings
    public static final double snakeSpeed = 10;

    public SnakeGame(int dimensionX, int dimensionY) {

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
        for (int i = 10; i >= 0; --i) {
            addSnakePart(i, 0);
            snakePath.add(Orientation.EAST);
        }
    }

    @Override
    public RedrawTask update(GameEngine engine, double deltaTime) {


        //SnakeMovement
        for (int i = snake.size() - 1; i >= 0; i--) {
            double distance = snakeSpeed * deltaTime;

            int currentIndex = i;
            do {
                if (currentIndex < 0) {
                    snakePath.remove(snakePath.size() - 1);
                    snakePath.add(0, currentDirection);
                    currentIndex++;
                }
                Vector2D vel = Vector2D.fromOrientation(snakePath.get(currentIndex));


                vel.setMag(Math.min(distance, snake.get(i).cloneVector2D().add(vel).floor().sub(snake.get(i)).getMag()));
                distance -= vel.getMag();
                snake.get(i).add(vel);
                currentIndex--;
            }
            while (distance > 0);
        }

        //rendering



        return gc -> {

            Canvas canvas = gc.getCanvas();
            //Prepare

            double scaleX = canvas.getWidth() / grid.length;
            double scaleY = canvas.getHeight() / grid[0].length;
            gc.save();
            gc.scale(scaleX, scaleY);
            gc.setFill(SnakeGameColors.backgroundColor);
            gc.clearRect(0, 0, dimensionX, dimensionY);

            gc.setFill(SnakeGameColors.snakeColor);
            for (Vector2D vector2D : snake) {
                gc.fillRect(vector2D.getX(), vector2D.getY() , 1, 1);
            }
            gc.restore();
            Task<String> task = new Task<String>() {
                @Override
                protected String call() throws Exception {
                    return null;
                }
            };

        };
    }

    private void setFruit() {
        Random random = new Random();
        int randomIndex = random.nextInt(freeSpots.size());
        for (Vector2D vector2D : freeSpots) {
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

    public void keyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case UP:
                currentDirection = Orientation.NORTH;
                break;
            case RIGHT:
                currentDirection = Orientation.EAST;
                break;
            case DOWN:
                currentDirection = Orientation.SOUTH;
                break;
            case LEFT:
                currentDirection = Orientation.WEST;
                break;
        }
    }
}
