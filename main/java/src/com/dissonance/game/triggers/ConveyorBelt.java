package com.dissonance.game.triggers;

import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.world.Tile;
import com.dissonance.framework.game.world.tiled.impl.AbstractTileTrigger;
import com.dissonance.framework.render.RenderService;

public class ConveyorBelt extends AbstractTileTrigger {
    @Override
    public void onTrigger(AnimatedSprite sprite, Tile tile) {
        if (tile.isFlippedHorizontal() && !tile.isFlippedDiegonally() && !tile.isFlippedVertical()) { //Move right
            sprite.setX(sprite.getX() + (RenderService.TIME_DELTA * 5f));
        } else if (tile.isFlippedVertical() && !tile.isFlippedDiegonally() && !tile.isFlippedHorizontal()) { //Move left
            sprite.setX(sprite.getX() - (RenderService.TIME_DELTA * 5f));
        } else if (tile.isFlippedDiegonally()) {
            if (tile.isFlippedHorizontal() && tile.isFlippedVertical()) { //Move down
                sprite.setY(sprite.getY() + (RenderService.TIME_DELTA * 5f));
            } else if (tile.isFlippedHorizontal()) { //Move up
                sprite.setY(sprite.getY() - (RenderService.TIME_DELTA * 5f));
            } else { //Move down
                sprite.setY(sprite.getY() + (RenderService.TIME_DELTA * 5f));
            }
        } else { //Move left
            sprite.setX(sprite.getX() - (RenderService.TIME_DELTA * 5f));
        }
    }

    @Override
    public long triggerTimeout() {
        return 0;
    }
}
