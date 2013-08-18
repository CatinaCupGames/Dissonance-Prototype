package com.tog.framework.game.sprites;

import com.tog.framework.render.Drawable;
import com.tog.framework.render.texture.Texture;

import java.awt.*;
import java.awt.image.BufferedImage;

import static org.lwjgl.opengl.GL11.*;

public abstract class UIElement implements Drawable {
    private boolean valid;
    private float width;
    private float height;
    private Texture UI;
    private BufferedImage UI_IMAGE;
    private float x;
    private float y;
    private String name;

    public UIElement(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
    public void setWidth(float width) {
        this.width = 2;
        while (this.width < width) {
            this.width *= 2;
        }
        invalidateView();
    }

    /**
     * Set the width of this UIElement. The parameter passed with be rounded to the down to the nearest power of 2.
     * This method will also invalidate the view and will call {@link UIElement#draw(java.awt.Graphics2D)} on the next
     * frame.
     * @param height
     *             The height to set UIElement to
     */
    public void setHeight(float height) {
        this.height = 2;
        while (this.height < height) {
            this.height *= 2;
        }
        invalidateView();
    }

    public void invalidateView() {
        valid = false;
    }

    private void redrawTexture() {
        if (valid)
            return;
        if (UI_IMAGE.getWidth() != width || UI_IMAGE.getHeight() != height) {
            UI.dispose();
            UI = null;
            UI_IMAGE = new BufferedImage((int)width, (int)height, BufferedImage.TYPE_INT_ARGB);
        }
        Graphics2D graphics2D = UI_IMAGE.createGraphics();
        graphics2D.setColor(Color.WHITE);
        graphics2D.drawRect(0, 0, (int) width, (int) height);
        draw(graphics2D);
        graphics2D.dispose();

        UI = Texture.convertToTexture(name, UI_IMAGE);
        valid = true;
    }

    @Override
    public void render() {
        if (UI == null && valid)
            return;
        else if (!valid)
            redrawTexture();
        UI.bind();
        float bx = UI.getTextureWidth() / 2;
        float by = UI.getTextureHeight() / 2;
        final float x = getX(), y = getY();
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
        UI.unbind();
    }

    public abstract void draw(Graphics2D graphics2D);

    @Override
    public int compareTo(Drawable o) {
        return Drawable.AFTER;
    }
}
