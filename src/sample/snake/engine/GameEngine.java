package sample.snake.engine;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
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

//        Timeline timeline = new Timeline(60, new KeyFrame(Duration.millis(1000 / 60.), new EventHandler<ActionEvent>() {
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {

//                secondsPassed.setValue((System.nanoTime() - startNanoTime) / 1_000_000_000.0);
                final long nanosSinceLastFrame = System.nanoTime() - lastFrameNanoTime;

                game.update(canvas.getGraphicsContext2D(), 1/60.);
                lastFrameNanoTime = System.nanoTime();

            }
        };
        animationTimer.start();
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