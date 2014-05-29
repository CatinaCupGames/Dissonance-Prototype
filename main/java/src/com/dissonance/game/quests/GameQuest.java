package com.dissonance.game.quests;

import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.combat.Weapon;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.impl.ToastText;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.LayerType;
import com.dissonance.framework.game.world.tiled.TiledObject;
import com.dissonance.framework.game.world.tiled.impl.TileObject;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.sound.Sound;
import com.dissonance.framework.system.Service;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.game.GameCache;
import com.dissonance.game.sprites.BlueGuard;
import com.dissonance.game.sprites.Farrand;
import com.dissonance.game.sprites.Jeremiah;
import com.dissonance.game.sprites.menu.Background;
import com.dissonance.game.sprites.menu.Static;
import com.dissonance.game.w.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class GameQuest  extends PauseQuest {
    private static final long DEFAULT_TIMEOUT = 6000L;
    private static final int DEFAULT_MAX = 4;
    private static final Random random = new Random();
    private static final Class[] TO_SPAWN = new Class[] {
            BlueGuard.class
    };
    public static GameQuest INSTANCE;
    public boolean unlockedControl = false;

    private Service.ServiceRunnable runnable;
    private HashMap<World, TiledObject[]> spawns = new HashMap<World, TiledObject[]>();
    private HashMap<TiledObject, CombatSprite[]> children = new HashMap<TiledObject, CombatSprite[]>();
    private HashMap<TiledObject, Long> timeout = new HashMap<TiledObject, Long>();
    public boolean factory_beltsactive;

    @Override
    public void startQuest() throws Exception {
        INSTANCE = this;
        setWorld(GameCache.RoofTopBeginning);
        GameCache.RoofTopBeginning.waitForWorldDisplayed();

        Camera.stopFollowing();

        Sound.stopSound("waldobuilding");

        Sound.playSound("bossfight");

        RoofTopBeginning.farrand.setCurrentWeapon(Weapon.getWeapon("farrandstaff").createItem(RoofTopBeginning.farrand));
        RoofTopBeginning.jeremiah.setCurrentWeapon(Weapon.getWeapon("jeremiahsword").createItem(RoofTopBeginning.jeremiah));
        RoofTopBeginning.farrand.giveSpells();
        RoofTopBeginning.jeremiah.giveSpells();

        Player player1 = Players.createPlayer1();
        if (player1.isPlaying())
            player1.changeSprite(RoofTopBeginning.farrand);
        else
            player1.joinAs(RoofTopBeginning.farrand);

        Player player2 = Players.getPlayer(2);
        if (player2 != null) {
            if (player2.isPlaying())
                player2.changeSprite(RoofTopBeginning.jeremiah);
            else
                player2.join();
        }

        Thread.sleep(700);

        RoofTopBeginning.farrand.face(Direction.RIGHT);
        RoofTopBeginning.jeremiah.face(Direction.LEFT);

        Dialog.displayDialog("LevelStart");

        if (player2 == null) {
            RoofTopBeginning.jeremiah.setMovementSpeed(25);
            RoofTopBeginning.jeremiah.setWaypoint(RoofTopBeginning.farrand.getX(), RoofTopBeginning.farrand.getY(), WaypointType.SIMPLE);
            RoofTopBeginning.jeremiah.disappear();
            RoofTopBeginning.jeremiah.waitForWaypointReached();
            RoofTopBeginning.farrand.unfreeze();
            RoofTopBeginning.jeremiah.unfreeze();
        } else {
            RoofTopBeginning.farrand.unfreeze();
            RoofTopBeginning.jeremiah.unfreeze();
        }
    }

    public void changeToRooftopMid() throws InterruptedException {
        setWorld(GameCache.RooftopMid);
        GameCache.RooftopMid.waitForWorldDisplayed();

        RooftopMid.farrand.freeze();
        RooftopMid.jeremiah.freeze();

        RooftopMid.farrand.setLayer(1);
        RooftopMid.jeremiah.setLayer(1);

        RooftopMid.farrand.setX(8f * 16f);
        RooftopMid.farrand.setY(7f * 16f);
        RooftopMid.jeremiah.setX(7f * 16f);
        RooftopMid.jeremiah.setY(2f * 16f);
        RooftopMid.farrand.face(Direction.RIGHT);
        RooftopMid.jeremiah.face(Direction.RIGHT);
        RenderService.INSTANCE.fadeFromBlack(1300);
        RenderService.INSTANCE.waitForFade();

        RooftopMid.farrand.unfreeze();
        RooftopMid.jeremiah.unfreeze();
    }

    public void changeToOutside1() throws InterruptedException {
        setWorld(GameCache.OutsideFighting);
        GameCache.OutsideFighting.waitForWorldDisplayed();

        OutsideFighting.farrand.setX(27f * 16f);
        OutsideFighting.farrand.setY(240f * 16f);
        OutsideFighting.farrand.setIsInvincible(false);

        OutsideFighting.jeremiah.setX(24f * 16f);
        OutsideFighting.jeremiah.setY(240f * 16f);
        OutsideFighting.jeremiah.setIsInvincible(false);

        Player player1 = Players.getPlayer1();
        player1.getSprite().setVisible(true);
        player1.getSprite().setUsePhysics(true);
        Camera.followSprite(player1.getSprite());

        Player player2 = Players.getPlayer(2);
        if (player2 != null && player2.getSprite() != null) {
            player2.getSprite().setVisible(true);
            player2.getSprite().setUsePhysics(true);
            Camera.followSprite(player2.getSprite());
        }

        RoofTopBeginning.farrand.unfreeze();
        RoofTopBeginning.jeremiah.unfreeze();
        RoofTopBeginning.farrand.setUsePhysics(true);
        RoofTopBeginning.jeremiah.setUsePhysics(true);


        runnable = RenderService.INSTANCE.runOnServiceThread(new Runnable() {
            @Override
            public void run() {
                update();
            }
        }, true, true);
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);

        if (world.equals(GameCache.OutsideFighting)) {
            if (!spawns.containsKey(world)) {
                ArrayList<TiledObject> temp = new ArrayList<>();
                Layer[] layers = world.getLayers(LayerType.OBJECT_LAYER);
                for (Layer l : layers) {
                    for (TiledObject obj : l.getObjectGroupData()) {
                        if (obj.getRawType().equals("espawn"))
                            temp.add(obj);
                    }
                }

                spawns.put(world, temp.toArray(new TiledObject[temp.size()]));
            }
        }
    }

    private void update() {
        TiledObject[] objs = spawns.get(getWorld());
        if (objs == null) return;
        for (TiledObject t : objs) {
            boolean valid;
            if (!Camera.isOffScreen(t.getX(), t.getY(), t.getWidth(), t.getHeight()))
                continue;
            CombatSprite[] sprites = children.get(t);
            if (sprites == null) {
                valid = true;
            } else {
                int i = 0;
                for (CombatSprite s : sprites) {
                    if (s.isDead())
                        i++;
                }
                valid = sprites.length == i;
            }

            if (valid) {
                if (children.containsKey(t))
                    children.remove(t);

                if (timeout.containsKey(t)) {
                    Long l = timeout.get(t);

                    long time = DEFAULT_TIMEOUT;
                    if (t.getProperty("timeout") != null) {
                        time = Long.parseLong(t.getProperty("timeout"));
                    }

                    if (System.currentTimeMillis() - l >= time) {
                        PlayableSprite player1 = Players.getPlayer1().getSprite();
                        if (player1.distanceFrom(new Vector(t.getX(), t.getY())) > 50f * 16f) {
                            continue;
                        }

                        timeout.remove(t);

                        int max = DEFAULT_MAX;
                        if (t.getProperty("spawn_max") != null) {
                            max = Integer.parseInt(t.getProperty("spawn_max"));
                        }

                        int toSpawn = random.nextInt(max) + 1;
                        CombatSprite[] temp = new CombatSprite[toSpawn];
                        for (int i = 0; i < toSpawn; i++) {
                            Class<?> class_ = TO_SPAWN[random.nextInt(TO_SPAWN.length)];
                            Sprite sprite = Sprite.fromClass(class_);

                            float sx = t.getX();
                            float sy = t.getY();
                            float ex = t.getX() + t.getWidth();
                            float ey = t.getY() + t.getHeight();

                            float x = random.nextInt((int) (ex - sx + 1)) + sx;
                            float y = random.nextInt((int) (ey - sy + 1)) + sy;

                            sprite.setX(x);
                            sprite.setY(y);
                            getWorld().loadAndAdd(sprite);
                            temp[i] = (CombatSprite) sprite;
                        }

                        children.put(t, temp);
                    }
                } else {
                    timeout.put(t, System.currentTimeMillis());
                }
            }
        }
    }

    public void turnOnBelts() {
        TileObject.setTileAnimationSpeed(50L);
        GameQuest.INSTANCE.factory_beltsactive = true;
    }

    @Override
    public void endQuest() throws IllegalAccessException {
        super.endQuest();


        if (runnable != null) runnable.kill();
    }

    @Override
    public String getName() {
        return "the actual game";
    }

    @Override
    public void onPlayerDeath(PlayableSprite sprite) {
        Camera.stopFollowing();



        RenderService.INSTANCE.provideData(4400f, RenderService.CROSS_FADE_DURATION);
        RenderService.INSTANCE.provideData(true, RenderService.DONT_UPDATE_TYPE);

        Iterator<UpdatableDrawable> updatableDrawableIterator = getWorld().getUpdatables();
        while (updatableDrawableIterator.hasNext()) {
            UpdatableDrawable ud = updatableDrawableIterator.next();
            if (ud instanceof AnimatedSprite)
                ((AnimatedSprite)ud).pauseAnimation();
            if (ud instanceof ToastText)
                getWorld().removeSprite((ToastText)ud);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Background b = new Background();
                b.setX(1024f / 2f);
                b.setY(512f / 2f);
                b.setLayer(100);
                b.setAlpha(0f);
                getWorld().loadAndAdd(b);

                Static s = new Static();
                s.setX(640f / 2f);
                s.setY(360f / 2f);
                s.setLayer(200);
                getWorld().loadAndAdd(s);
                long start = RenderService.getTime();

                try {
                    s.waitForLoaded();
                    getWorld().invalidateDrawableList();
                    float alpha = 0f;
                    while (alpha < 1f) {
                        alpha = (RenderService.getTime() - start) / 1700f;
                        if (alpha > 1f)
                            alpha = 1f;
                        b.setAlpha(alpha);
                        Thread.sleep(10);
                    }

                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (getWorld().getWorldLoader() instanceof DemoLevelWorldLoader) {
                    DemoLevelWorldLoader.farrand.setUsePhysics(false);
                    DemoLevelWorldLoader.jeremiah.setUsePhysics(false);
                    ((DemoLevelWorldLoader)getWorld().getWorldLoader()).onRespawn(getWorld());
                    DemoLevelWorldLoader.farrand.setUsePhysics(true);
                    DemoLevelWorldLoader.jeremiah.setUsePhysics(true);
                }

                Player player1 = Players.getPlayer1();
                player1.getSprite().setVisible(true);
                player1.getSprite().setAttacking(false);
                player1.getSprite().playAnimation();
                Camera.followSprite(player1.getSprite());

                Player player2 = Players.getPlayer(2);
                if (player2 != null && player2.getSprite() != null) {
                    player2.getSprite().setVisible(true);
                    player2.getSprite().setAttacking(false);
                    player2.getSprite().playAnimation();
                    Camera.followSprite(player2.getSprite());
                }

                try {
                    start = RenderService.getTime();
                    float alpha = 1f;
                    while (alpha > 0f) {
                        alpha = 1f - ((RenderService.getTime() - start) / 1700f);
                        if (alpha < 0f)
                            alpha = 0f;
                        b.setAlpha(alpha);
                        s.setAlpha(alpha);
                        Thread.sleep(10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                getWorld().removeSprite(s);
                getWorld().removeSprite(b);

                RenderService.INSTANCE.provideData(false, RenderService.DONT_UPDATE_TYPE);
                player1.getSprite().unfreeze();
                if (player2 != null && player2.getSprite() != null)
                    player2.getSprite().unfreeze();
            }
        }).start();
    }

    public void changeToFactory() throws InterruptedException {
        Camera.stopFollowing();
        if (runnable != null) { runnable.kill(); runnable = null; }

        setWorld(GameCache.FactoryFloor);
        GameCache.FactoryFloor.waitForWorldDisplayed();
        TileObject.setTileAnimationSpeed(Long.MAX_VALUE); //Stop the animation...I think?

        FactoryFloorCat.farrand.setX(5f * 16f);
        FactoryFloorCat.farrand.setY(208f * 16f);
        FactoryFloorCat.farrand.setLayer(2);
        FactoryFloorCat.farrand.setIsInvincible(false);

        FactoryFloorCat.jeremiah.setX(7f * 16f);
        FactoryFloorCat.jeremiah.setY(208f * 16f);
        FactoryFloorCat.jeremiah.setLayer(2);
        FactoryFloorCat.jeremiah.setIsInvincible(false);

        Player player1 = Players.getPlayer1();
        player1.getSprite().setVisible(true);
        Camera.followSprite(player1.getSprite());

        Player player2 = Players.getPlayer(2);
        if (player2 != null && player2.getSprite() != null) {
            player2.getSprite().setVisible(true);
            Camera.followSprite(player2.getSprite());
        }

        RenderService.INSTANCE.fadeFromBlack(800);
        RenderService.INSTANCE.waitForFade();

        FactoryFloorCat.farrand.setWaypoint(FactoryFloorCat.farrand.getX(), FactoryFloorCat.farrand.getY() - 48, WaypointType.SIMPLE);
        FactoryFloorCat.farrand.setWaypoint(FactoryFloorCat.jeremiah.getX(), FactoryFloorCat.jeremiah.getY() - 48, WaypointType.SIMPLE);
        player1.getSprite().waitForWaypointReached();
        if (player2 != null && player2.getSprite() != null)
            player2.getSprite().waitForWaypointReached();

        FactoryFloorCat.farrand.unfreeze();
        FactoryFloorCat.jeremiah.unfreeze();
    }

    public void changeToOffice1() throws InterruptedException {
        Camera.stopFollowing();
        setWorld(GameCache.OfficeFloor1);
        GameCache.OfficeFloor1.waitForWorldDisplayed();

        OfficeFloor1.farrand.setX(26f * 16f);
        OfficeFloor1.farrand.setY(85f * 16f);

        OfficeFloor1.jeremiah.setX(28f * 16f);
        OfficeFloor1.jeremiah.setY(85f * 16f);


        Player player1 = Players.getPlayer1();
        player1.getSprite().setVisible(true);
        Camera.followSprite(player1.getSprite());

        Player player2 = Players.getPlayer(2);
        if (player2 != null && player2.getSprite() != null) {
            player2.getSprite().setVisible(true);
            Camera.followSprite(player2.getSprite());
        }

        OfficeFloor1.farrand.unfreeze();
        OfficeFloor1.jeremiah.unfreeze();
    }

    public void changeToOffice2() throws InterruptedException {
        Camera.stopFollowing();
        setWorld(GameCache.OfficeFloor2);
        GameCache.OfficeFloor2.waitForWorldDisplayed();

        officefloor2.farrand.setUsePhysics(false);
        officefloor2.jeremiah.setUsePhysics(false);
        officefloor2.farrand.setX(18f * 16f);
        officefloor2.farrand.setY(8f * 16f);

        officefloor2.jeremiah.setX(21f * 16f);
        officefloor2.jeremiah.setY(8f * 16f);


        Player player1 = Players.getPlayer1();
        player1.getSprite().setVisible(true);
        Camera.followSprite(player1.getSprite());

        Player player2 = Players.getPlayer(2);
        if (player2 != null && player2.getSprite() != null) {
            player2.getSprite().setVisible(true);
            Camera.followSprite(player2.getSprite());
        }

        officefloor2.jeremiah.setUsePhysics(true);
        officefloor2.farrand.setUsePhysics(true);

        officefloor2.farrand.unfreeze();
        officefloor2.jeremiah.unfreeze();
    }
}
