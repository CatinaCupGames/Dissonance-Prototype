package com.dissonance.framework.game.item.impl;

import com.dissonance.framework.game.combat.Weapon;
import com.dissonance.framework.game.item.Item;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public class WeaponItem extends Item {
    private Weapon weapon;

    public WeaponItem(CombatSprite owner, Weapon w) {
        super(owner);
        this.weapon = w;
    }

    public Weapon getWeapon() {
        return weapon;
    }
}
