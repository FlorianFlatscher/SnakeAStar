package sample.snake.game.ai;

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
        Node[][] nodes = shortTailStar(blocked, start, target);
        return reconstructPath(nodes[((int) target.getX())][((int) target.getY())]);
    }

    private Node[][] shortTailStar(ArrayList<Point2D> blocked, Point2D start, Point2D target) {

        HashSet<String> closedSet = new HashSet<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node node, Node t1) {
                if (node.getCValue() > t1.getCValue()) {
                    return -1;
                }
                if (node.getCValue() < t1.getCValue()) {
                    return 1;
                }
                if (node.getGValue() > t1.getGValue()) {
                    return 1;
                }
                if (node.getGValue() < t1.getGValue()) {
                    return -1;
                }
                return 0;
            }
        });
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
                int offset = (neighbour.getX() == (int) target.getX() && neighbour.getY() == (int) target.getY()) ? 1 : 0;

                if (neighbour.getBlockedTimer() - current.getCValue() > 0 || closedSet.contains(neighbour + "" + neighbour.getCValue())) {
                    continue;
                }

                ArrayList<Node> currentPath = new ArrayList<>();

                currentPath.add(neighbour);

                Node from = current;

                while (from != null) {
                    currentPath.add(from);
                    from = from.getCameFrom();
                }

                for (int i = 1; i < blocked.size(); i++) {
                    Point2D n = blocked.get(i);
                    currentPath.add(nodes[((int) n.getX())][((int) n.getY())]);
                }

                Node tail = currentPath.get(blocked.size() - 1 + offset);

                ArrayList<Point2D> simulationBlocked = new ArrayList<>();

                for (int i = 0; i < blocked.size() - 1 + offset; i++) {
                    simulationBlocked.add(new Point2D(currentPath.get(i).getX(), currentPath.get(i).getY()));
                }
                Node[][] path = shortAStar(new HashSet<>(simulationBlocked), new Point2D(neighbour.getX(), neighbour.getY()), new Point2D(tail.getX(), tail.getY()));

                if (path[(int) tail.getX()][(int) tail.getY()].getCameFrom() == null) {
                    continue;
                }

//                if (!openSet.contains(neighbour) && !currentPath.subList(1, currentPath.size() - blocked.size() + 1).contains(neighbour)) {
                if (!currentPath.subList(1, blocked.size() + offset).contains(neighbour)) {
//                if (!openSet.contains(neighbour) && !simulationBlocked.subList(1, simulationBlocked.size()).contains(new Point2D(neighbour.getX(), neighbour.getY()))) {
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
            } catch (ArrayIndexOutOfBoundsException e) {

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

                if (closedSet.contains(neighbour) || neighbour.getBlockedTimer() - current.getCValue() > 0) {
                    continue;
                }
                if (neighbour.getX() == (int) target.getX() && neighbour.getY() == (int) target.getY()) {
                    neighbour.setCameFrom(current);
                    return nodes;
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


