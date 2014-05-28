package com.dissonance.game.sprites;

import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.framework.render.texture.sprite.SpriteTexture;
import com.dissonance.framework.system.utils.physics.Collidable;
import com.dissonance.framework.system.utils.physics.HitBox;
import com.sun.java.swing.plaf.windows.resources.windows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public abstract class ImagePhysicsSprite extends ImageSprite implements Collidable {
    private HitBox[] hb;
    public ImagePhysicsSprite(String image) {
        super(image);
    }

    public abstract String hitboxConfigPath();

    @Override
    public void onLoad() {
        float width = -1, height = -1;
        if (startW != -1)
            width = startW;
        if (startH != -1)
            height = startH;

        super.onLoad();

        if (width == -1) {
            width = getWidth();
            if (getTexture() != null)
                width = getTexture().getImageWidth();
        }
        if (height == -1) {
            height = getHeight();
            if (getTexture() != null)
                height = getTexture().getImageHeight();
        }

        hb = PhysicsSprite.readHitboxConfig(hitboxConfigPath(), width, height);

        HitBox.registerSprite(this);
    }

    @Override
    public void setWidth(float width) {
        super.setWidth(width);
        if (hitboxConfigPath().equals(""))
            hb[0].setMaxX(width);
    }

    @Override
    public void setHeight(float height) {
        super.setHeight(height);
        if (hitboxConfigPath().equals(""))
            hb[0].setMaxY(height);
    }

    @Override
    public void onUnload() {
        HitBox.unregisterSprite(this);
    }

    @Override
    public HitBox getHitBox() {
        return hb[0];
    }

    @Override
    public boolean isPointInside(float x, float y) {
        if (!visible)
            return false;
        float heightC = getHeight();
        float widthC = getWidth();

        float sX = getX() - (widthC / 2f);
        float sY = getY() - (heightC / 2f);
        for (HitBox hitBox : hb) {
            float minX = sX + hitBox.getMinX(), minY = sY + hitBox.getMinY(), maxX = sX + hitBox.getMaxX(), maxY = sY + hitBox.getMaxY();
            if (x > minX && y > minY && x <= maxX && y <= maxY)
                return true;
        }
        return false;
    }
}
