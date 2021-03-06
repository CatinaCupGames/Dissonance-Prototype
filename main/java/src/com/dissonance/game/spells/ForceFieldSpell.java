package com.dissonance.game.spells;

import com.dissonance.framework.game.combat.spells.Spell;
import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

import java.awt.*;
import java.util.List;

public abstract class ForceFieldSpell implements Spell {
    protected CombatSprite owner;

    public ForceFieldSpell(CombatSprite owner) {
        this.owner = owner;
    }

    @Override
    public void castSpell() {
        Polygon javaPolygon = new Polygon();
        int ox = (int) (owner.getX() + getRange());
        int oy = (int) (owner.getY() + getRange());
        for (int i = 0; i < 20; i++) {
            int x = (int) (ox * Math.cos(Math.toRadians(i * 18)));
            int y = (int) (oy * Math.sin(Math.toRadians(i * 18)));
            javaPolygon.addPoint(x, y);
        }

        List<CombatSprite> combatSprites = owner.getWorld().getAllCombatSprites();
        for (CombatSprite c : combatSprites) {
            if (!c.isAlly(owner)) {
                double defense = c.getWillPower();
                double attack = owner.getFocus();
                double damage;
                damage = ((attack * Math.log(attack)) / (defense / Math.log(defense))) * 2;
                if (damage > 100)
                    damage = 100;
                c.applyDamage(damage);
                c.applyStatusCondition(getEffect());
                onContact(c);
            }
        }
    }

    @Override
    public CombatSprite getOwner() {
        return owner;
    }

    public abstract void onContact(CombatSprite combat);

    public abstract StatusEffect getEffect();

    public abstract float getRange();
}
