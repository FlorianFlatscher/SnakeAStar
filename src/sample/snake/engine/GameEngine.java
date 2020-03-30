package sample.snake.engine;

import javafx.animation.AnimationTimer;
import javafx.beans.property.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

import javax.swing.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class GameEngine{
    private boolean running;
    private Canvas canvas;


    //Time
    private long startNanoTime;
    private long lastFrameNanoTime;

    private final DoubleProperty fps = new SimpleDoubleProperty(0);
    private final DoubleProperty secondsPassed = new SimpleDoubleProperty(0);
    private final LongProperty framesPassed = new SimpleLongProperty(0);
    private final DoubleProperty targetFPS = new SimpleDoubleProperty(100);

    public GameEngine (Canvas canvas) {
        Objects.requireNonNull(this.canvas = canvas);

    }

    public void startGame(Game g) {
        startNanoTime = lastFrameNanoTime = System.nanoTime();
        running = true;


        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                secondsPassed.setValue((System.nanoTime() - startNanoTime) / 1_000_000_000.0);
                RedrawTask redrawTask = null;
                final long nanosSinceLastFrame = System.nanoTime() - lastFrameNanoTime;
                if (nanosSinceLastFrame / 1_000_000_000.0 >= (1/getTargetFPS())) {
                    lastFrameNanoTime = System.nanoTime();

                    fps.setValue(1_000_000_000.0 / (nanosSinceLastFrame));

                    RedrawTask renderTask = g.update(GameEngine.this, nanosSinceLastFrame / 1_000_000_000.0);
                    renderTask.draw(canvas.getGraphicsContext2D());
                    //redrawManager.requestRedraw(renderTask);
                    framesPassed.setValue(framesPassed.get() + 1);
                }
                if (redrawTask != null) {
                    redrawTask.draw(canvas.getGraphicsContext2D());
                }
            }
        };

        timer.start();

    }

    public void stop() {
        running = false;
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