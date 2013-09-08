package com.dissonance.framework.game.world;

import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * Represents a "block" object in the level editor
 */
public class Block {

    private CollisionType collisionType;
    private int zpos;
    private float x;
    private float y;
    private String name;
    private HashMap<String, String> extras = new HashMap<String, String>();

    Block(CollisionType ctype, int zpos, float x, float y, String name, HashMap<String, String> extras) {
        this.collisionType = ctype;
        this.zpos = zpos;
        this.x = x;
        this.y = y;
        this.name = name;
        this.extras = extras;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public int getZpos() {
        return zpos;
    }

    public CollisionType getCollisionType() {
        return collisionType;
    }

    /**
     * Convert this Block object to an NPC object. <br></br>
     * This method will return null if this block can not be converted to an NPC
     */
    public void toNPC() {
        String classpath = null;
        for (String key : extras.keySet()) {
            if (key.equals("npc_classpath")) {
                classpath = extras.get(key);
            }
        }

        if (classpath == null)
            return; //TODO Return null

        //TODO Create new NPC object and return it
    }

    public enum CollisionType {

        PASSABLE(0),

        IMPASSABLE(1),

        PLATFORM(2),

        KILLABLE(3);

        int type;
        private CollisionType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static CollisionType fromInt(int i) {
            if (i >= 0 && i < 4)
                return CollisionType.values()[i];
            else
                throw new InvalidParameterException("The parameter \"i\" must be either 0, 1, 2, or 3!");
        }
    }
}
