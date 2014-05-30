package com.dissonance.game.spells;

import com.dissonance.framework.game.ai.astar.FastMath;
import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.combat.spells.Spell;
import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.framework.system.utils.physics.Collidable;
import com.dissonance.framework.system.utils.physics.HitBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class ProjectileSpell extends AnimatedSprite implements Spell {
    private HitBox[] hb;

    protected CombatSprite owner;
    protected float speed = 7;
    private float ox, oy;

    public ProjectileSpell(CombatSprite owner) {
        this.owner = owner;
    }

    @Override
    public void onMovement(Direction direction) {
        playAnimation();
        switch (direction) {
            case UP:
            case UP_LEFT:
            case UP_RIGHT:
                setAnimation("up");
                break;
            case DOWN:
            case DOWN_LEFT:
            case DOWN_RIGHT:
                setAnimation("down");
                break;
            case LEFT:
                setAnimation("left");
                break;
            case RIGHT:
                setAnimation("right");
                break;
        }
    }

    @Override
    public void onNoMovement() {
    }

    @Override
    public void castSpell() {
        try {
            ProjectileSpell spell = getClass().getConstructor(CombatSprite.class).newInstance(owner);

            spell.setX(owner.getX());
            spell.setY(owner.getY());
            spell.face(owner.getFacingDirection());
            if (owner.getFacingDirection() == Direction.DOWN)
                spell.setY(spell.getY() + (owner.getHeight() / 2f));
            else if (owner.getFacingDirection() == Direction.LEFT)
                spell.setX(spell.getX() - (owner.getWidth() / 2f));
            else if (owner.getFacingDirection() == Direction.RIGHT)
                spell.setX(spell.getX() + (owner.getWidth() / 2f));
            else if (owner.getFacingDirection() == Direction.UP)
                spell.setY(spell.getY() - (owner.getHeight() / 2f));
            spell.ox = spell.getX();
            spell.oy = spell.getY();

            spell.setLayer(owner.getLayer());

            owner.getWorld().loadAndAdd(spell);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        super.update();
        if (isUpdateCanceled())
            return;
        moveOneFrame();
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
        ArrayList<HitBox> hitboxes = new ArrayList<HitBox>();
        InputStream fIn = PhysicsSprite.class.getClassLoader().getResourceAsStream("sprites/" + getSpriteName() + "/hitbox.txt");
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
        if (hitboxes.size() == 0)
            hitboxes.add(new HitBox(sX, sY, bX, bY));

        hb = hitboxes.toArray(new HitBox[hitboxes.size()]);
    }

    @Override
    public CombatSprite getOwner() {
        return owner;
    }

    @Override
    public void setX(float x) {
        if (x <= 0)
            destory();
        else
            super.setX(x);
    }

    @Override
    public void setY(float y) {
        if (y <= 0)
            destory();
        else
            super.setY(y);
    }

    protected void moveOneFrame() {
        if (direction == Direction.NONE)
            return;
        float xadd = 0, yadd = 0;
        switch (direction) {
            case DOWN:
                yadd = 1;
                break;
            case UP:
                yadd = -1;
                break;
            case LEFT:
                xadd = -1;
                break;
            case RIGHT:
                xadd = 1;
                break;
        }

        if (xadd != 0)
            setX(getX() + ((getSpeed() * xadd) * RenderService.TIME_DELTA));

        if (yadd != 0)
            setY(getY() + ((getSpeed() * yadd) * RenderService.TIME_DELTA));

        for (HitBox hb : this.hb) {
            List<Collidable> collisions = hb.checkAndRetrieve(getWorld(), getX() - (getTexture().getWidth() / 2), getY() - (getTexture().getHeight() / 2), getLayer(), owner);
            if (collisions.size() > 0) {
                for (Collidable c : collisions) {
                    if (c instanceof PhysicsSprite) {
                        if (c instanceof CombatSprite) {
                            CombatSprite combat = (CombatSprite)c;

                            double defense = combat.getDefense();
                            double attack = owner.getFocus();
                            double damage;
                            damage = ((attack * Math.log(attack)) / (defense / Math.log(defense))) * 2;
                            if (damage > 100)
                                damage = 100;

                            damage = FastMath.fastRound((float) damage);

                            combat.applyDamage(damage);

                            if (getEffect() != null) combat.applyStatusCondition(getEffect());
                            onContact(combat);
                            destory();
                            setUpdateCanceled(true);
                            break;
                        }
                    } else { //It hit a wall, destory it
                        destory();
                        setUpdateCanceled(true);
                        break;
                    }
                }
            }
        }

        if (distanceFrom(new Vector(ox, oy)) > getRange()) {
            destory();
            setUpdateCanceled(true);
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
                float sX = 0, sY = 0, bX = width, bY = height;
                BufferedReader br = new BufferedReader(new InputStreamReader(fIn));

                String l;
                ArrayList<HitBox> hitboxes = new ArrayList<HitBox>();
                try {
                    while ((l = br.readLine()) != null) {
                        int minX, minY, maxX, maxY;
                        String[] str = l.split("\\:");

                        minX = Integer.parseInt(str[0]);
                        minY = Integer.parseInt(str[1]);
                        maxX = Integer.parseInt(str[2]);
                        maxY = Integer.parseInt(str[3]);

                        hitboxes.add(new HitBox(minX, minY, maxX, maxY));
                        /*if (l.split("\\:")[0].equals("minX")) {
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
                        }*/
                    }
                    hb = hitboxes.toArray(new HitBox[hitboxes.size()]);
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
                float sX = 0, sY = 0, bX = width, bY = height;
                BufferedReader br = new BufferedReader(new InputStreamReader(fIn));

                String l;
                ArrayList<HitBox> hitboxes = new ArrayList<HitBox>();
                try {
                    while ((l = br.readLine()) != null) {
                        int minX, minY, maxX, maxY;
                        String[] str = l.split("\\:");

                        minX = Integer.parseInt(str[0]);
                        minY = Integer.parseInt(str[1]);
                        maxX = Integer.parseInt(str[2]);
                        maxY = Integer.parseInt(str[3]);

                        hitboxes.add(new HitBox(minX, minY, maxX, maxY));
                        /*if (l.split("\\:")[0].equals("minX")) {
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
                        }*/
                    }
                    hb = hitboxes.toArray(new HitBox[hitboxes.size()]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    protected boolean destoried;
    protected void destory() {
        destoried = true;
        getWorld().removeSprite(this);
    }

    protected void setSpeed(float speed) {
        this.speed = speed;
    }

    protected float getSpeed() {
        return speed;
    }

    public abstract int getRange();

    public abstract StatusEffect getEffect();

    public abstract void onContact(CombatSprite contact);
}
