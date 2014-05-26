package com.dissonance.game.sprites.factory;

import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.sprites.Selectable;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.framework.render.texture.sprite.SpriteTexture;
import com.dissonance.framework.system.utils.physics.Collidable;
import com.dissonance.framework.system.utils.physics.HitBox;
import com.dissonance.game.scenes.ElevatorScene;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ElevatorDoor extends AnimatedSprite implements Selectable, Collidable {
    private HitBox[] hb;
    @Override
    public String getSpriteName() {
        return "elevator_door";
    }

    @Override
    public void onLoad() {
        super.onLoad();
        setX(getX() + 4.5f);
        setY(getY() + 6.5f);

        float width = getWidth(), height = getHeight();
        if (getTexture() != null) {
            width = getTexture().getImageWidth();
            height = getTexture().getImageHeight();
        }
        hb = PhysicsSprite.readHitboxConfig("sprites/" + getSpriteName() + "/hitbox.txt", width, height);

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

    @Override
    public boolean onSelected(PlayableSprite player) {
        double angle = angleTowards(player);
        if (angle > 227.0 && angle < 314.0) {
            player.freeze();
            player.ignoreCollisionWith(this);

            setAnimation("opening");
            reverseAnimation(false);
            playAnimation();

            setAnimationFinishedListener(new AnimatedSpriteEvent.OnAnimationFinished() {
                @Override
                public void onAnimationFinished(AnimatedSprite sprite) {
                    ElevatorScene scene = new ElevatorScene(ElevatorDoor.this);
                    scene.beginScene();
                }
            });
            return true;
        }
        return false;
    }

    @Override
    public double getDistanceRequired() {
        return 68;
    }
}
