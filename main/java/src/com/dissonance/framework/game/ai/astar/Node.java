package com.dissonance.framework.game.ai.astar;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.system.utils.physics.Collidable;
import com.dissonance.framework.system.utils.physics.HitBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

public final class Node implements Serializable {

    private final static int MOVE_COST = 10;
    private final static int DIAGONAL_MOVE_COST = 14;

    private Position position;
    private boolean passable;

    private Node parent;
    private boolean wasDiagonal;

    private int extraCost;
    private int gCost;
    private int hCost;

    public Node(Position position) {
        this(position, true, 0);
    }

    public Node(Position position, boolean passable, int extraCost) {
        this.position = position;
        this.passable = passable;
        this.extraCost = extraCost;
    }

    public void saveNode(DataOutputStream stream) throws IOException {
        stream.writeFloat(position.getX());
        stream.writeFloat(position.getY());
        stream.writeBoolean(passable);
        stream.writeInt(extraCost);
    }

    public void readNode(DataInputStream stream) throws IOException {
        position.setX(stream.readFloat());
        position.setY(stream.readFloat());
        passable = stream.readBoolean();
        extraCost = stream.readInt();
    }

    public void reset() {
        parent = null;
        wasDiagonal = false;
        gCost = 0;
        hCost = 0;
    }

    /**
     * A boolean indicating whether the move to this node was diagonal.
     */
    public boolean wasDiagonal() {
        return wasDiagonal;
    }

    /**
     * Sets whether the move from the parent node was diagonal.
     *
     * @param wasDiagonal A boolean indicating if the move was diagonal.
     */
    public void setWasDiagonal(boolean wasDiagonal) {
        this.wasDiagonal = wasDiagonal;
    }

    /**
     * Sets the position of this node.
     *
     * @param position The position of this node.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Gets the position of this node.
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the reachability of this node.
     */
    public void setPassable(boolean passable) {
        this.passable = passable;
    }

    /**
     * Gets the reachability of this node.
     */
    public boolean isPassable() {
        return passable;
    }

    /**
     * Gets the parent(previous) node of this node.
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Sets the parent (previous) node of this node.
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }

    /*
     * Gets the extra cost of movement to this node.
     */
    public int getExtraCost() {
        return extraCost;
    }

    /**
     * Sets the extra cost for this node. This could be used
     * for certain paths where movement speed is slower.<br />
     * The node's G cost will be updated with the new extra cost.
     */
    public void setExtraCost(int extraCost) {
        gCost = (gCost - this.extraCost) + extraCost;

        this.extraCost = extraCost;
    }

    /**
     * Gets the F (total) cost of this node. This is calculated
     * every time it's used.
     */
    public int getFCost() {
        return gCost + hCost;
    }

    /**
     * Gets the G cost from the start node to this node.
     */
    public int getGCost() {
        return gCost;
    }

    /**
     * Sets the G cost for this node. If this node has extra cost
     * it will be added to the G cost.
     */
    public void setGCost(int gCost) {
        this.gCost = gCost + extraCost;
    }

    /**
     * Sets the G cost of this node including the cost of the parent node.
     *
     * @param parent   The parent node of this node.
     * @param diagonal Whether the movement from the parent node was diagonal.
     */
    public void setGCost(Node parent, boolean diagonal) {
        setGCost(parent.getGCost() + (diagonal ? DIAGONAL_MOVE_COST : MOVE_COST));
    }

    /**
     * Sets the G cost of this node including the cost of the parent node using the
     * {@link #wasDiagonal} boolean to tell if the movement was diagonal.
     *
     * @param parent The parent node of this node.
     */
    public void setGCost(Node parent) {
        setGCost(parent, wasDiagonal);
    }

    /**
     * Calculates the G cost of this node using the {@link #wasDiagonal} boolean to
     * tell if the movement was diagonal.
     *
     * @param parent The parent node of this node;
     */
    public int calculateGCost(Node parent) {
        return parent.getGCost() + (wasDiagonal ? DIAGONAL_MOVE_COST : MOVE_COST);
    }


    /**
     * Get the estimated H (heuristic) cost from this node to the goal (end) node.
     */
    public int getHCost() {
        return hCost;
    }

    /**
     * Sets the H (heuristic) cost from this node to the goal (end) node.
     */
    public void setHCost(int hCost) {
        this.hCost = hCost;
    }

    /**
     * Sets the H (heuristic) cost to the specified end node using the <i>Manehattan</i> method.
     */
    public void setHCost(Node endNode) {
        setHCost((int) ((Math.abs(position.getX() - endNode.getPosition().getX()) +
                Math.abs(position.getY() - endNode.getPosition().getY()))
                * MOVE_COST));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Node)) {
            return false;
        }

        Node node = (Node) obj;

        return (node.position.equals(position));
    }
}
