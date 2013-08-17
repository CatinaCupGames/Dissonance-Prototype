package com.tog.framework.game.world;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;

public class World implements Drawable {
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

    public void load(final String world) throws WorldLoadFailedException {
        if (renderingService == null)
            throw new WorldLoadFailedException("The RenderService was not created! Try calling World.init() before loading a world.");

        //TODO Load world data

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
        addDrawable(this);

        if (renderingService.isPaused())
            renderingService.resume();
    }

    private boolean invalid = true;
    public Iterator<Drawable> getDrawable() {
        if (invalid) {
            System.out.println("SORTING DRAWABLES");
            long mili = System.currentTimeMillis();
            Collections.sort(drawable);
            long fmili = System.currentTimeMillis();
            System.out.println("Took " + (fmili - mili) + "ms to sort " + drawable.size() + " sprites.");
            invalid = false;
        }
        return drawable.iterator();
    }

    public void invalidateList() {
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
    public int compareTo(Drawable o) {
        return BEFORE;
    }
}
