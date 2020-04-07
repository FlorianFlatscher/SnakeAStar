package sample.snake.game;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Side;

import java.util.*;

public class SnakeBrain {
    Dimension2D dimension;

    public SnakeBrain(Dimension2D dimension) {
        this.dimension = dimension;
    }

    public Stack<Side> getShortestPath(HashSet<Point2D> blocked, Point2D start, Point2D target) {
        HashSet<Node> closedSet = new HashSet<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble((Node node) -> node.getCValue() + node.getGValue()).thenComparingDouble(Node::getGValue));

        for (Point2D point2D : blocked) {
            closedSet.add(new Node((int) (point2D.getX()), (int) Math.floor(point2D.getY())));
        }

        Node startNode = new Node((int) start.getX(), (int) start.getY());
        startNode.updateDist(target);
        startNode.setCValue(1);
        openSet.add(startNode);

        while (openSet.size() > 0) {
            Node current = openSet.poll();
            closedSet.add(current);
            if (current.getX() == (int) target.getX() && current.getY() == (int) target.getY()) {
                return reconstructPath(current);
            }
            for (Node neighbour : getNeighbours(current)) {
                if (closedSet.contains(neighbour))
                    continue;
                if (neighbour.getCValue() < current.getCValue() + 1) {
                    if (neighbour.cValue == 0) {
                        //Never looked at before
                        neighbour.cameFrom = current;
                        neighbour.updateDist(target);
                        neighbour.setCValue(current.cValue + 1);
                        openSet.add(neighbour);
                    } else {
                        //Fond a shorter path
                        openSet.remove(neighbour);
                        neighbour.cameFrom = current;
                        neighbour.setCValue(current.getCValue() + 1);
                        openSet.add(neighbour);
                    }
                }
            }
        }
        //No path found
        return null;
    }

    private Stack<Side> reconstructPath(Node current) {
        Stack<Side> stack = new Stack<>();
        while (current.cameFrom != null) {
            if (current.cameFrom.getX() - current.getX() < 0)
                stack.push(Side.RIGHT);
            else if (current.cameFrom.getX() - current.getX() > 0)
                stack.push(Side.LEFT);
            else if (current.cameFrom.getY() - current.getY() < 0)
                stack.push(Side.BOTTOM);
            else if (current.cameFrom.getY() - current.getY() > 0)
                stack.push(Side.TOP);
            current = current.cameFrom;
        }
        return stack;
    }

    private HashSet<Node> getNeighbours (Node n) {
        HashSet<Node> neighbours = new HashSet<>();
        neighbours.add(new Node(n.getX() - 1, n.getY()));
        neighbours.add(new Node(n.getX(), n.getY() - 1));
        neighbours.add(new Node(n.getX() + 1, n.getY()));
        neighbours.add(new Node(n.getX(), n.getY() + 1));
        neighbours.removeIf(node -> node.x < 0 || node.x >= dimension.getWidth() || node.y < 0 || node.y >= dimension.getHeight());
        return neighbours;
    }

    private static double clacDist (Node node, Point2D target) {
        int targetX = ((int) target.getX());
        int targetY = ((int) target.getY());
        int deltaX = Math.abs(node.getX() - targetX);
        int deltaY = Math.abs(node.getY() - targetY);

        if (deltaX > deltaY) {
            return 2 * deltaY + (deltaX - deltaY);
        } else {
            return 2 * deltaX + (deltaY - deltaX);
        }
    }

    private static class Node {
        private int x, y;
        private double gValue, cValue;
        private Node cameFrom;

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
            setGValue(SnakeBrain.clacDist(this, target));
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
    }

}

