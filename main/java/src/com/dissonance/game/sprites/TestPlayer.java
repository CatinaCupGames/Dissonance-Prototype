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
    public void onSelected(PlayableSprite sprite) {
        super.onSelected(sprite);
        System.out.println(sprite + " SELECTED MEH!");
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
    public int getAttack() {
        return 0;
    }

    @Override
    public int getDefense() {
        return 0;
    }

    @Override
    public int getSpeed() {
        return 0;
    }

    @Override
    public int getVigor() {
        return 0;
    }

    @Override
    public int getStamina() {
        return 0;
    }

    @Override
    public int getWillPower() {
        return 0;
    }

    @Override
    public int getFocus() {
        return 0;
    }

    @Override
    public int getMarksmanship() {
        return 0;
    }

    @Override
    public int getMagicResistance() {
        return 0;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.ALLY;
    }
}
