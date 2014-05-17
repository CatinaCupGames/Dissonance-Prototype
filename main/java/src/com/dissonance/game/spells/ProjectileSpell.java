package com.dissonance.game.spells;

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
import java.util.List;

public abstract class ProjectileSpell extends AnimatedSprite implements Spell {
    private HitBox hb;

    protected CombatSprite owner;
    protected float speed = 7;
    private float ox, oy;

    public ProjectileSpell(CombatSprite owner) {
        this.owner = owner;
    }

    @Override
    public void castSpell() {
        owner.getWorld().addSprite(this);
        setX(owner.getX());
        setY(owner.getY());
        setFacingDirection(owner.getFacingDirection());
        if (owner.getFacingDirection() == Direction.DOWN)
            setY(getY() + 32);
        else if (owner.getFacingDirection() == Direction.LEFT)
            setX(getX() - 32);
        else if (owner.getFacingDirection() == Direction.RIGHT)
            setX(getX() + 32);
        else if (owner.getFacingDirection() == Direction.UP)
            setY(getY() - 32);
        ox = getX();
        oy = getY();
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

        float sX = 0, sY = 0, bX = 16, bY = 32;
        InputStream fIn = ProjectileSpell.class.getClassLoader().getResourceAsStream("sprites/" + getSpriteName() + "/hitbox.txt");
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

    @Override
    public CombatSprite getOwner() {
        return owner;
    }

    protected void moveOneFrame() {
        if (direction == Direction.NONE)
            return;
        double xadd = 0, yadd = 0;
        if (direction == Direction.DOWN)
            yadd = -1;
        else if (direction == Direction.UP)
            yadd = 1;
        else if (direction == Direction.LEFT)
            xadd = 1;
        else if (direction == Direction.RIGHT)
            xadd = -1;


        setX((float) ((getX() + (speed * xadd)) * RenderService.TIME_DELTA));
        setY((float) ((getY() + (speed * yadd)) * RenderService.TIME_DELTA));

        List<Collidable> collisions = hb.checkAndRetrieve(getWorld(), getX() - (getTexture().getWidth() / 2), getY() - (getTexture().getHeight() / 2), owner);
        if (collisions.size() > 0) {
            for (Collidable c : collisions) {
                if (c instanceof PhysicsSprite) {
                    if (c instanceof CombatSprite) {
                        CombatSprite combat = (CombatSprite)c;

                        double defense = combat.getWillPower();
                        double attack = owner.getFocus();
                        double damage;
                        damage = ((attack * Math.log(attack)) / (defense / Math.log(defense))) * 2;
                        if (damage > 100)
                            damage = 100;
                        combat.applyDamage(damage);

                        combat.applyStatusCondition(getEffect());
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

        if (Math.sqrt( ((getX() - ox) * (getX() - ox)) + ((getY() - oy) * (getY() - oy)) ) > getRange()) {
            destory();
            setUpdateCanceled(true);
        }
    }

    protected void destory() {
        getWorld().removeSprite(this);
        //TODO Do other things
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
