package com.dissonance.game.quests;

import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.impl.ToastText;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.LayerType;
import com.dissonance.framework.game.world.tiled.TiledObject;
import com.dissonance.framework.game.world.tiled.impl.TileObject;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.sound.Sound;
import com.dissonance.framework.system.Service;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.game.GameCache;
import com.dissonance.game.scenes.EndScene;
import com.dissonance.game.scenes.FinalFightScene;
import com.dissonance.game.sprites.BlueGuard;
import com.dissonance.game.sprites.menu.Background;
import com.dissonance.game.sprites.menu.Static;
import com.dissonance.game.w.CityEntrySquare2;
import com.dissonance.game.w.DemoLevelWorldLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class BossQuest extends PauseQuest {
    public static boolean END;
    public static boolean RAISE;
    private Service.ServiceRunnable runnable;
    private HashMap<World, TiledObject[]> spawns = new HashMap<World, TiledObject[]>();
    private HashMap<TiledObject, CombatSprite[]> children = new HashMap<TiledObject, CombatSprite[]>();
    private HashMap<TiledObject, Long> timeout = new HashMap<TiledObject, Long>();    private static final long DEFAULT_TIMEOUT = 6000L;
    private static final int DEFAULT_MAX = 4;
    private static final Random random = new Random();
    private static final Class[] TO_SPAWN = new Class[] {
            BlueGuard.class
    };
    @Override
    public void startQuest() throws Exception {
        Camera.stopFollowing();
        WorldFactory.clearCache();
        World world = WorldFactory.getWorld("CityEntrySquare2", false);
        setWorld(world, false);
        world.waitForWorldDisplayed();
        Sound.stopSound("bossfight");
        CityEntrySquare2.farrand.refillHP();
        CityEntrySquare2.farrand.refillMP();
        CityEntrySquare2.jeremiah.refillHP();
        CityEntrySquare2.farrand.refillMP();
        CityEntrySquare2.farrand.freeze(true, BossQuest.class);
        CityEntrySquare2.jeremiah.freeze(true, BossQuest.class);

        CityEntrySquare2.farrand.setUsePhysics(false);
        CityEntrySquare2.jeremiah.setUsePhysics(false);

        CityEntrySquare2.farrand.face(Direction.DOWN);
        CityEntrySquare2.jeremiah.face(Direction.DOWN);

        CityEntrySquare2.farrand.setX(53 * 16);
        CityEntrySquare2.farrand.setY(28 * 16);
        CityEntrySquare2.farrand.setVisible(true);

        CityEntrySquare2.jeremiah.setX(55*16);
        CityEntrySquare2.jeremiah.setY(28*16);
        CityEntrySquare2.jeremiah.setVisible(true);
        CityEntrySquare2.jeremiah.appear();

        playSceneAndWait(FinalFightScene.class);
        Sound.playSound("breakbeat");

        CityEntrySquare2.farrand.setUsePhysics(true);
        CityEntrySquare2.jeremiah.setUsePhysics(true);

        Player player1 = Players.getPlayer1();

        CityEntrySquare2.farrand.unfreeze(BossQuest.class);
        CityEntrySquare2.jeremiah.unfreeze(BossQuest.class);

        Camera.followSprite(player1.getSprite());

        Player player2 = Players.getPlayer(2);
        if (player2 != null && player2.getSprite() != null) {
            player2.getSprite().unfreeze(BossQuest.class);
            Camera.followSprite(player2.getSprite());
        } else {
            if (player1.getSprite().equals(CityEntrySquare2.farrand)) {
                CityEntrySquare2.jeremiah.disappear();
            } else {
                CityEntrySquare2.farrand.disappear();
            }
        }

        CityEntrySquare2.admin1.setHostile(true);
        CityEntrySquare2.admin1.setCombatListener(LISTENER);
        CityEntrySquare2.admin2.setHostile(true);
        CityEntrySquare2.admin2.setCombatListener(LISTENER);
        CityEntrySquare2.admin3.setHostile(true);
        CityEntrySquare2.admin3.setCombatListener(LISTENER);

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

    private void endBoss() throws InterruptedException {
        CityEntrySquare2.farrand.freeze(true, BossQuest.class);
        CityEntrySquare2.jeremiah.freeze(true, BossQuest.class);
        CityEntrySquare2.farrand.setUsePhysics(false);
        CityEntrySquare2.jeremiah.setUsePhysics(false);

        Player player1 = Players.getPlayer1();
        Player player2 = Players.getPlayer(2);
        if (player2 != null && player2.getSprite() != null) {
            player2.getSprite().disappear();
            Thread.sleep(900);
            player2.getSprite().rawSetX(player1.getSprite().getX());
            player2.getSprite().rawSetY(player1.getSprite().getY() + 32);
            player2.getSprite().appear();
            Thread.sleep(900);
        } else {
            if (player1.getSprite().equals(CityEntrySquare2.farrand)) {
                CityEntrySquare2.jeremiah.rawSetX(player1.getSprite().getX());
                CityEntrySquare2.jeremiah.rawSetY(player1.getSprite().getY() + 32);
                CityEntrySquare2.jeremiah.appear();
            } else {
                CityEntrySquare2.farrand.rawSetX(player1.getSprite().getX());
                CityEntrySquare2.farrand.rawSetY(player1.getSprite().getY() + 32);
                CityEntrySquare2.farrand.appear();
            }
        }
        Sound.stopSound("breakbeat");
        playSceneAndWait(EndScene.class);

        RenderService.INSTANCE.fadeToBlack(1f);

        setNextQuest(new MenuQuest());
        try {
            endQuest();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
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
                player1.getSprite().ignore_movement = false;
                player1.getSprite().playAnimation();
                Camera.followSprite(player1.getSprite());

                Player player2 = Players.getPlayer(2);
                if (player2 != null && player2.getSprite() != null) {
                    player2.getSprite().setVisible(true);
                    player2.getSprite().setAttacking(false);
                    player2.getSprite().ignore_movement = false;
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

    @Override
    public String getName() {
        return "The Boss";
    }

    private int death = 0;
    private final CombatSprite.CombatSpriteEvents LISTENER = new CombatSprite.CombatSpriteEvents() {
        @Override
        public void onDeath(CombatSprite sprite) {
            death++;
            if (death >= 3) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            END = true;
                            TileObject.setTileAnimationSpeed(Long.MAX_VALUE);
                            runnable.kill();
                            for (TiledObject obj : children.keySet()) {
                                for (CombatSprite toKill : children.get(obj)) {
                                    getWorld().removeSprite(toKill);
                                }
                            }
                            endBoss();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    };
}
