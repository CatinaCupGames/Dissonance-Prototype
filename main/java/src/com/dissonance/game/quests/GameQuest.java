package com.dissonance.game.quests;

import com.dissonance.framework.game.ai.astar.Vector;
import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.combat.Weapon;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.LayerType;
import com.dissonance.framework.game.world.tiled.TiledObject;
import com.dissonance.framework.game.world.tiled.impl.TileObject;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.sound.Sound;
import com.dissonance.framework.system.Service;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.game.GameCache;
import com.dissonance.game.sprites.BlueGuard;
import com.dissonance.game.w.OutsideFighting;
import com.dissonance.game.w.RoofTopBeginning;
import com.dissonance.game.w.RooftopMid;

import java.util.ArrayList;
import java.util.HashMap;
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
        setWorld(GameCache.FactoryFloor);
        GameCache.FactoryFloor.waitForWorldDisplayed();
        TileObject.setTileAnimationSpeed(Long.MAX_VALUE); //Stop the animation...I think?

        Camera.stopFollowing();

        Sound.playSound("bossfight");

        Player player1 = Players.createPlayer1();
        if (player1.isPlaying())
            player1.changeSprite(OutsideFighting.farrand);
        else
            player1.joinAs(OutsideFighting.farrand);

        Player player2 = Players.getPlayer(2);
        if (player2 != null) {
            if (player2.isPlaying())
                player2.changeSprite(OutsideFighting.jeremiah);
            else
                player2.join();
        }

        turnOnBelts();


        RoofTopBeginning.farrand.setCurrentWeapon(Weapon.getWeapon("farrandstaff").createItem(RoofTopBeginning.farrand));
        RoofTopBeginning.jeremiah.setCurrentWeapon(Weapon.getWeapon("jeremiahsword").createItem(RoofTopBeginning.jeremiah));
    }

    public void changeToRooftopMid() throws InterruptedException {
        setWorld(GameCache.RooftopMid);
        GameCache.RooftopMid.waitForWorldDisplayed();

        RenderService.INSTANCE.fadeFromBlack(1300);
        RooftopMid.farrand.setLayer(0);
        RooftopMid.jeremiah.setLayer(0);
        RooftopMid.farrand.setUsePhysics(false);
        RooftopMid.jeremiah.setUsePhysics(false);

        RooftopMid.farrand.setMovementSpeed(8);
        RooftopMid.jeremiah.setMovementSpeed(8);
        RooftopMid.farrand.face(Direction.RIGHT);
        RooftopMid.jeremiah.face(Direction.RIGHT);
        RooftopMid.farrand.setX(3f * 16f);
        RooftopMid.farrand.setY(7f * 16f);
        RooftopMid.jeremiah.setX(2f * 16f);
        RooftopMid.jeremiah.setY(2f * 16f);
        PlayableSprite p1 = Players.getPlayer1().getSprite();
        PlayableSprite p2 = null;
        if (Players.getPlayer(2) != null) p2 = Players.getPlayer(2).getSprite();

        p1.setWaypoint(p1.getX() + (5f * 16f), p1.getY(), WaypointType.SIMPLE);
        if (p2 != null)
            p2.setWaypoint(p2.getX() + (5f * 16f), p2.getY(), WaypointType.SIMPLE);

       /* p1.waitForWaypointReached();
        if (p2 != null)
            p2.waitForWaypointReached();*/


        RooftopMid.farrand.setLayer(1);
        RooftopMid.jeremiah.setLayer(1);
        RooftopMid.farrand.setUsePhysics(true);
        RooftopMid.jeremiah.setUsePhysics(true);
        RooftopMid.farrand.unfreeze();
        RooftopMid.jeremiah.unfreeze();
    }

    public void changeToOutside1() throws InterruptedException {
        setWorld(GameCache.OutsideFighting);
        GameCache.OutsideFighting.waitForWorldDisplayed();

        OutsideFighting.farrand.setX(23f * 16f);
        OutsideFighting.farrand.setY(330f*16f);

        OutsideFighting.jeremiah.setX(24f*16f);
        OutsideFighting.jeremiah.setY(330f*16f);

        Player player1 = Players.getPlayer1();
        player1.getSprite().setVisible(true);
        Camera.followSprite(player1.getSprite());

        Player player2 = Players.getPlayer(2);
        if (player2 != null && player2.getSprite() != null) {
            player2.getSprite().setVisible(true);
            Camera.followSprite(player2.getSprite());
        }
          
        RoofTopBeginning.farrand.unfreeze();
        RoofTopBeginning.jeremiah.unfreeze();


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

    private void update() {
        TiledObject[] objs = spawns.get(getWorld());

        for (TiledObject t : objs) {
            boolean valid = false;
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
}
