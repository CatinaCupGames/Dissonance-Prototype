package com.dissonance.game.sprites;

import com.dissonance.framework.game.input.InputKeys;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.UpdatableDrawable;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.render.texture.TextureLoader;
import com.dissonance.game.Main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageSprite extends Sprite {

    @Override
    public void onLoad() {
        try {
            BufferedImage image = ImageIO.read(new File(Main.imagePath)); //An outside file must be read like this
            TextureLoader.setFastRedraw(false);
            Texture texture = Texture.convertToTexture("imagePreview", image);
            setTexture(texture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
