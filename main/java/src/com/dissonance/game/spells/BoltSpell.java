package com.dissonance.game.spells;

import com.dissonance.framework.game.combat.spells.Spell;
import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.sound.Sound;
import com.dissonance.framework.system.utils.Direction;

import java.util.List;

public abstract class BoltSpell implements Spell {
    protected CombatSprite owner;

    public BoltSpell(CombatSprite owner) {
        this.owner = owner;
    }

    @Override
    public void castSpell() {
        Sound.playSound("boltspell");

        Direction direction = owner.getFacingDirection();
        float x1, x2, y1, y2;
        switch (direction) {
            case DOWN:
                x1 = owner.getX() - (owner.getWidth() / 2);
                x2 = owner.getX() + (owner.getWidth() / 2);
                y1 = owner.getY() + (owner.getHeight() / 2);
                y2 = (owner.getY() + (owner.getHeight() / 2)) + getRange();
                break;
            case UP:
                x1 = owner.getX() - (owner.getWidth() / 2);
                x2 = owner.getX() + (owner.getWidth() / 2);
                y1 = (owner.getY() - (owner.getHeight() / 2)) - getRange();
                y2 = owner.getY() - (owner.getHeight() / 2);
                break;
            case LEFT:
                y1 = owner.getY() - (owner.getHeight() / 2);
                y2 = owner.getY() + (owner.getHeight() / 2);
                x1 = (owner.getX() - (owner.getWidth() / 2)) - getRange();
                x2 = owner.getX() - (owner.getWidth() / 2);
                break;
            case RIGHT:
                y1 = owner.getY() - (owner.getHeight() / 2);
                y2 = owner.getY() + (owner.getHeight() / 2);
                x1 = owner.getX() + (owner.getWidth() / 2);
                x2 = (owner.getX() + (owner.getWidth() / 2)) + getRange();
                break;
            default:
                x1 = owner.getX() - (owner.getWidth() / 2);
                x2 = owner.getX() + (owner.getWidth() / 2);
                y1 = owner.getY() - (owner.getHeight() / 2);
                y2 = owner.getY() + (owner.getHeight() / 2);
                break;
        }

        List<CombatSprite> sprites = owner.getWorld().getAllCombatSprites();
        for (CombatSprite s : sprites) {
            if (s == owner)
                continue;
            if (s.getX() >= x1 && s.getX() <= x2 && s.getY() >= y1 && s.getY() <= y2) {
                double defense = s.getWillPower();
                double attack = owner.getFocus();
                double damage;
                damage = ((attack * Math.log(attack)) / (defense / Math.log(defense))) * 2;
                if (damage > 100)
                    damage = 100;
                s.applyDamage(damage);
                s.applyStatusCondition(getEffect());
                onContact(s);
            }
        }
    }

    @Override
    public CombatSprite getOwner() {
        return owner;
    }

    public abstract StatusEffect getEffect();

    public abstract float getRange();

    public abstract void onContact(CombatSprite combat);
}
