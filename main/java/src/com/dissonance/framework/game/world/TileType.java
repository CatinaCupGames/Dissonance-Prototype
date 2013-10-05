package com.dissonance.framework.game.world;

public enum TileType {
    //TODO Add all tiles and there Tiled tileset ID
    DIRT(1);

    int ID;
    boolean passable;
    TileType(int ID, boolean passable) { this.ID = ID; this.passable = passable; }
    TileType(int ID) { this(ID, true); }

    public int getID() {
        return ID;
    }

    public boolean isPassable() {
        return passable;
    }


}
