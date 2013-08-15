package com.tog.framework.game.sprites.impl;

import com.tog.framework.game.sprites.AnimatedSprite;
import com.tog.framework.render.Camera;
import com.tog.framework.render.RenderService;
import org.lwjgl.input.Keyboard;

public class Player extends AnimatedSprite {

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        Camera.setX(Camera.translateToCameraCenter(getVector(), 32f, 32f).getX());
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        Camera.setY(Camera.translateToCameraCenter(getVector(), 32f, 32f).getY());
    }

    @Override
    public String getSpriteName() {
        return "player";
    }

    @Override
    public void update() {
        updateInput();

        if (isAnimationPaused() && (w || a || s || d))
            playAnimation();
        else if (!w && !a && !s && !d) {
            setFrame(0);
            pauseAnimation();
        }
    }

    boolean w, a, s, d;
    public void updateInput() {
        w = Keyboard.isKeyDown(Keyboard.KEY_W);
        d = Keyboard.isKeyDown(Keyboard.KEY_D);
        s = Keyboard.isKeyDown(Keyboard.KEY_S);
        a = Keyboard.isKeyDown(Keyboard.KEY_A);

        if (w)
            setY(getY() - (10 * RenderService.TIME_DELTA));
        if (s)
            setY(getY() + (10 * RenderService.TIME_DELTA));
        if (a)
            setX(getX() - (10 * RenderService.TIME_DELTA));
        if (d)
            setX(getX() + (10 * RenderService.TIME_DELTA));
    }


}
