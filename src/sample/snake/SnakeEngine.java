package sample.snake;

import javafx.animation.AnimationTimer;
import javafx.beans.property.SimpleDoubleProperty;
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
        final double totalSecondsPassed = (currentNanoTime - startNanoTime) / 100000000000.0;
        final long timePassedSincePastFrame = currentNanoTime - lastFrameNanoTime;
        final double animationTimeScale = timePassedSincePastFrame / (1000000000. / calculationsPerSecond);
        final double fps = animationTimeScale * calculationsPerSecond;
        System.out.println("fps: " + Math.round(fps));

        //Redrawing
        gc.setFill(backgroundColor);

        //Time
        lastFrameNanoTime = currentNanoTime;
    }
}
