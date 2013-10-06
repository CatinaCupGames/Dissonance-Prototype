package com.dissonance.framework.game.world;

public enum TileType {
    //TODO Add all tiles and there Tiled tileset ID
    DIRT(1);

    int ID;
    boolean passable;
    int extraCost;

    TileType(int ID, boolean passable, int extraCost) {
        this.ID = ID;
        this.passable = passable;
        this.extraCost = extraCost;
    }

    TileType(int ID, boolean passable) {
        this(ID, passable, 0);
    }

    TileType(int ID) {
        this(ID, true, 0);
    }

    public int getID() {
        return ID;
    }

    public boolean isPassable() {
        return passable;
    }

    /**
     * The extra cost is the extra time that the characters need to
     * move through this title. It's used by the enemy AI.
     *
     * @return The tile's extra cost.
     */
    public int getExtraCost() {
        return extraCost;
    }
}
