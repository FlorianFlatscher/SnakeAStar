package sample.snake.engine;

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

    public GameEngine(Canvas canvas, Class<T> gameClass)  {
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

        //Game
        Thread gameThread = new Thread(() -> {
            Canvas virtualCanvas = new Canvas();

            while (running.get()) {
                secondsPassed.setValue((System.nanoTime() - startNanoTime) / 1_000_000_000.0);
                final long nanosSinceLastFrame = System.nanoTime() - lastFrameNanoTime;
                if (nanosSinceLastFrame / 1_000_000_000.0 >= (1 / getTargetFPS())) {
                    lastFrameNanoTime = System.nanoTime();
                    fps.setValue(1_000_000_000.0 / (nanosSinceLastFrame));

                    virtualCanvas.setHeight(canvas.getHeight());
                    virtualCanvas.setWidth(canvas.getWidth());

                    game.update(virtualCanvas.getGraphicsContext2D(), nanosSinceLastFrame / 1_000_000_000.0);
                    WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
                    canvas.getGraphicsContext2D().drawImage(image, 0, 0);

                    for (GameTask<T> gameTask : gameTasks) {
                        gameTask.run(game);
                    }

                    framesPassed.setValue(framesPassed.get() + 1);


                }
            }
        });
        gameThread.setDaemon(true);
        running.set(true);
        gameThread.start();
    }

    public void addGameTask(GameTask<T> task) {
        gameTasks.add(task);
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