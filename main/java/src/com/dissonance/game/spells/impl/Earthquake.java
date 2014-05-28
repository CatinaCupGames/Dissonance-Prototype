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

        float radius = 5f * 16f; //20 tiles

        java.util.List<CombatSprite> combatSprites = owner.getWorld().getAllCombatSprites();
        for (CombatSprite c : combatSprites) {
            if (!c.isAlly(owner) && owner.distanceFrom(c) < radius) {
                Direction pushDirection = owner.directionTowards(c).opposite();
                switch (pushDirection) {
                    case UP:
                        c.rawSetY((float) (c.getY() - (((radius) - c.distanceFrom(owner)))));
                        break;
                    case DOWN:
                        c.rawSetY((float) (c.getY() + (((radius) - c.distanceFrom(owner)))));
                        break;
                    case LEFT:
                        c.rawSetX((float) (c.getX() - (((radius) - c.distanceFrom(owner)))));
                        break;
                    case RIGHT:
                        c.rawSetX((float) (c.getX() + (((radius) - c.distanceFrom(owner)))));
                        break;
                }

                System.out.println(c.getX() + " : " + c.getY());
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
