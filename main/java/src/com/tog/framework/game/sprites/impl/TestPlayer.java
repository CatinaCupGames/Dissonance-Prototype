package com.tog.framework.game.sprites.impl;

import com.tog.framework.render.RenderService;
import org.lwjgl.input.Keyboard;

public class TestPlayer extends PlayableSprite {

    @Override
    public void onLoad() {
        super.onLoad();
    }

    @Override
    public String getSpriteName() {
        return "player";
    }

    @Override
    public void update() {
        if (!isPlaying())
            return;
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


    @Override
    public double getAttack() {
        return 0;
    }

    @Override
    public double getDefense() {
        return 0;
    }

    @Override
    public double getSpeed() {
        return 0;
    }

    @Override
    public double getVigor() {
        return 0;
    }

    @Override
    public double getStamina() {
        return 0;
    }

    @Override
    public double getWillPower() {
        return 0;
    }

    @Override
    public double getFocus() {
        return 0;
    }

    @Override
    public double getMarksmanShip() {
        return 0;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.ALLY;
    }
}
