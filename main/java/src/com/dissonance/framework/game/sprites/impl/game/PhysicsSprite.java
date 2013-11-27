package com.dissonance.framework.game.sprites.impl.game;

import com.dissonance.framework.render.texture.sprite.SpriteTexture;
import com.dissonance.framework.system.utils.physics.Collidable;
import com.dissonance.framework.system.utils.physics.HitBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class PhysicsSprite extends AbstractWaypointSprite implements Collidable {
    private HitBox hb;
    private float heightC = -1;
    private float widthC = -1;

    @Override
    public HitBox getHitBox() {
        return hb;
    }

    @Override
    public boolean isPointInside(float x, float y) {
        if (heightC == -1 || widthC == -1) {
            if (getTexture() instanceof SpriteTexture) {
                SpriteTexture temp = (SpriteTexture)getTexture();
                heightC = temp.getHeight();
                widthC = temp.getWidth();
            } else {
                heightC = getHeight();
                widthC = getWidth();
            }
        }

        float sX = getX();
        float sY = getY() + (heightC / 4.0f);

        sX += hb.getMinX();
        sY += hb.getMinY();
        float maxValue = sX + hb.getMaxX();
        float maxValueY = sY + hb.getMaxY();
        maxValue -= (widthC / 4.0f);
        maxValueY -= (heightC / 2.0f);
        return x > sX && x < maxValue && y > sY && y < maxValueY;
    }

    @Override
    public void setX(float x) {

        float oX = super.getX();
        super.setX(x);

        if (hb != null && hb.checkForCollision(getWorld(), this)) {
            super.setX(oX);
            if (hb.getLastCollide() instanceof PhysicsSprite) {
                float add = getX() - hb.getLastCollide().getX();
                for (int i = 0; i < 1000 && hb.checkForCollision(getWorld(), this); i++) {
                    super.setX(super.getX() + (add < 0 ? -1 : 1));
                }
            }
        }
    }

    @Override
    public void setY(float y) {
        float oY = super.getY();
        super.setY(y);

        if (hb != null && hb.checkForCollision(getWorld(), this)) {
            super.setY(oY);
            if (hb.getLastCollide() instanceof PhysicsSprite) {
                float add = getY() - hb.getLastCollide().getY();
                for (int i = 0; i < 1000 && hb.checkForCollision(getWorld(), this); i++) {
                    super.setY(super.getY() + (add < 0 ? -1 : 1));
                }
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();

        float sX = 0, sY = 0, bX = 16, bY = 32;
        InputStream fIn = PhysicsSprite.class.getClassLoader().getResourceAsStream("sprites/" + getSpriteName() + "/hitbox.txt");
        if (fIn != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(fIn));

            String l;
            try {
                while ((l = br.readLine()) != null) {
                    if (l.split("\\:")[0].equals("minX")) {
                        try {
                            sX = Integer.parseInt(l.split("\\:")[1]);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    } else if (l.split("\\:")[0].equals("minY")) {
                        try {
                            sY = Integer.parseInt(l.split("\\:")[1]);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    } else if (l.split("\\:")[0].equals("maxX")) {
                        try {
                            bX = Integer.parseInt(l.split("\\:")[1]);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    } else if (l.split("\\:")[0].equals("maxY")) {
                        try {
                            bY = Integer.parseInt(l.split("\\:")[1]);
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        hb = new HitBox(sX, sY, bX, bY);

        HitBox.registerSprite(this);
    }

    @Override
    public void onUnload() {
        HitBox.unregisterSprite(this);
    }
}
