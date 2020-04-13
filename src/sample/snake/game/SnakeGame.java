package sample.snake.game;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import sample.snake.game.ai.SnakeBrain;

import java.util.*;

public class SnakeGame {


    //Game
    private Dimension2D dimension;
    private ArrayList<Point2D> snakeTiles;
    private Point2D fruit;
    private Side currentOrientation = Side.RIGHT;
    private Side scheduledOrientation = currentOrientation;
    private double movementOffset;
    private SnakeBrain brain;
    private Stack<Side> path;

    //Settings
    public SimpleDoubleProperty snakeSpeed = new SimpleDoubleProperty(50);

    public SnakeGame(Dimension2D dimension) {
        //Game
        this.dimension = dimension;
        snakeTiles = new ArrayList<>();
        for (int i = 2; i >= 0; --i) {
            snakeTiles.add(new Point2D(i, 0));
        }
        setFruit();

        brain = new SnakeBrain(dimension);
        path = brain.getShortestPath(new ArrayList<>(snakeTiles), snakeTiles.get(0), fruit);
        currentOrientation = path.pop();
    }


    public boolean update(GraphicsContext gc) {

        //Background
        gc.setFill(Color.WHITE);
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        //Movement
        double distanceLeft = snakeSpeed.get();
        while (distanceLeft > 0) {
            double stepSize = Math.min(distanceLeft, 1 - movementOffset);
            distanceLeft -= stepSize;

            movementOffset += stepSize;
            if (movementOffset >= 1) {


                movementOffset = 0;
                addNewSnakePoint();

                if (path.size() == 0) {
                    System.out.println("fruit = " + fruit);
                    path = brain.getShortestPath(new ArrayList<>(snakeTiles), snakeTiles.get(0), fruit);
                }

                if (path != null) {
                    currentOrientation = path.pop();
                } else {
                    render(gc);
                    return false;
                }
            }
        }
        render(gc);
        return true;
    }

    private void render(GraphicsContext gc) {
        Canvas canvas = gc.getCanvas();

        double scaleX = canvas.getWidth() / dimension.getWidth();
        double scaleY = canvas.getHeight() / dimension.getHeight();

        gc.save();
        gc.scale(scaleX, scaleY);



        //Fruit
        gc.setFill(Color.ORANGE);
        gc.fillRect(fruit.getX(), fruit.getY(), 1, 1);

        //Snake
        gc.setFill(Color.GREENYELLOW);
        for (int i = 0; i < snakeTiles.size() - 1; i++) {
            Point2D point = snakeTiles.get(i);
            gc.fillRect(point.getX(), point.getY(), 1, 1);
        }
        gc.save();
        gc.translate(-0.5, -0.5);
        Point2D centerFront = snakeTiles.get(0).add(new Point2D(0.5, 0.5)).add(getOrientationUnitVector().multiply(movementOffset));
//        gc.setFill(Color.BLACK);
        gc.fillRect(centerFront.getX(), centerFront.getY(), 1, 1);
        Point2D centerBack = null;
        Point2D frontBlock = snakeTiles.get(0).add(getOrientationUnitVector());
        if (frontBlock.equals(fruit)) {
            centerBack = snakeTiles.get(snakeTiles.size() - 1).add(0.5, 0.5);
        } else {
            centerBack = snakeTiles.get(snakeTiles.size() - 1).add(snakeTiles.get(snakeTiles.size() - 2).subtract(snakeTiles.get(snakeTiles.size() - 1)).multiply(movementOffset).add(0.5, 0.5));
        }
//        gc.setFill(Color.BLUE);
        gc.fillRect(centerBack.getX(), centerBack.getY(), 1, 1);
        gc.restore();
        gc.restore();
    }

    private Point2D getOrientationUnitVector() {
        switch (currentOrientation) {
            case TOP:
                return new Point2D(0, -1);
            case LEFT:
                return new Point2D(-1, 0);
            case BOTTOM:
                return new Point2D(0, +1);
            case RIGHT:
                return new Point2D(+1, 0);
        }
        throw new IllegalArgumentException();
    }

    private void setFruit() {
        Point2D point;
        do {
            Random random = new Random();
//            point = new Point2D(dimension.getWidth() - 1, random.nextInt((int) dimension.getHeight()));
            point = new Point2D(random.nextInt((int) dimension.getWidth()), random.nextInt((int) dimension.getHeight()));
        } while (snakeTiles.contains(point));
        fruit = point;
    }

    private void addNewSnakePoint() {
        snakeTiles.add(0, snakeTiles.get(0).add(getOrientationUnitVector()));
        if (!snakeTiles.get(0).equals(fruit)) {
            snakeTiles.remove(snakeTiles.size() - 1);
        } else {
            setFruit();
        }
    }

    public void keyPressed(KeyEvent event) {
//        switch (event.getCode()) {
//            case W:
//                scheduledOrientation = Side.TOP;
//                break;
//            case A:
//                scheduledOrientation = Side.LEFT;
//                break;
//            case S:
//                scheduledOrientation = Side.BOTTOM;
//                break;
//            case D:
//                scheduledOrientation = Side.RIGHT;
//                break;
//            default:
//                break;
//        }
    }

    public double getSnakeSpeed() {
        return snakeSpeed.get();
    }

    public SimpleDoubleProperty snakeSpeedProperty() {
        return snakeSpeed;
    }
}
