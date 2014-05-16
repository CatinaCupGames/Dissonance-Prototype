package com.dissonance.framework.game.world;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.TileSet;
import com.dissonance.framework.game.world.tiled.impl.AbstractTileTrigger;
import com.dissonance.framework.game.world.tiled.impl.AbstractTrigger;
import com.dissonance.framework.system.utils.physics.Collidable;
import com.dissonance.framework.system.utils.physics.HitBox;
import org.lwjgl.util.vector.Vector2f;

import java.util.HashMap;

public class Tile implements Collidable {
    private static HashMap<Layer, HashMap<String, AbstractTileTrigger>> instances = new HashMap<Layer, HashMap<String, AbstractTileTrigger>>();

    private final long id;
    private final int cost;
    private boolean passable = true;
    private boolean trigger_tile = false;
    private AbstractTileTrigger triggerClass;
    private final float x, y;
    private final Layer containingLayer;
    private final World parent;
    private final boolean flipH, flipD, flipL;

    public Tile(long id, float x, float y, Layer layer, World world, boolean flipH, boolean flipL, boolean flipD) {
        this.parent = world;
        this.id = id;
        this.x = x;
        this.y = y;
        this.containingLayer = layer;
        this.flipH = flipH;
        this.flipL = flipL;
        this.flipD = flipD;

        int cost1;
        if (hasProperty("cost", world)) {
            try {
                cost1 = Integer.parseInt(getProperty("cost", world));
            } catch (Throwable t) {
                cost1 = 0;
            }
        } else {
            cost1 = 0;
        }
        cost = cost1;

        if (hasProperty("passable", world)) {
            passable = getProperty("passable", world).toLowerCase().equals("true");
        }

        if (hasProperty("triggerclass", world)) {
            trigger_tile = true;
            String tmp = getProperty("triggerclass", world);
            if (instances.containsKey(containingLayer) && instances.get(containingLayer).containsKey(tmp)) {
                triggerClass = instances.get(containingLayer).get(tmp);
            } else {
                try {
                    triggerClass = (AbstractTileTrigger) Class.forName(tmp).newInstance();

                    HashMap<String, AbstractTileTrigger> temp = new HashMap<String, AbstractTileTrigger>();
                    temp.put(tmp, triggerClass);
                    instances.put(layer, temp);
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean isFlippedHorizontal() {
        return flipH;
    }

    public boolean isFlippedVertical() {
        return flipL;
    }

    public boolean isFlippedDiegonally() {
        return flipD;
    }

    public boolean isTriggerTile() {
        return trigger_tile;
    }

    public AbstractTileTrigger getTrigger() {
        return triggerClass;
    }

    public long getID() {
        return id;
    }

    public int getExtraCost() {
        return cost;
    }

    public boolean isPassable() {
        return passable;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    HitBox hitBox;
    @Override
    public HitBox getHitBox() {
        if (hitBox == null) {
            hitBox = new HitBox(0, 0, 32, 32);
        }
        return hitBox;
    }

    @Override
    public boolean isPointInside(float x, float y) {
        return false; //We don't need to check for tiles..
    }

    public Layer getContainingLayer() {
        return containingLayer;
    }

    public World getParentWorld() {
        return parent;
    }

    public Tile getRelativeTile(Vector2f addLocation) {
        return containingLayer.getTileAt(x + addLocation.x, y + addLocation.y, parent);
    }

    public Tile getTileAboveThis() {
        return getRelativeTile(new Vector2f(1, 0));
    }

    public Tile getTileBelowThis() {
        return getRelativeTile(new Vector2f(-1, 0));
    }

    public Tile getTileLeftOfThis() {
        return getRelativeTile(new Vector2f(0, -1));
    }

    public Tile getTileRightOfThis() {
        return getRelativeTile(new Vector2f(0, 1));
    }

    public String getProperty(String property, World world) {
        TileSet t = world.getTiledData().findTileSetFromID(id);
        if (t == null)
            return null;
        return t.getTileProperty(this, property);
    }

    public boolean hasProperty(String property, World world) {
        return getProperty(property, world) != null;
    }
}
