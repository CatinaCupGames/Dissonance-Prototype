package com.dissonance.game.sprites.factory;

import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Players;
import com.dissonance.game.quests.GameQuest;
import com.dissonance.game.sprites.ImageSprite;

public class Key extends ImageSprite {
    public Key() {
        super("sprites/img/Keycard.png");
    }

    @Override
    public void render() {
        update();
        super.render();
    }

    private void update() {
        PlayableSprite[] sprites = Players.getCurrentlyPlayingSprites();
        for (PlayableSprite sprite : sprites) {
            float x = sprite.getX();
            float y = sprite.getY();
            float minx = getX() - (getWidth() / 2f);
            float miny = getY() - (getHeight() / 2f);
            float maxx = getX() + 8;
            float maxy = getY() + 4;

            if (x <= maxx && x >= minx
                    && y <= maxy && y >= miny) {
                GameQuest.INSTANCE.unlockedControl = true;

            }
        }
    }
}
