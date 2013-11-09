package com.dissonance.framework.game.world;

import com.dissonance.framework.game.ai.astar.NodeMap;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.LayerType;
import com.dissonance.framework.game.world.tiled.TiledObject;
import com.dissonance.framework.game.world.tiled.WorldData;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.render.texture.sprite.SpriteTexture;
import com.dissonance.framework.system.Service;
import com.dissonance.framework.system.ServiceManager;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;
import com.dissonance.framework.system.utils.Validator;
import com.google.gson.Gson;
import org.jbox2d.common.Vec2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class World implements UpdatableDrawable {
    private static final Gson GSON = new Gson();
    private static final float GRAVITY = 0f;
    private static final float TIME_STEP = 1f / 60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;

    private transient final ArrayList<Drawable> drawable = new ArrayList<>();
    private transient org.jbox2d.dynamics.World physicsWorld;
    private String name;
    private NodeMap nodeMap;
    private int ID;
    private transient Service renderingService;
    private transient Texture texture;
    private boolean invalid = true;
    private WorldData tiledData;
    private List<UpdatableDrawable> udrawables = new ArrayList<>();

    World(int ID) {
        this.ID = ID;
        physicsWorld = new org.jbox2d.dynamics.World(new Vec2(0, GRAVITY));
    }

    public int getID() {
        return ID;
    }

    public void init() {
        renderingService = ServiceManager.createService(RenderService.class);
    }

    public void switchTo() {
        //TODO Move all playable sprites to this world maybe?
        if (renderingService == null)
            return;
        renderingService.provideData(this, RenderService.WORLD_DATA_TYPE);
    }

    @Override
    public void update() {
        if (this.physicsWorld != null) {
            this.physicsWorld.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }

    @Override
    public void render() {
    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }

    public void load(final String world) throws WorldLoadFailedException {
        if (renderingService == null)
            throw new WorldLoadFailedException("The RenderService was not created! Try calling World.init() before loading a world.");

        InputStream in = getClass().getClassLoader().getResourceAsStream("worlds/" + world + ".json");
        if (in != null) {
            try {
                tiledData = GSON.fromJson(new InputStreamReader(in), WorldData.class);
                renderingService.runOnServiceThread(new Runnable() {
                    @Override
                    public void run() {
                        tiledData.loadAllTileSets();
                        tiledData.assignAllLayers();
                        System.out.println("Creating tiles..");
                        long ms = System.currentTimeMillis();
                        drawable.addAll(tiledData.createDrawables());
                        System.out.println("Done! Took " + (System.currentTimeMillis() - ms) + "ms. Added " + drawable.size() + " tiles!");
                    }
                });
                name = world;

                nodeMap = new NodeMap(this, tiledData.getWidth(), tiledData.getHeight());

                nodeMap.readMap();
                in.close();
            } catch (Exception e) {
                throw new WorldLoadFailedException("Error loading Tiled file!", e);
            }
        }

        addDrawable(this); //TODO Maybe remove this..

        if (renderingService.isPaused())
            renderingService.resume();
    }

    public Iterator<Drawable> getSortedDrawables() {
        if (invalid) {
            Collections.sort(drawable);
            invalid = false;
        }
        return drawable.iterator();
    }

    public Iterator<UpdatableDrawable> getUpdatables() {
        return udrawables.iterator();
    }

    public void invalidateDrawableList() {
        invalid = true;
    }

    private void addDrawable(final Drawable draw, final Runnable run) {
        if (renderingService == null)
            throw new IllegalStateException("init() has not been called on this world!");
        Validator.validateNotNull(draw, "drawable");

        renderingService.runOnServiceThread(new Runnable() {

            @Override
            public void run() {
                drawable.add(draw);
                if (draw instanceof UpdatableDrawable) {
                    UpdatableDrawable ud = (UpdatableDrawable)draw;
                    if (!(ud instanceof World))
                        ud.init();
                    udrawables.add(ud);
                }
                if (run != null)
                    run.run();
            }
        }, true); //Force adding drawables on next frame
    }

    /**
     * Add this renderer to the list of Drawable objects to render on the screen
     * @param draw
     */
    public void addDrawable(final Drawable draw) {
        addDrawable(draw, null);
    }

    public void addSprite(final Sprite sprite) {
        addDrawable(sprite, new Runnable() {
            @Override
            public void run() {
                sprite.setWorld(World.this);
                sprite.onLoad();
            }
        });
    }

    public void removeSprite(final Sprite sprite) {
        removeDrawable(sprite, new Runnable() {

            @Override
            public void run() {
                sprite.onUnload();
                sprite.setWorld(null);
            }
        });
    }

    public void onUnload() { //This method is called when the world is not shown but is still in memory
        //TODO Do stuff to save memory when this world is not shown
    }

    public void onDispose() {
        drawable.clear();
        texture.dispose();
        tiledData.dispose();
        renderingService = null;
    }

    public void removeDrawable(final Drawable drawable) {
        removeDrawable(drawable, null);
    }

    public void removeDrawable(final Drawable drawable, final Runnable runnable) {
        if (renderingService == null)
            throw new IllegalStateException("init() has not been called on this world!");
        Validator.validateNotNull(drawable, "sprite");

        renderingService.runOnServiceThread(new Runnable() {

            @Override
            public void run() {
                World.this.drawable.remove(drawable);
                if (runnable != null)
                    runnable.run();
            }
        }, true);
    }

    public void loadTextureForSprite(final String resource, final Sprite sprite) {
        if (renderingService == null)
            throw new IllegalStateException("init() has not been called on this world!");
        Validator.validateNotNull(sprite, "sprite");
        Validator.validateNotNull(resource, "resource");

        renderingService.runOnServiceThread(new Runnable() {

            @Override
            public void run() {
                try {
                    Texture t = Texture.retriveTexture(resource);
                    sprite.setTexture(t);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadAnimatedTextureForSprite(final AnimatedSprite sprite) {
        if (renderingService == null)
            throw new IllegalStateException("init() has not been called on this world!");
        Validator.validateNotNull(sprite, "sprite");

        renderingService.runOnServiceThread(new Runnable() {

            @Override
            public void run() {
                try {
                    SpriteTexture t = SpriteTexture.retriveSpriteTexture(sprite.getSpriteName());
                    sprite.setTexture(t);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadAndAdd(Drawable d) {
        if (d instanceof Sprite) {
            if (d instanceof AnimatedSprite) {
                loadAnimatedTextureForSprite((AnimatedSprite) d);
            }
            addSprite((Sprite) d);
        } else {
            addDrawable(d);
        }
    }

    /**
     * Find an {@link AnimatedSprite} in this world with the name <b>name</b>
     *
     * @param name The full name of the {@link AnimatedSprite}
     * @return The {@link AnimatedSprite} with the same name
     */
    public AnimatedSprite getAnimatedSpriteByName(String name) {
        for (Drawable d : drawable) {
            if (d instanceof AnimatedSprite) {
                AnimatedSprite sprite = (AnimatedSprite) d;
                if (sprite.getSpriteName().equals(name))
                    return sprite;
            }
        }
        return null;
    }

    /**
     * Find an {@link AnimatedSprite} with the name similar to <b>name</b> <br></br>
     * If more than 1 {@link AnimatedSprite} is found with similar names, then this method will return name.
     *
     * @param name The search term
     * @return An {@link AnimatedSprite} object with a similar name. <br></br>
     *         This method will return null if no {@link AnimatedSprite} was found <b>or</b> if 2 or more sprites were found with the given search term.
     */
    public AnimatedSprite findAnimatedSprite(String name) {
        AnimatedSprite toreturn = null;
        for (Drawable d : drawable) {
            if (d instanceof AnimatedSprite) {
                AnimatedSprite sprite = (AnimatedSprite) d;
                if (toreturn == null && sprite.getSpriteName().contains(name)) {
                    toreturn = sprite;
                } else if (toreturn != null && sprite.getSpriteName().contains(name)) {
                    return null;
                }
            }
        }
        return toreturn;
    }

    public Tile getTileAt(float x, float y, Layer layer) {
        if (!layer.isTiledLayer())
            throw new InvalidParameterException("The layer specified is not a tile layer!");

        return layer.getTileAt(x, y);
    }

    public Tile getTileAt(float x, float y, int layernumber) {
        Validator.validateNotBelow(layernumber, 0, "layer");
        Validator.validateNotOver(layernumber, tiledData.getLayers().length, "layer");
        Layer l = tiledData.getLayers()[layernumber];

        return getTileAt(x, y, l);
    }

    public Layer[] getLayers(LayerType type) {
        List<Layer> layers = new ArrayList<Layer>();
        for (Layer l : tiledData.getLayers()) {
            if (l.getLayerType() == type)
                layers.add(l);
        }

        return layers.toArray(new Layer[layers.size()]);
    }

    public Layer[] getLayers() {
        return tiledData.getLayers();
    }

    public int getWidth() {
        return tiledData.getWidth();
    }

    public int getHeight() {
        return tiledData.getHeight();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NodeMap getNodeMap() {
        return nodeMap;
    }

    public void setNodeMap(NodeMap map) {
        this.nodeMap = map;
    }

    public org.jbox2d.dynamics.World getPhysicsWorld() {
        return this.physicsWorld;
    }

    public WorldData getTiledData() {
        return tiledData;
    }

    @Override
    public int compareTo(Drawable o) {
        return Drawable.BEFORE;
    }
}
