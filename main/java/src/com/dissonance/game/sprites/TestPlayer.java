package com.dissonance.game.sprites;

import com.dissonance.framework.game.sprites.impl.PlayableSprite;

public class TestPlayer extends PlayableSprite {

    @Override
    public void onLoad() {
        super.onLoad();
        setFrame(0);
        pauseAnimation();
    }

    @Override
    public String getSpriteName() {
        return "player";
    }

    @Override
    public void update() {
        super.update();
        if (isAnimationPaused() && (w || a || s || d))
            playAnimation();
        else if (!w && !a && !s && !d) {
            setFrame(0);
            pauseAnimation();
        }
    }

    @Override
    public void onDeselect() {
        super.onDeselect();
        setFrame(0);
        pauseAnimation();
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
