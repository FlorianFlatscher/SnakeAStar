package sample.snake.engine;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.scene.canvas.Canvas;

import java.util.Objects;

public class GameEngine extends AnimationTimer {
    private Game game;

    //Time
    private long startNanoTime;
    private long lastFrameNanoTime;
    public static final double calculationsPerSecond = 1; //Animation unit

    private final DoubleProperty fps = new SimpleDoubleProperty(0);
    private final DoubleProperty secondsPassed = new SimpleDoubleProperty(0);
    private final LongProperty framesPassed = new SimpleLongProperty(0);

    @Override
    public void handle(long currentNanoTime) {
        //Time
        secondsPassed.setValue((currentNanoTime - startNanoTime) / 100000000000.);
        final long nanosSinceLastFrame = currentNanoTime - lastFrameNanoTime;
        final double animationUnitScale = nanosSinceLastFrame / (1000000000. / calculationsPerSecond);
        fps.setValue(1000000000. / nanosSinceLastFrame);

        Objects.requireNonNull(game, "No game to start!");
        game.update(this, animationUnitScale);

        //Time
        lastFrameNanoTime = currentNanoTime;
        framesPassed.setValue(framesPassed.get() + 1);
    }

    public void startGame(Game g) {
        this.game = Objects.requireNonNull(g);

        super.start();
        startNanoTime = lastFrameNanoTime = System.nanoTime();
    }

    public double getFps() {
        return fps.get();
    }

    public DoubleProperty fpsProperty() {
        return fps;
    }

    public long getFramesPassed() {
        return framesPassed.get();
    }

    public LongProperty framesPassedProperty() {
        return framesPassed;
    }

    public Game getGame() {
        return game;
    }

    public GameEngine setGame(Game game) {
        this.game = game;
        return this;
    }
}
