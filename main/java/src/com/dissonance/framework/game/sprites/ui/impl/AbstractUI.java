package com.dissonance.framework.game.sprites.ui.impl;

import com.dissonance.framework.game.sprites.ui.UI;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.GameSettings;

import javax.swing.plaf.nimbus.AbstractRegionPainter;
import java.util.ArrayList;

public abstract class AbstractUI implements UI {
    private float x, y;
    protected float width, height;
    protected boolean opened;
    protected World world;
    private AbstractUI parent;
    private ArrayList<AbstractUI> children = new ArrayList<>();

    public AbstractUI() {
        this(null);
    }

    public AbstractUI(AbstractUI parent) {
        setParent(parent);
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
    public void setX(float x) {
        float dif = this.x - x;
        this.x = x;
        for (AbstractUI child : children) {
            child.setX(child.x - dif);
        }
    }

    @Override
    public void setY(float y) {
        float dif = this.y - y;
        this.y = y;
        for (AbstractUI child : children) {
            child.setY(child.y - dif);
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
    }

    public void marginTop(float value) {
        setY(getTopOfParent() + value + (height / 2f));
    }

    public void marginBottom(float value) {
        setY((getBottomOfParent() - value) - (height / 2f));
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

    public AbstractUI getParent() {
        return parent;
    }

    public ArrayList<AbstractUI> getChildren() {
        return children;
    }

    private float getLeftOfParent() {
        if (parent == null)
            return 0;
        else
            return parent.getX() - (parent.getWidth() / 2f);
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
            return GameSettings.Display.window_width / 2f;
        else
            return parent.getX() + (parent.getWidth() / 2f);
    }

    private float getTopOfParent() {
        if (parent == null)
            return 0;
        else
            return parent.getY() - (parent.getHeight() / 2f);
    }

    private float getBottomOfParent() {
        if (parent == null)
            return GameSettings.Display.window_height / 2f;
        else
            return parent.getY() + (parent.getHeight() / 2f);
    }



    public void display() {
        if (opened)
            return;
        world = WorldFactory.getCurrentWorld();
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
