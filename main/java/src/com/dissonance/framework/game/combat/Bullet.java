package com.dissonance.framework.game.combat;

import com.dissonance.framework.game.item.impl.WeaponItem;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.world.tiled.TiledObject;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.sound.Sound;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.framework.system.utils.physics.Collidable;
import com.dissonance.framework.system.utils.physics.HitBox;

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
    private float xAdd, yAdd;
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

        switch (this.direction) {
            case UP:
                yAdd = -(weapon.getWeaponInfo().getBulletSpeed());
                xAdd = angle;
                break;
            case DOWN:
                yAdd = (weapon.getWeaponInfo().getBulletSpeed());
                xAdd = angle;
                break;
            case LEFT:
                xAdd = -(weapon.getWeaponInfo().getBulletSpeed());
                yAdd = angle;
                break;
            case RIGHT:
                xAdd = (weapon.getWeaponInfo().getBulletSpeed());
                yAdd = angle;
                break;
            case UP_LEFT:
                yAdd = -(weapon.getWeaponInfo().getBulletSpeed());
                xAdd = -(weapon.getWeaponInfo().getBulletSpeed());
                yAdd /= 1.5f;
                xAdd /= 1.5f;
                yAdd += angle;
                xAdd += angle;
                break;
            case UP_RIGHT:
                yAdd = -(weapon.getWeaponInfo().getBulletSpeed());
                xAdd = (weapon.getWeaponInfo().getBulletSpeed());
                yAdd /= 1.5f;
                xAdd /= 1.5f;
                yAdd += angle;
                xAdd += angle;
                break;
            case DOWN_LEFT:
                yAdd = (weapon.getWeaponInfo().getBulletSpeed());
                xAdd = -(weapon.getWeaponInfo().getBulletSpeed());
                yAdd /= 1.5f;
                xAdd /= 1.5f;
                yAdd += angle;
                xAdd += angle;
                break;
            case DOWN_RIGHT:
                yAdd = (weapon.getWeaponInfo().getBulletSpeed());
                xAdd = (weapon.getWeaponInfo().getBulletSpeed());
                yAdd /= 1.5f;
                xAdd /= 1.5f;
                yAdd += angle;
                xAdd += angle;
                break;
            default:
                yAdd = -(weapon.getWeaponInfo().getBulletSpeed() * RenderService.TIME_DELTA);
                xAdd = angle;
                break;
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        Direction direction = this.direction.simple();
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
    protected void onCollideX(float oldX, float newX, Collidable hit, HitBox hb) {
        if (hit instanceof CombatSprite) {
            collide((CombatSprite) hit);
        } else if (hit instanceof TiledObject) {
            if (((TiledObject) hit).isSpawn() || ((TiledObject) hit).isTrigger()) return;
            //TODO: play wall hit sound
            explode();
        }
    }

    @Override
    protected void onCollideY(float oldY, float newY, Collidable hit, HitBox hb) {
        if (hit instanceof CombatSprite) {
            if (!(hit instanceof PlayableSprite)) {
                collide((CombatSprite) hit);
            }
        } else if (hit instanceof TiledObject) {
            if (((TiledObject) hit).isSpawn()) return;
            //TODO: play wall hit sound
            explode();
        }
    }

    @Override
    public void update() {
        super.update();
        if (exploded)
            return;

        float tX = xAdd;
        float tY = yAdd;
        if (tX != angle) tX *= RenderService.TIME_DELTA;
        if (tY != angle) tY *= RenderService.TIME_DELTA;

        setY(getY() + tY);
        setX(getX() + tX);

        if (direction == Direction.UP) {
            if (startY - getY() >= range) {
                explode();
            }
        } else if (direction == Direction.DOWN) {
            if (getY() - startY >= range) {
                explode();
            }
        } else if (direction == Direction.RIGHT) {
            if (getX() - startX >= range) {
                explode();
            }
        } else if (direction == Direction.LEFT) {
            if (startX - getX() >= range) {
                explode();
            }
        } else if (direction == Direction.UP_LEFT) {
            if (startX - getX() >= range || startY - getY() >= range) {
                explode();
            }
        } else if (direction == Direction.UP_RIGHT) {
            if (getX() - startX >= range || startY - getY() >= range) {
                explode();
            }
        } else if (direction == Direction.DOWN_LEFT) {
            if (startX - getX() >= range || getY() - startY >= range) {
                explode();
            }
        } else if (direction == Direction.DOWN_RIGHT) {
            if (getX() - startX >= range || getY() - startY >= range) {
                explode();
            }
        }
    }
}
