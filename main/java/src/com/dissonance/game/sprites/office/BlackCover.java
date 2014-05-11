package com.dissonance.game.sprites.office;

import com.dissonance.framework.render.RenderService;
import com.dissonance.game.sprites.ImageSprite;

import static org.lwjgl.opengl.GL11.glColor4f;

/**
 * Created by Jmerrill on 5/6/2014.
 */
public class BlackCover extends ImageSprite{
    public BlackCover(){
        super("sprites/img/blackcover.png");
    }
    @Override
    public void render(){
        glColor4f(1f,1f,1f,1f);
        super.render();
        glColor4f(1f,1f,1f, RenderService.getCurrentAlphaValue());
    }
}
