package com.dissonance.game.sprites;

import com.dissonance.framework.game.ai.astar.Position;
import com.dissonance.framework.game.sprites.impl.PlayableSprite;
import com.dissonance.game.quests.TestQuest;
import org.lwjgl.input.Keyboard;

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

        if (isPlaying()) {
            if (Keyboard.isKeyDown(Keyboard.KEY_8)) {
                System.out.println(getX() + " " + getY());
            } else if (Keyboard.isKeyDown(Keyboard.KEY_9)) {
                TestQuest.xx = getX();
                TestQuest.yy = getY();
                TestQuest.pl.setWaypoint(new Position(getX(), getY()));
            }
        } else {
            if (Keyboard.isKeyDown(Keyboard.KEY_7)) {
                System.out.println(getX() + " " + getY());
            }
        }
    }

    @Override
    public void onDeselect() {
        super.onDeselect();
        setFrame(0);
        pauseAnimation();
    }

    @Override
    public int getLevel() {
        return 1;
    }

    @Override
    public void onLevelUp() {
    }

    @Override
    public int getAttack() {
        return 1;
    }

    @Override
    public int getDefense() {
        return 1;
    }

    @Override
    public int getSpeed() {
        return 1;
    }

    @Override
    public int getVigor() {
        return 1;
    }

    @Override
    public int getStamina() {
        return 1;
    }

    @Override
    public int getWillPower() {
        return 1;
    }

    @Override
    public int getFocus() {
        return 1;
    }

    @Override
    public int getMarksmanship() {
        return 1;
    }

    @Override
    public int getMagicResistance() {
        return 1;
    }

    @Override
    public CombatType getCombatType() {
        return CombatType.ALLY;
    }
}
