package com.dissonance.framework.game.player;

import com.dissonance.framework.game.player.input.InputKeys;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;

public class KeyboardInput implements Input {

    KeyboardInput() { }

    @Override
    public void checkMovement(PlayableSprite playableSprite) {
        float speed = playableSprite.movementSpeed();

        playableSprite.w = InputKeys.checkKeyboard(InputKeys.MOVEUP);
        playableSprite.d = InputKeys.checkKeyboard(InputKeys.MOVERIGHT);
        playableSprite.s = InputKeys.checkKeyboard(InputKeys.MOVEDOWN);
        playableSprite.a = InputKeys.checkKeyboard(InputKeys.MOVELEFT);
        if (playableSprite.w) {
            playableSprite.setY(playableSprite.getY() - (speed * RenderService.TIME_DELTA));
            playableSprite.setFacing(playableSprite.a ? Direction.UP_LEFT : playableSprite.d ? Direction.UP_RIGHT : Direction.UP);
        }
        if (playableSprite.s) {
            playableSprite.setY(playableSprite.getY() + (speed * RenderService.TIME_DELTA));
            playableSprite.setFacing(playableSprite.a ? Direction.DOWN_LEFT : playableSprite.d ? Direction.DOWN_RIGHT : Direction.DOWN);
        }
        if (playableSprite.a) {
            playableSprite.setX(playableSprite.getX() - (speed * RenderService.TIME_DELTA));
            playableSprite.setFacing(playableSprite.w ? Direction.UP_LEFT : playableSprite.s ? Direction.DOWN_LEFT : Direction.LEFT);
        }
        if (playableSprite.d) {
            playableSprite.setX(playableSprite.getX() + (speed * RenderService.TIME_DELTA));
            playableSprite.setFacing(playableSprite.w ? Direction.UP_RIGHT : playableSprite.s ? Direction.DOWN_RIGHT : Direction.RIGHT);
        }

        if (!playableSprite.ignore_movement) {
            if (!playableSprite.w && !playableSprite.d && !playableSprite.s && !playableSprite.a)
                playableSprite._onNoMovement();
            else
                playableSprite._onMovement(playableSprite.getDirection());
        }
    }

    @Override
    public void checkKeys(PlayableSprite playableSprite) {
        if (!playableSprite.use_switch && playableSprite.party.size() > 0) {
            if (InputKeys.checkKeyboard(InputKeys.SWITCH)) {
                playableSprite.use_switch = true;
                playableSprite.setVisible(false);
                //TODO Find next open party member and switch to it.
                /*PlayableSprite next = playableSprite.party.get(PlayableSprite.s_index);
                next.rawSetX(getX());
                next.rawSetY(getY());
                PlayableSprite.s_index++;
                if (s_index >= next.party.size())
                    s_index = 0;
                next.select();
                next.setVisible(true);*/
            }
        } else if (!InputKeys.checkKeyboard(InputKeys.SWITCH) && playableSprite.use_switch) playableSprite.use_switch = false;

        if (!playableSprite.use_attack && !playableSprite.is_dodging) {
            if (InputKeys.checkKeyboard(InputKeys.ATTACK)) {
                boolean skip = playableSprite.checkSelect();
                if (!skip && playableSprite.getCurrentWeapon() != null) {
                    if (playableSprite.isMoving())
                        playableSprite.getCurrentWeapon().use("stab");
                    else
                        playableSprite.getCurrentWeapon().use("swipe");
                    playableSprite.ignore_movement = true;
                }
                playableSprite.use_attack = true;
            }
        } else if (!InputKeys.checkKeyboard(InputKeys.ATTACK)) playableSprite.use_attack = false;

        if (!playableSprite.use_dodge && !playableSprite.is_dodging && playableSprite.allow_dodge) {
            if (InputKeys.checkKeyboard(InputKeys.DODGE)) {
                playableSprite.dodge(playableSprite.getDirection());
            }
        } else if (!InputKeys.checkKeyboard(InputKeys.DODGE)) playableSprite.use_dodge = false;

        if (!playableSprite.usespell1) {
            if (InputKeys.checkKeyboard(InputKeys.MAGIC1)) {
                if (playableSprite.hasSpell1())
                    playableSprite.useSpell1();
                else {
                    //TODO Play sound
                }
                playableSprite.usespell1 = true;
            }
        } else if (!InputKeys.checkKeyboard(InputKeys.MAGIC1)) playableSprite.usespell1 = false;

        if (!playableSprite.usespell2) {
            if (InputKeys.checkKeyboard(InputKeys.MAGIC2)) {
                if (playableSprite.hasSpell2())
                    playableSprite.useSpell2();
                else {
                    //TODO Play sound
                }
                playableSprite.usespell2 = true;
            }
        } else if (!InputKeys.checkKeyboard(InputKeys.MAGIC2)) playableSprite.usespell2 = false;
    }

    @Override
    public String getName() {
        return "Keyboard";
    }

    @Override
    public void update() { } //Do nothing
}
