package com.dissonance.framework.game.sprites.impl.game;

import com.dissonance.framework.game.combat.Bullet;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.system.utils.physics.Collidable;

import java.math.BigDecimal;
import java.util.List;

public abstract class ExplodableSprite extends PhysicsSprite {

    /**
     * The explosion radius of this sprite. Any sprite
     * within this radius will take damage according to the
     * following formula:
     * {@link #getBaseDamage() baseDamage} * (1 - victimDistance / explosionRadius)
     */
    public abstract float getExplosionRadius();

    /**
     * The base (maximum) damage this sprite can deal to a
     * {@link CombatSprite} when it explodes. The actual damage
     * depends on the distance of the victim and is calculated
     * according to the following formula:
     * baseDamage * (1 - victimDistance / {@link #getExplosionRadius() explosionRadius})
     */
    public abstract float getBaseDamage();

    /**
     * The name of the explosion sprite that will be displayed
     * after this sprite explodes.
     */
    public abstract String getExplosionName();

    private float explosionSize = -1;

    public final void setExplosionSize(float explosionSize) {
        this.explosionSize = explosionSize;
    }

    protected void explode(Collidable hit) {
        if (!(hit instanceof Bullet)) {
            return;
        }

        world.removeSprite((Bullet) hit);

        List<CombatSprite> sprites = world.getAllCombatSprites();
        for (CombatSprite sprite : sprites) {
            float lx = sprite.getX() - getX();
            float ly = sprite.getY() - getY();
            float distance = (float) Math.sqrt(lx * lx + ly * ly);
            if (distance >= getExplosionRadius()) {
                continue;
            }

            double damage = new BigDecimal(getBaseDamage() * (1 - distance / getExplosionRadius())).setScale(2, 4).doubleValue();

            if (damage > 0) {
                sprite.applyDamage(damage);
            }
        }

        getWorld().removeSprite(this);

        ExplosionSprite sprite = new ExplosionSprite(getExplosionName(), getX(), getY());
        world.loadAndAdd(sprite);
    }

    @Override
    public void update() {
        super.update();

        if (getHitBox() != null && getHitBox().checkForCollision(this)) {
            explode(getHitBox().getLastCollide());
        }
    }

    private final class ExplosionSprite extends AnimatedSprite {
        private String spriteName;

        private ExplosionSprite(String spriteName, float x, float y) {
            this.spriteName = spriteName;
            setX(x);
            setY(y);

            setAnimationFinishedListener(new AnimatedSpriteEvent.OnAnimationFinished() {
                @Override
                public void onAnimationFinished(AnimatedSprite sprite) {
                    world.removeSprite(ExplosionSprite.this);
                }
            });
        }

        @Override
        public String getSpriteName() {
            return spriteName;
        }

        @Override
        public void onLoad() {
            super.onLoad();
            setLayer(2);

            if (ExplodableSprite.this.explosionSize > 0) {
                setWidth(ExplodableSprite.this.explosionSize);
                setHeight(ExplodableSprite.this.explosionSize);
            }
        }
    }
}
