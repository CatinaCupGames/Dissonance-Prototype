package com.dissonance.game.triggers;

import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.world.tiled.impl.AbstractTrigger;

/**
 * Created by john on 5/23/14.
 */
public class EnterFactory extends AbstractTrigger {
    @Override
    protected void onTrigger(PlayableSprite sprite) throws Throwable {

    }

    @Override
    protected long triggerTimeout() {
        return 0;
    }
}
