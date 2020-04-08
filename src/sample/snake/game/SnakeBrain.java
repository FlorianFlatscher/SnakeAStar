package sample.snake.game;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Side;

import java.util.*;

public class SnakeBrain {
    Dimension2D dimension;

    private static final int[] fastSin = new int[]{0, 1, 0, -1};
    private static final int[] fastCos = new int[]{1, 0, -1, 0};

    public SnakeBrain(Dimension2D dimension) {
        this.dimension = dimension;

    }

    public Stack<Side> getShortestPath(ArrayList<Point2D> blocked, Point2D start, Point2D target) {
        Node[][] nodes = shortSnakeStar(blocked, start, target);
        return reconstructPath(nodes[((int) target.getX())][((int) target.getY())]);
    }

    private Node[][] shortSnakeStar(ArrayList<Point2D> blocked, Point2D start, Point2D target) {
        HashSet<Node> closedSet = new HashSet<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble((Node node) -> node.getCValue() + node.getGValue()).thenComparingDouble(Node::getGValue));
        Node[][] nodes = new Node[(int) dimension.getWidth()][(int) dimension.getHeight()];

        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                nodes[i][j] = new Node(i, j);
            }
        }

        for (int i = 0; i < blocked.size(); i++) {
            Point2D point2D = blocked.get(i);
            nodes[((int) point2D.getX())][((int) point2D.getY())].setBlockedTimer(blocked.size() - i);
        }

        Node startNode = nodes[((int) start.getX())][((int) start.getY())];
        startNode.updateDist(target);
        startNode.setCValue(1);
        openSet.add(startNode);

        while (openSet.size() > 0) {
            Node current = openSet.poll();
            if (current.getX() == (int) target.getX() && current.getY() == (int) target.getY()) {
                return nodes;
            }
            closedSet.add(current);

            HashSet<Node> neighbours = new HashSet<>();
            for (int i = 0; i < 4; i++) {
                try {
                    neighbours.add(nodes[current.getX() + fastCos[i]][current.getY() + fastSin[i]]);
                } catch (ArrayIndexOutOfBoundsException ignored) {

                }
            }

            for (Node neighbour : neighbours) {
                if (closedSet.contains(neighbour) || neighbour.getBlockedTimer() - current.getCValue() >= 0) {
                    continue;
                }
                if (neighbour.cameFrom == null) {
                    //Never looked at before
                    neighbour.cameFrom = current;
                    neighbour.updateDist(target);
                    neighbour.setCValue(current.getCValue() + 1);
                    openSet.offer(neighbour);
                } else if (neighbour.getCValue() > current.getCValue() + 1) {
                    //Found some shorter path
                    openSet.remove(neighbour);
                    neighbour.cameFrom = current;
                    neighbour.setCValue(current.getCValue() + 1);
                    openSet.offer(neighbour);
                }
            }
        }

        //No path found
        return nodes;
    }

    public Stack<Side> hamoltonianSnakeStar() {
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


    private static double clacDist(Node node, Point2D target) {
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

    static class Node {
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

        @Override
        public String toString() {
            return "Node{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }

        public int getBlockedTimer() {
            return blockedTimer;
        }

        public void setBlockedTimer(int blockedTimer) {
            this.blockedTimer = blockedTimer;
        }
    }

}

