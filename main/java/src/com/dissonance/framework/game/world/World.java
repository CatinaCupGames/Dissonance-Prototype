package com.dissonance.framework.game.world;

import com.dissonance.framework.game.ai.astar.NodeMap;
import com.dissonance.framework.game.sprites.AnimatedSprite;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.world.tiled.WorldData;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.RenderService;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;

public final class World implements Drawable {
    private static final Gson GSON = new Gson();
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
    private WorldData tiledData;

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

        InputStream in = getClass().getClassLoader().getResourceAsStream("worlds/" + world + ".json");
        if (in != null) {
            try {
                tiledData = GSON.fromJson(new InputStreamReader(in), WorldData.class);
                tiledData.loadAllTileSets();
                System.out.println(tiledData);
            } catch (Exception e) {
                throw new WorldLoadFailedException("Error loading Tiled file!", e);
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

    /**
     * Find an {@link AnimatedSprite} in this world with the name <b>name</b>
     * @param name
     *            The full name of the {@link AnimatedSprite}
     * @return
     *        The {@link AnimatedSprite} with the same name
     */
    public AnimatedSprite getAnimatedSpriteByName(String name) {
        for (Drawable d : drawable) {
            if (d instanceof AnimatedSprite) {
                AnimatedSprite sprite = (AnimatedSprite)d;
                if (sprite.getSpriteName().equals(name))
                    return sprite;
            }
        }
        return null;
    }

    /**
     * Find an {@link AnimatedSprite} with the name similar to <b>name</b> <br></br>
     * If more than 1 {@link AnimatedSprite} is found with similar names, then this method will return name.
     * @param name
     *            The search term
     * @return
     *        An {@link AnimatedSprite} object with a similar name. <br></br>
     *        This method will return null if no {@link AnimatedSprite} was found <b>or</b> if 2 or more sprites were found with the given search term.
     */
    public AnimatedSprite findAnimatedSprite(String name) {
        AnimatedSprite toreturn = null;
        for (Drawable d : drawable) {
            if (d instanceof AnimatedSprite) {
                AnimatedSprite sprite = (AnimatedSprite)d;
                if (toreturn == null && sprite.getSpriteName().contains(name)) {
                    toreturn = sprite;
                } else if (toreturn != null && sprite.getSpriteName().contains(name)) {
                    return null;
                }
            }
        }
        return toreturn;
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
