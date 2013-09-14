package com.dissonance.framework.game.world;

import com.dissonance.framework.game.ai.astar.NodeMap;
import com.dissonance.framework.game.sprites.AnimatedSprite;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.render.texture.sprite.SpriteTexture;
import com.dissonance.framework.render.texture.tile.TileTexture;
import com.dissonance.framework.system.Service;
import com.dissonance.framework.system.ServiceManager;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;
import com.dissonance.framework.system.utils.ReflectionUtils;
import com.dissonance.framework.system.utils.Validator;
import org.jbox2d.common.Vec2;
import tiled.core.Map;
import tiled.core.MapLayer;
import tiled.core.TileLayer;
import tiled.core.TileSet;
import tiled.io.TMXMapReader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;

public final class World implements Drawable {
    private static final TMXMapReader MAP_READER = new TMXMapReader();
    private static final float GRAVITY = 9.81f;
    private static final float TIME_STEP = 1f / 60f;
    private static final int VELOCITY_ITERATIONS = 6;
    private static final int POSITION_ITERATIONS = 2;

    private transient final ArrayList<Drawable> drawable = new ArrayList<>();
    private transient org.jbox2d.dynamics.World physicsWorld;
    private String name;
    private NodeMap node_map;
    private int ID;
    private transient Service renderingService;
    private transient Texture texture;
    private boolean invalid = true;
    private Map tiledMap;

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
        if(this.physicsWorld != null) {
            this.physicsWorld.step(TIME_STEP, VELOCITY_ITERATIONS, POSITION_ITERATIONS);
        }
    }

    @Override
    public void render() {
        if (texture == null)
            return;
        texture.bind();
        float bx = texture.getTextureWidth() / 2;
        float by = texture.getTextureHeight() / 2;
        final float x = 0, y = 0;
        //glColor3f(1f, .5f, .5f); DEBUG LINE FOR TEXTURES
        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex2f(x - bx, y - by);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex2f(x + bx, y - by);
        glTexCoord2f(1f, 1f); //top right
        glVertex2f(x + bx, y + by);
        glTexCoord2f(0f, 1f); //top left
        glVertex2f(x - bx, y + by);
        glEnd();
        texture.unbind();
    }

    public void load(final String world) throws WorldLoadFailedException {
        if (renderingService == null)
            throw new WorldLoadFailedException("The RenderService was not created! Try calling World.init() before loading a world.");

        InputStream in = getClass().getClassLoader().getResourceAsStream("worlds/" + world + ".tmx");
        if (in != null) {
            try {
                tiledMap = MAP_READER.readMap(in);
                //Load all tileSets into textures
                for (TileSet sets : tiledMap.getTileSets()) {
                    Image awtImage = ReflectionUtils.getPrivateField("tileSetImage", sets, Image.class);
                    if (!(awtImage instanceof BufferedImage)) {
                        continue;
                    }
                    Texture temp = Texture.convertToTexture(sets.getName(), (BufferedImage)awtImage);
                    TileTexture tileset = new TileTexture(temp, sets.getTileWidth(), sets.getTileHeight(), sets.getTileSpacing(), sets.getTileMargin(), sets.getTilesPerRow());
                    Texture.replaceTexture(temp, tileset);
                }

                for (MapLayer layer : tiledMap) {
                    if (layer instanceof TileLayer) {
                        TileLayer tlayer = (TileLayer)layer;

                    }
                }
            } catch (Exception e) {
                throw new WorldLoadFailedException("Error loading TMX file!", e);
            }
        }
        //===TEMP CODE===
        renderingService.runOnServiceThread(new Runnable() {

            @Override
            public void run() {
                try {
                    World.this.texture = Texture.retriveTexture("worlds/" + world + "/" + world + ".png");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //===TEMP CODE===

        addDrawable(this); //TODO Maybe remove this..

        if (renderingService.isPaused())
            renderingService.resume();
    }

    public Iterator<Drawable> getDrawable() {
        if (invalid) {
            Collections.sort(drawable);
            invalid = false;
        }
        return drawable.iterator();
    }

    public void invalidateDrawableList() {
        invalid = true;
    }

    private void addDrawable(final Drawable draw, final Runnable run) {
        if (renderingService == null)
            throw new IllegalStateException("init() has not been called on this world!");
        Validator.validateNotNull(draw, "sprite");

        renderingService.runOnServiceThread(new Runnable() {

            @Override
            public void run() {
                drawable.add(draw);
                if (!(draw instanceof World))
                    draw.init();
                if (run != null)
                    run.run();
            }
        }, true); //Force adding drawables on next frame
    }

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public NodeMap getNodeMap() {
        return node_map;
    }

    public void setNodeMap(NodeMap map) {
        this.node_map = map;
    }

    public org.jbox2d.dynamics.World getPhysicsWorld() {
        return this.physicsWorld;
    }

    public int compareTo(Drawable o) {
        return BEFORE;
    }
}
