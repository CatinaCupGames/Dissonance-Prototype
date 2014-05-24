package com.dissonance.game.sprites.outside;

import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Players;
import com.dissonance.game.sprites.ImagePhysicsSprite;

public class Building5 extends ImagePhysicsSprite {
    public Building5() {
        super("sprites/buildings/building5.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "sprites/buildings/building5.txt";
    }
    @Override
    public void onLoad() {
        super.onLoad();

        setCutOffMargin(-60f);
    }

    @Override
    public void render() {
        update();
        super.render();
    }

    private void update() {
        setAlpha(1f);
        PlayableSprite[] sprites = Players.getCurrentlyPlayingSprites();
        for (PlayableSprite sprite : sprites) {
            float x = sprite.getX();
            float y = sprite.getY();
            float minx = getX() - (getWidth() / 2f);
            float miny = getY() - (getHeight() / 2f);
            float maxx = getX() + 176f;
            float maxy = getY() + 349f;

            if (x <= maxx && x >= minx
                    && y <= maxy && y >= miny) {
                setAlpha(0.4f);
            }
        }
    }

    @Override
    public boolean neverClip() {
        return true;
    }
}
