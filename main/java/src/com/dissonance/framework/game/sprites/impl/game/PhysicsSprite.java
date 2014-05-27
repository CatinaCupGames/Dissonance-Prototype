package com.dissonance.framework.game.sprites.impl.game;

import com.dissonance.framework.game.ai.astar.FastMath;
import com.dissonance.framework.game.combat.Bullet;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.world.Tile;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.LayerType;
import com.dissonance.framework.game.world.tiled.TiledObject;
import com.dissonance.framework.game.world.tiled.impl.AbstractTrigger;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.sprite.SpriteTexture;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;
import com.dissonance.framework.system.utils.physics.Collidable;
import com.dissonance.framework.system.utils.physics.HitBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public abstract class PhysicsSprite extends AbstractWaypointSprite implements Collidable {
    private HitBox[] hb;
    private float heightC = -1;
    private float widthC = -1;
    private boolean moving;
    private List<Collidable> ignore = new ArrayList<>();
    private boolean physics = true;

    @Override
    public HitBox getHitBox() {
        return hb[0];
    }

    public void ignoreCollisionWith(Collidable sprite) {
        if (!ignore.contains(sprite))
            ignore.add(sprite);
    }

    public void ignoreCollisionWith(Collidable... sprites) {
        for (Collidable s : sprites) {
            if (!ignore.contains(s))
                ignore.add(s);
        }
    }

    public void clearPhysicsIgnores() {
        ignore.clear();
    }

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
    public void setX(float x) {
        float oX = super.getX();
        super.setX(x);

        if (!physics)
            return;
        if (hb == null)
            return;
        for (HitBox hitBox : hb) {
            if (hitBox.checkForCollision(this, ignore)) {
                onCollideX(oX, x, hitBox.getLastCollide(), hitBox);
            }
        }
    }

    @Override
    public void setY(float y) {
        float oY = super.getY();
        super.setY(y);

        if (!physics)
            return;
        if (hb == null)
            return;
        for (HitBox hitBox : hb) {
            if (hitBox.checkForCollision(this, ignore)) {
                onCollideY(oY, y, hitBox.getLastCollide(), hitBox);
            }
        }
    }

    @Override
    public void rawSetX(float x) {
        float oX = super.getX();
        super.rawSetX(x);

        if (!physics)
            return;
        if (hb == null)
            return;
        for (HitBox hitBox : hb) {
            if (hitBox.checkForCollision(this, ignore)) {
                onCollideX(oX, x, hitBox.getLastCollide(), hitBox);
            }
        }
    }

    @Override
    public void rawSetY(float y) {
        float oY = super.getY();
        super.rawSetY(y);

        if (!physics)
            return;
        if (hb == null)
            return;
        for (HitBox hitBox : hb) {
            if (hitBox.checkForCollision(this, ignore)) {
                onCollideY(oY, y, hitBox.getLastCollide(), hitBox);
            }
        }
    }

    protected void onCollideX(float oldX, float newX, Collidable hit, HitBox hb) {
        Collidable c = hb.getLastCollide();

        if (c instanceof Bullet) {
            //In Soviet Russia, bullets collide with YOU!
            return;
        }

        if (c instanceof PhysicsSprite) {
            if (ignore.contains(c))
                return;
            super.rawSetX(oldX);
            float add = getX() - c.getX();
            for (int i = 0; i < 1000 && hb.checkForCollision(this, ignore); i++) {
                super.rawSetX(super.getX() + (add < 0 ? -1 : 1));
            }
        } else if (c instanceof Tile) {
            super.rawSetX(oldX);
            float add = getX() - c.getX();
            for (int i = 0; i < 1000 && hb.checkForCollision(this, ignore); i++) {
                super.rawSetX(super.getX() + (add < 0 ? -1 : 1));
            }
        } else if (c instanceof TiledObject) {
            TiledObject to = (TiledObject) c;
            if (to.isTrigger()) {
                AbstractTrigger abstractTrigger = to.getTrigger();
                abstractTrigger.onCollide(this);
            } else if (to.isHitbox())
                super.rawSetX(oldX);
        } else {
            if (ignore.contains(c))
                return;
            super.rawSetX(oldX);
        }
    }

    protected void onCollideY(float oldY, float newY, Collidable hit, HitBox hb) {
        Collidable c = hb.getLastCollide();

        if (c instanceof Bullet) {
            //In Soviet Russia, bullets collide with YOU!
            return;
        }

        if (c instanceof PhysicsSprite) {
            if (ignore.contains(c))
                return;
            super.rawSetY(oldY);
            float add = getY() - hb.getLastCollide().getY();
            for (int i = 0; i < 1000 && hb.checkForCollision(this, ignore); i++) {
                super.rawSetY(super.getY() + (add < 0 ? -1 : 1));
            }
        } else if (c instanceof Tile) {
            super.rawSetY(oldY);
            float add = getY() - c.getY();
            for (int i = 0; i < 1000 && hb.checkForCollision(this, ignore); i++) {
                super.rawSetY(super.getY() + (add < 0 ? -1 : 1));
            }
        } else if (c instanceof TiledObject) {
            TiledObject to = (TiledObject) c;
            if (to.isTrigger()) {
                AbstractTrigger abstractTrigger = to.getTrigger();
                abstractTrigger.onCollide(this);
            } else if (to.isHitbox())
                super.rawSetY(oldY);
        } else {
            if (ignore.contains(c))
                return;
            super.rawSetY(oldY);
        }
    }

    @Override
    public boolean setAnimation(int index) {
        boolean value = super.setAnimation(index);
        if (value) {
            InputStream fIn = PhysicsSprite.class.getClassLoader().getResourceAsStream("sprites/" + getSpriteName() + "/hitbox-" + getCurrentAnimation().getName() + ".txt");
            if (fIn != null) {
                float width = 32, height = 32;
                if (getTexture() != null) {
                    width = (int) getTexture().getWidth();
                    height = (int) getTexture().getHeight();
                }

                hb = readHitboxConfig(fIn, width, height);
            }
        }
        return value;
    }

    @Override
    public boolean setAnimation(String name) {
        boolean value = super.setAnimation(name);
        if (value) {
            InputStream fIn = PhysicsSprite.class.getClassLoader().getResourceAsStream("sprites/" + getSpriteName() + "/hitbox-" + getCurrentAnimation().getName() + ".txt");
            if (fIn != null) {
                float width = 32, height = 32;
                if (getTexture() != null) {
                    width = (int) getTexture().getWidth();
                    height = (int) getTexture().getHeight();
                }
                hb = readHitboxConfig(fIn, width, height);
            }
        }
        return value;
    }

    @Override
    public void update() {
        super.update();
        if (isUpdateCanceled())
            return;
        float x = getX() + 8.5f;
        float y = getY() + (getHeight() / 2f) - 6f;
        Layer[] layers = getWorld().getLayers(LayerType.TILE_LAYER);
        for (Layer layer : layers) {
            Tile tile = getWorld().getTileAt(x / 16f, FastMath.fastCeil((y - 8f) / 16f), layer);
            if (tile != null && tile.isTriggerTile())
                tile.getTrigger().onCollide(this, tile);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();

        float width = 32, height = 32;
        if (getTexture() != null) {
            width = getTexture().getWidth();
            height = getTexture().getHeight();
        }
        hb = readHitboxConfig("sprites/" + getSpriteName() + "/hitbox.txt", width, height);

        HitBox.registerSprite(this);
    }

    @Override
    public void onUnload() {
        HitBox.unregisterSprite(this);
    }

    public HitBox[] getHitBoxes() {
        return hb;
    }

    public static HitBox[] readHitboxConfig(String path, float width, float height) {
        float sX = 0, sY = 0;
        if (path != null && !path.equals("")) {
            InputStream fIn = PhysicsSprite.class.getClassLoader().getResourceAsStream(path);
            if (fIn != null) {
                return readHitboxConfig(fIn, width, height);
            }
        }
        return new HitBox[] { new HitBox(sX, sY, width, height) };
    }

    public static HitBox[] readHitboxConfig(InputStream fIn, float width, float height) {
        float sX = 0, sY = 0;
        ArrayList<HitBox> hitboxes = new ArrayList<HitBox>();
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
        if (hitboxes.size() == 0)
            hitboxes.add(new HitBox(sX, sY, width, height));

        return hitboxes.toArray(new HitBox[hitboxes.size()]);
    }

    public void setUsePhysics(boolean value) {
        this.physics = value;
    }

    public boolean isUsingPhysics() {
        return physics;
    }
}
