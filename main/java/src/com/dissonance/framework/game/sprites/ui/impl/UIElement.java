package com.dissonance.framework.game.sprites.ui.impl;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.render.texture.TextureLoader;
import com.dissonance.framework.system.GameSettings;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.lwjgl.opengl.GL11.*;

/**
 * @deprecated UIElements are slow and buggy, please dont use it. <br></br>
 * If you want to create Menu's or HUD's with text, then have your class implements {@link com.dissonance.framework.game.sprites.ui.UI} and
 * use {@link org.newdawn.slick.TrueTypeFont} to draw text using OpenGL.
 */
@Deprecated
public abstract class UIElement implements com.dissonance.framework.game.sprites.ui.UI {
    private boolean valid;
    private boolean complete_invalid;
    private int width;
    private int height;
    private float dWidth, dHeight;
    private float tX, tY;
    private Texture UI;
    private BufferedImage UI_IMAGE;
    private float x;
    private float y;
    private String name;
    private static int instanceCount = 0;
    private boolean halted = false;

    public UIElement() {
        setName(instanceCount + "-UIELEMENT-" + instanceCount);
        instanceCount++;
    }

    public void marginLeft(float value) {
        this.x = value + (width / 2f);
    }

    public void marginRight(float value) {
        this.x = ((GameSettings.Display.window_width / 2f) - value) - (width / 2f);
    }

    public void marginTop(float value) {
        this.y = value + (height / 2f);
    }

    public void marginBottom(float value) {
        this.y = ((GameSettings.Display.window_height / 2f) - value) - (height / 2f);
    }

    public void centerHorizontal() {
        marginLeft((GameSettings.Display.window_width / 4f) - (width / 2f));
    }

    public void centerVertical() {
        marginTop((GameSettings.Display.window_height / 4f) - (height / 2f));
    }

    public String getName() {
        return name;
    }

    public void displayUI() {
        displayUI(true);
    }

    public void displayUI(World world) {
        displayUI(false, world);
    }

    public void displayUI(boolean halt) {
        displayUI(halt, WorldFactory.getCurrentWorld());
    }

    public void displayUI(boolean halt, World world) {
        world.addDrawable(this);
        if (halt) {
           PlayableSprite.getCurrentlyPlayingSprite().freeze(true, UIElement.class);
            //PlayableSprite.haltMovement();
            halted = true;
        }
    }

    private void _close() {
        if (halted) {
            PlayableSprite.getCurrentlyPlayingSprite().unfreeze(UIElement.class);
        }
        GameService.getCurrentWorld().removeDrawable(this);

        if (UI != null) {
            UI.dispose();
        }
        if (UI_IMAGE != null) {
            UI_IMAGE = null;
        }
    }

    public void close() {
        if (RenderService.isInRenderThread()) {
            RenderService.INSTANCE.runOnServiceThread(new Runnable() {
                @Override
                public void run() {
                    _close();
                }
            }, true);
        } else {
            _close();
        }
    }

    /**
     * Set the name of this UIElement <br></br>
     * <b>Warning: </b> Names must be unique. If 2 UIElements have the same name, the first one rendered will be used
     * for both, even when the {@link UIElement#draw(java.awt.Graphics2D)} method is invoked on both. This is caused
     * by how {@link Texture#convertToTexture(String, java.awt.image.BufferedImage)} cache's texture's.
     * @param name
     *            The name for this UIElement
     */
    public void setName(String name) {
        this.name = name;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    /**
     * Set the width of this UIElement. The parameter passed with be rounded to the down to the nearest power of 2.
     * This method will also invalidate the view and will call {@link UIElement#draw(java.awt.Graphics2D)} on the next
     * frame.
     * @param width
     *             The width to set UIElement to
     */
    public void setWidth(int width) {
        this.width = width;
        completelyInvalidateView();
    }

    /**
     * Set the width of this UIElement. The parameter passed with be rounded to the down to the nearest power of 2.
     * This method will also invalidate the view and will call {@link UIElement#draw(java.awt.Graphics2D)} on the next
     * frame.
     * @param height
     *             The height to set UIElement to
     */
    public void setHeight(int height) {
        this.height = height;
        completelyInvalidateView();
    }

    /**
     * Sets how big this UIElement is drawn on the screen. {@link org.lwjgl.opengl.GL11#GL_NEAREST} will be used when scaling up and down.
     * @param width The draw width for this UIElement.
     */
    public void setDrawWidth(float width) {
        this.dWidth = width;
    }

    /**
     * Sets how big this UIElement is drawn on the screen. {@link org.lwjgl.opengl.GL11#GL_NEAREST} will be used when scaling up and down.
     * @param height The draw height for this UIElement.
     */
    public void setDrawHeight(float height) {
        this.dHeight = height;
    }

    public void resetDrawSize() {
        this.dWidth = width;
        this.dHeight = height;
    }

    /**
     * Invalidate this UIElement but reuse the current texture
     */
    public void invalidateView() {
        valid = false;
    }

    /**
     * Completely invalidate this UIElement and discard the current texture
     */
    public void completelyInvalidateView() {
        valid = false;
        complete_invalid = true;
    }

    private void redrawTexture() {
        if (valid || width == 0 || height == 0)
            return;
        if (UI_IMAGE == null || complete_invalid) {
            UI_IMAGE = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            resetDrawSize();
        }
        Graphics2D graphics2D = UI_IMAGE.createGraphics();
        graphics2D.setColor(new Color(0f,0f,0f,0f));
        graphics2D.fillRect(0,0, width, height);
        draw(graphics2D);
        graphics2D.dispose();
        boolean old = TextureLoader.isFastRedraw();
        TextureLoader.setFastRedraw(true);

        if (UI == null) UI = Texture.convertToTexture(name, UI_IMAGE);
        else Texture.redrawTexture(UI, UI_IMAGE);

        tX = UI.getWidth();
        tY = UI.getHeight();
        valid = true;
        TextureLoader.setFastRedraw(old);
    }

    @Override
    public void render() {
        if (UI == null && valid)
            return;
        else if (!valid && width != 0 && height != 0)
            redrawTexture();
        else if (!valid)
            return;
        //glDisable(GL_DEPTH_TEST);
        UI.bind();
        float bx = dWidth / 2;
        float by = dHeight / 2;
        final float x = getX(), y = getY();
        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex3f(x - bx, y - by, 0f);
        glTexCoord2f(tX, 0f); //bottom right
        glVertex3f(x + bx, y - by, 0f);
        glTexCoord2f(tX, tY); //top right
        glVertex3f(x + bx, y + by, 0f);
        glTexCoord2f(0f, tY); //top left
        glVertex3f(x - bx, y + by, 0f);
        glEnd();
        UI.unbind();
        //glEnable(GL_DEPTH_TEST);
    }

    public abstract void draw(Graphics2D graphics2D);

    @Override
    public int compareTo(Drawable o) {
        return UpdatableDrawable.AFTER;
    }
}
