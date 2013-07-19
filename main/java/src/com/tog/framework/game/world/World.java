package com.tog.framework.game.world;

import com.tog.framework.game.sprites.Sprite;
import com.tog.framework.render.RenderService;
import com.tog.framework.render.Texture;
import com.tog.framework.system.Service;
import com.tog.framework.system.ServiceManager;
import com.tog.framework.system.exceptions.WorldLoadFailedException;
import com.tog.framework.system.utils.Validator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class World implements Sprite {
    private final ArrayList<Sprite> sprites = new ArrayList<>();
    private Service renderingService;
    private Texture texture;

    public void init() {
        if (renderingService != null) {
            renderingService.provideData(this, RenderService.WORLD_DATA_TYPE);
            return;
        }
        renderingService = ServiceManager.createService(RenderService.class);
        renderingService.provideData(this, RenderService.WORLD_DATA_TYPE);
    }

    public void load(String world) throws WorldLoadFailedException {
        if (renderingService == null)
            throw new WorldLoadFailedException("The RenderService was not created! Try calling World.init before loading a world.");

        //TODO Load world data

        loadTextureForSprite("world/tex/" + world + ".png", this);
        addSprite(this);

        if (renderingService.isPaused())
            renderingService.resume();
    }

    public Iterator<Sprite> getSprites() {
        return sprites.iterator();
    }

    public void addSprite(final Sprite sprite) {
        if (renderingService == null)
            throw new IllegalStateException("init() has not been called on this world!");
        Validator.validateNotNull(sprite, "sprite");

        renderingService.runOnServiceThread(new Runnable() {

            @Override
            public void run() {
                sprite.setWorld(World.this);
                sprites.add(sprite);
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

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void setWorld(World w) { } //This sprite is a world

    @Override
    public World getWorld() {
        return this;
    }

    @Override
    public float getX() {
        return 0;
    }

    @Override
    public float getY() {
        return 0;
    }
}
