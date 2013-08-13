package com.tog.framework.game.world;

import com.tog.framework.game.sprites.Sprite;
import com.tog.framework.game.sprites.AnimatedSprite;
import com.tog.framework.render.Drawable;
import com.tog.framework.render.RenderService;
import com.tog.framework.render.texture.Texture;
import com.tog.framework.render.texture.sprite.SpriteTexture;
import com.tog.framework.system.Service;
import com.tog.framework.system.ServiceManager;
import com.tog.framework.system.exceptions.WorldLoadFailedException;
import com.tog.framework.system.utils.Validator;
import org.lwjgl.util.vector.Vector2f;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class World extends Sprite {
    private final ArrayList<Drawable> drawable = new ArrayList<>();
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
            throw new WorldLoadFailedException("The RenderService was not created! Try calling World.init() before loading a world.");

        //TODO Load world data

        loadTextureForSprite("world/tex/" + world + ".png", this);
        addSprite(this);

        if (renderingService.isPaused())
            renderingService.resume();
    }

    public Iterator<Drawable> getDrawable() {
        return drawable.iterator();
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

    float x, y;
    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public Vector2f getVector() {
        return new Vector2f(0, 0);
    }

    @Override
    public void update() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}