package com.dissonance.framework.game.item.impl;

import com.dissonance.framework.game.combat.Weapon;
import com.dissonance.framework.game.item.Item;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.impl.UpdatableSprite;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.framework.system.utils.physics.Collidable;
import com.dissonance.framework.system.utils.physics.HitBox;

import java.util.Iterator;
import java.util.List;

public class WeaponItem extends Item {
    private Weapon weapon;

    public WeaponItem(CombatSprite owner, Weapon w) {
        super(owner);
        this.weapon = w;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    @Override
    public String getItemName() {
        return weapon.getName();
    }

    @Override
    public boolean isStackable() {
        return false;
    }

    @Override
    public void use(Object... parameters) {
        if (weapon.isSpell()) {
            //TODO Code for spells
        } else if (weapon.isGun()) {
            //TODO Code for guns
        } else {
            final Direction facingDirection = getOwner().getDirection();
            if (parameters.length > 0) {
                String type = (String) parameters[0];
                if (type.equals("swipe")) {
                    //TODO Swipe code
                } else {
                    getOwner().setAnimation("sword_stab");
                    if (getOwner() instanceof PlayableSprite)
                        ((PlayableSprite)getOwner()).freeze();

                    HitBox swordHitbox = new HitBox(0, 0, (facingDirection == Direction.DOWN || facingDirection == Direction.UP ? weapon.getSwipeRange() : 3), (facingDirection == Direction.RIGHT || facingDirection == Direction.LEFT ? weapon.getSwipeRange() : 3));
                    swordHitbox.setX(getOwner().getX());
                    swordHitbox.setY(getOwner().getY());
                    final float finalRange = weapon.getRange();
                    final float steps = finalRange / getOwner().getCurrentAnimation().size() - 1;
                    final HitBox finalSwordHitbox = swordHitbox;
                    getOwner().setAnimationFrameListener(new AnimatedSprite.AnimatedSpriteEvent.OnAnimationFrame() {
                        @Override
                        public void onAnimationFrame(AnimatedSprite sprite) {
                            List<Collidable> list = finalSwordHitbox.checkAndRetrieve(sprite.getWorld(), finalSwordHitbox.getX(), finalSwordHitbox.getY(), sprite);
                            System.out.println(list);
                            System.out.println("X: " + finalSwordHitbox.getX() + " Y: " + finalSwordHitbox.getY() + "         " + finalSwordHitbox.getMinX() + " : " + finalSwordHitbox.getMinY() + "       " + finalSwordHitbox.getMaxX() + " : " + finalSwordHitbox.getMaxY());
                            switch (facingDirection) {
                                case UP:
                                    finalSwordHitbox.setMinY(finalSwordHitbox.getMinY() - steps);
                                    break;
                                case DOWN:
                                    finalSwordHitbox.setMaxY(finalSwordHitbox.getMaxY() + steps);
                                    break;
                                case LEFT:
                                    finalSwordHitbox.setMinX(finalSwordHitbox.getMinX() - steps);
                                    break;
                                case RIGHT:
                                    finalSwordHitbox.setMaxX(finalSwordHitbox.getMaxX() + steps);
                                    break;
                            }
                        }
                    });
                    getOwner().setAnimationFinishedListener(new AnimatedSprite.AnimatedSpriteEvent.OnAnimationFinished() {
                        @Override
                        public void onAnimationFinished(AnimatedSprite sprite) {
                            sprite.setAnimationFinishedListener(null);
                            sprite.setAnimationFrameListener(null);
                            if (getOwner() instanceof PlayableSprite) {
                                ((PlayableSprite)getOwner()).unfreeze();
                                ((PlayableSprite)getOwner()).attacking = false;
                            }
                            sprite.setAnimation(0);
                        }
                    });
                    getOwner().playAnimation();
                }
            }
        }

        /*Direction facingDirection = getOwner().getDirection();
        int range = weapon.getRange();
        int swipe = weapon.getSwipeRange();
        double xadd = 0, yadd = 0;
        if (facingDirection == Direction.UP)
            yadd = range;
        else if (facingDirection == Direction.DOWN)
            yadd = -range;
        else if (facingDirection == Direction.LEFT)
            xadd = range;
        else if (facingDirection == Direction.RIGHT)
            xadd = -range;

        double x = getOwner().getX() + xadd;
        double y = getOwner().getX() + yadd;

        double xmin = x - swipe;
        double xmax = x + swipe;
        double ymin = y - swipe;
        double ymax = y + swipe;
        Iterator<UpdatableDrawable> sprites = getOwner().getWorld().getUpdatables();
        while (sprites.hasNext()) {
            UpdatableDrawable ud = sprites.next();
            if (ud != null && ud instanceof CombatSprite) {
                CombatSprite combatSprite = (CombatSprite)ud;
                if (combatSprite.getX() > xmin && combatSprite.getX() < xmax && combatSprite.getY() > ymin && combatSprite.getY() < ymax) { //TODO Check within hitbox, not bounds of sprite
                    combatSprite.strike(getOwner(), this);
                }
            }
        }*/
    }
}
