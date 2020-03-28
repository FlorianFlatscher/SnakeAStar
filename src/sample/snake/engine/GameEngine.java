package sample.snake.engine;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
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
    private final DoubleProperty targetFPS = new SimpleDoubleProperty(60);

    public GameEngine (Canvas canvas) {
        Objects.requireNonNull(this.canvas = canvas);
    }

    public void startGame(Game g) {
        startNanoTime = lastFrameNanoTime = System.nanoTime();
        running = true;

        CanvasRedrawTaskManager redrawManager = new CanvasRedrawTaskManager(canvas);
        redrawManager.start();

        Thread t = new Thread(() -> {
            while (running) {
                secondsPassed.setValue((System.nanoTime() - startNanoTime) / 1_000_000_000.);
                final long nanosSinceLastFrame = System.nanoTime() - lastFrameNanoTime;

                if (nanosSinceLastFrame >= 1_000_000_000. * (1/getTargetFPS())) {
                    fps.setValue(1_000_000_000. / (nanosSinceLastFrame));

                    RedrawTask renderTask = g.update(this, nanosSinceLastFrame / 1_000_000_000.0);
                    redrawManager.requestRedraw(renderTask);
                    lastFrameNanoTime = System.nanoTime();
                    framesPassed.setValue(framesPassed.get() + 1);
                }
            }
        });
        t.setDaemon(true);
        t.start();

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

class CanvasRedrawTaskManager extends AnimationTimer {
    private final AtomicReference<RedrawTask> data = new AtomicReference<>(null);
    private Canvas canvas;

    public CanvasRedrawTaskManager(Canvas c) {
        canvas = c;
    }

    public void requestRedraw(RedrawTask dataToDraw) {
        data.set(dataToDraw);
        start(); // in case, not already started
    }

    public void handle(long now) {
        // check if new data is available
        RedrawTask dataToDraw = data.getAndSet(null);
        if (dataToDraw != null) {
            dataToDraw.draw(canvas.getGraphicsContext2D());
        }
    }
}

