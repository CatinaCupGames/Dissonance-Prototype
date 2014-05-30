package com.dissonance.game.spells.impl;

import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.sprites.impl.game.ParticleSprite;
import com.dissonance.framework.sound.Sound;
import com.dissonance.game.spells.ProjectileSpell;
import com.dissonance.game.spells.statuseffects.Burn;

import java.awt.*;
import java.util.Random;

public class Fire extends ProjectileSpell {
    private ParticleSprite.ParticleSource source;
    public Fire(CombatSprite owner) {
        super(owner);
    }

    @Override
    public StatusEffect getEffect() {
        return null;
    }

    @Override
    public int getRange() {
        return 384;
    }

    @Override
    protected float getSpeed() {
        return 25f;
    }

    @Override
    protected void moveOneFrame() {
        super.moveOneFrame();
        if (!destoried) {
            if (source == null) {
                source = ParticleSprite.createParticlesAt(getX(), getY(), getWorld())
                        .setSpeed(getSpeed() / 2f)
                        .setTime(300L)
                        .setRate(100)
                        .setCount(300)
                        .setColor(Color.RED);
            }
            source.setX(getX());
            source.setY(getY());
        } else {
            if (source != null) source.end();
        }
    }

    final Random random = new Random();
    @Override
    public void onContact(CombatSprite combat) {
        if (random.nextInt(100) <= 40)
            combat.applyStatusCondition(new Burn(6000, owner.getFocus()));
    }

    @Override
    public int mpCost() {
        return 5;
    }

    @Override
    public String getName() {
        return "FireBall";
    }

    @Override
    public String getSpriteName() {
        return "fireball_spell";
    }

    @Override
    public void castSpell(){
        super.castSpell();
        Sound.playSound("firespell");
    }
}
