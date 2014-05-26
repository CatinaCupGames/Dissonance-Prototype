package com.dissonance.game.sprites.outside;

import com.dissonance.game.sprites.ImagePhysicsSprite;

public class SmallBuilding2 extends ImagePhysicsSprite {
    public SmallBuilding2() {
        super("sprites/buildings/smallbuilding2.png");
    }

    @Override
    public String hitboxConfigPath() {
        return "sprites/buildings/smallbuilding2.txt";
    }
    /*
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

             if (x <= maxx && x >= minx
                     && y <= maxy && y >= miny) {
                 setAlpha(0.4f);
             }
         }
     }
      */
    @Override
    public boolean neverClip() {
        return true;
    }
}
