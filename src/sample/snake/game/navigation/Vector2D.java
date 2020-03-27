package sample.snake.game.navigation;

import java.util.Objects;

public class Vector2D {
    private double x,y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public Vector2D setX(double x) {
        this.x = x;
        return this;
    }

    public double getY() {
        return y;
    }

    public Vector2D setY(double y) {
        this.y = y;
        return this;
    }

    public void multiply(double factor) {
        x *= factor;
        y *= factor;
    }

    public void add(Vector2D other) {
        x += other.getX();
        y += other.getY();
    }

    public double getOrientation() {
        return Math.atan(this.y / this.x);
    }

    public static Vector2D fromOrientation(double radians) {
        return new Vector2D(Math.cos(radians), Math.sin(radians));
    }

    public static Vector2D fromOrientation(Orientation orientation) {
        return fromOrientation(orientation.toRadians());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector2D vector2D = (Vector2D) o;
        return Double.compare(vector2D.getX(), getX()) == 0 &&
                Double.compare(vector2D.getY(), getY()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    public Vector2D cloneVector2D() {
        return new Vector2D(x, y);
    }

    @Override
    public String toString() {
        return "Vector2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
