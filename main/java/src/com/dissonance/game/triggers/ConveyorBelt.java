package com.dissonance.game.triggers;

import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.world.Tile;
import com.dissonance.framework.game.world.tiled.impl.AbstractTileTrigger;
import com.dissonance.framework.render.RenderService;

public class ConveyorBelt extends AbstractTileTrigger {
    private static final float SPEED = 5f;
    @Override
    public void onTrigger(AnimatedSprite sprite, Tile tile) {
        if (tile.isFlippedHorizontal() && !tile.isFlippedDiegonally() && !tile.isFlippedVertical()) { //Move right
            sprite.teleportX(sprite.getX() + (RenderService.TIME_DELTA * SPEED));
        } else if (tile.isFlippedVertical() && !tile.isFlippedDiegonally() && !tile.isFlippedHorizontal()) { //Move left
            sprite.teleportX(sprite.getX() - (RenderService.TIME_DELTA * SPEED));
        } else if (tile.isFlippedHorizontal() && tile.isFlippedVertical() && !tile.isFlippedDiegonally()) { //Move right
            sprite.teleportX(sprite.getX() + (RenderService.TIME_DELTA * SPEED));
        } else if (tile.isFlippedDiegonally()) {
            if (tile.isFlippedHorizontal() && tile.isFlippedVertical()) { //Move down
                sprite.teleportY(sprite.getY() + (RenderService.TIME_DELTA * SPEED));
            } else if (tile.isFlippedHorizontal()) { //Move up
                sprite.teleportY(sprite.getY() - (RenderService.TIME_DELTA * SPEED));
            } else { //Move down
                sprite.teleportY(sprite.getY() + (RenderService.TIME_DELTA * SPEED));
            }
        } else { //Move left
            sprite.teleportX(sprite.getX() - (RenderService.TIME_DELTA * SPEED));
        }
    }

    @Override
    public long triggerTimeout() {
        return 0;
    }
}