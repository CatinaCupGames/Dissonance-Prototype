package com.dissonance.game.sprites;

import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public class Admin extends Enemy {
    public Admin(String spriteName, StatType statType, CombatType combatType) {
        super("admin", statType, combatType);
    }
}
