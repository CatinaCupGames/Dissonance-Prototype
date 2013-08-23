package com.tog.framework.game.world;

import com.tog.framework.game.GameService;
import com.tog.framework.system.exceptions.WorldLoadFailedException;
import com.tog.framework.system.utils.Validator;

public class WorldFactory {
    private static final int WORLD_CACHE_LIMIT = 5;
    private static int worldCount;
    private static final int WORLD_ACCESS_LIMIT_SECONDS = 60 * 2;
    private static WorldHolder[] cacheWorlds = new WorldHolder[WORLD_CACHE_LIMIT];

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
        if (isWorldStillLoaded(name)) {
            for (WorldHolder cacheWorld : cacheWorlds) {
                if (cacheWorld.world.getName().equals(name))
                    return cacheWorld.world;
            }
        }

        if (worldCount >= WORLD_CACHE_LIMIT) {
            cleanCache(true);
        }

        int index = nextOpenSpot();
        if (nextOpenSpot() == -1) {
            cleanCache(true);
            index = nextOpenSpot();
            if (index == -1)
                throw new WorldLoadFailedException("World Cache is full!");
        }
        World w = new World(index);
        w.init();
        w.load(name);
        WorldHolder worldHolder = new WorldHolder();
        worldHolder.world = w;
        worldHolder.lastAccess = System.currentTimeMillis();
        worldCount++;
        cacheWorlds[index] = worldHolder;


        return worldHolder.world;
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

    public static void swapView(World old, World newworld) {
        Validator.validateNotNull(newworld, "NewWorld");
        if (old != null) {
            WorldHolder w = getWorldHolder(old.getID());
            if (w != null) {
                w.lastAccess = System.currentTimeMillis();
            }
            old.onUnload();
        }
        WorldHolder w = getWorldHolder(newworld.getID());
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
        newworld.switchTo();
    }

    public static void cleanCache(boolean force) {
        long curr = System.currentTimeMillis();
        boolean removed = false;
        for (int i = 0; i < WORLD_CACHE_LIMIT; i++) {
            long since = curr - cacheWorlds[i].lastAccess;
            if ((since / 1000) > WORLD_ACCESS_LIMIT_SECONDS && GameService.getCurrentWorld().getID() != cacheWorlds[i].world.getID()) {
                cacheWorlds[i].world.onDispose();
                cacheWorlds[i] = null;
                removed = true;
                worldCount--;
            }
        }

        if (force) {
            if (!removed) {
                for (int i = 0; i < WORLD_CACHE_LIMIT; i++) {
                    long since = curr - cacheWorlds[i].lastAccess;
                    if (GameService.getCurrentWorld().getID() != cacheWorlds[i].world.getID()) {
                        cacheWorlds[i].world.onDispose();
                        cacheWorlds[i] = null;
                        worldCount--;
                        break;
                    }
                }
            }
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
