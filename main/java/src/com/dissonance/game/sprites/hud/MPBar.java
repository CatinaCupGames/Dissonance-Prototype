package com.dissonance.game.sprites.hud;

import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.Texture;
import com.sun.accessibility.internal.resources.accessibility;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class MPBar extends AbstractUI {
    public MPBar(BaseHUD parent) {
        super(parent);
        tip = new MPTip(this);
    }

    private static Texture texture;
    private float displayMP = 100f;
    private float actualMP = 100f;
    private MPTip tip;

    //Ease stuff
    private boolean ease;
    private float target;
    private float startH;
    private long start;
    @Override
    protected void onOpen() {
        try {
            if (texture == null)
                texture = Texture.retriveTexture("sprites/menu/player_hud/mbar.png");

            setWidth(texture.getTextureWidth());
            setHeight(texture.getTextureHeight());

            alignToTexture(texture);

            marginLeft(53f);
            marginBottom(68f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (tip.isOpened())
            tip.close();

        tip.display(world);
        setMP(100f);
    }

    @Override
    protected void onClose() { }

    int temp = 0;
    @Override
    public void update() {
        if (ease) {
            long since = RenderService.getTime() - start;
            float temp = Camera.ease(startH, target, 500f, since);
            _setDisplayMP(temp);
            if (temp == target)
                ease = false;
        }

        temp++;
        if (temp % 60 == 0) {
            temp = 0;
            setMP(displayMP - 3);
        }
    }

    public void setMP(float MP) {
        if (MP < 0)
            MP = 0;
        if (MP > 100)
            MP = 100;
        if (target == MP) return;

        ease = true;
        target = MP;
        startH = this.displayMP;
        start = RenderService.getTime();

        this.actualMP = target;

    }

    private void _setDisplayMP(float MP) {
        this.displayMP = MP;
        if (MP <= 0 || MP >= 100)
            tip.setVisible(false);
        else {
            tip.setVisible(true);
            tip.marginRight(-0.00001690555f * (MP * MP) + -1.160275634f * MP + 127.1552738f);
        }
    }

    public float getMP() {
        return actualMP;
    }

    @Override
    public void render() {
        if (texture == null)
            return;
        float x = getX(), y = getY(), bx = getWidth() / 2f, by = getHeight() / 2f, z = 0;

        float percent = bx * (-.02f * displayMP + 2);

        texture.bind();
        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex3f(x - bx, y - by, z);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex3f(x + (bx - percent), y - by, z);
        glTexCoord2f(1f, 1f); //top right
        glVertex3f(x + (bx - percent), y + by, z);
        glTexCoord2f(0f, 1f); //top left
        glVertex3f(x - bx, y + by, z);
        glEnd();
        texture.unbind();
    }
}
