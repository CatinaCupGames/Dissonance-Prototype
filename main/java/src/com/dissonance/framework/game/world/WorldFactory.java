package com.dissonance.framework.game.world;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.debug.Debug;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;
import com.dissonance.framework.system.utils.Validator;
import org.lwjgl.Sys;

import java.util.Arrays;
import java.util.Random;

public class WorldFactory {
    private static final int WORLD_CACHE_LIMIT = 5;
    private static final Random random = new Random();
    private static int worldCount;
    public static final long WORLD_ACCESS_LIMIT_SECONDS = 120L * 1000L;
    private static WorldHolder[] cacheWorlds = new WorldHolder[WORLD_CACHE_LIMIT];
    private static World lastWorld;
    private static World currentWorld;

    /**
     * Get a world by its name <br></br>
     * If the world is not loaded into memory, then the world cache will be cleared and the new {@link World} will be
     * loaded into memory and returned.
     * @param name
     *            The name of the world
     * @return
     *        The {@link World} object
     * @throws WorldLoadFailedException
     *                              If the world fails to load into memory
     */
    public static World getWorld(String name) throws WorldLoadFailedException {
        return getWorld(name, true, null);
    }

    public static World getWorld(String name, WorldLoader toUse) throws WorldLoadFailedException { return getWorld(name, false, toUse); }

    public static World getWorld(String name, boolean cache) throws WorldLoadFailedException { return getWorld(name, cache, null); }

    public static World getWorld(String name, boolean cache, WorldLoader toUse) throws WorldLoadFailedException {
        if (isWorldStillLoaded(name)) {
            for (WorldHolder cacheWorld : cacheWorlds) {
                if (cacheWorld.world.getName().equals(name)) {
                    System.out.println("[World Factory] " + cacheWorld.world.getName() + " returned from cache.");
                    return cacheWorld.world;
                }
            }
        }
        int index = -1;
        if (cache) {
            System.out.println("[World Factory] Cache store requested for new World \"" + name + "\"");
            if (worldCount >= WORLD_CACHE_LIMIT)
                cleanCache(true);

            index = nextOpenSpot();
            if (nextOpenSpot() == -1) {
                cleanCache(true);
                index = nextOpenSpot();
                if (index == -1)
                    throw new WorldLoadFailedException("World Cache is full!");
            }
        }

        World w = new World(index == -1 ? random.nextInt() : index);
        w.init();
        w.load(name, toUse);
        System.out.println("[World Factory] " + w.getName() + " loaded into memory.");
        if (cache) {
            WorldHolder worldHolder = new WorldHolder();
            worldHolder.world = w;
            worldHolder.lastAccess = System.currentTimeMillis();
            worldCount++;
            cacheWorlds[index] = worldHolder;
            System.out.println("[World Factory] Added " + w.getName() + " to factory cache.");
            System.out.println("[World Factory] Placed world in cache index " + index + "/" + WORLD_CACHE_LIMIT);
            return worldHolder.world;
        }

        return w;
    }

    private static int nextOpenSpot() {
        for (int i = 0; i < cacheWorlds.length; i++) {
            if (cacheWorlds[i] == null)
                return i;
        }
        return -1;
    }

    /**
     * Returns whether the {@link World} with the given name is still loaded
     * into memory
     * @param name
     *            The {@link World} name
     * @return
     *        True if its still loaded, false if its not
     */
    public static boolean isWorldStillLoaded(String name) {
        return getWorldHolder(name) != null;
    }

    private static WorldHolder getWorldHolder(String name) {
        if (name == null)
            return null;
        for (WorldHolder cacheWorld : cacheWorlds) {
            if (cacheWorld == null)
                continue;
            if (cacheWorld.world.getName() != null && cacheWorld.world.getName().equals(name))
                return cacheWorld;
        }
        return null;
    }

    private static WorldHolder getWorldHolder(int ID) {
        for (WorldHolder cacheWorld : cacheWorlds) {
            if (cacheWorld == null)
                continue;
            if (cacheWorld.world.getID() == ID)
                return cacheWorld;
        }
        return null;
    }

    public static void swapView(World newWorld, boolean fadetoblack) {
        swapView(newWorld, fadetoblack, true);
    }

