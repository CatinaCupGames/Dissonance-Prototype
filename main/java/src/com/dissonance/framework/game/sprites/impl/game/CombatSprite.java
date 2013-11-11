package com.dissonance.framework.game.sprites.impl.game;

import com.dissonance.framework.game.combat.Spell;
import com.dissonance.framework.game.combat.Weapon;
import com.dissonance.framework.game.item.Item;
import com.dissonance.framework.game.item.impl.WeaponItem;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.system.utils.Validator;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public abstract class CombatSprite extends AbstractWaypointSprite {
    private ArrayList<Item> inventory = new ArrayList<Item>();
    private int weaponIndex;
    private boolean isCastingSpell = false;
    //==FIXED STATS==//
    private double HP = 100; //This is a fixed stat

    //==VARIABLE STATS==//
    protected int level = 1;

    public int getLevel() {
        return level;
    }

    public void levelUp() {
        level++;
        onLevelUp();
    }

    public abstract void onLevelUp();

    /**
     * Determines the damage a physical attack by the player will deal,
     * as well as the knockback the attack will cause to the enemy
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The attack stat, otherwise 0
     */
    public abstract int getAttack();

    /**
     * Determines the damage taken by incoming attacks
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The defense stat, otherwise 0
     */
    public abstract int getDefense();

    /**
     * Determines movement speed and the ability to dodge attacks
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The speed stat, otherwise 0
     */
    public abstract int getSpeed();

    /**
     * Determines how high the critical hit rate is for the player's
     * attacks, as well as how low the critical hit rate is for attacks against
     * the player. Also determines the player's chance to survive an otherwise
     * fatal blow
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The vigor stat, otherwise 0
     */
    public abstract int getVigor();

    /**
     * Used to perform dodges and special abilities, depletes with use and replenishes over time
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The stamina stat, otherwise 0
     */
    public abstract int getStamina();

    /**
     * Determines the damage magic attacks deal, as well as the
     * damage received from magic attacks
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The willpower stat, otherwise 0
     */
    public abstract int getWillPower();

    /**
     * Determines the damage absorbed by magic shields, the HP
     * replenished by cure magic, the duration of spells' effects, and the
     * effectiveness of buff/debuff spells
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The focus stat, otherwise 0
     */
    public abstract int getFocus();

    /**
     * Determines recoil, reload speed, and jam frequency when
     * using guns
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The marksmanship stat, otherwise 0
     */
    public abstract int getMarksmanship();

    /**
     * For non-magic users, determines damage dealt by incoming magic attacks
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The magic resistance stat, othwerwise 0
     */
    public abstract int getMagicResistance();

    /**
     * Get the {@link CombatType} of this Sprite. The {@link CombatType} determines
     * what Status Conditions are affected by this CombatSprite.
     *
     * @return
     *        {@link CombatType}
     */
    public abstract CombatType getCombatType();

    public void castSpell(Spell spell, Weapon data) {
        if (spell == null)
            return;
        isCastingSpell = true;
        setAnimation(data.getAnimationRow());
        setAnimationSpeed(data.getAnimationSpeed());
        spell.activate();
        super.setAnimationFinishedListener(new AnimatedSpriteEvent.OnAnimationFinished() {
            @Override
            public void onAnimationFinished(AnimatedSprite sprite) {
                isCastingSpell = false;
            }
        });
    }

    @Override
    public void update() {
        super.update();
        if (isUpdateCanceled())
            return;
        if (isCastingSpell)
            setUpdateCanceled(true);
        if (HP <= 0)
            setUpdateCanceled(true);
    }

    public double getHP() {
        return HP;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    /**
     * Add an {@link Item} to this {@link CombatSprite} inventory. <br></br>
     * If this {@link CombatSprite} already has an instance of the {@link Item} in the inventory, then
     * the {@link Item#setItemCount(int)} method is invoked to combine the amounts, otherwise the {@link Item} is added. <br></br>
     * @param item
     *            The {@link Item} object to add
     * @return
     *        The index where the {@link Item} was put.
     */
    public int addItem(Item item) {

        if (item.getOwner() != this) {
            item.setItemOwner(this);
        }

        WeaponItem w = getCurrentWeapon();
        int index;
        if (item.isStackable()) {
            index = getFirstIndex(item.getItemName());
            if (index == -1) {
                inventory.add(item);
                index = inventory.size() - 1;
            } else {
                Item i = getItem(index);
                i.setItemCount(i.getItemCount() + item.getItemCount()); //Combine these items
            }
        } else {
            inventory.add(item);
            index = inventory.size() - 1;
        }


        if (w != null && index == inventory.size() - 1) //Only update index if item was added
            weaponIndex = getFirstIndex(w);

        return index;
    }

    /**
     * Remove an instance of an {@link Item} from this player's inventory. This will remove ALL the instances of the item.
     * @param item
     *            The {@link Item} to remove
     */
    public void removeItem(Item item) {
        WeaponItem w = getCurrentWeapon();
        inventory.remove(item);
        if (w != null && !w.equals(item))
            weaponIndex = getFirstIndex(w); //Recalculate index of weapon
    }

    /**
     * Remove an {@link Item} at the specified index. <br></br>
     * @param index
     *             The index
     */
    public void removeItem(int index) {
        Validator.validateInRange(index, 0, inventory.size() - 1, "index");

        WeaponItem w = getCurrentWeapon();
        inventory.remove(index);
        if (w != null && weaponIndex != index)
            weaponIndex = getFirstIndex(w); //Recalculate index of weapon
    }

    public int getFirstIndex(Item item) {
        return getFirstIndex(item.getItemName());
    }

    public int getFirstIndex(String item) {
        for (int i = 0; i < inventory.size(); i++) {
            Item it = inventory.get(i);
            if (it.getItemName().equalsIgnoreCase(item))
                return i;
        }
        return -1;
    }

    public int getItemCount() {
        return inventory.size();
    }

    public boolean hasItem(String name) {
        for (Item i : inventory) {
            if (i.getItemName().equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public Item getItem(String name) {
        for (Item i : inventory) {
            if (i.getItemName().equalsIgnoreCase(name))
                return i;
        }
        return null;
    }

    public Item getItem(int index) {
        Validator.validateInRange(index, 0, inventory.size() - 1, "index");
        return inventory.get(index);
    }

    public WeaponItem getCurrentWeapon() {
        return (WeaponItem) inventory.get(weaponIndex);
    }

    public int getCurrentWeaponIndex() {
        return weaponIndex;
    }

    public void setCurrentWeapon(int index) {
        Validator.validateInRange(index, 0, inventory.size() - 1, "index");
        if (!(inventory.get(index) instanceof WeaponItem))
            throw new InvalidParameterException("The item in the specified index is not a weapon!");
        weaponIndex = index;
    }

    public void setCurrentWeapon(WeaponItem item) {
        int index;
        if ((index = getFirstIndex(item)) == -1)
            index = addItem(item);

        setCurrentWeapon(index);
    }

    public void strike(CombatSprite attacker, WeaponItem with) {
        double defense = getDefense() + (getCurrentWeapon() != null ? getCurrentWeapon().getWeapon().getDefense() : 0);
        double attack = attacker.getAttack() + with.getWeapon().getAttack();
        double damage;
        damage = (attack * Math.log(attack * 4)) / defense;

        HP -= damage;
        //TODO Display damage

        if (HP <= 0) {
            //TODO Give attacker EXP
            //TODO Play death animation for this sprite
            setAnimationFinishedListener(new AnimatedSpriteEvent.OnAnimationFinished() {
                @Override
                public void onAnimationFinished(AnimatedSprite sprite) {
                    getWorld().removeSprite(CombatSprite.this);
                }
            });
        }
    }


    public enum CombatType {
        /**
         * Any thing that the player can play as
         */
        ALLY,

        /**
         * Hostile things that aren't human
         */
        CREATURE,

        /**
         * Human military units
         */
        TROOP,

        /**
         * Lack the organic carbon-based compounds and souls that we
         * privileged folks possess
         */
        MACHINE
    }
}
