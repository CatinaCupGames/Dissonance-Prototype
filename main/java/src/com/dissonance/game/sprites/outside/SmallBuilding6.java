package com.dissonance.game.sprites.outside;

import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Players;
import com.dissonance.game.sprites.ImagePhysicsSprite;

public class SmallBuilding6 extends ImagePhysicsSprite {
    public SmallBuilding6() {
        super("sprites/buildings/smallbuilding6.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "sprites/buildings/smallbuilding6.txt";
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
            float maxx = getX() + Math.abs((getWidth() / 2f) - getTexture().getImageWidth());
            float maxy = getY() + Math.abs((getHeight() / 2f) - getTexture().getImageHeight());
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
