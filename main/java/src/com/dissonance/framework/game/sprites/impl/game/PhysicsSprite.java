package com.dissonance.framework.game.sprites.impl.game;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.game.world.tiled.TiledObject;
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

public abstract class PhysicsSprite extends AbstractWaypointSprite implements Collidable {
    private HitBox hb;
    private float heightC = -1;
    private float widthC = -1;
    private boolean moving;

    @Override
    public HitBox getHitBox() {
        return hb;
    }

    @Override
    public boolean isPointInside(float x, float y) {
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

        float minX = sX + hb.getMinX(), minY = sY + hb.getMinY(), maxX = sX + hb.getMaxX(), maxY = sY + hb.getMaxY();
        return x > minX && y > minY && x <= maxX && y <= maxY;
    }

    @Override
    public void setX(float x) {
        float oX = super.getX();
        super.setX(x);

        if (hb != null && hb.checkForCollision(this)) {
            Collidable c = hb.getLastCollide();
            if (c instanceof PhysicsSprite) {
                super.setX(oX);
                float add = getX() - c.getX();
                for (int i = 0; i < 1000 && hb.checkForCollision(this); i++) {
                    super.setX(super.getX() + (add < 0 ? -1 : 1));
                }
            } else if (c instanceof TiledObject) {
                TiledObject to = (TiledObject) c;
                if (to.isHitbox())
                    super.setX(oX);
                else if (to.isDoor() && this instanceof PlayableSprite) { //2meta4me
                    String target = to.getDoorTarget();
                    if (target.equalsIgnoreCase("")) {
                        super.setX(oX);
                        return;
                    }
                    String world = to.getDoorWorldTarget();
                    final World worldObj;
                    if (world.equalsIgnoreCase("")) {
                        worldObj = getWorld();
                    } else {
                        try {
                            worldObj = WorldFactory.getWorld(world);
                        } catch (WorldLoadFailedException e) {
                            e.printStackTrace();
                            super.setX(oX);
                            return;
                        }
                    }

                    final TiledObject spawn = worldObj.getSpawn(target);
                    if (spawn == null) {
                        super.setX(oX);
                        return;
                    }

                    ((PlayableSprite)this).freeze();
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            if (worldObj != getWorld()) {
                                RenderService.INSTANCE.fadeToBlack(1000);
                                WorldFactory.swapView(worldObj, true);
                                setWorld(worldObj);
                            }

                            PhysicsSprite.super.setX(spawn.getX());
                            PhysicsSprite.super.setY(spawn.getY());
                            Camera.setPos(Camera.translateToCameraCenter(getVector(), 32));
                            ((PlayableSprite)PhysicsSprite.this).unfreeze();
                        }
                    }).start();

                }
            }
        }
    }

    @Override
    public void setY(float y) {
        float oY = super.getY();
        super.setY(y);

        if (hb != null && hb.checkForCollision(this)) {
            Collidable c = hb.getLastCollide();
            if (c instanceof PhysicsSprite) {
                super.setY(oY);
                float add = getY() - hb.getLastCollide().getY();
                for (int i = 0; i < 1000 && hb.checkForCollision(this); i++) {
                    super.setY(super.getY() + (add < 0 ? -1 : 1));
                }
            } else if (c instanceof TiledObject) {
                TiledObject to = (TiledObject) c;
                if (to.isHitbox())
                    super.setY(oY);
                else if (to.isDoor() && this instanceof PlayableSprite) { //2meta4me
                    String target = to.getDoorTarget();
                    if (target.equalsIgnoreCase("")) {
                        super.setY(oY);
                        return;
                    }
                    String world = to.getDoorWorldTarget();
                    World worldObj;
                    if (world.equalsIgnoreCase("")) {
                        worldObj = getWorld();
                    } else {
                        try {
                            worldObj = WorldFactory.getWorld(world);
                        } catch (WorldLoadFailedException e) {
                            e.printStackTrace();
                            super.setY(oY);
                            return;
                        }
                    }

                    TiledObject spawn = worldObj.getSpawn(target);
                    if (spawn == null) {
                        super.setY(oY);
                        return;
                    }

                    if (worldObj != getWorld()) {
                        RenderService.INSTANCE.fadeToBlack(300);
                        WorldFactory.swapView(worldObj, true);
                        setWorld(worldObj);
                    }

                    super.setX(spawn.getX());
                    super.setY(spawn.getY());
                }
            }
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();

        float sX = 0, sY = 0, bX = 32, bY = 32;
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
