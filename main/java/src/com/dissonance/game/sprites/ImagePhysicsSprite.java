package com.dissonance.game.sprites;

import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.framework.render.texture.sprite.SpriteTexture;
import com.dissonance.framework.system.utils.physics.Collidable;
import com.dissonance.framework.system.utils.physics.HitBox;

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
        super.onLoad();

        float width = getWidth(), height = getHeight();
        if (getTexture() != null) {
            width = getTexture().getImageWidth();
            height = getTexture().getImageHeight();
        }
        hb = PhysicsSprite.readHitboxConfig(hitboxConfigPath(), width, height);

        HitBox.registerSprite(this);
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
