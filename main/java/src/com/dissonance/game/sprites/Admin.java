package com.dissonance.game.sprites;

import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public class Admin extends Enemy {
    public Admin() {
        super("admin", StatType.NON_MAGIC, CombatType.HUMAN);
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }
}
