package com.dissonance.framework.game.world;

import com.dissonance.framework.game.ai.NodeMap;
import com.dissonance.framework.game.sprites.AnimatedSprite;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.render.texture.sprite.SpriteTexture;
import com.dissonance.framework.system.Service;
import com.dissonance.framework.system.ServiceManager;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;
import com.dissonance.framework.system.utils.Validator;
import org.jbox2d.common.Vec2;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.lwjgl.opengl.GL11.*;

public final class World implements Drawable {
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
    private String texture_location;
    private Sprite[] preloadSprites = null;
    private Drawable[] preloadDrawable = null;
    private boolean invalid = true;

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
        glVertex3f(x - bx, y - by, 0f);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex3f(x + bx, y - by, 0f);
        glTexCoord2f(1f, 1f); //top right
        glVertex3f(x + bx, y + by, 0f);
        glTexCoord2f(0f, 1f); //top left
        glVertex3f(x - bx, y + by, 0f);
        glEnd();
        texture.unbind();
    }

    public void load(final String world) throws WorldLoadFailedException {
        if (renderingService == null)
            throw new WorldLoadFailedException("The RenderService was not created! Try calling World.init() before loading a world.");

        texture_location = "default";
        InputStream fis = getClass().getResourceAsStream("worlds/" + world + "/" + world + ".dat");
        if (fis != null) {
            try {
                GZIPInputStream gzip = new GZIPInputStream(fis);
                ObjectInputStream objectInputStream = new ObjectInputStream(gzip);

                name = objectInputStream.readUTF();
                if (objectInputStream.readByte() == 1) {
                    preloadSprites = (Sprite[]) objectInputStream.readObject();
                }
                if (objectInputStream.readByte() == 1) {
                    preloadDrawable = (Drawable[]) objectInputStream.readObject();
                }
                if (objectInputStream.readByte() == 1) {
                    Object worldListener = objectInputStream.readObject(); //TODO Attach listener..?
                }
                if (objectInputStream.readByte() == 1) {
                    texture_location = objectInputStream.readUTF();
                }
                node_map = (NodeMap) objectInputStream.readObject();
            } catch (IOException e) {
                throw new WorldLoadFailedException("There was an error reading the World's .dat file!", e);
            } catch (ClassNotFoundException e) {
                throw new WorldLoadFailedException("There was a problem looking up a class!", e);
            }
        }

        renderingService.runOnServiceThread(new Runnable() {

            @Override
            public void run() {
                try {
                    World.this.texture = Texture.retriveTexture(texture_location.equals("default") ? "worlds/" + world + "/" + world + ".png" : texture_location);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        addDrawable(this);

        if (preloadDrawable != null) {
            for (Drawable d : preloadDrawable) {
                loadAndAdd(d);
            }
        }

        if (preloadSprites != null) {
            for (Sprite s : preloadSprites) {
                loadAndAdd(s);
            }
        }

        if (renderingService.isPaused())
            renderingService.resume();
    }

    public void save(String filepath) throws IOException {
        File f = new File(filepath);
        if (!f.exists()) {
            boolean b = f.createNewFile();
            if (!b)
                throw new IOException("Could not create .dat file!");
        }

        FileOutputStream fileOutputStream = new FileOutputStream(f);
        GZIPOutputStream gzipOutputStream = new GZIPOutputStream(fileOutputStream);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(gzipOutputStream);


        boolean hasSprites = preloadSprites != null;
        boolean hasDrawable = preloadDrawable != null;
        boolean hasListener = false; //TODO Check
        boolean hasTexture = !texture_location.equals("default");
        objectOutputStream.writeUTF(name);
        objectOutputStream.writeByte(hasSprites ? 1 : 0);
        if (hasSprites)
            objectOutputStream.writeObject(preloadSprites);
        objectOutputStream.writeByte(hasDrawable ? 1 : 0);
        if (hasDrawable)
            objectOutputStream.writeObject(preloadDrawable);
        objectOutputStream.writeByte(hasListener ? 1 : 0);
        if (hasListener) {
            //TODO Write listener
        }
        objectOutputStream.writeByte(hasTexture ? 1 : 0);
        if (hasTexture)
            objectOutputStream.writeUTF(texture_location);
        objectOutputStream.writeObject(node_map);
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
        });
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
        });
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

    public void setPreloadedSprites(Sprite[] sprites) {
        this.preloadSprites = sprites;
    }

    public void setPreloadedDrawables(Drawable[] drawables) {
        this.preloadDrawable = drawables;
    }

    public void setCustomTexturePath(String path) {
        this.texture_location = path;
    }

    public org.jbox2d.dynamics.World getPhysicsWorld() {
        return this.physicsWorld;
    }

    public int compareTo(Drawable o) {
        return BEFORE;
    }
}
