package com.tog.framework.game.ai;

import com.tog.framework.system.utils.Validator;

import java.util.LinkedList;
import java.util.List;

/**
 * A NodeMap contains the nodes that are being searched and provides pathfinding methods.
 */
public final class NodeMap {

    private Node[][] nodes;

    private int width;
    private int height;

    private List<Node> openList;
    private List<Node> closedList;

    /**
     * Creates a new NodeMap with the specified width and height.
     *
     * @param width  The width of the map
     * @param height The height of the map
     */
    public NodeMap(int width, int height) {
        Validator.validateNotBelow(width, 1, "width");
        Validator.validateNotBelow(height, 1, "height");

        nodes = new Node[width][height];

        this.width = width - 1;
        this.height = height - 1;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                nodes[x][y] = new Node(new Position(x, y));
            }
        }
    }

    /**
     * Sets the reachability of the node at the specified position.
     */
    public void setReachable(int x, int y, boolean reachable) {
        getNode(x, y).setReachable(reachable);
    }

    /**
     * Sets the reachability of the node at the specified position.
     */
    public void setReachable(Position position, boolean reachable) {
        getNode(position).setReachable(reachable);
    }

    /**
     * Gets the node at the specified position.
     */
    public Node getNode(int x, int y) {
        Validator.validateInRange(x, 0, width + 1, "x");
        Validator.validateInRange(y, 0, height + 1, "y");

        return nodes[x][y];
    }

    /**
     * Gets the node at the specified position.
     */
    public Node getNode(Position position) {
        return getNode(position.getX(), position.getY());
    }

    /**
     * Gets all the nodes in this NodeMap.
     */
    public Node[][] getNodes() {
        return nodes;
    }

    /**
     * Finds the optimal path from the start position to the end position.
     *
     * @param start The position to start from.
     * @param goal  The final position.
     */
    public final List<Position> findPath(Position start, Position goal) {
        Validator.validateInRange(start.getX(), 0, width + 1, "start x");
        Validator.validateInRange(start.getY(), 0, height + 1, "start y");
        Validator.validateInRange(goal.getX(), 0, width + 1, "goal x");
        Validator.validateInRange(goal.getY(), 0, height + 1, "goal y");
        openList = new LinkedList<>();
        closedList = new LinkedList<>();
        openList.add(nodes[start.getX()][start.getY()]);

        Node currentNode;

        while (true) {
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
