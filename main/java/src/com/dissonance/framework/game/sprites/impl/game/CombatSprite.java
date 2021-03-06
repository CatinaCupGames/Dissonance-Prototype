package com.dissonance.framework.game.sprites.impl.game;

import com.dissonance.framework.game.ai.astar.FastMath;
import com.dissonance.framework.game.combat.spells.Spell;
import com.dissonance.framework.game.combat.spells.StatusEffect;
import com.dissonance.framework.game.item.Item;
import com.dissonance.framework.game.item.impl.WeaponItem;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.sound.Sound;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.framework.system.utils.MovementType;
import com.dissonance.framework.system.utils.Validator;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;

import static org.lwjgl.opengl.GL11.*;

public abstract class CombatSprite extends PhysicsSprite {
    private ArrayList<Item> inventory = new ArrayList<Item>();
    private ArrayList<Spell> spells = new ArrayList<Spell>();
    private Spell spell1;
    private Spell spell2;
    private final ArrayList<StatusEffect> effects = new ArrayList<StatusEffect>();
    private int weaponIndex;
    private CombatSpriteEvents listener;

    protected MovementType mType = MovementType.RUNNING;


    protected float dodgeX, dodgeY, dodgeStartX, dodgeStartY, totalDodgeTime;
    protected long dodgeStartTime;
    protected boolean is_dodging, allow_dodge = true;
    protected Direction dodgeDirection;


    private transient ArrayList<PlayableSprite> lockers = new ArrayList<PlayableSprite>();
    private transient ArrayList<Float[]> lockedOnColor = new ArrayList<Float[]>();
    private transient ArrayList<Float> lockOnRotation = new ArrayList<Float>();

    private boolean isCastingSpell = false;
    //==FIXED STATS==//
    private double MAX_HP = 100;
    private double HP = 100;

    //==VARIABLE STATS==//
    protected int level = 1;
    private double MAX_MP = 100;
    private double MP = 100;
    private boolean attacking;
    private boolean isInvincible;
    private int stamina;
    private int maxStamina;

    public void setCombatListener(CombatSpriteEvents listener) {
        this.listener = listener;
    }

    public CombatSpriteEvents getCombatListener() {
        return this.listener;
    }


    public int getLevel() {
        return level;
    }

    public void levelUp() {
        level++;
        refillHP();
        setMaxHP(getMaxHP() + 100);
        onLevelUp();
    }

    public void lockOn(PlayableSprite sprite) {
        Float[] colors = new Float[3];
        switch (sprite.getPlayer().getNumber()) {
            case 1:
                colors[0] = 255f;
                colors[1] = 0f;
                colors[2] = 12f;
                break;
            case 2:
                colors[0] = 73f;
                colors[1] = 79f;
                colors[2] = 255f;
                break;
            case 3:
                colors[0] = 255f;
                colors[1] = 223f;
                colors[2] = 68f;
                break;
            case 4:
                colors[0] = 29f;
                colors[1] = 255f;
                colors[2] = 0f;
                break;
            default:
                final Random random = new Random();
                colors[0] = (float) random.nextInt(255);
                colors[1] = (float) random.nextInt(255);
                colors[2] = (float) random.nextInt(255);
                break;
        }

        colors[0] = colors[0] / 255f;
        colors[1] = colors[1] / 255f;
        colors[2] = colors[2] / 255f;

        lockedOnColor.add(colors);
        lockOnRotation.add(0f);
        lockers.add(sprite);
    }

    public MovementType getMovementType() {
        return mType;
    }

    public void setMovementType(MovementType type) {
        this.mType = type;
    }

    public void removeLock(PlayableSprite sprite) {
        if (lockers.contains(sprite)) {
            int index = lockers.indexOf(sprite);
            lockers.remove(index);
            lockedOnColor.remove(index);
            lockOnRotation.remove(index);
        }
    }

    public abstract void onLevelUp();

