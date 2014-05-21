package com.dissonance.game.sprites;

import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.render.texture.Texture;

import java.io.IOException;

public class ImageSprite extends Sprite {
    private String image;
    public ImageSprite(String image) {
        this.image = image;
    }

    @Override
    public void onLoad() {
        try {
            setTexture(Texture.retrieveTexture(image));
            setCutOffMargin(getTexture().getImageHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
