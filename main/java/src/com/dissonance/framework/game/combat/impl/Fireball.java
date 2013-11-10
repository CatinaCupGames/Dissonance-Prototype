package com.dissonance.framework.game.combat.impl;

import com.dissonance.framework.game.combat.Spell;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.system.utils.Direction;

public class Fireball extends Spell {
    public Fireball(Direction direction, CombatSprite owner, double speed) {
        super(direction, owner, speed);
    }

    public Fireball(Direction direction, CombatSprite owner) {
        super(direction, owner);
    }

    @Override
    protected void onDestroy() { }

    @Override
    protected void onActivate() {
        setActive(true);
    }

    @Override
    public void update() {
        super.update();
        if (isActive()) {
            moveOneFrame();
        }
    }

    @Override
    public String getSpriteName() {
        return "fireball_spell";
    }
}