    /**
     * Determines the value a physical attack by the player will deal,
     * as well as the knockback the attack will cause to the enemy
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The attack stat, otherwise 0
     */
    public abstract int getAttack();

    /**
     * Determines the value taken by incoming attacks
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
    public int getStamina() {
        return stamina;
    }

    public int getMaxStamina() {
        return maxStamina;
    }

    public void setMaxStamina(int value) {
        this.maxStamina = value;
    }

    /**
     * Determines the value magic attacks deal, as well as the
     * value received from magic attacks
     *
     * If this Sprite does not have this stat, then this method should return 0
     * @return
     *        The willpower stat, otherwise 0
     */
    public abstract int getWillPower();

    /**
     * Determines the value absorbed by magic shields, the HP
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


    public abstract void setAttack(int attack);

    public abstract void setDefense(int defense);

    //TODO Add abstract setters for all other stats

    /**
     * Get the {@link CombatType} of this Sprite. The {@link CombatType} determines
     * what Status Conditions are affected by this CombatSprite.
     *
     * @return
     *        {@link CombatType}
     */
    public abstract CombatType getCombatType();

    private transient Texture texture;
    @Override
    public void render() {
        super.render();
        for (int i = 0; i < lockers.size(); i++) {
            if (texture == null) {
                try {
                    texture = Texture.retrieveTexture("sprites/img/target.png");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            float x = getX();
            float y = getY();
            float z = 0f;
            float bx = 32f / 2f;
            float by = 32f / 2f;

            Float[] colors = lockedOnColor.get(i);
            Float rotation = lockOnRotation.get(i);
            glPushMatrix();
            glTranslatef(x, y, 0f);
            glRotatef(rotation, 0f, 0f, 1f);
            glColor3f(colors[0], colors[1], colors[2]);
            texture.bind();
            glBegin(GL_QUADS);
            glTexCoord2f(0f, 0f); //bottom left
            glVertex3f(-bx, -by, z);
            glTexCoord2f(1f, 0f); //bottom right
            glVertex3f(bx, -by, z);
            glTexCoord2f(1f, 1f); //top right
            glVertex3f(bx, by, z);
            glTexCoord2f(0f, 1f); //top left
            glVertex3f(-bx, by, z);
            glEnd();
            texture.unbind();
            glPopMatrix();
            glColor3f(1f, 1f, 1f);
        }
    }

    protected void dodge(Direction direction1) {
        dodge(direction1, movementSpeed * 8.5f);
    }


    protected void dodge(Direction direction1, float speed) {
        if (!canDodge())
            return;
        if (getStamina() - 20 < 0)
            return;
        String ani;
        /*

        ALGEBRA TIME!

        d = distance
        s = speed
        t = total dodge time
        f = time per frame

        t = 4f
        s = 2.5 * movementSpeed()
        d = s * (t / f)
        f = (st) / d


        ==============================

        As long as t = 4f, then d = 4s
         */
        int DISTANCE = 120; //TODO Maybe change this
        DISTANCE *= 0.8f;
        DISTANCE /= 2f;
        switch (direction1) {
            case UP:
            case UP_LEFT:
            case UP_RIGHT:
                ani = "dodge_up";
                int i = 0;
                /*for (; i < DISTANCE; i++) {
                    if (getHitBox().checkForCollision(this, getX(), getY() - i))
                        break;
                }*/
                dodgeY = getY() - DISTANCE;
                dodgeX = 0;
                break;
            case DOWN:
            case DOWN_LEFT:
            case DOWN_RIGHT:
                ani = "dodge_down";
                int ii = 0;
               /* for (; ii < DISTANCE; ii++) {
                    if (getHitBox().checkForCollision(this, getX(), getY() + ii))
                        break;
                }*/
                dodgeY = getY() + DISTANCE;
                dodgeX = 0;
                break;
            case LEFT:
                ani = "dodge_left";
                int iii = 0;
                /*for (; iii < DISTANCE; iii++) {
                    if (getHitBox().checkForCollision(this, getX() - iii, getY()))
                        break;
                }*/
                dodgeX = getX() - DISTANCE;
                dodgeY = 0;
                break;
            case RIGHT:
                ani = "dodge_right";
                int iiii = 0;
                /*for (; iiii < DISTANCE; iiii++) {
                    if (getHitBox().checkForCollision(this, getX() + iiii, getY()))
                        break;
                }*/
                dodgeX = getX() + DISTANCE;
                dodgeY = 0;
                break;
            default:
                return;
        }
        this.dodgeDirection = direction1;
        setAnimation(ani);
        setAnimationSpeed(70);
        totalDodgeTime = 4 * getAnimationSpeed();
        totalDodgeTime -= speed;
        setAnimationSpeed((int) (((1f/4f) * speed) + ((1f/4f) * totalDodgeTime)));
        playAnimation();
        dodgeStartTime = System.currentTimeMillis();
        dodgeStartX = getX();
        dodgeStartY = getY();
        setStamina(getStamina() - 20);
        is_dodging = true;
        allow_dodge = false;
    }


    private long lastGive;
    @Override
    public void update() {
        super.update();
        if (isUpdateCanceled())
            return;

        if (System.currentTimeMillis() - lastGive >= 1000) {
            lastGive = System.currentTimeMillis();
            int add = 5;
            while (stamina + add > maxStamina)
                add--;
            setStamina(getStamina() + add);
        }

        checkDodge();

        if (effects.size() > 0) {
            synchronized (effects) {
                Iterator<StatusEffect> effectIterator = effects.iterator();
                while (effectIterator.hasNext()) {
                    StatusEffect effect = effectIterator.next();
                    if (effect.inflict(this))
                        effectIterator.remove();
                }
            }
        }

        for (int i = 0; i < lockOnRotation.size(); i++) {
            Float f = lockOnRotation.get(i);
            f = (f + (5f * RenderService.TIME_DELTA));
            if (f >= 360f)
                f = 0f;

            lockOnRotation.set(i, f);
        }

        if (isCastingSpell)
            setUpdateCanceled(true);
        if (HP <= 0)
            setUpdateCanceled(true);
    }

    protected void checkDodge() {
        if (is_dodging) {
            float moveX, moveY;
            if (dodgeX != 0) {
                float dif = (float)(System.currentTimeMillis() - dodgeStartTime);
                float percent;
                if (dif > totalDodgeTime) {
                    percent = 1;
                } else {
                    percent = dif / totalDodgeTime;
                }
                float oX = super.getX();
                moveX = dodgeStartX + ((dodgeX - dodgeStartX) * percent);
                rawSetX(moveX);
                if (moveX == dodgeX || oX == super.getX()) {
                    setAnimation(0);
                    is_dodging = false;
                    face(dodgeDirection);
                    com.dissonance.framework.system.utils.Timer.delayedInvokeRunnable(100, new Runnable() {
                        @Override
                        public void run() {
                            allow_dodge = true;
                        }
                    });
                }
            } else if (dodgeY != 0) {
                float dif = (float)(System.currentTimeMillis() - dodgeStartTime);
                float percent;
                if (dif > totalDodgeTime) {
                    percent = 1;
                } else {
                    percent = dif / totalDodgeTime;
                }
                float oY = super.getY();
                moveY = dodgeStartY + ((dodgeY - dodgeStartY) * percent);
                rawSetY(moveY);
                face(getFacingDirection());
                if (moveY == dodgeY || oY == super.getY()) {
                    setAnimation(0);
                    is_dodging = false;
                    face(dodgeDirection);
                    com.dissonance.framework.system.utils.Timer.delayedInvokeRunnable(100, new Runnable() {
                        @Override
                        public void run() {
                            allow_dodge = true;
                        }
                    });
                }
            }
        }
    }

    public boolean isMoving() {
        return movementDetect;
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
        if (MP < spell1.mpCost()) {
            //TODO Play sound
            return;
        }
        this.isCastingSpell = true;
        MP -= spell1.mpCost();
        spell1.castSpell();
        this.isCastingSpell = false;
    }

    public void useSpell2() {
        if (!hasSpell2())
            return;
        if (MP < spell2.mpCost()) {
            //TODO Play sound
            return;
        }
        this.isCastingSpell = true;
        MP -= spell2.mpCost();
        spell2.castSpell();
        this.isCastingSpell = false;
    }

    public void useSpell(Spell spell) {
        Validator.validateNotNull(spell, "spell");
        if (MP < spell.mpCost()) {
            //TODO Play sound
            return;
        }
        isCastingSpell = true;
        MP -= spell.mpCost();
        spell.castSpell();
        isCastingSpell = false;
    }

    public void applyStatusCondition(StatusEffect effect) {
        synchronized (effects) {
            effects.add(effect);
            effect.startEffect(this);
        }
    }

    public double getHP() {
        return HP;
    }

    public void refillHP() {
        HP = MAX_HP;
    }

    public void refillMP() {
        MP = MAX_MP;
    }

    public double getMaxHP() {
        return MAX_HP;
    }

    public double getMaxMP() { return MAX_MP; }

    public void setMaxHP(double HP) {
        this.MAX_HP = HP;
    }

    public void setMaxMP(double MP) { this.MAX_MP = MP; }

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

    public void addWeapon(WeaponItem item) {
        setCurrentWeapon(item);
    }

    public void strike(CombatSprite attacker, WeaponItem with) {
        Sound.playSound("hit2");
        if (isInvincible)
            return;
        if (isAlly(attacker))
            return;
        double defense = getDefense() + (getCurrentWeapon() != null ? getCurrentWeapon().getWeaponInfo().getDefense() : 0);
        double attack = attacker.getAttack() + with.getWeaponInfo().getAttack();
        double damage;
        damage = (attack / defense) * 12;
        if (damage > 100)
            damage = 100;

        damage = FastMath.fastRound((float) damage);

        applyDamage(damage);
        if (HP <= 0) {
            //TODO Give attacker EXP
        }

    }

    public void applyDamage(double damage) {
        if (isInvincible)
            return;
        HP -= damage;
        toastText("-" + (int)damage).setToastFontSize(32f).setTint(255, 24, 38, 1);
        if (HP <= 0) {
            onDeath();
        }
    }

    protected void onDeath() {
        if (listener != null)
            listener.onDeath(this);

        getWorld().removeSprite(this);
    }

    public abstract boolean isAlly(CombatSprite sprite);

    public void setHP(double HP) {
        this.HP = HP;
    }

    public double getMP() {
        return MP;
    }

    public void setMP(double MP) {
        this.MP = MP;
    }

    public abstract void setSpeed(int speed);

    public abstract void setVigor(int vigor);

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public abstract void setWillpower(int willpower);

    public abstract void setFocus(int focus);

    public abstract void setMarksmanship(int marksmanship);

    public void onAttack() {

    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public StatusEffect[] getStatusEffects() {
        return effects.toArray(new StatusEffect[effects.size()]);
    }

    public boolean isDead() {
        return HP == 0.0;
    }

    public boolean isDodging() {
        return is_dodging;
    }

    public boolean isIs_dodging() {
        return is_dodging;
    }

    public void setIs_dodging(boolean is_dodging) {
        this.is_dodging = is_dodging;
    }

    public boolean canDodge() {
        return allow_dodge;
    }

    public void setIsInvincible(boolean isInvincible) {
        this.isInvincible = isInvincible;
    }

    public boolean isInvincible() {
        return isInvincible;
    }

    public void setInvincible(boolean isInvincible) {
        this.isInvincible = isInvincible;
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

    public interface CombatSpriteEvents {
        public void onDeath(CombatSprite sprite);
    }
}