    /**
     * Change which world is currently being viewed.
     * @param newworld
     *                The new world to display. This parameter <b>cannot</b> be null.
     */
    public static void swapView(final World newworld, final boolean fadetoblack, final boolean cache) {
        if (RenderService.isInRenderThread()) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    swapView(newworld, fadetoblack, cache);
                }
            }).start();
            return;
        }
        Validator.validateNotNull(newworld, "NewWorld");
        WorldHolder w;
        if (cache) {
            w = getWorldHolder(newworld.getID());
            if (w != null) {
                w.lastAccess = System.currentTimeMillis();
            } else {
                cleanCache(true);
                int index = nextOpenSpot();
                w = new WorldHolder();
                w.lastAccess = System.currentTimeMillis();
                w.world = newworld;
                cacheWorlds[index] = w;
            }
        }
        if (currentWorld != null && !RenderService.INSTANCE.isCrossFading()) {
            w = getWorldHolder(currentWorld.getID());
            if (w != null) {
                w.lastAccess = System.currentTimeMillis();
            }
            currentWorld.onUnload();
        }
        newworld.switchTo(fadetoblack);
        if (!RenderService.INSTANCE.isCrossFading())  {
            RenderService.INSTANCE.runOnServiceThread(new Runnable() {
                @Override
                public void run() {
                    newworld.onDisplay();
                }
            });
        }
        lastWorld = currentWorld;
        currentWorld = newworld;
        System.out.println("[World Factory] New World: " + currentWorld.getName() + ", Old World: " + (lastWorld == null ? "null" : lastWorld.getName()));
        if (Debug.isDebugging()) {
            System.err.println("[World Factory] REQUESTER: " + Arrays.toString(Thread.currentThread().getStackTrace()));
        }
    }

    public static World getCurrentWorld() {
        return currentWorld;
    }

    public static World getLastWorld() {
        return lastWorld;
    }

    /**
     * Clear the {@link World} cache. The <b>force</b> parameter determines whether to remove worlds regardless of when they were
     * last accessed. <br></br>
     * When a {@link World} is disposed, it's {@link com.dissonance.framework.game.world.World#onDispose()} method
     * is invoked. <br></br>
     * If the <b>force</b> parameter is false, then world's will only be removed if there last access property is
     * greater than {@link WorldFactory#WORLD_ACCESS_LIMIT_SECONDS} <br></br>
     * The currently selected {@link World} is never disposed.
     * @param force
     *             Whether to force clearing of all worlds except the currently selected one.
     */
    public static void cleanCache(boolean force) {
        long curr = System.currentTimeMillis();
        boolean removed = false;
        for (int i = 0; i < WORLD_CACHE_LIMIT; i++) {
            if (cacheWorlds[i] == null) continue;
            long since = curr - cacheWorlds[i].lastAccess;
            if (since > WORLD_ACCESS_LIMIT_SECONDS && GameService.getCurrentWorld().getID() != cacheWorlds[i].world.getID()) {
                System.out.println("[World Factory] Disposing " + cacheWorlds[i].world.getName() + ".");
                cacheWorlds[i].world.onDispose();
                cacheWorlds[i] = null;
                removed = true;
                worldCount--;
            }
        }

        if (force && !removed) {
            System.out.println("[World Factory] Force cleanup requested.");
            for (int i = 0; i < WORLD_CACHE_LIMIT; i++) {
                if (cacheWorlds[i] == null)
                    continue;
                if (GameService.getCurrentWorld() == null || GameService.getCurrentWorld().getID() != cacheWorlds[i].world.getID()) {
                    System.out.println("[World Factory] Disposing " + cacheWorlds[i].world.getName() + ".");
                    cacheWorlds[i].world.onDispose();
                    cacheWorlds[i] = null;
                    worldCount--;
                    break;
                }
            }

            System.out.println("[World Factory] Shifting cache");
            int count = 0;
            for (int i = 0; i < WORLD_CACHE_LIMIT; i++) {
                if (cacheWorlds[i] == null)
                    continue;
                count++;
            }
            WorldHolder[] temp = new WorldHolder[count];
            int ii = 0;
            for (int i = 0; i < WORLD_CACHE_LIMIT; i++) {
                if (cacheWorlds[i] == null)
                    continue;
                temp[ii] = cacheWorlds[i];
                cacheWorlds[i] = null;
                ii++;
            }

            System.arraycopy(temp, 0, cacheWorlds, 0, temp.length);
        }
    }

    public static void clearCache() {
        for (int i = 0; i < WORLD_CACHE_LIMIT; i++) {
            if (cacheWorlds[i] == null || GameService.getCurrentWorld().getID() == cacheWorlds[i].world.getID())
                continue;
            System.out.println("[World Factory] Disposing " + cacheWorlds[i].world.getName() + ".");
            cacheWorlds[i].world.onDispose();
            cacheWorlds[i] = null;
            worldCount--;
        }
    }

    private static class WorldHolder {
        public long lastAccess;
        public World world;

        public int hashCode() {
            return (int) lastAccess;
        }

        public boolean equals(Object object) {
            if (object instanceof World) {
                World w = (World)object;
                return w.equals(world);
            } else if (object instanceof WorldHolder) {
                WorldHolder ww = (WorldHolder)object;
                return ww.lastAccess == lastAccess && ww.world.equals(world);
            } else {
                return false;
            }
        }
    }
}
