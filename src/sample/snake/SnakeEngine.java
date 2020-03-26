package sample.snake;

import javafx.animation.AnimationTimer;
import javafx.beans.property.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.Objects;
import java.util.Optional;

public class SnakeEngine extends AnimationTimer {
    //Canvas
    private GraphicsContext gc;
    private Canvas canvas;

    //Time
    private final long startNanoTime = System.nanoTime();
    private long lastFrameNanoTime = startNanoTime;
    public static final double calculationsPerSecond = 60;

    private final DoubleProperty fps = new SimpleDoubleProperty(0);
    private final DoubleProperty totalSecondsPassed = new SimpleDoubleProperty(0);
    private final LongProperty framesPassed = new SimpleLongProperty(0);



    //Colors
    public static final Paint backgroundColor = Color.WHITE;

    public SnakeEngine(Canvas canvas) {
        //Canvas
        Objects.requireNonNull(canvas);
        this.gc = canvas.getGraphicsContext2D();
        this.canvas = canvas;
    }

    @Override
    public void handle(long currentNanoTime)
    {
        //Time
        totalSecondsPassed.setValue((currentNanoTime - startNanoTime) / 100000000000.);
        final long nanosSinceLastFrame = currentNanoTime - lastFrameNanoTime;
        final double animationTimeScale = nanosSinceLastFrame / (1000000000. / calculationsPerSecond);
        fps.setValue(1000000000. / nanosSinceLastFrame);

        //Redrawing
        gc.setFill(backgroundColor);

        //Time
        lastFrameNanoTime = currentNanoTime;
        framesPassed.setValue(framesPassed.get() + 1);
    }

    public double getFps() {
        return fps.get();
    }

    public ReadOnlyDoubleProperty fpsProperty() {
        return fps;
    }

    public long getFramesPassed() {
        return framesPassed.get();
    }

    public ReadOnlyLongProperty framesPassedProperty() {
        return framesPassed;
    }
}
