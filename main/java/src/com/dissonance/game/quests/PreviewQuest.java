package com.dissonance.game.quests;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.game.sprites.ImageSprite;

public class PreviewQuest extends AbstractQuest {
    private ImageSprite preview;
    public PreviewQuest(ImageSprite preview) {
        this.preview = preview;
    }

    @Override
    public void startQuest() throws Exception {
        World w = WorldFactory.getWorld("test_tileset");
        setWorld(w);
        w.waitForWorldLoaded();
        w.loadAndAdd(preview);
    }

    @Override
    public String getName() {
        return "preview";
    }
}
