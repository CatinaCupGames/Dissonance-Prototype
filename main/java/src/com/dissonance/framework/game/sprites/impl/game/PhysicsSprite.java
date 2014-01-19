package com.dissonance.framework.game.sprites.impl.game;

import com.dissonance.framework.game.combat.Bullet;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
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
            onCollideX(oX, x, hb.getLastCollide());
        }
    }

    @Override
    public void setY(float y) {
        float oY = super.getY();
        super.setY(y);

        if (hb != null && hb.checkForCollision(this)) {
            onCollideY(oY, y, hb.getLastCollide());
        }
    }

    protected void onCollideX(float oldX, float newX, Collidable hit) {
        Collidable c = hb.getLastCollide();

        if (c instanceof Bullet) {
            //In Soviet Russia, bullets collide with YOU!
            return;
        }

        if (c instanceof PhysicsSprite) {
            super.setX(oldX);
            float add = getX() - c.getX();
            for (int i = 0; i < 1000 && hb.checkForCollision(this); i++) {
                super.setX(super.getX() + (add < 0 ? -1 : 1));
            }
        } else if (c instanceof TiledObject) {
            TiledObject to = (TiledObject) c;
            if (to.isHitbox())
                super.setX(oldX);
            else if (to.isTrigger() && this instanceof PlayableSprite && ((PlayableSprite)this).isSelected()) { //2meta4me
                AbstractTrigger abstractTrigger = to.getTrigger();
                abstractTrigger.onCollide((PlayableSprite)this);
            }
            else if (to.isDoor() && this instanceof PlayableSprite && ((PlayableSprite)this).isSelected()) { //3meta5me
                String target = to.getDoorTarget();
                if (target.equalsIgnoreCase("")) {
                    super.setX(oldX);
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
                        super.setX(oldX);
                        return;
                    }
                }

                final TiledObject spawn = worldObj.getSpawn(target);
                if (spawn == null) {
                    super.setX(oldX);
                    return;
                }

                ((PlayableSprite) this).freeze();
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
                        while (RenderService.getCurrentAlphaValue() != 1) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                        ((PlayableSprite) PhysicsSprite.this).unfreeze();
                    }
                }).start();

            }
        }
    }

    protected void onCollideY(float oldY, float newY, Collidable hit) {
        Collidable c = hb.getLastCollide();

        if (c instanceof Bullet) {
            //In Soviet Russia, bullets collide with YOU!
            return;
        }

        if (c instanceof PhysicsSprite) {
            super.setY(oldY);
            float add = getY() - hb.getLastCollide().getY();
            for (int i = 0; i < 1000 && hb.checkForCollision(this); i++) {
                super.setY(super.getY() + (add < 0 ? -1 : 1));
            }
        } else if (c instanceof TiledObject) {
            TiledObject to = (TiledObject) c;
            if (to.isHitbox())
                super.setY(oldY);
            else if (to.isTrigger() && this instanceof PlayableSprite && ((PlayableSprite)this).isSelected()) { //2meta4me
                AbstractTrigger abstractTrigger = to.getTrigger();
                abstractTrigger.onCollide((PlayableSprite)this);
            }
            else if (to.isDoor() && this instanceof PlayableSprite && ((PlayableSprite)this).isSelected()) { //3meta5me
                String target = to.getDoorTarget();
                if (target.equalsIgnoreCase("")) {
                    super.setY(oldY);
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
                        super.setY(oldY);
                        return;
                    }
                }

                final TiledObject spawn = worldObj.getSpawn(target);
                if (spawn == null) {
                    super.setY(oldY);
                    return;
                }

                ((PlayableSprite) this).freeze();
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
                        Camera.setPos(Camera.translateToCameraCenter(getVector(), getHeight()));
                        while (RenderService.getCurrentAlphaValue() != 1) {
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                break;
                            }
                        }
                        ((PlayableSprite) PhysicsSprite.this).unfreeze();
                    }
                }).start();
            }
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
                float sX = 0, sY = 0, bX = width , bY = height;
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
                    hb = new HitBox(sX, sY, bX, bY);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
                float sX = 0, sY = 0, bX = width , bY = height;
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
                    hb = new HitBox(sX, sY, bX, bY);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    @Override
    public void onLoad() {
        super.onLoad();
        float width = 32, height = 32;
        if (getTexture() != null) {
            width = getTexture().getWidth();
            height = getTexture().getHeight();
        }

        float sX = 0, sY = 0, bX = width, bY = height;
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
