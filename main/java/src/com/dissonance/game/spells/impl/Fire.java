package com.dissonance.game.spells.impl;

import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.game.spells.BoltSpell;
import com.dissonance.game.spells.ProjectileSpell;
import com.dissonance.game.spells.statuseffects.Burn;
import com.sun.org.apache.xml.internal.security.utils.resolver.implementations.ResolverAnonymous;

import java.util.Random;

public class Fire extends ProjectileSpell {
    public Fire(CombatSprite owner) {
        super(owner);
    }

    @Override
    public StatusEffect getEffect() {
        return null;
    }

    @Override
    public int getRange() {
        return 80;
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
        return "fire";
    }

    @Override
    public String getSpriteName() {
        return "FireBall";
    }
}
