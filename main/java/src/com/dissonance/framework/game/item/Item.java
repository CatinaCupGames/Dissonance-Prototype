package com.dissonance.framework.game.item;

import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public abstract class Item {
    protected CombatSprite owner;

    public Item(CombatSprite owner) {
        this.owner = owner;
    }

    public CombatSprite getOwner() {
        return owner;
    }
}
