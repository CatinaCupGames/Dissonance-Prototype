package com.dissonance.framework.game.combat.spells.impl;

import com.dissonance.framework.game.combat.spells.Spell;
import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.render.UpdatableDrawable;

import java.awt.*;
import java.util.*;
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
                //TODO Apply damage
                c.applyStatusCondition(getEffect());
            }
        }
    }

    @Override
    public CombatSprite getOwner() {
        return owner;
    }

    public abstract StatusEffect getEffect();

    public abstract float getRange();
}
