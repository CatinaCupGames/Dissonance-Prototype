package com.dissonance.framework.game.world;

import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.texture.Texture;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.HashMap;

/**
 * Represents a "block" object in the level editor
 */
public class Block implements Drawable {

    private CollisionType collisionType;
    private int zpos;
    private float x;
    private float y;
    private String name;
    private Texture texture;
    private HashMap<String, String> extras = new HashMap<String, String>();

    Block(CollisionType ctype, int zpos, float x, float y, String name, HashMap<String, String> extras) throws IOException {
        this.collisionType = ctype;
        this.zpos = zpos;
        this.x = x;
        this.y = y;
        this.name = name;
        this.extras = extras;
        texture = Texture.retriveTexture("tiles/" + name + ".png");
    }

    public void bindTexture() {
        texture.bind();
    }

    public void unbindTexture() {
        texture.unbind();
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

    @Override
    public void init() { }

    @Override
    public void update() {
    }

    @Override
    public void render() {
    }

    @Override
    public int compareTo(Drawable o) {
        return 0;
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
