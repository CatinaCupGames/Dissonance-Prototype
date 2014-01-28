package com.dissonance.framework.game.world;

import com.dissonance.framework.game.ai.astar.NodeMap;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.UIElement;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.LayerType;
import com.dissonance.framework.game.world.tiled.TiledObject;
import com.dissonance.framework.game.world.tiled.WorldData;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.render.shader.impl.Light;
import com.dissonance.framework.render.shader.impl.LightShader;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.render.texture.sprite.SpriteTexture;
import com.dissonance.framework.system.ServiceManager;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;
import com.dissonance.framework.system.utils.Timer;
import com.dissonance.framework.system.utils.Validator;
import com.google.gson.Gson;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class World {
    private static final Gson GSON = new Gson();
    private static String wlpackage = "com.dissonance.game.w";
    private static LightShader lightShader;

    private transient final ArrayList<Drawable> drawable = new ArrayList<>();
    private String name;
    private NodeMap nodeMap;
    private int ID;
    private transient RenderService renderingService;
    private boolean invalid = true;
    private boolean loaded = false;
    private WorldData tiledData;
    private WorldLoader loader;
    private List<UIElement> uiElements = new ArrayList<UIElement>();
    private List<UpdatableDrawable> udrawables = new ArrayList<>();
    private List<CombatSprite> combatCache = new ArrayList<CombatSprite>();
    private List<Light> lights = new ArrayList<Light>();
    private float worldBrightness = 1f;


    World(int ID) {
        this.ID = ID;
    }

    public static void setDefaultLoaderPackage(String wlpackage) {
        World.wlpackage = wlpackage;
    }

    public int getID() {
        return ID;
    }

    public void init() {
        renderingService = ServiceManager.createService(RenderService.class);
    }

    public RenderService getRenderService() {
        return renderingService;
    }

    public void setWorldLoader(WorldLoader loader) {
        this.loader = loader;
    }

    public WorldLoader getWorldLoader() {
        return loader;
    }

    public List<UIElement> getElements() {
        return uiElements;
    }

    public void switchTo(boolean fadeToBlack) {
        if (renderingService == null)
            return;
        if (!renderingService.isCrossFading() && fadeToBlack) {
            if (!renderingService.isFading())
                renderingService.fadeToBlack(1000);
            try {
                renderingService.waitForFade();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        renderingService.provideData(this, RenderService.WORLD_DATA_TYPE);
        if (!renderingService.isCrossFading() && fadeToBlack) {
            Timer.delayedInvokeRunnable(300, new Runnable() {

                @Override
                public void run() {
                    renderingService.fadeFromBlack(1000);
                }
            });
        }
    }

    public void load(final String world) throws WorldLoadFailedException {
        if (renderingService == null)
            throw new WorldLoadFailedException("The RenderService was not created! Try calling World.init() before loading a world.");

        name = world;
        InputStream in = getClass().getClassLoader().getResourceAsStream("worlds/" + world + ".json");
        if (in != null) {
            try {
                tiledData = GSON.fromJson(new InputStreamReader(in), WorldData.class);
                renderingService.runOnServiceThread(new Runnable() {
                    @Override
                    public void run() {
                        if (lightShader == null) { //Only build the shader on the render thread
                            lightShader = new LightShader();
                            lightShader.build();
                        }

                        tiledData.loadAllTileSets();
                        tiledData.assignAllLayers();
                        System.out.println("Creating tiles..");
                        long ms = System.currentTimeMillis();
                        drawable.addAll(tiledData.createDrawables());
                        System.out.println("Done! Took " + (System.currentTimeMillis() - ms) + "ms. Added " + drawable.size() + " tiles!");
                        tiledData.loadTriggers();
                        WorldLoader loader = null;
                        if (World.this.loader == null) {
                            System.out.println("Searching for loader..");
                            if (tiledData.getProperty("loader") != null) {
                                try {
                                    Class<?> class_ = Class.forName(tiledData.getProperty("loader"));
                                    if (WorldLoader.class.isAssignableFrom(class_)) {
                                        loader = (WorldLoader) class_.newInstance();
                                    }
                                } catch (Exception e) {
                                    loader = attemptSearchForWorldLoader();
                                }
                            } else {
                                loader = attemptSearchForWorldLoader();
                            }

                            if (loader != null) {
                                loader.onLoad(World.this);
                            } else {
                                System.out.println("No loader found..");
                            }
                        } else {
                            World.this.loader.onLoad(World.this);
                        }

                        lightShader.addAll(lights);
                        lightShader.setOverallBrightness(worldBrightness);

                        loaded = true;
                        _wakeLoadWaiters();
                    }
                });

                nodeMap = new NodeMap(this, tiledData.getWidth(), tiledData.getHeight());

                nodeMap.readMap();
                in.close();
            } catch (Exception e) {
                throw new WorldLoadFailedException("Error loading Tiled file!", e);
            }
        } else { //Find and invoke WorldLoader for this world
            renderingService.runOnServiceThread(new Runnable() {

                @Override
                public void run() {
                    if (lightShader == null) { //Only build the shader on the render thread
                        lightShader = new LightShader();
                        lightShader.build();
                    }

                    WorldLoader loader = attemptSearchForWorldLoader();
                    if (loader != null)
                        loader.onLoad(World.this);

                    lightShader.addAll(lights);
                    lightShader.setOverallBrightness(worldBrightness);

                    loaded = true;
                    _wakeLoadWaiters();
                }
            });
        }

        if (renderingService.isPaused())
            renderingService.resume();
    }

    public synchronized void waitForWorldLoaded() throws InterruptedException {
        while (true) {
            if (loaded)
                break;
            super.wait(0L);
        }
    }

    private synchronized void _wakeLoadWaiters() {
        super.notifyAll();
    }

    private WorldLoader attemptSearchForWorldLoader() {
        try {
            Class<?> class_ = Class.forName(wlpackage + "." + name);
            if (WorldLoader.class.isAssignableFrom(class_)) {
                return (WorldLoader) class_.newInstance();
            }
        } catch (Exception ignored) {
        }
        return null;
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

    public Light createLight(float x, float y, float brightness, float radius) {
        Light l = new Light(x, y, radius, brightness);
        addLight(l);
        return l;
    }

    public Light createLight(float x, float y, float brightness, float radius, Color color) {
        Light l = new Light(x, y, radius, brightness, color);
        addLight(l);
        return l;
    }

    public Light createLight(float x, float y, float brightness, float radius, float r, float g, float b) {
        Light l = new Light(x, y, radius, brightness, new Color(r, g, b));
        addLight(l);
        return l;
    }

    public List<Light> getLights() {
        return lights;
    }

    public float getWorldBrightness() {
        return worldBrightness;
    }

    public void setWorldBrightness(float brightness) {
        this.worldBrightness = brightness;

        lightShader.setOverallBrightness(brightness);
    }

    public void addLight(Light l) {
        this.lights.add(l);

        lightShader.add(l);
    }

    public void removeLight(Light l) {
        this.lights.remove(l);

        lightShader.remove(l);
    }

    private void addDrawable(final Drawable draw, final Runnable run) {
        if (renderingService == null)
            throw new IllegalStateException("init() has not been called on this world!");
        Validator.validateNotNull(draw, "drawable");

        renderingService.runOnServiceThread(new Runnable() {

            @Override
            public void run() {
                if (draw instanceof UIElement) {
                    UIElement ue = (UIElement)draw;
                    ue.init();
                    uiElements.add(ue);
                    udrawables.add(ue);
                    if (run != null)
                        run.run();
                    return;
                }
                drawable.add(draw);
                if (draw instanceof UpdatableDrawable) {
                    UpdatableDrawable ud = (UpdatableDrawable) draw;
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
     *
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
                if (sprite instanceof CombatSprite)
                    combatCache.add((CombatSprite) sprite);
            }
        });
    }

    public void removeSprite(final Sprite sprite) {
        removeDrawable(sprite, new Runnable() {

            @Override
            public void run() {
                sprite.onUnload();
                sprite.setWorld(null);
                if (sprite instanceof CombatSprite)
                    combatCache.remove(sprite);
            }
        });
    }

    public List<CombatSprite> getAllCombatSprites() {
        return Collections.unmodifiableList(combatCache);
    }

    public void onUnload() { //This method is called when the world is not shown but is still in memory
        //TODO Do stuff to save memory when this world is not shown
        if (lightShader != null) {
            lightShader.clear();
            lightShader.setOverallBrightness(1f);
        }
    }

    public void onDispose() {
        drawable.clear();
        udrawables.clear();
        combatCache.clear();
        tiledData.dispose();
        renderingService = null;
    }

    public void removeDrawable(final Drawable drawable) {
        removeDrawable(drawable, null);
    }

    public void toWorldSpace(Vector2f vector2f) {
        vector2f.x *= tiledData.getTileWidth();
        vector2f.y *= tiledData.getTileHeight();
    }

    public void removeDrawable(final Drawable drawable, final Runnable runnable) {
        if (renderingService == null)
            throw new IllegalStateException("init() has not been called on this world!");
        Validator.validateNotNull(drawable, "sprite");

        renderingService.runOnServiceThread(new Runnable() {

            @Override
            public void run() {
                if (drawable instanceof UIElement) {
                    uiElements.remove(drawable);
                    udrawables.remove(drawable);
                    if (runnable != null)
                        runnable.run();
                    return;
                }
                World.this.drawable.remove(drawable);
                if (drawable instanceof UpdatableDrawable)
                    World.this.udrawables.remove(drawable);
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
        if (sprite.getTexture() != null)
            return;
        renderingService.runOnServiceThread(new Runnable() {

            @Override
            public void run() {
                try {
                    SpriteTexture t = SpriteTexture.retrieveSpriteTexture(sprite.getSpriteName());
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
     * This method will return null if no {@link AnimatedSprite} was found <b>or</b> if 2 or more sprites were found with the given search term.
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

    /**
     * Returns all polygons (if any) that collide with the specified point. If the point specified in the parameter is
     * inside a polygon in this world, then that polygon is added to the list of polygon's to return.<br></br>
     * <b>NOTE: This method only work for the square tool and the triangle tool in Tiled. However, the triangle tool can
     * make complex polygons.</b>
     *
     * @param x The x-coordinate to check against
     * @param y The y-coordinate to check against
     * @return A {@link List} of {@link TiledObject} that intersect with the point specified.
     */
    public List<TiledObject> getPolygonsAt(float x, float y) {
        ArrayList<TiledObject> objects = new ArrayList<TiledObject>();
        Layer[] objLayers = getLayers(LayerType.OBJECT_LAYER);
        for (Layer layer : objLayers) {
            for (TiledObject obj : layer.getObjectGroupData()) {
                if (obj.isPointInside(x, y)) {
                    objects.add(obj);
                }
            }
        }
        return objects;
    }

    public TiledObject getSpawn(String name) {
        Layer[] objLayers = getLayers(LayerType.OBJECT_LAYER);
        for (Layer layer : objLayers) {
            for (TiledObject obj : layer.getObjectGroupData()) {
                if (obj.isSpawn()) {
                    if (obj.getName() != null && obj.getName().equalsIgnoreCase(name))
                        return obj;
                }
            }
        }
        return null;
    }

    public Tile getTileAt(float x, float y, Layer layer) {
        if (!layer.isTiledLayer())
            throw new InvalidParameterException("The layer specified is not a tile layer!");

        return layer.getTileAt(x, y, this);
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

    public WorldData getTiledData() {
        return tiledData;
    }

    public int getDrawableCount() {
        return drawable.size();
    }
}
