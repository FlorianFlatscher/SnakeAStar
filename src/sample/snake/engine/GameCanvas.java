package sample.snake.engine;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.Binding;
import javafx.beans.property.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


public class GameCanvas extends Canvas {
    //Canvas
    private GraphicsContext gc;

    //Time
    private final long startNanoTime = System.nanoTime();
    private long lastFrameNanoTime = startNanoTime;
    public static final double calculationsPerSecond = 1; //Animation unit

    private final DoubleProperty fps = new SimpleDoubleProperty(0);
    private final DoubleProperty SecondsPassed = new SimpleDoubleProperty(0);
    private final LongProperty framesPassed = new SimpleLongProperty(0);

    private AnimationTimer engine;

    public GameCanvas() {
        //Canvas
        this.gc = this.getGraphicsContext2D();

        engine = new AnimationTimer() {
            @Override
            public void handle(long currentNanoTime)
            {
                //Time
                SecondsPassed.setValue((currentNanoTime - startNanoTime) / 100000000000.);
                final long nanosSinceLastFrame = currentNanoTime - lastFrameNanoTime;
                final double animationUnitScale = nanosSinceLastFrame / (1000000000. / calculationsPerSecond);
                fps.setValue(1000000000. / nanosSinceLastFrame);


                //Time
                lastFrameNanoTime = currentNanoTime;
                framesPassed.setValue(framesPassed.get() + 1);
            }
        };
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

    public void startEngine() {
        engine.start();
    }
}
