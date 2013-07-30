package com.tog.framework.game.sprites.impl;

import com.tog.framework.game.sprites.Sprite;
import com.tog.framework.render.texture.Texture;
import com.tog.framework.render.texture.sprite.SpriteTexture;
import com.tog.framework.system.Game;
import com.tog.framework.system.ticker.Tick;
import org.lwjgl.util.vector.Vector2f;

import java.security.InvalidParameterException;

import static org.lwjgl.opengl.GL11.*;

public abstract class AnimatedSprite extends Sprite implements Tick {

    @Override
    public void setTexture(Texture texture) {
        if (texture instanceof SpriteTexture) {
            super.setTexture(texture);
        } else
            throw new InvalidParameterException("An AnimatedSprite can only have a SpriteTexture!");
    }

    @Override
    public void onLoad() {
        Game.getSystemTicker().addTick(this);
    }

    public abstract String getSpriteName();

    @Override
    public void render() {
        if (getTexture() == null)
            return;
        final SpriteTexture texture = (SpriteTexture)getTexture();
        texture.bind();
        float bx = texture.getWidth();
        float by = texture.getHeight();
        final float x = getX(), y = getY();
        //glColor4f(0f, 0f, 0f, 1f); //DEBUG LINE FOR TEXTURES
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        Vector2f bl = texture.getTextureCord(SpriteTexture.BOTTOM_LEFT);
        Vector2f br = texture.getTextureCord(SpriteTexture.BOTTOM_RIGHT);
        Vector2f ur = texture.getTextureCord(SpriteTexture.TOP_RIGHT);
        Vector2f ul = texture.getTextureCord(SpriteTexture.TOP_LEFT);
        System.out.println("(" + bx + ":" + by + ") " + bl + " " + br + " " + ur + " " + ul);
        glBegin(GL_QUADS);
        glTexCoord2f(bl.getX(), bl.getY()); //bottom left
        glVertex3f(x - bx, y - by, 0f);
        glTexCoord2f(br.getX(), br.getY()); //bottom right
        glVertex3f(x + bx, y - by, 0f);
        glTexCoord2f(ur.getX(), ur.getY()); //top right
        glVertex3f(x + bx, y + by, 0f);
        glTexCoord2f(ul.getX(), ul.getY()); //top left
        glVertex3f(x - bx, y + by, 0f);
        glEnd();
        texture.unbind();
        glDisable(GL_BLEND);
        //glColor3f(1f, 1f, 1f);
    }

    @Override
    public void tick() {
        if (getTexture() != null && getTexture() instanceof SpriteTexture) {
            ((SpriteTexture)getTexture()).step();
        }
    }

    @Override
    public boolean inSeparateThread() {
        return false;
    }

    @Override
    public int getTimeout() {
        return 1000;
    }
}
