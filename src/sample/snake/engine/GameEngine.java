package sample.snake.engine;

import javafx.animation.AnimationTimer;
import javafx.beans.property.*;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class GameEngine<GameType extends Game> {
    //Canvas
    private Canvas canvas;

    //Game
    private GameType game;
    private AtomicBoolean running = new AtomicBoolean(false);
    private final List<GameTask> gameTasks = new ArrayList<>();


    //Time
    private long startNanoTime;
    private long lastFrameNanoTime;
    private final DoubleProperty fps = new SimpleDoubleProperty(0);
    private final DoubleProperty targetFPS = new SimpleDoubleProperty(100);
    private final DoubleProperty secondsPassed = new SimpleDoubleProperty(0);
    private final LongProperty framesPassed = new SimpleLongProperty(0);

    public GameEngine(Canvas canvas, GameType game) {
        Objects.requireNonNull(canvas = canvas);
        canvas.setFocusTraversable(true);
        canvas.requestFocus();
        this.game = game;
    }


    public void start() {
        running.set(false);
        startNanoTime = lastFrameNanoTime = System.nanoTime();
        framesPassed.setValue(0);
        AtomicReference<KeyCode> lastKey = new AtomicReference<>();
        canvas.setOnKeyPressed((keyEvent -> lastKey.set(keyEvent.getCode())));

        //Game
        Thread gameThread = new Thread(() -> {
            Canvas virtualCanvas = new Canvas();

            while (running.get()) {
                secondsPassed.setValue((System.nanoTime() - startNanoTime) / 1_000_000_000.0);
                final long nanosSinceLastFrame = System.nanoTime() - lastFrameNanoTime;
                if (nanosSinceLastFrame / 1_000_000_000.0 >= (1 / getTargetFPS())) {
                    lastFrameNanoTime = System.nanoTime();

                    fps.setValue(1_000_000_000.0 / (nanosSinceLastFrame));

                    game.update(virtualCanvas.getGraphicsContext2D(), nanosSinceLastFrame / 1_000_000_000.0);
                    //redrawManager.requestRedraw(renderTask);


                    framesPassed.setValue(framesPassed.get() + 1);

                    synchronized (gameTasks) {

                    }
                }
            }
        });
        running.set(true);
        gameThread.start();
    }

    public void addGameTask() {
        synchronized (gameTasks) {
            gameTasks.addAll(Arrays.asList(tasks));
        }
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