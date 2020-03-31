package sample.snake.engine;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import sample.snake.game.SnakeGame;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class GameEngine<T extends Game> {
    //Canvas
    private Canvas canvas;

    //Game
    private T game;
    private AtomicBoolean running = new AtomicBoolean(false);
    private final ConcurrentLinkedDeque<GameTask<T>> gameTasks = new ConcurrentLinkedDeque<>();


    //Time
    private long startNanoTime;
    private long lastFrameNanoTime;
    private final DoubleProperty fps = new SimpleDoubleProperty(0);
    private final DoubleProperty targetFPS = new SimpleDoubleProperty(100);
    private final DoubleProperty secondsPassed = new SimpleDoubleProperty(0);
    private final LongProperty framesPassed = new SimpleLongProperty(0);

    public GameEngine(Canvas canvas, Class<T> gameClass) {
        Objects.requireNonNull(this.canvas = canvas);
        canvas.setFocusTraversable(true);
        canvas.requestFocus();
        try {
            game = gameClass.getConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public void start() {
        running.set(false);
        startNanoTime = lastFrameNanoTime = System.nanoTime();
        framesPassed.setValue(0);
        AtomicReference<KeyCode> lastKey = new AtomicReference<>();
        canvas.setOnKeyPressed((keyEvent -> lastKey.set(keyEvent.getCode())));

        Timeline line = new Timeline(120, new KeyFrame());
            @Override
            public void handle(long l) {
                secondsPassed.setValue((System.nanoTime() - startNanoTime) / 1_000_000_000.0);
                final long nanosSinceLastFrame = System.nanoTime() - lastFrameNanoTime;
                fps.setValue(1_000_000_000.0 / (nanosSinceLastFrame));
                lastFrameNanoTime = System.nanoTime();

                framesPassed.setValue(framesPassed.get() + 1);
            }


        final LongProperty lastUpdateTime = new SimpleLongProperty(0);
        final AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long timestamp) {
                if (lastUpdateTime.get() > 0) {
                    long elapsedTime = timestamp - lastUpdateTime.get();
                    checkCollisions(ballContainer.getWidth(), ballContainer.getHeight());
                    game.update(canvas.getGraphicsContext2D(), 1/60.);

                    frameStats.addFrame(elapsedTime);
                }
                lastUpdateTime.set(timestamp);
            }

        };
        timer.start();

    }

    public void stopGame() {
        running.set(false);
    }

    public double getFps() {
        return fps.get();
    }

    public double getTargetFPS() {
        return targetFPS.get();
    }

    public DoubleProperty targetFPSProperty() {
        return targetFPS;
    }

    public long getFramesPassed() {
        return framesPassed.get();
    }
}