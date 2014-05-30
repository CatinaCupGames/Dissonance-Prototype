package com.dissonance.framework.game.player;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.ai.behaviors.Behavior;
import com.dissonance.framework.game.scene.dialog.DialogUI;
import com.dissonance.framework.game.sprites.Selectable;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.sprites.impl.game.ParticleSprite;
import com.dissonance.framework.game.world.Tile;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.game.world.tiled.TiledObject;
import com.dissonance.framework.game.world.tiled.impl.AbstractTileTrigger;
import com.dissonance.framework.game.world.tiled.impl.AbstractTrigger;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.framework.system.utils.MovementType;
import com.dissonance.framework.system.utils.Timer;
import com.dissonance.framework.system.utils.physics.Collidable;
import com.dissonance.framework.system.utils.physics.HitBox;
import org.lwjgl.util.vector.Vector2f;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class PlayableSprite extends CombatSprite {
    private PlayableSpriteEvent.OnSelectedEvent selectedEvent;
    private PlayableSpriteEvent.OnDeselectedEvent deselectedEvent;
    private Input input;

    private boolean isPlaying = false;
    private MovementType mType = MovementType.RUNNING;
    private boolean frozen;

    boolean controller_extend;
    boolean keyboard_extend;
    boolean use_dodge;
    boolean isAttacking = false;

    ArrayList<PlayableSprite> party = new ArrayList<PlayableSprite>();

    private static PlayableSprite currentlyPlaying;
    private CombatSprite locked;
    public boolean ignore_movement = false;

    private boolean appear;
    private long startTime;
    private float start;
    private float end;
    private Runnable appearRunnable;
    private ParticleSprite.ParticleSource source;

    public PlayableSprite(Input input) {
        super();
        this.input = input;
    }

    public PlayableSprite() {
        super();
    }

    /**
     * Returns whether this sprite is playable or not. A sprite is playable when they have an {@link com.dissonance.framework.game.player.Input}
     * attach to them.
     * @return Whether this sprite is playable or not.
     */
    public boolean isPlayable() {
        return input != null;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public Input getInput() {
        return input;
    }


    @Override
    public void onDeath() {
        if (GameService.getCurrentQuest() != null)
            GameService.getCurrentQuest().playerDied(this);
    }

    public void appear() {
        appear(null);
    }

    public void appear(Runnable runnable) {
        appear = true;

        startTime = System.currentTimeMillis();
        start = getAlpha();
        end = 1f;

        if (source != null)
            source.end();

        source = ParticleSprite.createParticlesAt(getX(), (getY() + getHeight() / 2f), getWorld())
                .setCount(50)
                .setRate(300)
                .setTime(800)
                .setSpeed(4);

        appearRunnable = runnable;
    }

    public void disappear() {
        disappear(null);
    }

    public void disappear(Runnable runnable) {
        appear = true;

        startTime = System.currentTimeMillis();
        start = getAlpha();
        end = 0f;

        if (source != null)
            source.end();

        source = ParticleSprite.createParticlesAt(getX(), (getY() + getHeight() / 2f), getWorld())
                .setCount(50)
                .setRate(150)
                .setTime(800)
                .setSpeed(4);

        appearRunnable = runnable;
    }

    @Override
    public void setX(float x) {
        if (GameService.coop_mode && Camera.isFollowing(this) && (Camera.isOffScreen(x + getWidth(), y, getWidth(), getHeight()) || Camera.isOffScreen(x - getWidth(), y, getWidth(), getHeight())))
            return;
        super.setX(x);
    }

    @Override
    public void setY(float y) {
        if (GameService.coop_mode && Camera.isFollowing(this) && (Camera.isOffScreen(x, y + getHeight(), getWidth(), getHeight()) || Camera.isOffScreen(x, y - getHeight(), getWidth(), getHeight())))
            return;
        super.setY(y);
    }

    @Override
    public void rawSetX(float x) {
        if (GameService.coop_mode && Camera.isFollowing(this) && (Camera.isOffScreen(x + getWidth(), y, getWidth(), getHeight()) || Camera.isOffScreen(x - getWidth(), y, getWidth(), getHeight())))
            return;
        super.rawSetX(x);
    }

    @Override
    public void rawSetY(float y) {
        if (GameService.coop_mode && Camera.isFollowing(this) && (Camera.isOffScreen(x, y + getHeight(), getWidth(), getHeight()) || Camera.isOffScreen(x, y - getHeight(), getWidth(), getHeight())))
            return;
        super.rawSetY(y);
    }

    @Override
    protected void onCollideX(float oldX, float newX, Collidable hit, HitBox hb) {
        if (hit instanceof TiledObject) {
            TiledObject obj = (TiledObject)hit;
            if (obj.isDoor()) {
                _teleport(obj, oldX, -1f);
                return;
            }
        }
        super.onCollideX(oldX, newX, hit, hb);
    }

    @Override
    protected void onCollideY(float oldY, float newY, Collidable hit, HitBox hb) {
        if (hit instanceof TiledObject) {
            TiledObject obj = (TiledObject)hit;
            if (obj.isDoor()) {
                _teleport(obj, -1f, oldY);
                return;
            }
        }
        super.onCollideY(oldY, newY, hit, hb);
    }

    private void _teleport(TiledObject obj, float oldX, float oldY) {
        String target = obj.getDoorTarget();
        if (target.equalsIgnoreCase("")) {
            if (oldX != -1f)
                super.rawSetX(oldX);
            if (oldY != -1f)
                super.rawSetY(oldY);
            return;
        }
        String world = obj.getDoorWorldTarget();
        final World worldObj;
        if (world.equalsIgnoreCase("")) {
            worldObj = getWorld();
        } else {
            try {
                worldObj = WorldFactory.getWorld(world);
            } catch (WorldLoadFailedException e) {
                e.printStackTrace();
                if (oldX != -1f)
                    super.rawSetX(oldX);
                if (oldY != -1f)
                    super.rawSetY(oldY);
                return;
            }
        }

        final TiledObject spawn = worldObj.getSpawn(target);
        if (spawn == null) {
            if (oldX != -1f)
                super.rawSetX(oldX);
            if (oldY != -1f)
                super.rawSetY(oldY);
            return;
        }

        freeze();
        final PlayableSprite[] sprites = Players.getCurrentlyPlayingSprites();
        for (PlayableSprite sprite : sprites) {
            if (sprite == this)
                continue;
            sprite.freeze();
        }
        isTeleporting = true;
        if (worldObj != getWorld()) {
            RenderService.INSTANCE.fadeToBlack(1000);
            WorldFactory.swapView(worldObj, true);
            for (PlayableSprite sprite : sprites) {
                sprite.setWorld(worldObj);
            }
        }

        for (PlayableSprite sprite : sprites) {
            sprite.x = spawn.getX();
            sprite.y = spawn.getY();
        }
        Camera.setPos(Camera.translateToCameraCenter(getVector(), 32));
        while (RenderService.getCurrentAlphaValue() != 1) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }
        }
        for (PlayableSprite sprite : sprites) {
            sprite.unfreeze();
        }
        isTeleporting = false;
    }

    /**
     * Sets this {@link PlayableSprite PlayableSprite's}
     * {@link PlayableSpriteEvent.OnSelectedEvent OnSelectedEvent listener} to the specified listener.
     *
     * @param selectedListener The new event listener.
     */
    public void setSelectedListener(PlayableSpriteEvent.OnSelectedEvent selectedListener) {
        this.selectedEvent = selectedListener;
    }

    /**
     * Sets this {@link PlayableSprite PlayalbeSprite's}
     * {@link PlayableSpriteEvent.OnDeselectedEvent OnDeselectedEvent listener} to the specified listener.
     *
     * @param deselectedListener The new event listener.
     */
    public void setDeselectedListener(PlayableSpriteEvent.OnDeselectedEvent deselectedListener) {
        this.deselectedEvent = deselectedListener;
    }

    public MovementType getMovementType() {
        return mType;
    }

    public void setMovementType(MovementType type) {
        if (type == MovementType.FROZEN) {
            oMType = this.mType;
        }
        this.mType = type;
        if (mType == MovementType.FROZEN)
            freeze();
    }

    public boolean isPlayer1() {
        return player != null && player.getNumber() == 1;
    }

    @Override
    public boolean isAlly(CombatSprite sprite) {
        return sprite == this || (sprite instanceof PlayableSprite && party.contains(sprite));
    }

    @Override
    public boolean isMoving() {
        return input != null && input.isMoving(this);
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean value) {
        this.isAttacking = value;
    }

    @Override
    protected void dodge(Direction direction1) {
        if (frozen)
            return;
        super.dodge(direction1, movementSpeed() * 8.5f);
        freeze();
        if (!is_dodging) {
            unfreeze();
            return;
        }
        ignore_movement = true;
    }

    @Override
    public void update() {
        super.update();
        if (isUpdateCanceled())
            return;
        if (isPlaying) {
            input.update();

            checkMovement();
            input.checkKeys(this);
        }

        if (appear) {
            float value = Camera.ease(start, end, 800, (System.currentTimeMillis() - startTime));
            setAlpha(value);
            if (value == end) {
                appear = false;
                if (source != null) source.end();

                source = null;

                if (appearRunnable != null)
                    appearRunnable.run();

                if (end == 0f)
                    setVisible(false);
                else
                    setVisible(true);
            }
        }
    }

    protected float movementSpeed() {
        switch (mType) {
            case WALKING:
                return 10 + (getSpeed() / 2);
            case RUNNING:
                return 15 + (getSpeed());
            default:
                return 0;
        }
    }

    public void joinParty(PlayableSprite joiner) {
        for (PlayableSprite p : party) {
            if (!p.party.contains(joiner)) {
                p.party.add(joiner); //Add the newcomer to everyone elses party
                p.ignoreCollisionWith(joiner);
            }
            if (!joiner.party.contains(p)) {
                joiner.party.add(p); //Add everyone else to the newcomer's party
                joiner.ignoreCollisionWith(p);
            }
        }
        if (!party.contains(joiner)) {
            party.add(joiner); //Add the newcomer to this players party
            ignoreCollisionWith(joiner);
        }
        if (!joiner.party.contains(this)) {
            joiner.party.add(this); //Add this player to the newcomer's party
            joiner.ignoreCollisionWith(this);
        }
    }

    public PlayableSprite[] getParty() {
        return party.toArray(new PlayableSprite[party.size()]);
    }

    boolean w, a, s, d;
    protected void checkMovement() {
        if (frozen)
            return;
        if (ignore_movement && !isAttacking() && !is_dodging)
            ignore_movement = false;

        input.checkMovement(this);
    }

    @Override
    protected void checkDodge() {
        boolean value = is_dodging;
        super.checkDodge();
        if (value && !is_dodging) {
            unfreeze();
            ignore_movement = false;
            face(dodgeDirection);
        }
    }

    void _onNoMovement() { onNoMovement(); }

    void _onMovement(Direction direction) { onMovement(direction); }

    public void findLock() {
        double lowest = 0;
        CombatSprite lowestC = null;
        Iterator<UpdatableDrawable> sprites = getWorld().getUpdatables();
        while (sprites.hasNext()) {
            UpdatableDrawable d = sprites.next();
            if (d == this)
                continue;
            if (d instanceof CombatSprite) {
                CombatSprite combatSprite = (CombatSprite)d;

                if (isAlly(combatSprite))
                    continue;

                final Vector2f v2 = combatSprite.getVector();
                final Vector2f v1 = getVector();
                double distance = Math.sqrt(((v2.x - v1.x) * (v2.x - v1.x)) + ((v2.y - v1.y) * (v2.y - v1.y)));
                if (distance <= 0.00001)
                    distance = 0;

                if (distance < 150) {
                    if (distance < lowest || lowestC == null) {
                        lowestC = combatSprite;
                        lowest = distance;
                    }
                }
            }
        }

        locked = lowestC;
        if (locked != null) {
            locked.lockOn(this);
        }
    }

    public void clearLock() {
        if (locked != null) {
            locked.removeLock(this);
        }
        locked = null;
    }

    protected boolean checkSelect() {
        if (DialogUI.currentDialogBox() != null)
            return false;
        Iterator<UpdatableDrawable> sprites = getWorld().getUpdatables(); //Sprites will always be Updatable
        while (sprites.hasNext()) {
            UpdatableDrawable d = sprites.next();
            if (d == this)
                continue;
            if (d instanceof Selectable) {
                Selectable sprite = (Selectable)d;
                final Vector2f v2 = sprite.getVector();
                final Vector2f v1 = getVector();
                double distance = Math.sqrt(((v2.x - v1.x) * (v2.x - v1.x)) + ((v2.y - v1.y) * (v2.y - v1.y)));
                if (distance <= 0.00001)
                    distance = 0;

                if (!isFacing(sprite, distance))
                    continue;
                if (distance <= sprite.getDistanceRequired()) {
                    sprite.onSelected(this);
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isFacing(Selectable s, double distance) {
        return getFacingDirection() == directionTowards(new Vector(s.getX(), s.getY()));
    }


    public boolean isFrozen() {
        return frozen;
    }

    private MovementType oMType;
    public void freeze() {
        freeze(false, null);
    }

    private ArrayList<Class<?>> classLockers = new ArrayList<>();

    /**
     * Freeze this sprite and <b>prevent it from receiving input from a controller.</b> <br></br>
     * The <b>lock</b> parameter specifies whether to lock the call to {@link PlayableSprite#unfreeze(Class)} so only the class <b>classLocker</b> can
     * unfreeze the player. <br></br>
     * If another class already has a lock on the freeze, then a "list of locks" is created. In order for the player to unfreeze, all
     * classes that requested the freeze must invoke {@link PlayableSprite#unfreeze(Class)}.
     * If the same class invokes this method with a lock request 2 times, then the same class must invoke {@link PlayableSprite#unfreeze()} 2 times respectively. <br></br>
     * This is behavior is intended for cases such as where
     * one menu class has a lock on the freeze, but then another menu appears and steals the lock. When that menu closes and the old menu remains open, the player will
     * be unfrozen. This is also to prevent one menu class from opening up another menu where that class cannot receive a lock, and when it closes both menus, the player is stuck frozen. <br></br>
     * To unlock the freeze, you <b>MUST</b> call {@link PlayableSprite#unfreeze(Class)} passing the locker class as a parameter.
     * @param lock Whether or not to request a lock to the call to {@link PlayableSprite#unfreeze()}
     * @param classLocker The class that can unlock (unfreeze) the player. This is normally the calling class
     */
    public void freeze(boolean lock, Class<?> classLocker) {
        if (mType != MovementType.FROZEN) {
            this.oMType = this.mType;
            this.mType = MovementType.FROZEN;
        }
        frozen = true;
        onNoMovement();
        if (lock) {
            if (classLocker == null)
                throw new InvalidParameterException("The class locker cannot be null when requesting a lock!");
            classLockers.add(classLocker);
        }
    }

    public boolean unfreeze() {
        return unfreeze(null);
    }

    public boolean unfreeze(Class<?> classLocker) {
        if (classLocker != null) {
            if (classLockers.contains(classLocker))
                classLockers.remove(classLocker);
        }
        if (classLockers.size() == 0) {
            if (this.mType == MovementType.FROZEN) {
                this.mType = this.oMType;
            }
            frozen = false;
        }
        return classLockers.size() == 0;
    }

    private Player player;

    void select(Player player) {
        if (!isPlayable())
            throw new InvalidParameterException("This sprite has no input controller!");

        setBehavior(null);

        if (selectedEvent != null) {
            selectedEvent.onSelectedEvent(this);
        }

        isPlaying = true;

        Camera.followSprite(this);

        this.player = player;
    }

    @Override
    public void setBehavior(Behavior behavior) {
        if (!isPlaying())
            super.setBehavior(behavior);
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * @deprecated This method is deprecated, please read the co-op wiki for more details. This method will be removed once all dependencies are resolved.
     */
    @Deprecated
    public void select() { }

    public void deselect() {
        if (deselectedEvent != null) {
            deselectedEvent.onDeselectedEvent(this);
        }
        Camera.stopFollowing(this);
        onDeselect();
        if (isPlaying)
            throw new RuntimeException("super.onDeselect was not executed! Try putting super.onDeselect at the top of your method!");
    }

    protected void onDeselect() {
        isPlaying = false;
        Camera.setCameraEaseListener(null); //Safety net

        w = false;
        a = false;
        s = false;
        d = false;
        player = null;
        input = null;
    }



    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isSelected() {
        return isPlaying;
    }

    /**
     * @deprecated This method is deprecated, see the coop wiki for more info
     */
    @Deprecated
    public static PlayableSprite getCurrentlyPlayingSprite() {
        return null;
    }

    @Deprecated
    public static void haltMovement() {
        if (currentlyPlaying == null)
            return;
        currentlyPlaying.freeze();
    }

    @Deprecated
    public static void resumeMovement() {
        if (currentlyPlaying == null)
            return;
        currentlyPlaying.freeze();
    }

    private final Camera.CameraMovementListener listener = new Camera.CameraMovementListener() {
        @Override
        public void onMovement(float x, float y, long time) {
        }

        @Override
        public void onMovementFinished() {
            Camera.setCameraEaseListener(null); //Reset listener
            Camera.followSprite(PlayableSprite.this);
        }
    };

    public CombatSprite getLocker() {
        return locked;
    }



    public interface PlayableSpriteEvent {
        /**
         * Interface definition for a callback to be invoked when the {@link PlayableSprite} has been selected by the player.
         */
        public interface OnSelectedEvent {
            public void onSelectedEvent(PlayableSprite sprite);
        }

        /**
         * Interface definition for a callback to be invoked when the {@link PlayableSprite} has been deselected.
         */
        public interface OnDeselectedEvent {
            public void onDeselectedEvent(PlayableSprite sprite);
        }
    }
}
