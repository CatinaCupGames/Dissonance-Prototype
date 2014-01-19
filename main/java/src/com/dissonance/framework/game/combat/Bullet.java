package com.dissonance.framework.game.combat;

import com.dissonance.framework.game.item.impl.WeaponItem;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;
import com.dissonance.framework.game.world.tiled.TiledObject;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.TextureLoader;
import com.dissonance.framework.sound.Sound;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.framework.system.utils.physics.Collidable;

import java.util.Random;

public class Bullet extends PhysicsSprite {

    private final static Random random = new Random();

    private final static double damageModifier = Math.PI;
    private final static float rangeModifier = (float) Math.E;

    private WeaponItem weapon;
    private CombatSprite owner;

    private Direction direction;
    private float startX;
    private float startY;
    private float angle;
    private float range;
    private double damage;
    private boolean exploded;

    public Bullet(WeaponItem weapon) {
        this.weapon = weapon;
        this.owner = weapon.getOwner();

        exploded = false;

        setWorld(owner.getWorld());
    }

    public void fire(Direction direction) {
        getWorld().loadAndAdd(this);

        setX(owner.getX());
        setY(owner.getY());

        startX = getX();
        startY = getY();

        this.direction = direction;

        angle = 3 * random.nextFloat() / weapon.getWeaponInfo().getAccuracy() / owner.getMarksmanship();
        damage = damageModifier * owner.getMarksmanship() * weapon.getWeaponInfo().getBulletDamage();
        range = weapon.getWeaponInfo().getRange() * rangeModifier;

        if (random.nextBoolean()) {
            angle = -angle;
        }

        Sound.playSound("shotproto");
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (direction == Direction.UP) {
            setAnimation("shoot_top");
        } else if (direction == Direction.DOWN) {
            setAnimation("shoot_bottom");
        } else if (direction == Direction.RIGHT) {
            setAnimation("shoot_right");
        } else if (direction == Direction.LEFT) {
            setAnimation("shoot_left");
        }
    }

    @Override
    public String getSpriteName() {
        return weapon.getWeaponInfo().getBulletName();
    }

    public void collide(CombatSprite target) {
        if (target == owner) {
            return;
        }

        explode();

        target.applyDamage(damage);

        //TODO: animate victim
    }

    private void explode() {
        if (exploded) {
            return;
        }

        exploded = true;

        setAnimation("explode");
        setAnimationFinishedListener(new AnimatedSpriteEvent.OnAnimationFinished() {
            @Override
            public void onAnimationFinished(AnimatedSprite sprite) {
                owner.getWorld().removeSprite(sprite);
                setAnimationFinishedListener(null);
            }
        });
    }

    @Override
    protected void onCollideX(float oldX, float newX, Collidable hit) {
        if (hit instanceof CombatSprite) {
            collide((CombatSprite) hit);
        } else if (hit instanceof TiledObject) {
            if (((TiledObject)hit).isSpawn()) return;
            //TODO: play wall hit sound
            explode();
        }
    }

    @Override
    protected void onCollideY(float oldY, float newY, Collidable hit) {
        if (hit instanceof CombatSprite) {
            if (!(hit instanceof PlayableSprite)) {
                collide((CombatSprite) hit);
            }
        } else if (hit instanceof TiledObject) {
            if (((TiledObject)hit).isSpawn()) return;
            //TODO: play wall hit sound
            explode();
        }
    }

    @Override
    public void update() {
        super.update();
        if (exploded)
            return;

        if (direction == Direction.UP) {
            setY(getY() - (weapon.getWeaponInfo().getBulletSpeed() * RenderService.TIME_DELTA));

            setX(getX() + angle);

            if (startY - getY() >= range) {
                explode();
            }
        } else if (direction == Direction.DOWN) {
            setY(getY() + (weapon.getWeaponInfo().getBulletSpeed() * RenderService.TIME_DELTA));

            setX(getX() + angle);

            if (getY() - startY >= range) {
                explode();
            }
        } else if (direction == Direction.RIGHT) {
            setX(getX() + (weapon.getWeaponInfo().getBulletSpeed() * RenderService.TIME_DELTA));

            setY(getY() + angle);

            if (getX() - startX >= range) {
                explode();
            }
        } else if (direction == Direction.LEFT) {
            setX(getX() - (weapon.getWeaponInfo().getBulletSpeed() * RenderService.TIME_DELTA));

            setY(getY() + angle);

            if (startX - getX() >= range) {
                explode();
            }
        }
    }
}
