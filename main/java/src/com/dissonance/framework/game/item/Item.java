package com.dissonance.framework.game.item;

import com.dissonance.framework.game.sprites.impl.game.CombatSprite;

/**
 * A class that represents an item that can be given to any {@link com.dissonance.framework.game.sprites.impl.game.CombatSprite}
 */
public abstract class Item {
    protected CombatSprite owner;
    private int count;

    public Item(CombatSprite owner) {
        this.owner = owner;
        this.count = 1;
    }

    /**
     * The owner of this item
     * @return The {@link com.dissonance.framework.game.sprites.impl.game.CombatSprite} that owns this item
     */
    public CombatSprite getOwner() {
        return owner;
    }

    /**
     * How many of this item exist. <br></br>
     * Example: 3 apples
     * @return The count for this item
     */
    public int getItemCount() {
        return count;
    }

    /**
     * Set the count for this item <br></br>
     * Example: 5 apples
     * @param count  The new count for this item
     */
    public void setItemCount(int count) {
        if (!isStackable() && count > 1)
            count = 1;
        this.count = count;
        if (count <= 0)
            owner.removeItem(this);
    }

    /**
     * The item's name that is in-game friendly.
     * @return The item's name
     */
    public abstract String getItemName();

    /**
     * Whether this item can be stacked or not (If this item's {@link com.dissonance.framework.game.item.Item#getItemCount()} can return more than 1)
     * @return True if this item can be stacked, otherwise false.
     */
    public abstract boolean isStackable();

    /**
     * This method is invoked when the item is used.
     * @param parameters A set of parameters that may or may not be provided
     */
    public abstract void use(Object... parameters);

    /**
     * Set the owner of this item.
     * @param itemOwner The new owner
     */
    public void setItemOwner(CombatSprite itemOwner) {
        this.owner = itemOwner;
    }

    /**
     * Set the owner of this item.
     * @param owner The new owner.
     */
    public void setOwner(CombatSprite owner) {
        this.owner = owner;
    }
}
