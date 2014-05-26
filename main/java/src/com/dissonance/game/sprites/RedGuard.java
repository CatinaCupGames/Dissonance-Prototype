package com.dissonance.game.sprites;

import com.dissonance.framework.game.combat.Weapon;
import com.dissonance.framework.system.utils.Direction;

public class RedGuard extends Enemy {
    public RedGuard() {
        super("gunguard", StatType.NON_MAGIC, CombatType.HUMAN);
    }


    @Override
    public void onMovement(Direction direction) {
        if (isAttacking())
            return;
        if (isAnimationPaused()) {
            super.setFrame(2);
            playAnimation();
        }

        switch (direction) {
            case UP:
            case UP_LEFT:
            case UP_RIGHT:
                setAnimation("walk_back");
                break;
            case DOWN:
            case DOWN_LEFT:
            case DOWN_RIGHT:
                setAnimation("walk_front");
                break;
            case LEFT:
                setAnimation("walk_left");
                break;
            case RIGHT:
                setAnimation("walk_right");
                break;
        }
    }

    @Override
    public void onNoMovement() {
        if (isMoving() || isAttacking()) {
            return;
        }
        super.setFrame(1);
        pauseAnimation();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        pauseAnimation();

        //TODO Give gun weapon
        setAttack(10);
        setDefense(4);
        setSpeed(10);
        setVigor(6);
        setStamina(8);
        setMarksmanship(12);
    }

    @Override
    public void update() {
        super.update();
        if (isUpdateCanceled())
            return;
        runAI();
    }

    private void runAI() {

    }
}
