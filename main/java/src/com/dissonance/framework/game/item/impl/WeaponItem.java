package com.dissonance.framework.game.item.impl;

import com.dissonance.framework.game.combat.Weapon;
import com.dissonance.framework.game.item.Item;
import com.dissonance.framework.game.sprites.impl.UpdatableSprite;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.system.utils.Direction;

import java.util.Iterator;

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
            //TODO Animate spell
        } else {
            //TODO Animate sprite
        }

        Direction facingDirection = getOwner().getDirection();
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
        }
    }
}
