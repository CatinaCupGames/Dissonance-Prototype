package com.dissonance.game.sprites;

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
        float sX = 0, sY = 0, bX = width, bY = height;
        ArrayList<HitBox> hitboxes = new ArrayList<HitBox>();
        if (!hitboxConfigPath().equals("")) {
            InputStream fIn = ImagePhysicsSprite.class.getClassLoader().getResourceAsStream(hitboxConfigPath());
            if (fIn != null) {
                BufferedReader br = new BufferedReader(new InputStreamReader(fIn));

                String l;
                try {
                    while ((l = br.readLine()) != null) {
                        int minX, minY, maxX, maxY;
                        String[] str = l.split("\\:");

                        minX = Integer.parseInt(str[0]);
                        minY = Integer.parseInt(str[1]);
                        maxX = Integer.parseInt(str[2]);
                        maxY = Integer.parseInt(str[3]);

                        hitboxes.add(new HitBox(minX, minY, maxX, maxY));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (hitboxes.size() == 0)
            hitboxes.add(new HitBox(sX, sY, bX, bY));

        hb = hitboxes.toArray(new HitBox[hitboxes.size()]);

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

    private float heightC = -1;
    private float widthC = -1;
    @Override
    public boolean isPointInside(float x, float y) {
        if (!visible)
            return false;
        if (heightC == -1 || widthC == -1) {
            if (getTexture() instanceof SpriteTexture) {
                SpriteTexture temp = (SpriteTexture) getTexture();
                heightC = temp.getHeight();
                widthC = temp.getWidth();
            } else {
                heightC = getHeight();
                widthC = getWidth();
            }
        }

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
