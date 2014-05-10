package com.dissonance.framework.game.item.impl;

import com.dissonance.framework.game.combat.Bullet;
import com.dissonance.framework.game.combat.Weapon;
import com.dissonance.framework.game.item.Item;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;
import com.dissonance.framework.game.world.tiled.TiledObject;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.render.texture.sprite.SpriteTexture;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.framework.system.utils.physics.Collidable;
import com.dissonance.framework.system.utils.physics.HitBox;

import java.util.ArrayList;
import java.util.List;

public class WeaponItem extends Item {
    private Weapon weapon;

    private long lastShot;

    public WeaponItem(CombatSprite owner, Weapon w) {
        super(owner);

        this.weapon = w;
    }

    public Weapon getWeaponInfo() {
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
        if (weapon.isGun()) {
            //TODO animate shooter

            long time = System.currentTimeMillis();

            if (lastShot + weapon.getFiringSpeed() >= time) {
                return;
            }

            lastShot = time;

            Direction direction = getOwner().getDirection();

            new Bullet(this).fire(direction);
        } else {
            final Direction facingDirection = getOwner().getDirection();
            if (parameters.length > 0) {
                String type = (String) parameters[0];
                if (type.equals("swipe")) {

                    /**
                     * =========================================================
                     * This chunk of code is the sword swiping detection code
                     * =========================================================
                     */

                    if (getOwner() instanceof PlayableSprite) {
                        ((PlayableSprite)getOwner()).freeze();
                    }

                    switch (getOwner().getDirection()) {
                        case UP:
                            getOwner().setAnimation("swipe_up");
                            break;
                        case LEFT:
                            getOwner().setAnimation("swipe_left");
                            break;
                        case RIGHT:
                            getOwner().setAnimation("swipe_right");
                            break;
                        case DOWN:
                            getOwner().setAnimation("swipe_down");
                            break;
                    }
                    float height;
                    float width;
                    Texture texture = getOwner().getTexture();
                    if (texture instanceof SpriteTexture) {
                        height = texture.getHeight();
                        width = texture.getWidth();
                    } else {
                        height = getOwner().getHeight();
                        width = getOwner().getWidth();
                    }
                    final float minX, minY, maxX, maxY, x, y, xadd, yadd;
                    float range = weapon.getRange();
                    final float swipe = weapon.getSwipeRange();
                    switch (facingDirection) {
                        case UP:
                            minY = -range;
                            maxY = 0;
                            minX = 0;
                            maxX = swipe;
                            y = getOwner().getY() - (height / 2f);
                            x = getOwner().getX() - (width / 2f);
                            xadd = width / (float) (getOwner().getFrameCount() - 1);
                            yadd = 0;
                            break;
                        case DOWN:
                            minY = 0;
                            maxY = range;
                            minX = 0;
                            maxX = swipe;
                            y = getOwner().getY() + (height / 2);
                            x = getOwner().getX() - (width / 2);
                            xadd = width / (float) (getOwner().getFrameCount() - 1);
                            yadd = 0;
                            break;
                        case LEFT:
                            minY = 0;
                            maxY = swipe;
                            minX = -range;
                            maxX = 0;
                            y = getOwner().getY() - (height / 2);
                            x = getOwner().getX() - (width / 2);
                            yadd = height / (float) (getOwner().getFrameCount() - 1);
                            xadd = 0;
                            break;
                        case RIGHT:
                            minY = 0;
                            maxY = swipe;
                            minX = 0;
                            maxX = range;
                            y = getOwner().getY() - (height / 2);
                            x = getOwner().getX() + (width / 2);
                            yadd = height / (float) (getOwner().getFrameCount() - 1);
                            xadd = 0;
                            break;
                        default:
                            minY = 0;
                            maxY = 0;
                            minX = 0;
                            maxX = 0;
                            x = getOwner().getX();
                            y = getOwner().getY();
                            xadd = 0;
                            yadd = 0;
                            break;
                    }

                    final HitBox swordHitBox = new HitBox(minX, minY, maxX, maxY);
                    swordHitBox.setX(x);
                    swordHitBox.setY(y);
                    final int[] temp_step = new int[1];
                    final List<CombatSprite> hits = new ArrayList<CombatSprite>();
                    getOwner().setAnimationFrameListener(new AnimatedSprite.AnimatedSpriteEvent.OnAnimationFrame() {
                        @Override
                        public void onAnimationFrame(AnimatedSprite sprite) {
                            List<Collidable> list = swordHitBox.checkAndRetrieve(sprite.getWorld(), swordHitBox.getX(), swordHitBox.getY(), sprite);
                            for (Collidable c : list) {
                                if (c instanceof CombatSprite) {
                                    CombatSprite combatSprite = (CombatSprite) c;
                                    if (!hits.contains(combatSprite)) {
                                        System.out.println("HIT ON STEP: " + temp_step[0] + " @ (" + swordHitBox.getX() + "," + swordHitBox.getY() + ")");
                                        combatSprite.strike(getOwner(), WeaponItem.this);
                                        hits.add(combatSprite);
                                    }
                                } else if (c instanceof TiledObject) {
                                    //TODO They hit a wall! We should stop it and play a sound or something
                                    break;
                                }
                            }
                            temp_step[0]++;
                            if (xadd != 0)
                                swordHitBox.setX(swordHitBox.getX() + xadd);
                            if (yadd != 0)
                                swordHitBox.setY(swordHitBox.getY() + yadd);
                        }
                    });
                    getOwner().setAnimationFinishedListener(new AnimatedSprite.AnimatedSpriteEvent.OnAnimationFinished() {
                        @Override
                        public void onAnimationFinished(AnimatedSprite sprite) {
                            sprite.setAnimationFinishedListener(null);
                            sprite.setAnimationFrameListener(null);
                            if (getOwner() instanceof PlayableSprite) {
                                ((PlayableSprite)getOwner()).unfreeze();
                                ((PlayableSprite) getOwner()).ignore_movement = false;
                            }
                            sprite.setAnimation(0);
                            hits.clear();
                        }
                    });
                    getOwner().playAnimation();

                    /**
                     * ===================
                     * END OF CODE CHUNK
                     * ===================
                     */
                } else {

                    /**
                     * =========================================================
                     * This chunk of code is the sword stabbing detection code
                     * =========================================================
                     */
                    switch (getOwner().getDirection()) {
                        case UP:
                            getOwner().setAnimation("stap_up");
                            break;
                        case DOWN:
                            getOwner().setAnimation("stab_down");
                            break;
                        case LEFT:
                            getOwner().setAnimation("stab_left");
                            break;
                        case RIGHT:
                            getOwner().setAnimation("stab_right");
                            break;
                    }

                    HitBox swordHitbox = new HitBox(0, 0, (facingDirection == Direction.DOWN || facingDirection == Direction.UP ? weapon.getSwipeRange() : 3), (facingDirection == Direction.RIGHT || facingDirection == Direction.LEFT ? weapon.getSwipeRange() : 3));
                    swordHitbox.setX(getOwner().getX());
                    swordHitbox.setY(getOwner().getY());
                    final float finalRange = weapon.getRange();
                    final float steps = finalRange / (float) (getOwner().getCurrentAnimation().size() - 1);
                    final HitBox finalSwordHitbox = swordHitbox;
                    final ArrayList<CombatSprite> hits = new ArrayList<CombatSprite>();
                    getOwner().setAnimationFrameListener(new AnimatedSprite.AnimatedSpriteEvent.OnAnimationFrame() {
                        @Override
                        public void onAnimationFrame(AnimatedSprite sprite) {
                            List<Collidable> list = finalSwordHitbox.checkAndRetrieve(sprite.getWorld(), finalSwordHitbox.getX(), finalSwordHitbox.getY(), sprite);
                            for (Collidable c : list) {
                                if (c instanceof CombatSprite) {
                                    CombatSprite combatSprite = (CombatSprite) c;
                                    if (!hits.contains(combatSprite)) {
                                        combatSprite.strike(getOwner(), WeaponItem.this);
                                        hits.add(combatSprite);
                                    }
                                } else if (c instanceof TiledObject) {
                                    //TODO They hit a wall! We should stop it and play a sound or something
                                    break;
                                }
                            }
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
                                ((PlayableSprite) getOwner()).ignore_movement = false;
                            }
                            sprite.setAnimation(0);
                            hits.clear();
                        }
                    });
                    getOwner().playAnimation();

                    /**
                     * ==================
                     * END OF CODE CHUNK
                     * ==================
                     */
                }
            }
        }
    }
}
