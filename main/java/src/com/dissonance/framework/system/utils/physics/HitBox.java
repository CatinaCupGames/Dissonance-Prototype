package com.dissonance.framework.system.utils.physics;

import com.dissonance.framework.game.ai.astar.FastMath;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.framework.game.world.Tile;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.LayerType;
import com.dissonance.framework.game.world.tiled.TiledObject;
import com.dissonance.framework.render.texture.sprite.SpriteTexture;

import java.util.ArrayList;
import java.util.List;

public class HitBox {
    private static ArrayList<Collidable> cache = new ArrayList<Collidable>();
    private float minX, maxX, minY, maxY;
    private float x, y;
    private Collidable lastCollide;

    public HitBox(float minX, float minY, float maxX, float maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxY = maxY;
        this.maxX = maxX;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Collidable getLastCollide() {
        return lastCollide;
    }

    /**
     * Check for collision with any {@link Collidable} objects and return the <b>FIRST</b> object found colliding. <br></br>
     * The x and y returned by {@link com.dissonance.framework.system.utils.physics.HitBox#getX()} and {@link com.dissonance.framework.system.utils.physics.HitBox#getY()} are used when {@link HitBox#checkForCollision(com.dissonance.framework.game.world.World, float, float, Sprite)} is invoked.
     *
     * @param world The world to check in
     * @return The <b>FIRST</b> {@link Collidable} object found. Sprites are checked first, then TiledObjects and then lastly
     * tiles. <br></br>
     * If no objects were found, then a null value is returned.
     */
    public boolean checkForCollision(World world) {
        return checkForCollision(world, x, y, null);
    }

    /**
     * Check for collision with any {@link Collidable} objects and return the <b>FIRST</b> object found colliding.
     *
     * @param sprite The sprite to check collision for.
     * @return The <b>FIRST</b> {@link Collidable} object found. Sprites are checked first, then TiledObjects and then lastly
     * tiles. <br></br>
     * If no objects were found, then a null value is returned.
     */
    public boolean checkForCollision(Sprite sprite) {
        float height;
        float width;
        if (sprite.getTexture() instanceof SpriteTexture) {
            SpriteTexture temp = (SpriteTexture) sprite.getTexture();
            height = temp.getHeight();
            width = temp.getWidth();
        } else {
            height = sprite.getHeight();
            width = sprite.getWidth();
        }

        float sX = sprite.getX() - (width / 2f);
        float sY = sprite.getY() - (height / 2f);

        sX += minX;
        sY += minY;

        return checkForCollision(sprite.getWorld(), sX, sY, sprite, sprite.getLayer());
    }

    public boolean checkForCollision(Sprite sprite, float x, float y) {
        float height;
        float width;
        if (sprite.getTexture() instanceof SpriteTexture) {
            SpriteTexture temp = (SpriteTexture) sprite.getTexture();
            height = temp.getHeight();
            width = temp.getWidth();
        } else {
            height = sprite.getHeight();
            width = sprite.getWidth();
        }

        float sX = x - (width / 2f);
        float sY = y - (height / 2f);

        sX += minX;
        sY += minY;

        return checkForCollision(sprite.getWorld(), sX, sY, sprite, sprite.getLayer());
    }

    /**
     * Check for collision with any {@link Collidable} objects and return the <b>FIRST</b> object found colliding.
     *
     * @param world  The world to check in
     * @param startX The startX position.
     * @param startY The startY position.
     * @return The <b>FIRST</b> {@link Collidable} object found. Sprites are checked first, then TiledObjects and then lastly
     * tiles. <br></br>
     * If no objects were found, then a null value is returned.
     */
    public boolean checkForCollision(World world, float startX, float startY, Sprite ignore) {
        return checkForCollision(world, startX, startY, ignore, 1);
    }

    /**
     * Check for collision with any {@link Collidable} objects and return the <b>FIRST</b> object found colliding.
     *
     * @param world  The world to check in
     * @param startX The startX position.
     * @param startY The startY position.
     * @return The <b>FIRST</b> {@link Collidable} object found. Sprites are checked first, then TiledObjects and then lastly
     * tiles. <br></br>
     * If no objects were found, then a null value is returned.
     */
    public boolean checkForCollision(World world, float startX, float startY, Sprite ignore, int layer) {
        if (world == null)
            return false;
        float halfx = (maxX - minX) / 2f;
        float halfy = (maxY - minY) / 2f;
        for (float x = startX; x < startX + (maxX - minX); x++) {
            for (float y = startY; y < startY + (maxY - minY); y++) {
                for (Collidable sprite : cache) {
                    if (sprite.getWorld() != world)
                        continue;
                    if (sprite == ignore)
                        continue;
                    if (sprite.isPointInside(x, y)) {
                        lastCollide = sprite;
                        return true;
                    }
                }
                List<TiledObject> list = world.getPolygonsAt(x, y);
                if (list.size() > 0) {
                    lastCollide = list.get(0);
                    return true;
                }

                Layer pLayer = world.getLayer(layer, LayerType.TILE_LAYER);
                if (pLayer != null) {
                    if (pLayer.getProperty("collideLayer") != null) {
                        if (pLayer.getProperty("collideLayer").equals("ground") || pLayer.getProperty("collideLayer").equals("nonground")) {
                            boolean nonground = pLayer.getProperty("collideLayer").equals("nonground");
                            Layer[] layers = world.getLayers(LayerType.TILE_LAYER);
                            for (Layer faggot : layers) {
                                if ((faggot.isGroundLayer() && !nonground) || (!faggot.isGroundLayer() && nonground)) {
                                    Tile t = world.getTileAt(FastMath.fastFloor((x + halfx + 2) / 16f), FastMath.fastFloor((y + (halfy * 2)) / 16f), faggot);
                                    if (t != null && !t.isPassable()) {
                                        System.out.println(t.getX() + " : " + t.getY() + " : " + t.getContainingLayer().getName());
                                        lastCollide = t;
                                        return true;
                                    }
                                }
                            }
                        } else {
                            int l = Integer.parseInt(pLayer.getProperty("collideLayer"));
                            Layer[] layers = world.getLayers(LayerType.TILE_LAYER);
                            for (Layer faggot : layers) {
                                if (faggot.getLayerNumber() == l) {
                                    Tile t = world.getTileAt(FastMath.fastFloor((x + halfx + 2) / 16f), FastMath.fastFloor((y + (halfy * 2)) / 16f), faggot);
                                    if (t != null && !t.isPassable()) {
                                        lastCollide = t;
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Layer[] layers = world.getLayers(LayerType.TILE_LAYER);
                    for (Layer l : layers) {
                        Tile t = world.getTileAt(FastMath.fastFloor((x + halfx + 2) / 16f), FastMath.fastFloor((y + (halfy * 2)) / 16f), l);
                        if (t != null && !t.isPassable()) {
                            lastCollide = t;
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public List<Collidable> checkAndRetrieve(World world, float startX, float startY, Sprite ignore) {
        return checkAndRetrieve(world, startX, startY, 1, ignore);
    }

    public List<Collidable> checkAndRetrieve(World world, float startX, float startY, int layer,  Sprite ignore) {
        if (world == null) return new ArrayList<Collidable>();

        float halfx = (maxX - minX) / 2f;
        float halfy = (maxY - minY) / 2f;
        ArrayList<Collidable> collidables = new ArrayList<Collidable>();
        for (float x = minX + startX; x < startX + (maxX - minX); x++) {
            for (float y = minY + startY; y < startY + (maxY - minY); y++) {
                for (Collidable sprite : cache) {
                    if (sprite == ignore)
                        continue;
                    if (sprite.isPointInside(x, y) && !collidables.contains(sprite)) {
                        collidables.add(sprite);
                    }
                }
                List<TiledObject> list = world.getPolygonsAt(x, y);
                if (list.size() > 0) {
                    for (TiledObject t : list) {
                        if (collidables.contains(t))
                            continue;
                        collidables.add(t);
                    }
                }

                Layer pLayer = world.getLayer(layer, LayerType.TILE_LAYER);
                if (pLayer != null) {
                    String prop = pLayer.getProperty("collideLayer");
                    if (prop != null) {
                        if (prop.equals("ground") || prop.equals("nonground")) {
                            boolean nonground = prop.equals("nonground");
                            Layer[] layers = world.getLayers(LayerType.TILE_LAYER);
                            for (Layer faggot : layers) {
                                if ((faggot.isGroundLayer() && !nonground) || (!faggot.isGroundLayer() && nonground)) {
                                    Tile t = world.getTileAt(FastMath.fastFloor((x + halfx + 2) / 16f), FastMath.fastFloor((y + (halfy * 2)) / 16f), faggot);
                                    if (t != null && !t.isPassable()) {
                                        if (collidables.contains(t))
                                            continue;
                                        collidables.add(t);
                                    }
                                }
                            }
                        } else {
                            int l = Integer.parseInt(prop);
                            Layer[] layers = world.getLayers(LayerType.TILE_LAYER);
                            for (Layer faggot : layers) {
                                if (faggot.getLayerNumber() == l) {
                                    Tile t = world.getTileAt(FastMath.fastFloor((x + halfx + 2) / 16f), FastMath.fastFloor((y + (halfy * 2)) / 16f), faggot);
                                    if (t != null && !t.isPassable()) {
                                        if (collidables.contains(t))
                                            continue;
                                        collidables.add(t);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Layer[] layers = world.getLayers(LayerType.TILE_LAYER);
                    for (Layer l : layers) {
                        Tile t = world.getTileAt(FastMath.fastFloor((x + halfx + 2) / 16f), FastMath.fastFloor((y + (halfy * 2)) / 16f), l);
                        if (t != null && !t.isPassable()) {
                            if (collidables.contains(t))
                                continue;
                            collidables.add(t);
                        }
                    }
                }
            }
        }

        return collidables;
    }

    public float getMinY() {
        return minY;
    }

    public float getMinX() {
        return minX;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMaxY() {
        return maxY;
    }

    public void setMinX(float x) {
        this.minX = x;
    }

    public void setMinY(float y) {
        this.minY = y;
    }

    public void setMaxX(float x) {
        this.maxX = x;
    }

    public void setMaxY(float y) {
        this.maxY = y;
    }

    public static void registerSprite(Collidable collidable) {
        if (!cache.contains(collidable))
            cache.add(collidable);
    }

    public static void unregisterSprite(Collidable collidable) {
        if (cache.contains(collidable))
            cache.remove(collidable);
    }

    public static ArrayList<Collidable> getCollidables() {
        return cache;
    }
}
