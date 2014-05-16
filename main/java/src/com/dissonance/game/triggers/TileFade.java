package com.dissonance.game.triggers;

import com.dissonance.framework.game.ai.astar.FastMath;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.world.Tile;
import com.dissonance.framework.game.world.tiled.Layer;
import com.dissonance.framework.game.world.tiled.impl.AbstractTileTrigger;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.Service;

import java.util.HashMap;

public class TileFade extends AbstractTileTrigger {
    private static HashMap<Layer, Service.ServiceRunnable> uwot = new HashMap<>();

    @Override
    public void onTrigger(final AnimatedSprite sprite, final Tile tile) {
        if (!(sprite instanceof PlayableSprite))
            return;

        int layer = tile.getContainingLayer().getGameLayer(sprite.getWorld());
        if (layer >= sprite.getLayer()) {
            tile.getContainingLayer().setAlpha(0.5f);
            if (uwot.containsKey(tile.getContainingLayer()))
                return;
            Service.ServiceRunnable service = RenderService.INSTANCE.runOnServiceThread(new Runnable() {
                @Override
                public void run() {
                    float x = sprite.getX() + 8.5f;
                    float y = sprite.getY() + (sprite.getHeight() / 2f) - 6f;

                    Tile t = sprite.getWorld().getTileAt(x / 16f, FastMath.fastCeil((y - 8f) / 16f), tile.getContainingLayer());
                    System.out.println("Testing layer " + (tile.getContainingLayer().getGameLayer(tile.getParentWorld())));
                    if (!t.isTriggerTile() || !(t.getTrigger() instanceof TileFade)) {
                        tile.getContainingLayer().setAlpha(1f);
                        uwot.remove(tile.getContainingLayer());
                        RenderService.INSTANCE.removeServiceTick(this);
                        System.out.println("kill");
                    }
                }
            }, true, true);

            uwot.put(tile.getContainingLayer(), service);
        }
    }

    @Override
    public long triggerTimeout() {
        return 0;
    }
}
