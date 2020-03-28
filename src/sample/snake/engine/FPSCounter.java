package sample.snake.engine;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyLongProperty;
import javafx.beans.property.SimpleDoubleProperty;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class FPSCounter {
    private Queue<Double> history;
    private int noiseMending;
    private DoubleProperty fps = new SimpleDoubleProperty();

    public FPSCounter (int noiseMending) {
        history = new ArrayDeque<>();
        this.noiseMending = noiseMending;
    }

    public void note (double fps) {
        double newFps = this.fps.get();
        newFps *= history.size();

        newFps += fps;

        if (history.size() > noiseMending) {
            newFps -= history.poll();
        }
        history.add(fps);
        newFps /= history.size();
        this.fps.setValue(newFps);
    }

    public double getFps() {
        return fps.get();
    }

    public ReadOnlyDoubleProperty fpsProperty() {
        return fps;
    }
}
