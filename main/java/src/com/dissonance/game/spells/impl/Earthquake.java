package com.dissonance.game.spells.impl;

import com.dissonance.framework.game.combat.spells.Spell;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.sound.Sound;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.game.sprites.Admin;
import com.dissonance.game.sprites.BlueGuard;
import com.dissonance.game.sprites.RedGuard;

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

        java.util.List<CombatSprite> combatSprites = owner.getWorld().getAllCombatSprites();
        for (CombatSprite c : combatSprites) {
            if (!c.isAlly(owner) && owner.distanceFrom(c) < radius) {
                if (c instanceof BlueGuard) {
                    final BlueGuard blueGuard = (BlueGuard)c;
                    blueGuard.fallOver(new Runnable() {
                        @Override
                        public void run() {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    blueGuard.fallOver = false;
                                    blueGuard.setInvincible(false);
                                    blueGuard.setHostile(true);
                                }
                            }).start();
                        }
                    });
                    blueGuard.setInvincible(false);
                } else if (c instanceof RedGuard) {
                    final RedGuard blueGuard = (RedGuard)c;
                    blueGuard.fallOver(new Runnable() {
                        @Override
                        public void run() {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    blueGuard.fallOver = false;
                                    blueGuard.setInvincible(false);
                                    blueGuard.setHostile(true);
                                }
                            }).start();
                        }
                    });
                    blueGuard.setInvincible(false);
                }
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
