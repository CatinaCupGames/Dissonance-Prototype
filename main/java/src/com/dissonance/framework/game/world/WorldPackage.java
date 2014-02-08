package com.dissonance.framework.game.world;

import com.dissonance.framework.system.exceptions.WorldLoadFailedException;

import java.util.ArrayList;
import java.util.List;

/**
 * Safely load World's into memory without the {@link com.dissonance.framework.game.world.WorldFactory} unloading them!
 */
public class WorldPackage {
    private ArrayList<World> worlds = new ArrayList<World>();
    private WorldPackage() { }

    public static WorldPackage createWorldPackage(String... worlds) {
        WorldPackage worldPackage = new WorldPackage();
        for (String s : worlds) {
            try {
                World w = WorldFactory.getWorld(s, false);
                worldPackage.worlds.add(w);
            } catch (WorldLoadFailedException e) {
                e.printStackTrace();
            }
        }

        return worldPackage;
    }

    public List<World> getWorlds() {
        return worlds;
    }

    public int getWorldCount() {
        return worlds.size();
    }

    public void clear() {
        for (World w : worlds) {
            w.onDispose();
        }
        worlds.clear();
    }
}
