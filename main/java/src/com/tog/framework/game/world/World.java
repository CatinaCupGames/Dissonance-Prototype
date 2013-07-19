package com.tog.framework.game.world;

import com.tog.framework.game.sprites.Sprite;
import com.tog.framework.render.Texture;
import com.tog.framework.system.ServiceManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class World {
    private final ArrayList<Sprite> sprites = new ArrayList<>();

    Texture temp = null;

    public Iterator<Sprite> getSprites() {
        return sprites.iterator();
    }

    public void uwotm8() {
        ServiceManager.getService("com.tog.framework.render.RenderService").runOnServiceThread(new Runnable() {
            @Override
            public void run() {
                try {
                    temp = Texture.retriveTexture("test.png");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sprites.add(new Sprite() {

                    @Override
                    public Texture getTexture() {
                        return temp;
                    }

                    @Override
                    public float getX() {
                        return 0;
                    }

                    @Override
                    public float getY() {
                        return 0;
                    }
                });
            }
        });

    }

}
