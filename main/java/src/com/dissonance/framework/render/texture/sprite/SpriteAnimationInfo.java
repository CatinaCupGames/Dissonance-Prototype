package com.dissonance.framework.render.texture.sprite;

public class SpriteAnimationInfo {
    private String name;
    private int row;
    private int count;
    private long default_speed;
    private boolean loop;
    private int lastRow;

    SpriteAnimationInfo(String name, long default_speed, int row, int count, boolean loop, int lastRow) {
        this.name = name;
        this.default_speed = default_speed;
        this.row = row;
        this.count = count;
        this.loop = loop;
        this.lastRow = lastRow;
    }

    public int getLastRow() {
        return lastRow;
    }

    public String getName() {
        return name;
    }

    public long getDefaultSpeed() {
        return default_speed;
    }

    public boolean doesLoop() {
        return loop;
    }

    public int size() {
        return count;
    }

    int getRow() {
        return row;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SpriteAnimationInfo) {
            SpriteAnimationInfo sai = (SpriteAnimationInfo)obj;
            return sai.row == row && sai.name.equals(name) && sai.default_speed == default_speed;
        }
        return false;
    }

    public int hashCode() {
        return (name + ":" + row).hashCode();
    }
}
