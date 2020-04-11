package sample.snake.game.ai;

import javafx.geometry.Point2D;

import java.util.Objects;

public class Node {
    private int x, y;
    private double gValue, cValue;
    private Node cameFrom;
    private int blockedTimer;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public double getGValue() {
        return gValue;
    }

    public void setGValue(double gValue) {
        this.gValue = gValue;
    }

    public double getCValue() {
        return cValue;
    }

    public void setCValue(double cValue) {
        this.cValue = cValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return getX() == node.getX() &&
                getY() == node.getY();
    }

    public void updateDist(Point2D target) {
        setGValue(SnakeBrain.clacManhattanDist(this, target));
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    public Node getCameFrom() {
        return cameFrom;
    }

    public void setCameFrom(Node cameFrom) {
        this.cameFrom = cameFrom;
    }



    public int getBlockedTimer() {
        return blockedTimer;
    }

    public void setBlockedTimer(int blockedTimer) {
        this.blockedTimer = blockedTimer;
    }


}
