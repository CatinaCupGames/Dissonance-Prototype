package com.tog.framework.game.ai;

import com.tog.framework.system.utils.Validator;

import java.util.LinkedList;
import java.util.List;

public final class NodeMap {

    private Node[][] nodes;

    private int width;
    private int height;

    private List<Node> openList;
    private List<Node> closedList;

    public NodeMap(int width, int height) {
        Validator.validateNotBelow(width, 1, "width");
        Validator.validateNotBelow(height, 1, "height");

        nodes = new Node[width][height];

        this.width = width - 1;
        this.height = height - 1;

        initializeNodes();
    }

    private void initializeNodes() {
        for (int x = 0; x <= width; x++) {
            for (int y = 0; y <= height; y++) {
                nodes[x][y] = new Node(new Position(x, y));
            }
        }
    }

    public void setReachable(int x, int y, boolean reachable) {
        getNode(x, y).setReachable(reachable);
    }

    public void setReachable(Position position, boolean reachable) {
        getNode(position).setReachable(reachable);
    }

    public Node getNode(int x, int y) {
        Validator.validateInRange(x, 0, width + 1, "x");
        Validator.validateInRange(y, 0, height + 1, "y");

        return nodes[x][y];
    }

    public Node getNode(Position position) {
        return getNode(position.getX(), position.getY());
    }

    public Node[][] getNodes() {
        return nodes;
    }

    public final List<Position> findPath(Position start, Position goal) {
        Validator.validateInRange(start.getX(), 0, width + 1, "start x");
        Validator.validateInRange(start.getY(), 0, height + 1, "start y");
        Validator.validateInRange(goal.getX(), 0, width + 1, "goal x");
        Validator.validateInRange(goal.getY(), 0, height + 1, "goal y");
        openList = new LinkedList<>();
        closedList = new LinkedList<>();
        openList.add(nodes[start.getX()][start.getY()]);

        boolean found = false;
        Node currentNode;

        while (!found) {
            currentNode = lowestFInOpen();
            closedList.add(currentNode);
            openList.remove(currentNode);

            if ((currentNode.getPosition().equals(goal))) {
                return calcPath(nodes[goal.getX()][goal.getY()], currentNode);
            }

            List<Node> adjacentNodes = getAdjacent(currentNode);
            for (Node adjacent : adjacentNodes) {
                if (!openList.contains(adjacent)) {
                    adjacent.setParent(currentNode);
                    adjacent.setHCost(nodes[goal.getX()][goal.getY()]);
                    adjacent.setGCost(currentNode);
                    openList.add(adjacent);
                } else {
                    if (adjacent.getGCost() > adjacent.calculateGCost(currentNode)) {
                        adjacent.setParent(currentNode);
                        adjacent.setGCost(currentNode);
                    }
                }
            }

            if (openList.isEmpty()) {
                return new LinkedList<>();
            }
        }
        return null;
    }

    private List<Position> calcPath(Node start, Node goal) {
        LinkedList<Position> path = new LinkedList<>();

        Node curr = goal;

        while (true) {
            path.addFirst(curr.getPosition());

            if (curr.getParent() == null) {
                break;
            }
            curr = curr.getParent();

            if (curr.equals(start)) {
                break;
            }
        }
        return path;
    }

    private Node lowestFInOpen() {
        Node cheapest = openList.get(0);
        for (Node node : openList) {
            if (node.getFCost() < cheapest.getFCost()) {
                cheapest = node;
            }
        }
        return cheapest;
    }

    private List<Node> getAdjacent(Node node) {
        int x = node.getPosition().getX();
        int y = node.getPosition().getY();
        List<Node> adj = new LinkedList<>();

        Node temp;
        if (x > 0) {
            temp = this.getNode((x - 1), y);
            if (temp.isReachable() && !closedList.contains(temp)) {
                temp.setWasDiagonal(false);
                adj.add(temp);
            }
        }

        if (x < width) {
            temp = this.getNode((x + 1), y);
            if (temp.isReachable() && !closedList.contains(temp)) {
                temp.setWasDiagonal(false);
                adj.add(temp);
            }
        }

        if (y > 0) {
            temp = this.getNode(x, (y - 1));
            if (temp.isReachable() && !closedList.contains(temp)) {
                temp.setWasDiagonal(false);
                adj.add(temp);
            }
        }

        if (y < height) {
            temp = this.getNode(x, (y + 1));
            if (temp.isReachable() && !closedList.contains(temp)) {
                temp.setWasDiagonal(false);
                adj.add(temp);
            }
        }

        if (x < width && y < height) {
            temp = this.getNode((x + 1), (y + 1));
            if (temp.isReachable() && !closedList.contains(temp)) {
                temp.setWasDiagonal(true);
                adj.add(temp);
            }
        }

        if (x > 0 && y > 0) {
            temp = this.getNode((x - 1), (y - 1));
            if (temp.isReachable() && !closedList.contains(temp)) {
                temp.setWasDiagonal(true);
                adj.add(temp);
            }
        }

        if (x > 0 && y < height) {
            temp = this.getNode((x - 1), (y + 1));
            if (temp.isReachable() && !closedList.contains(temp)) {
                temp.setWasDiagonal(true);
                adj.add(temp);
            }
        }

        if (x < width && y > 0) {
            temp = this.getNode((x + 1), (y - 1));
            if (temp.isReachable() && !closedList.contains(temp)) {
                temp.setWasDiagonal(true);
                adj.add(temp);
            }
        }

        return adj;
    }
}
