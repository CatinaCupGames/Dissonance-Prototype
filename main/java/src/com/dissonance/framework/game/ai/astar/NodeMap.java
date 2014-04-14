package com.dissonance.framework.game.ai.astar;

import com.dissonance.framework.game.world.Tile;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.LayerType;
import com.dissonance.framework.system.utils.Validator;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * A NodeMap contains the nodes that are being searched and provides pathfinding methods.
 */
public final class NodeMap implements Serializable {

    private Node[][] nodes;
    private int width;
    private int height;
    private List<Node> openList;
    private List<Node> closedList;
    private World world;

    /**
     * Creates a new NodeMap with the specified width and height.
     *
     * @param width  The width of the map
     * @param height The height of the map
     */
    public NodeMap(World world, int width, int height) {
        Validator.validateNotBelow(width, 1, "width");
        Validator.validateNotBelow(height, 1, "height");
        Validator.validateNotNull(world, "world");

        nodes = new Node[width][height];

        this.width = width - 1;
        this.height = height - 1;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                nodes[x][y] = new Node(new Position(x, y));
            }
        }

        this.world = world;
    }

    public void readMap() {
        String fileName = "config" + File.separator + world.getName() + ".nodes";
        try (DataInputStream stream = new DataInputStream(new GZIPInputStream(new FileInputStream(fileName)))) {
            for (Node[] nodeArray : nodes) {
                for (Node node : nodeArray) {
                    node.readNode(stream);
                }
            }
            System.out.println("NodeMap.java: Successfully read map!");
        } catch (IOException e) {
            System.out.println("NodeMap.java: Error reading map! Constructing new map!");
            constructMap();
        }
    }

    public void constructMap() {
        String fileName = "config" + File.separator + world.getName() + ".nodes";
        new File(fileName).delete();

        Layer[] layers = world.getLayers(LayerType.TILE_LAYER);
        for (int x = 0; x < world.getTiledData().getWidth(); x++) {
            for (int y = 0; y < world.getTiledData().getHeight(); y++) {
                for (Layer layer : layers) {
                    Tile t = world.getTileAt(x, y, layer);
                    if (t != null) {
                        nodes[x][y].setPassable(t.isPassable());
                        nodes[x][y].setExtraCost(t.getExtraCost());
                    } else {
                        if (nodes[x][y] == null) {
                            nodes[x][y] = new Node(new Position(x, y));
                            nodes[x][y].setPassable(false);
                        }
                        nodes[x][y].setPassable(false); //No tile should default to unpassable..always
                    }
                }
            }
        }
        System.out.println("NodeMap.java: Successfully constructed map!");
        saveMap();
    }

    public void saveMap() {
        String fileName = "config" + File.separator + world.getName() + ".nodes";
        try (DataOutputStream stream = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(fileName)))) {
            for (Node[] nodeArray : nodes) {
                for (Node node : nodeArray) {
                    node.saveNode(stream);
                }
            }
            System.out.println("NodeMap.java: Successfully saved map!");
        } catch (IOException e) {
            System.out.println("NodeMap.java: Error saving map!");
        }
    }

    /**
     * Sets the reachability of the node at the specified position.
     */
    public void setReachable(int x, int y, boolean reachable) {
        getNode(x, y).setPassable(reachable);
    }

    /**
     * Sets the reachability of the node at the specified position.
     */
    public void setReachable(Position position, boolean reachable) {
        getNode(position).setPassable(reachable);
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
        return getNode((int) position.x, (int) position.y);
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
        openList.add(nodes[FastMath.fastFloor(start.x)][FastMath.fastFloor(start.y)]);

        Node currentNode;

        while (true) {
            currentNode = lowestFInOpen();
            closedList.add(currentNode);
            openList.remove(currentNode);

            if ((currentNode.getPosition().equals(goal))) {
                return calcPath(nodes[FastMath.fastFloor(goal.x)][FastMath.fastFloor(goal.y)], currentNode);
            }

            List<Node> adjacentNodes = getAdjacent(currentNode);
            for (Node adjacent : adjacentNodes) {
                if (!openList.contains(adjacent)) {
                    adjacent.setParent(currentNode);
                    adjacent.setHCost(nodes[FastMath.fastFloor(goal.x)][FastMath.fastFloor(goal.y)]);
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
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        nodes[x][y].reset();
                    }
                }
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

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                nodes[x][y].reset();
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
        int x = FastMath.fastFloor(node.getPosition().x);
        int y = FastMath.fastFloor(node.getPosition().y);
        List<Node> adj = new LinkedList<>();

        Node temp;
        if (x > 0) {
            temp = this.getNode((x - 1), y);
            if (temp.isPassable() && !closedList.contains(temp)) {
                temp.setWasDiagonal(false);
                adj.add(temp);
            }
        }

        if (x < width) {
            temp = getNode((x + 1), y);
            if (temp.isPassable() && !closedList.contains(temp)) {
                temp.setWasDiagonal(false);
                adj.add(temp);
            }
        }

        if (y > 0) {
            temp = getNode(x, (y - 1));
            if (temp.isPassable() && !closedList.contains(temp)) {
                temp.setWasDiagonal(false);
                adj.add(temp);
            }
        }

        if (y < height) {
            temp = getNode(x, (y + 1));
            if (temp.isPassable() && !closedList.contains(temp)) {
                temp.setWasDiagonal(false);
                adj.add(temp);
            }
        }

        boolean hasUnreachable = false;

        if (x < width && y < height) {
            if (!getNode((x + 1), (y + 1)).isPassable()) {
                hasUnreachable = true;
            }
        }

        if (x > 0 && y > 0) {
            temp = this.getNode((x - 1), (y - 1));
            if (!temp.isPassable()) {
                hasUnreachable = true;
            }
        }

        if (x > 0 && y < height) {
            temp = this.getNode((x - 1), (y + 1));
            if (!temp.isPassable()) {
                hasUnreachable = true;
            }
        }

        if (x < width && y > 0) {
            temp = this.getNode((x + 1), (y - 1));
            if (!temp.isPassable()) {
                hasUnreachable = true;
            }
        }


        if (x < width && y < height) {
            temp = this.getNode((x + 1), (y + 1));
            if (temp.isPassable() && !closedList.contains(temp) && !hasUnreachable) {
                temp.setWasDiagonal(true);
                adj.add(temp);
            }
        }

        if (x > 0 && y > 0) {
            temp = this.getNode((x - 1), (y - 1));
            if (temp.isPassable() && !closedList.contains(temp) && !hasUnreachable) {
                temp.setWasDiagonal(true);
                adj.add(temp);
            }
        }

        if (x > 0 && y < height) {
            temp = this.getNode((x - 1), (y + 1));
            if (temp.isPassable() && !closedList.contains(temp) && !hasUnreachable) {
                temp.setWasDiagonal(true);
                adj.add(temp);
            }
        }

        if (x < width && y > 0) {
            temp = this.getNode((x + 1), (y - 1));
            if (temp.isPassable() && !closedList.contains(temp) && !hasUnreachable) {
                temp.setWasDiagonal(true);
                adj.add(temp);
            }
        }

        return adj;
    }
}
