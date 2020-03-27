package sample.snake.game;

import javafx.scene.canvas.GraphicsContext;
import sample.snake.engine.Game;
import sample.snake.engine.GameCanvas;
import sample.snake.engine.GameEngine;

import java.util.Objects;

public class SnakeGame implements Game {
    //Engine
    GameCanvas canvas;
    GraphicsContext gc;
    GameEngine engine;

    //Game
    public SnakeGame(GameCanvas canvas) {
        Objects.requireNonNull(canvas);
        this.canvas = canvas;
        this.gc = canvas.getGraphicsContext2D();
    }
    @Override
    public void update(GameEngine g, double animationUnitScale) {

    }
}
