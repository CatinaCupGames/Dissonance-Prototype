package com.dissonance.game.sprites.outside;

import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Players;
import com.dissonance.game.sprites.ImagePhysicsSprite;

public class Building2 extends ImagePhysicsSprite {
    public Building2() {
        super("sprites/buildings/building2.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "sprites/buildings/building2.txt";
    }

    @Override
    public void onLoad() {
        super.onLoad();

        setCutOffMargin(-60);
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
            float maxx = getX() + 192f;
            float maxy = getY() + 93f;
            maxy -= 30;
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
