package sample.snake.game.ai;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Side;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Light;
import javafx.scene.paint.Color;

import java.util.*;

public class SnakeBrain {
    Dimension2D dimension;

    private static final int[] fastSin = new int[]{0, 1, 0, -1};
    private static final int[] fastCos = new int[]{1, 0, -1, 0};

    public SnakeBrain(Dimension2D dimension) {
        this.dimension = dimension;

    }

    public Stack<Side> getShortestPath(ArrayList<Point2D> blocked, Point2D start, Point2D target) {
        Node[][] nodes = shortTailStar(blocked, start, target);
        return reconstructPath(nodes[((int) target.getX())][((int) target.getY())]);
    }

    private Node[][] shortTailStar(ArrayList<Point2D> blocked, Point2D start, Point2D target) {
        HashSet<String> closedSet = new HashSet<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(Node::getGValue));
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


            closedSet.add(current.toString() + "" + current.getCValue());

            HashSet<Node> neighbours = getNeighbours(nodes, current);

            for (Node neighbour : neighbours) {

                //Insert here
                if (closedSet.contains(neighbour.toString() + "" + (current.getCValue() + 1)) || neighbour.getBlockedTimer() - current.getCValue() > 0) {
                    continue;
                }
// Insert here
//                if (closedSet.contains(neighbour) || neighbour.getBlockedTimer() - current.getCValue() > 0) {
//                    continue;
//                }

                ArrayList<Point2D> simulation = new ArrayList<>();

                simulation.add(new Point2D(neighbour.getX(), neighbour.getY()));

                Node from = current;
                int offset = (neighbour.getX() == (int) target.getX() && neighbour.getY() == (int) target.getY()) ? 5 : 4;

                while (from != null && simulation.size() < blocked.size() + offset) {
                    simulation.add(new Point2D(from.getX(), from.getY()));
                    from = from.getCameFrom();
                }

                int size = simulation.size();
                for (int i = 1; i < blocked.size() + offset - size; i++) {
                    if (offset)
                    Point2D n = blocked.get(i);
                    simulation.add(new Point2D(n.getX(), n.getY()));
                }

                Point2D tail = simulation.get(simulation.size() - 1);

                Node[][] path = shortAStar(new HashSet<>(simulation.subList(0, simulation.size() - 1)), new Point2D(neighbour.getX(), neighbour.getY()), new Point2D(tail.getX(), tail.getY()));

                if (path[(int) tail.getX()][(int) tail.getY()].getCameFrom() == null) {
                    continue;
                }

                if (!simulation.subList(1, simulation.size() - offset).contains(new Point2D(neighbour.getX(), neighbour.getY()))) {
                    //Never looked at before
                    openSet.remove(neighbour);
                    neighbour.setCameFrom(current);
                    neighbour.updateDist(target);
                    neighbour.setCValue(current.getCValue() + 1);
                    openSet.offer(neighbour);

                    if (neighbour.getX() == (int) target.getX() && neighbour.getY() == (int) target.getY()) {
                        return nodes;
                    }
                }
//                else if (neighbour.getCValue() > current.getCValue() + 1) {
//                    //Found some shorter path
//                    openSet.remove(neighbour);
//                    neighbour.setCameFrom(current);
//                    neighbour.setCValue(current.getCValue() + 1);
//                    openSet.offer(neighbour);
//                }



            }
        }

        //No path found
        return nodes;
    }

    private HashSet<Node> getNeighbours(Node[][] nodes, Node current) {
        HashSet<Node> neighbours = new HashSet<>();
        for (int i = 0; i < 4; i++) {
            try {
                neighbours.add(nodes[current.getX() + fastCos[i]][current.getY() + fastSin[i]]);
            } catch (ArrayIndexOutOfBoundsException ignored) {

            }
        }
        return neighbours;
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

            closedSet.add(current);

            HashSet<Node> neighbours = getNeighbours(nodes, current);

            for (Node neighbour : neighbours) {
                if (neighbour.getX() == (int) target.getX() && neighbour.getY() == (int) target.getY()) {
                    neighbour.setCameFrom(current);
                    return nodes;
                }
                if (closedSet.contains(neighbour) || neighbour.getBlockedTimer() - current.getCValue() > 0) {
                    continue;
                }
                if (neighbour.getCameFrom() == null) {
                    //Never looked at before
                    neighbour.setCameFrom(current);
                    neighbour.updateDist(target);
                    neighbour.setCValue(current.getCValue() + 1);
                    openSet.offer(neighbour);


                } else if (neighbour.getCValue() > current.getCValue() + 1) {
                    //Found some shorter path
                    openSet.remove(neighbour);
                    neighbour.setCameFrom(current);
                    neighbour.setCValue(current.getCValue() + 1);
                    openSet.offer(neighbour);
                }
            }
        }

        //No path found
        return nodes;
    }

    private Node[][] shortAStar(HashSet<Point2D> blocked, Point2D start, Point2D target) {
        HashSet<Node> closedSet = new HashSet<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble((Node node) -> node.getCValue() + node.getGValue()).thenComparingDouble(Node::getGValue));
        Node[][] nodes = new Node[(int) dimension.getWidth()][(int) dimension.getHeight()];

        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                nodes[i][j] = new Node(i, j);
            }
        }

        for (Point2D block : blocked) {
            closedSet.add(new Node(((int) block.getX()), ((int) block.getY())));
        }

        Node startNode = nodes[((int) start.getX())][((int) start.getY())];
        startNode.updateDist(target);
        startNode.setCValue(1);
        openSet.add(startNode);

        while (openSet.size() > 0) {
            Node current = openSet.poll();
            if (current.getX() == (int) target.getX() && current.getY() == (int) target.getY()) {
                current.setCameFrom(current);
                return nodes;
            }
            closedSet.add(current);

            HashSet<Node> neighbours = getNeighbours(nodes, current);

            for (Node neighbour : neighbours) {


                if (closedSet.contains(neighbour)) {
                    continue;
                }



                if (neighbour.getCameFrom() == null) {
                    //Never looked at before
                    neighbour.setCameFrom(current);
                    neighbour.updateDist(target);
                    neighbour.setCValue(current.getCValue() + 1);
                    openSet.offer(neighbour);


                } else if (neighbour.getCValue() > current.getCValue() + 1) {
                    //Found some shorter path
                    openSet.remove(neighbour);
                    neighbour.setCameFrom(current);
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
        while (current.getCameFrom() != null) {
            if (current.getCameFrom().getX() - current.getX() < 0)
                stack.push(Side.RIGHT);
            else if (current.getCameFrom().getX() - current.getX() > 0)
                stack.push(Side.LEFT);
            else if (current.getCameFrom().getY() - current.getY() < 0)
                stack.push(Side.BOTTOM);
            else if (current.getCameFrom().getY() - current.getY() > 0)
                stack.push(Side.TOP);
            current = current.getCameFrom();
        }
        return stack;
    }


    public static double clacManhattanDist(Node node, Point2D target) {
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


}


