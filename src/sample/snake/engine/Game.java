package sample.snake.engine;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public abstract class Game {
    public abstract void update(GraphicsContext graphicsContext, double deltaTime);
}
