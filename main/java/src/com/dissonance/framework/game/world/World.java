package com.dissonance.framework.game.world;

import com.dissonance.framework.game.ai.astar.NodeMap;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.sprites.ui.UI;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.LayerType;
import com.dissonance.framework.game.world.tiled.TiledObject;
import com.dissonance.framework.game.world.tiled.WorldData;
import com.dissonance.framework.game.world.tiled.impl.TileObject;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.render.framebuffer.Framebuffer;
import com.dissonance.framework.render.shader.impl.Light;
import com.dissonance.framework.render.shader.impl.LightShader;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.render.texture.sprite.SpriteTexture;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.framework.system.ServiceManager;
import com.dissonance.framework.system.debug.Debug;
import com.dissonance.framework.system.debug.DebugSprite;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;
import com.dissonance.framework.system.utils.Timer;
import com.dissonance.framework.system.utils.Validator;
import com.dissonance.framework.system.utils.physics.HitBox;
import com.dissonance.framework.system.utils.proxyhelper.ProxyFactory;
import com.google.gson.Gson;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.List;

public final class World {
    private static final Gson GSON = new Gson();
    private static String wlpackage = "com.dissonance.game.w";
    private static LightShader lightShader;

    private transient final ArrayList<Drawable> drawable = new ArrayList<>();
    private transient final ArrayList<Drawable> unsorted = new ArrayList<>();
    private String name;
    private NodeMap activeNodeMap;
    private NodeMap nodeMap;
    private int ID;
    private transient RenderService renderingService;
    private transient ArrayList<Framebuffer> frames = new ArrayList<Framebuffer>();
    private boolean invalid = true;
    private boolean loaded = false;
    private boolean showing = false;
    private boolean extreamSpeed = false;
    private WorldData tiledData;
    private WorldLoader loader;
    private List<UI> uiElements = new ArrayList<UI>();
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

    public List<UI> getElements() {
        return uiElements;
    }

    public boolean useExtreamSpeed() {
        return extreamSpeed;
    }

    public void useExtreamSpeed(boolean value) {
        this.extreamSpeed = value;
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
        load(world, null);
    }

