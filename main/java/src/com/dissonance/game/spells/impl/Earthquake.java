package com.dissonance.game.spells.impl;

import com.dissonance.framework.game.combat.spells.Spell;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.sound.Sound;
import com.dissonance.framework.system.utils.Direction;

import java.awt.*;
import java.util.*;

public class Earthquake implements Spell {
    private CombatSprite owner;
    public Earthquake(CombatSprite owner) {
        this.owner = owner;
    }

    @Override
    public void castSpell() {
        Camera.shake(Direction.DOWN, 4000L, 5, 0.5);
        Sound.playSound("earthquake");

        float radius = 20f * 16f; //20 tiles
        Polygon javaPolygon = new Polygon();
        int ox = (int) (owner.getX() + radius);
        int oy = (int) (owner.getY() + radius);
        for (int i = 0; i < 20; i++) {
            int x = (int) (ox * Math.cos(Math.toRadians(i * 18)));
            int y = (int) (oy * Math.sin(Math.toRadians(i * 18)));
            javaPolygon.addPoint(x, y);
        }

        java.util.List<CombatSprite> combatSprites = owner.getWorld().getAllCombatSprites();
        for (CombatSprite c : combatSprites) {
            if (!c.isAlly(owner)) {
                Direction pushDirection = owner.directionTowards(c).opposite();
                c.forceAnimationDirection(pushDirection);
                c.setMovementSpeed(30);
                c.moveTowards(pushDirection, 5f * 16f)
            }
        }
    }

    @Override
    public int mpCost() {
        return 15;
    }

    @Override
    public CombatSprite getOwner() {
        return owner;
    }

    @Override
    public String getName() {
        return "Earthquake";
    }
}
