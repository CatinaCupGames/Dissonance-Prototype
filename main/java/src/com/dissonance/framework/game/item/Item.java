package com.dissonance.framework.game.item;

import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

public abstract class Item {
    protected CombatSprite owner;
    private int count;

    public Item(CombatSprite owner) {
        this.owner = owner;
        this.count = 1;
    }

    public CombatSprite getOwner() {
        return owner;
    }

    public int getItemCount() {
        return count;
    }

    public void setItemCount(int count) {
        if (!isStackable() && count > 1)
            count = 1;
        this.count = count;
        if (count <= 0)
            owner.removeItem(this);
    }

    public abstract String getItemName();

    public abstract boolean isStackable();
    
    public abstract void use(Object... parameters);

    public void setItemOwner(CombatSprite itemOwner) {
        this.owner = itemOwner;
    }

    public void setOwner(CombatSprite owner) {
        this.owner = owner;
    }
}
