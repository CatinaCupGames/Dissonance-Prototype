package com.dissonance.framework.game.sprites.ui.impl;

import com.dissonance.framework.game.sprites.ui.UI;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.system.GameSettings;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.glColor4f;

public abstract class AbstractUI implements UI {
    private float x, y;
    protected float width, height;
    protected boolean opened;
    protected World world;
    protected float alpha = 1f;
    private AbstractUI parent;
    private float[] alignment = new float[] { 0, 0, 0, 0 }; //Alignment for textures
    private ArrayList<UI> children = new ArrayList<>();
    private boolean scale = true;

    public AbstractUI() {
        this(null);
    }

    public AbstractUI(AbstractUI parent) {
        setParent(parent);
    }

    public void scale(boolean value) {
        this.scale = value;
    }

    public boolean isScaling() {
        return scale;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    @Override
    public final void render() {
        if (!scale) {
            RenderService.removeScale();
        }
        float alpha = RenderService.getCurrentAlphaValue();
        if (this.alpha < 1) {
            alpha = this.alpha - (1 - RenderService.getCurrentAlphaValue());
            if (alpha < 0)
                alpha = 0;
        }
        glColor4f(1f, 1f, 1f, alpha);
        onRender();
        glColor4f(1f, 1f, 1f, RenderService.getCurrentAlphaValue());
        if (!scale) {
            RenderService.resetScale();
        }
    }

    protected abstract void onRender();

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setX(float x) {
        float dif = this.x - x;
        this.x = x;
        for (UI child : children) {
            child.setX(child.getX() - dif);
        }
    }

    @Override
    public void setY(float y) {
        float dif = this.y - y;
        this.y = y;
        for (UI child : children) {
            child.setY(child.getY() - dif);
        }
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public boolean neverClip() {
        return false;
    }

    @Override
    public boolean neverSort() {
        return false;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void marginLeft(float value) {
        setX(getLeftOfParent() + value + (width / 2f));
    }

    public void marginRight(float value) {
        setX((getRightOfParent() - value) - (width / 2f));
        if (!scale && parent == null)
            setY(getY() * 2f);
    }

    public void marginTop(float value) {
        setY(getTopOfParent() + value + (height / 2f));
    }

    public void marginBottom(float value) {
        setY((getBottomOfParent() - value) - (height / 2f));
        if (!scale && parent == null)
            setY(getY() * 2f);
    }

    public void centerHorizontal() {
        marginLeft((getParentWidth() / 4f) - (width / 2f));
    }

    public void centerVertical() {
        marginTop((getParentHeight() / 4f) - (height / 2f));
    }

    public void setParent(AbstractUI parent) {
        if (this.parent != null)
            this.parent.children.remove(this);

        this.parent = parent;
        if (this.parent != null)
            this.parent.children.add(this);
    }

    public void setAlignment(float leftAdd, float topAdd, float bottomAdd, float rightAdd) {
        this.alignment[0] = leftAdd;
        this.alignment[1] = topAdd;
        this.alignment[2] = bottomAdd;
        this.alignment[3] = rightAdd;
    }

    public void alignToTexture(Texture texture) {
        float bH = texture.getTextureHeight() - texture.getImageHeight();
        float rW = texture.getTextureWidth() - texture.getImageWidth();
        setAlignment(0f, 0f, bH, rW);
    }

    public AbstractUI getParent() {
        return parent;
    }

    public ArrayList<UI> getChildren() {
        return children;
    }

    private float getLeftOfParent() {
        if (parent == null)
            return 0 + alignment[0];
        else
            return (parent.getX() - (parent.getWidth() / 2f)) + alignment[0];
    }

    private float getParentWidth() {
        if (parent == null)
            return GameSettings.Display.window_width;
        else
            return parent.getWidth() * 2f;
    }

    private float getParentHeight() {
        if (parent == null)
            return GameSettings.Display.window_height;
        else
            return parent.getHeight() * 2f;
    }

    private float getRightOfParent() {
        if (parent == null)
            return (GameSettings.Display.window_width / 2f) + alignment[3];
        else
            return (parent.getX() + (parent.getWidth() / 2f)) + alignment[3];
    }

    private float getTopOfParent() {
        if (parent == null)
            return 0 + alignment[1];
        else
            return (parent.getY() - (parent.getHeight() / 2f)) + alignment[1];
    }

    private float getBottomOfParent() {
        if (parent == null)
            return (GameSettings.Display.window_height / 2f) + alignment[2];
        else
            return (parent.getY() + (parent.getHeight() / 2f)) + alignment[2];
    }



    public void display() {
        if (opened)
            return;
        world = WorldFactory.getCurrentWorld();
        world.loadAndAdd(this);
        opened = true;
    }

    public void display(World world) {
        if (opened)
            return;
        this.world = world;
        world.loadAndAdd(this);
        opened = true;
    }

    public void close() {
        if (!opened)
            return;
        if (RenderService.isInRenderThread()) {
            RenderService.INSTANCE.runOnServiceThread(new Runnable() {

                @Override
                public void run() {
                    world.removeDrawable(AbstractUI.this);
                }
            }, true);
        } else {
            world.removeDrawable(this);
        }
        opened = false;
        onClose();
    }

    public boolean isOpened() {
        return opened;
    }

    @Override
    public void init() {
        onOpen();
        if (width <= 0)
            throw new RuntimeException("The width of this UI is less than or equal to 0! The width must be a positive, non-zero number!");
        if (height <= 0)
            throw new RuntimeException("The height of this UI is less than or equal to 0! The height must be a positive, non-zero number!");

    }

    @Override
    public int compareTo(Drawable d) {
        return Drawable.AFTER;
    }

    protected abstract void onOpen();

    protected abstract void onClose();

}
