package com.dissonance.framework.game.sprites.impl.game;

import com.dissonance.framework.system.utils.HitBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class PhysicsSprite extends AbstractWaypointSprite {
    private HitBox hb;

    public HitBox getHitbox() {
        return hb;
    }

    @Override
    public void setX(float x) {

        float oX = super.getX();
        super.setX(x);

        if (hb != null && hb.checkForCollision(getWorld(), this)) {
            super.setX(oX);
        }
    }

    @Override
    public void setY(float y) {
        float oY = super.getY();
        super.setY(y);

        if (hb != null && hb.checkForCollision(getWorld(), this)) {
            super.setY(oY);
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
    }
}