    public void load(final String world, WorldLoader toUse) throws WorldLoadFailedException {
        name = world;
        InputStream in = getClass().getClassLoader().getResourceAsStream("worlds/" + world + ".json");
        if (in != null) {
            try {
                tiledData = GSON.fromJson(new InputStreamReader(in), WorldData.class);
                tiledData.assignAllLayers();
                tiledData.loadTriggers();
                loadTiledSprites();

                nodeMap = new NodeMap(this, tiledData.getWidth(), tiledData.getHeight());
                System.out.println("Reading default node map for world \"" + world + "\"");
                nodeMap.readMap();
                setActiveNodeMap(nodeMap);
            } catch (Exception e) {
                throw new WorldLoadFailedException("Error loading Tiled file! (" + name + ")", e);
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (toUse != null) this.loader = toUse;

        if (loader == null) {
            System.out.println("Searching for loader..");
            if (tiledData != null && tiledData.getProperty("loader") != null) {
                try {
                    Class<?> class_ = Class.forName(tiledData.getProperty("loader"));
                    if (WorldLoader.class.isAssignableFrom(class_)) {
                        loader = (WorldLoader) class_.newInstance();
                        //loader = ProxyFactory.createSafeObject(trueLoader, WorldLoader.class);
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
            loader.onLoad(World.this);
        }

        loaded = true;

        if (renderingService.isPaused())
            renderingService.resume();
    }

    private void loadTiledSprites() {
        Layer[] objLayers = getLayers(LayerType.OBJECT_LAYER);
        for (Layer layer : objLayers) {
            for (TiledObject obj : layer.getObjectGroupData()) {
                if (obj.getRawType().equals("sprite")) {
                    String name = obj.getName();
                    String w = obj.getProperty("width");
                    String h = obj.getProperty("height");
                    String l = obj.getProperty("layer");

                    try {
                        Sprite sprite = Sprite.fromClass(Class.forName("com.dissonance.game.sprites." + name));
                        sprite.setX(obj.getX());
                        sprite.setY(obj.getY());
                        if (w != null) {
                            float width = Float.parseFloat(w);
                            sprite.setStartWidth(width);
                        }
                        if (h != null) {
                            float height = Float.parseFloat(h);
                            sprite.setStartHeight(height);
                        }
                        if (l != null) {
                            int ll = Integer.parseInt(l);
                            sprite.setLayer(ll);
                        }
                        sprite.setProperties(obj.getProperties());
                        loadAndAdd(sprite);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean prepared = false;
    public void prepareTiles() {
        if (prepared)
            return;
        if (!RenderService.isInRenderThread())
            throw new IllegalAccessError("You must be on the render thread to prepare tiles!");
        if (tiledData == null)
            return;
        if (lightShader == null) { //Only build the shader on the render thread
            lightShader = new LightShader();
            lightShader.build();
        }

        tiledData.loadAllTileSets(); //OpenGL safe needed
        System.out.println("Creating tiles..");
        long ms = System.currentTimeMillis();
        drawable.addAll(tiledData.createDrawables(World.this)); //OpenGL safe needed
        if (Debug.isDebugging()) {
            DebugSprite debug = new DebugSprite();
            debug.display(World.this);
        }
        System.out.println("Done! Took " + (System.currentTimeMillis() - ms) + "ms. Added " + drawable.size() + " tiles!");
        System.out.println("Attempting to generate frame buffers..");

        //== OpenGL safe needed ==
        if (GameSettings.Graphics.useFBO && tiledData.getPixelWidth() > GameSettings.Display.window_width / 2f && tiledData.getPixelHeight() > GameSettings.Display.window_height / 2f) {
            try {
                Layer[] layers = getLayers(LayerType.TILE_LAYER);

                for (Layer layer : layers) {
                    if (!layer.isGroundLayer() && !extreamSpeed)
                        continue;
                    if (!Framebuffer.enoughSpaceFor(tiledData.getPixelWidth(), tiledData.getPixelHeight()))
                        break; //We ran out of space.. :/
                    Framebuffer frame = new Framebuffer(tiledData.getPixelWidth(), tiledData.getPixelHeight());
                    frame.generate();
                    frame.begin();
                    invalid = false;


                    Iterator<Drawable> drawableIterator = getSortedDrawables();
                    while (drawableIterator.hasNext()) {
                        Drawable d = drawableIterator.next();
                        if (d instanceof TileObject) {
                            TileObject t = (TileObject) d;
                            if (t.getTiledLayer() == layer && !t.isParallaxLayer() && !t.isAnimated()) {
                                t.renderIgnoreFade();
                                drawableIterator.remove();
                            }
                        }
                    }

                    frame.end();
                    frame.setLayer(layer.getGameLayer(this));
                    System.out.println("Success!");
                    if (Debug.isDebugging())
                        System.err.println("Framebuffer space used: " + Framebuffer.getMemoryUsed() + "\nFramebuffer space left: " + (Framebuffer.getMemoryLimit() - Framebuffer.getMemoryUsed()));
                    addDrawable(frame);
                    frames.add(frame);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                System.err.println("Framebuffers are not supported! Legacy rendering will be used!");
                GameSettings.Graphics.useFBO = false;
                try {
                    GameSettings.saveGameSettings();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        //== OpenGL safe needed ==

        invalidateDrawableList(); //Be sure to invalidate the drawable list

        prepared = true;
    }

    /**
     *
     * @throws InterruptedException
     * @deprecated This method is deprecated because worlds are now loaded in the same thread as the invoker.
     */
    @Deprecated
    public synchronized void waitForWorldLoaded() throws InterruptedException { }

    private final DisplayWaiters waiter = new DisplayWaiters();
    public synchronized void waitForWorldDisplayed() throws InterruptedException {
        waiter._wait();
    }

    private WorldLoader attemptSearchForWorldLoader() {
        try {
            Class<?> class_ = Class.forName(wlpackage + "." + name);
            if (WorldLoader.class.isAssignableFrom(class_)) {
                return (WorldLoader) class_.newInstance();
                //return ProxyFactory.createSafeObject(trueLoader, WorldLoader.class);
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    public Iterator<Drawable> getSortedDrawables() {
        if (invalid) {
            if (Debug.isDebugging()) System.err.println("Sorting Drawables!");
            try {
                Collections.sort(drawable);
            } catch (Throwable t) {
                t.printStackTrace();
            }
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

        if (showing)
            lightShader.setOverallBrightness(brightness);
    }

    public void addLight(Light l) {
        this.lights.add(l);
        if (showing)
            lightShader.add(l);
    }

    public void removeLight(Light l) {
        this.lights.remove(l);
        if (showing)
            lightShader.remove(l);
    }

    private void addDrawable(final Drawable draw, final Runnable run) {
        if (renderingService == null)
            throw new IllegalStateException("init() has not been called on this world!");
        Validator.validateNotNull(draw, "drawable");

        renderingService.runOnServiceThread(new Runnable() {

            @Override
            public void run() {
                //Drawable proxyDrawable = ProxyFactory.createSafeObject(draw, Drawable.class);
                if (draw instanceof UI) {
                    UI ue = (UI)draw;
                    //UI proxyUI = ProxyFactory.createSafeObject(ue, UI.class);
                    //proxyUI.init();
                    ue.init();
                    uiElements.add(ue);
                    udrawables.add(ue);
                    if (run != null)
                        run.run();
                    return;
                }
                if (!draw.neverSort())
                    drawable.add(draw);
                else
                    unsorted.add(draw);
                if (draw instanceof UpdatableDrawable) {
                    UpdatableDrawable ud = (UpdatableDrawable) draw;
                    //UpdatableDrawable proxyUd = ProxyFactory.createSafeObject(ud, UpdatableDrawable.class);
                    //proxyUd.init();
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
                if (!sprite.isLoaded()) {
                    sprite.onLoad();
                    sprite.completeLoading();
                }
                if (sprite instanceof CombatSprite && !combatCache.contains(sprite))
                    combatCache.add((CombatSprite) sprite);
                generateID(sprite);
            }
        });
    }

    private static final Random random = new Random();
    private void generateID(Sprite sprite) {
        int ID;
        do {
            ID = random.nextInt();
        } while(!isIDFree(ID));
        setID(sprite, ID);
    }

    private boolean isIDFree(int ID) {
        for (Drawable d : drawable) {
            if (d instanceof Sprite) {
                Sprite s = (Sprite) d;
                if (s.getID() == ID)
                    return false;
            }
        }
        return true;
    }

    private void setID(Sprite sprite, int ID) {
        try {
            Field field = Sprite.class.getDeclaredField("ID");
            field.setAccessible(true);
            field.set(sprite, ID);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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

    public void onDisplay() { //This method is called when the world is displayed on the screen
        prepareTiles();

        showing = true;

        if (lightShader != null) {
            lightShader.addAll(lights);
            lightShader.setOverallBrightness(worldBrightness);
        }

        if (loader != null)
            loader.onDisplay(this);

        waiter._wakeAllWaiters();

        if (tiledData != null) {
            float minX = 0f, minY = 0f;
            float tWidth = (tiledData.getWidth() * tiledData.getTileWidth());
            float tHeight = (tiledData.getHeight() * tiledData.getTileHeight());
            float removeX = 0f, removeY = 0f;
            if (tWidth > (GameSettings.Display.game_width / 2f) + 16f)
                removeX = (GameSettings.Display.game_width / 2f) + 16f;
            else {
                Camera.removeBounds();
                return;
            }
            if (tHeight > (GameSettings.Display.game_height / 2f) + 16f)
                removeY = (GameSettings.Display.game_height / 2f) + 16f;
            else {
                Camera.removeBounds();
                return;
            }

            Camera.setBounds(
                    minX,
                    minY,
                    tWidth - removeX,
                    tHeight - removeY
            );
        } else {
            Camera.removeBounds();
        }
    }

    public void onUnload() { //This method is called when the world is not shown but is still in memory
        //TODO Do stuff to save memory when this world is not shown
        if (lightShader != null) {
            lightShader.clear();
            lightShader.setOverallBrightness(1f);
        }
        showing = false;
    }

    public void onDispose() {
        Drawable[] drawables = drawable.toArray(new Drawable[drawable.size()]);
        for (Drawable d : drawables) {
            if (d instanceof Sprite)
                ((Sprite)d).onUnload();
        }
        UI[] uis = uiElements.toArray(new UI[uiElements.size()]);
        for (UI u : uis) {
            u.close();
        }

        drawable.clear();
        unsorted.clear();
        udrawables.clear();
        layerCache.clear();
        for (CombatSprite c : combatCache) {
            HitBox.unregisterSprite(c);
        }
        combatCache.clear();
        lights.clear();
        prepared = false;

        if (tiledData != null) tiledData.dispose();
        for (Framebuffer frame : frames) {
            frame.dispose();
        }

        renderingService = null;
        showing = false;
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
                if (drawable instanceof UI) {
                    while (World.this.udrawables.contains(drawable))
                        World.this.udrawables.remove(drawable);
                    while (World.this.uiElements.contains(drawable))
                        World.this.uiElements.remove(drawable);
                    if (runnable != null)
                        runnable.run();
                    return;
                }
                if (drawable.neverSort()) {
                    while (World.this.unsorted.contains(drawable))
                        World.this.unsorted.remove(drawable);
                } else {
                    while (World.this.drawable.contains(drawable))
                        World.this.drawable.remove(drawable);
                }
                if (drawable instanceof UpdatableDrawable) {
                    while (World.this.udrawables.contains(drawable))
                        World.this.udrawables.remove(drawable);
                }
                if (runnable != null)
                    runnable.run();
            }
        }, true);
    }

    private <T> void searchAndRemove(List<T> list, T object) {
        T toremove = null;
        for (T obj : list) {
            T trueObject = ProxyFactory.unwrapObject(obj);
            if (object.equals(trueObject)) {
                toremove = obj;
                break;
            }
        }
        if (toremove != null) {
            boolean success = list.remove(toremove);
            if (!success)
                System.out.println("uwotm8");
        }
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
                    Texture t = Texture.retrieveTexture(resource);
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
    public void loadAndAdd(Drawable... drawables) {
        for (Drawable d : drawables) {
            if (d instanceof Sprite) {
                if (d instanceof AnimatedSprite) {
                    loadAnimatedTextureForSprite((AnimatedSprite) d);
                }
                addSprite((Sprite) d);
            } else {
                addDrawable(d);
            }
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
        if (tiledData == null)
            return objects;
        Layer[] objLayers = getLayers(LayerType.OBJECT_LAYER);
        for (Layer layer : objLayers) {
            for (TiledObject obj : layer.getObjectGroupData()) {
                if ((obj.isDoor() || obj.isTrigger() || obj.isHitbox()) && obj.isPointInside(x, y)) {
                    objects.add(obj);
                }
            }
        }
        return objects;
    }

    public TiledObject getSpawn(String name) {
        if (tiledData == null)
            return null;
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
        if (tiledData == null)
            return null;
        Validator.validateNotBelow(layernumber, 0, "layer");
        Validator.validateNotOver(layernumber, tiledData.getLayers().length, "layer");
        Layer l = tiledData.getLayers()[layernumber];

        return getTileAt(x, y, l);
    }


    private EnumMap<LayerType, Layer[]> layerCache = new EnumMap<LayerType, Layer[]>(LayerType.class);
    public Layer[] getLayers(LayerType type) {
        if (tiledData == null)
            return new Layer[0];
        if (!layerCache.containsKey(type)) {
            List<Layer> layers = new ArrayList<Layer>();
            for (Layer l : tiledData.getLayers()) {
                if (l.getLayerType() == type)
                    layers.add(l);
            }

            Layer[] temp = layers.toArray(new Layer[layers.size()]);
            layerCache.put(type, temp);
        }

        return layerCache.get(type);
    }

    public Layer[] getLayers() {
        return tiledData.getLayers();
    }

    public Layer getLowestGroundLayer() {
        if (tiledData == null)
            return null;

        Layer lowestLayer = null;
        Layer[] layers = getLayers(LayerType.TILE_LAYER);
        for (Layer l : layers) {
            if (!l.isGroundLayer())
                continue;
            if (lowestLayer == null) {
                lowestLayer = l;
            } else if (lowestLayer.getLayerNumber() > l.getLayerNumber())
                lowestLayer = l;
        }

        return lowestLayer;
    }

    public Layer getHighestGroundLayer() {
        if (tiledData == null)
            return null;

        Layer highestLayer = null;
        Layer[] layers = getLayers(LayerType.TILE_LAYER);
        for (Layer l : layers) {
            if (!l.isGroundLayer())
                continue;
            if (highestLayer == null) {
                highestLayer = l;
            } else if (highestLayer.getLayerNumber() < l.getLayerNumber())
                highestLayer = l;
        }

        return highestLayer;
    }

    /**
     * Returns the width of the world in tiles.
     */
    public int getWidth() {
        if (tiledData == null)
            return getPixelWidth();
        return tiledData.getWidth();
    }

    /**
     * Returns the width of the world in pixels.
     */
    public int getPixelWidth() {
        if (tiledData == null)
            return 640;
        return tiledData.getWidth() * tiledData.getTileWidth();
    }

    /**
     * Returns the height of the world in tiles.
     */
    public int getHeight() {
        if (tiledData == null)
            return getPixelHeight();
        return tiledData.getHeight();
    }

    /**
     * Returns the height of the world in pixels.
     */
    public int getPixelHeight() {
        if (tiledData == null)
            return 360;
        return tiledData.getHeight() * tiledData.getTileHeight();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NodeMap getNodeMap() {
        return activeNodeMap;
    }

    public void setActiveNodeMap(NodeMap map) {
        this.activeNodeMap = map;
    }

    public NodeMap getDefaultNodeMap() {
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

    public int getUpdatableCount() {
        return this.udrawables.size();
    }

    public Iterator<Drawable> getUnsortedDrawables() {
        return unsorted.iterator();
    }

    public void dispose() {
        onUnload();
        onDispose();
    }

    public void requestMoveDrawable(final Drawable drawable, final int i) {
        if (drawable.neverSort() || drawable instanceof UI) {
            RenderService.INSTANCE.runOnServiceThread(new Runnable() {
                @Override
                public void run() {
                    if (drawable instanceof UI) {
                        UI ui = (UI)drawable;
                        uiElements.remove(ui);

                        uiElements.add(i, ui);
                    } else {
                        unsorted.remove(drawable);
                        unsorted.add(i, drawable);
                    }
                }
            }, true); //Always invoke the request on the next frame, to avoid a concurrent exception
        } else {
            throw new InvalidParameterException("Only drawables that don't sort or UI drawables can be moved!");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        World world = (World) o;

        if (ID != world.ID) return false;
        if (!combatCache.equals(world.combatCache)) return false;
        if (!drawable.equals(world.drawable)) return false;
        if (!lights.equals(world.lights)) return false;
        if (!name.equals(world.name)) return false;
        if (tiledData != null ? !tiledData.equals(world.tiledData) : world.tiledData != null) return false;
        if (!udrawables.equals(world.udrawables)) return false;
        if (!uiElements.equals(world.uiElements)) return false;
        if (!unsorted.equals(world.unsorted)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + ID;
        return result;
    }

    public Layer getLayer(float layer, LayerType type) {
        Layer[] layers = getLayers(LayerType.TILE_LAYER);
        for (Layer l : layers) {
            if (l.getGameLayer(this) == layer)
                return l;
        }

        return null;
    }

    public List<UI> getUIList() {
        return this.uiElements;
    }

    private class DisplayWaiters {

        public synchronized void _wait() throws InterruptedException {
            while (true) {
                if (World.this.showing)
                    break;
                super.wait(0L);
            }
        }

        public synchronized void _wakeAllWaiters() {
            super.notifyAll();
        }
    }
}
