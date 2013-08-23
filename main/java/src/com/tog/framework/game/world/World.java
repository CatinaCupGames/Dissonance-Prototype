package com.tog.framework.game.world;

import com.tog.framework.game.ai.NodeMap;
import com.tog.framework.game.sprites.AnimatedSprite;
import com.tog.framework.game.sprites.Sprite;
import com.tog.framework.render.Drawable;
import com.tog.framework.render.RenderService;
import com.tog.framework.render.texture.Texture;
import com.tog.framework.render.texture.sprite.SpriteTexture;
import com.tog.framework.system.Service;
import com.tog.framework.system.ServiceManager;
import com.tog.framework.system.exceptions.WorldLoadFailedException;
import com.tog.framework.system.utils.Validator;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static org.lwjgl.opengl.GL11.*;

public final class World implements Drawable {
    private String name;
    private NodeMap node_map;

    private int ID;

    private transient final ArrayList<Drawable> drawable = new ArrayList<>();
    private transient Service renderingService;
    private transient Texture texture;

    World(int ID) {
        this.ID = ID;
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

    private String texture_location;
    private Sprite[] preloadSprites = null;
    private Drawable[] preloadDrawable = null;
    public void load(final String world) throws WorldLoadFailedException {
        if (renderingService == null)
            throw new WorldLoadFailedException("The RenderService was not created! Try calling World.init() before loading a world.");

        texture_location =  "default";
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
                node_map = (NodeMap)objectInputStream.readObject();
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


    private boolean invalid = true;
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
            addSprite((Sprite)d);
        } else {
            addDrawable(d);
        }
    }

    public String getName() {
        return name;
    }

    public NodeMap getNodeMap() {
        return node_map;
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

    public void setNodeMap(NodeMap map) {
        this.node_map = map;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Drawable o) {
        return BEFORE;
    }
}
