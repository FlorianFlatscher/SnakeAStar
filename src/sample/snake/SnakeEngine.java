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
    private final SimpleDoubleProperty animationTimeScale = new SimpleDoubleProperty(1);

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
        double t = (currentNanoTime - startNanoTime) / 1000000000.0;

        //Redrawing
        gc.setFill(backgroundColor);
    }

    public double getAnimationTimeScale() {
        return animationTimeScale.get();
    }

    public SimpleDoubleProperty animationTimeScaleProperty() {
        return animationTimeScale;
    }
}
