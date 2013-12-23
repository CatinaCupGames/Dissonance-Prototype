package com.dissonance.framework.game.sprites.impl.game;

import com.dissonance.framework.game.combat.spells.Spell;
import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.item.Item;
import com.dissonance.framework.game.item.impl.WeaponItem;
import com.dissonance.framework.system.utils.Validator;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class CombatSprite extends PhysicsSprite {
    private ArrayList<Item> inventory = new ArrayList<Item>();
    private ArrayList<Spell> spells = new ArrayList<Spell>();
    private Spell spell1;
    private Spell spell2;
    private final ArrayList<StatusEffect> effects = new ArrayList<StatusEffect>();
    private int weaponIndex;
    private boolean isCastingSpell = false;
    //==FIXED STATS==//
    private double HP = 100; //This is a fixed stat

    //==VARIABLE STATS==//
    protected int level = 1;
    private float MP;

    public int getLevel() {
        return level;
    }

    public void levelUp() {
        level++;
        HP = 100;
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

    @Override
    public void update() {
        super.update();
        if (isUpdateCanceled())
            return;

        if (effects.size() > 0) {
            synchronized (effects) {
                Iterator<StatusEffect> effectIterator = effects.iterator();
                while (effectIterator.hasNext()) {
                    StatusEffect effect = effectIterator.next();
                    if (!effect.hasStarted())
                        effect.startEffect();
                    if (effect.inflict(this))
                        effectIterator.remove();
                }
            }
        }

        if (isCastingSpell)
            setUpdateCanceled(true);
        if (HP <= 0)
            setUpdateCanceled(true);
    }

    public void addSpell(Spell spell) {
        if (!spells.contains(spell))
            spells.add(spell);
    }

    public List<Spell> getAllSpells() {
        return Collections.unmodifiableList(spells);
    }

    public Spell getSpell(String name) {
        for (Spell s : spells) {
            if (s.getName().equals(name))
                return s;
        }
        return null;
    }

    public Spell getSpell(int i) {
        Validator.validateNotBelow(i, 0, "index");
        Validator.validateNotOver(i, spells.size() - 1, "index");

        return spells.get(i);
    }

    public boolean hasSpell(Spell spell) {
        return spells.contains(spell);
    }

    public boolean hasSpell1() {
        return spell1 != null;
    }

    public boolean hasSpell2() {
        return spell2 != null;
    }

    public void setSpell1(Spell spell) {
        this.spell1 = spell;
    }

    public void setSpell2(Spell spell) {
        this.spell2 = spell;
    }

    public void useSpell1() {
        if (!hasSpell1())
            return;
        this.isCastingSpell = true;
        spell1.castSpell();
        this.isCastingSpell = false;
    }

    public void useSpell2() {
        if (!hasSpell1())
            return;
        this.isCastingSpell = true;
        spell1.castSpell();
        this.isCastingSpell = false;
    }

    public void useSpell(Spell spell) {
        Validator.validateNotNull(spell, "spell");
        isCastingSpell = true;
        spell.castSpell();
        isCastingSpell = false;
    }

    public void applyStatusCondition(StatusEffect effect) {
        synchronized (effects) {
            effects.add(effect);
        }
    }

    public double getHP() {
        return HP;
    }

    public List<Item> getInventory() {
        return Collections.unmodifiableList(inventory);
    }

    public List<WeaponItem> getAllWeapons() {
        ArrayList<WeaponItem> list = new ArrayList<WeaponItem>();
        for (Item i : inventory) {
            if (i instanceof WeaponItem) {
                list.add((WeaponItem) i);
            }
        }
        return Collections.unmodifiableList(list);
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
        if (inventory.size() == 0 || weaponIndex >= inventory.size())
            return null;
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
        double defense = getDefense() + (getCurrentWeapon() != null ? getCurrentWeapon().getWeaponInfo().getDefense() : 0);
        double attack = attacker.getAttack() + with.getWeaponInfo().getAttack();
        double damage;
        damage = ((attack * Math.log(attack)) / (defense / Math.log(defense))) * 2;
        if (damage > 100)
            damage = 100;

        applyDamage(damage);
        if (HP <= 0) {
            //TODO Give attacker EXP
        }
    }

    public void applyDamage(double damage) {
        HP -= damage;
        if (HP <= 0) {
            //TODO Play death animation for this sprite
            getWorld().removeSprite(this);
        }
    }

    public abstract boolean isAlly(CombatSprite sprite);

    public void setHP(double HP) {
        this.HP = HP;
    }

    public float getMP() {
        return MP;
    }

    public void setMP(float MP) {
        this.MP = MP;
    }


    public enum CombatType {
        /**
         * Hostile things that aren't human
         */
        CREATURE,

        /**
         * Pretty self-explanatory
         */
        HUMAN,

        /**
         * Lack the organic carbon-based compounds and souls that we
         * privileged folks possess
         */
        MACHINE
    }
}
