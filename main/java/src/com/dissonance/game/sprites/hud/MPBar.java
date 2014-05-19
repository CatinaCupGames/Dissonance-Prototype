package com.dissonance.game.sprites.hud;

import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.Texture;

import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class MPBar extends AbstractUI {
    public MPBar(BaseHUD parent) {
        super(parent);
        tip = new MPTip(this);
    }

    private static Texture texture;
    private double displayMP = 100f;
    private double actualMP = 100f;
    private double maxMP = 100f;
    private MPTip tip;

    //Ease stuff
    private boolean ease;
    private double target;
    private double startH;
    private long start;
    @Override
    protected void onOpen() {
        try {
            if (texture == null)
                texture = Texture.retrieveTexture("sprites/menu/player_hud/mbar.png");

            setWidth(texture.getTextureWidth());
            setHeight(texture.getTextureHeight());

            alignToTexture(texture);

            marginLeft(50f);
            marginBottom(19f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (tip.isOpened())
            tip.close();

        tip.display(world);
        setMP(maxMP);
    }

    @Override
    protected void onClose() { }

    @Override
    public void update() {
        if (ease) {
            long since = RenderService.getTime() - start;
            float temp = Camera.ease((float)startH, (float)target, 500f, since);
            _setDisplayMP(temp);
            if (temp == target)
                ease = false;
        }
    }

    public void setMP(double MP) {
        if (MP < 0)
            MP = 0;
        if (MP > maxMP)
            MP = maxMP;
        if (target == MP && ease) return;

        ease = true;
        target = (MP / maxMP) * 100;
        startH = this.displayMP;
        start = RenderService.getTime();

        this.actualMP = MP;

    }

    private void _setDisplayMP(float MP) {
        this.displayMP = MP;
        if (MP <= 0 || MP >= maxMP)
            tip.setVisible(false);
        else {
            tip.setVisible(true);
            tip.marginRight(-0.00001690555f * (MP * MP) + -1.160275634f * MP + 127.1552738f);
        }
    }

    public double getMP() {
        return actualMP;
    }

    public double getMaxMP() { return maxMP; }

    public void setMaxMP(double max) {
        this.maxMP = max;
    }

    @Override
    public void setAlpha(float alpha) {
        super.setAlpha(alpha);
        tip.setAlpha(alpha);
    }

    @Override
    public void onRender() {
        if (texture == null)
            return;
        float x = getX(), y = getY(), bx = getWidth() / 2f, by = getHeight() / 2f, z = 0;

        double percent = bx * (-.02f * displayMP + 2);

        texture.bind();
        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex3f(x - bx, y - by, z);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex3d(x + (bx - percent), y - by, z);
        glTexCoord2f(1f, 1f); //top right
        glVertex3d(x + (bx - percent), y + by, z);
        glTexCoord2f(0f, 1f); //top left
        glVertex3f(x - bx, y + by, z);
        glEnd();
        texture.unbind();
    }
}
