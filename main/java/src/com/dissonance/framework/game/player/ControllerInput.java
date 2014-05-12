package com.dissonance.framework.game.player;

import com.dissonance.framework.game.player.input.InputKeys;
import com.dissonance.framework.game.player.input.joypad.Joypad;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;
import net.java.games.input.Controller;
import org.lwjgl.util.vector.Vector2f;

public class ControllerInput implements Input {
    private Joypad controller;
    public ControllerInput(Joypad controller) {
        this.controller = controller;
    }

    Controller getController() {
        return controller.getController();
    }
    
    @Override
    public void checkMovement(PlayableSprite playableSprite) {
        Vector2f values = new Vector2f(InputKeys.getJoypadValue(InputKeys.MOVEX, controller.getController()), InputKeys.getJoypadValue(InputKeys.MOVEY, controller.getController()));
        if (values.lengthSquared() < 0.25f)
            values = new Vector2f(0,0);

        playableSprite.setX(playableSprite.getX() + values.x * (playableSprite.movementSpeed() * RenderService.TIME_DELTA));
        playableSprite.setY(playableSprite.getY() + values.y * (playableSprite.movementSpeed() * RenderService.TIME_DELTA));
        double angle = Math.toDegrees(Math.atan2(-values.y, values.x));
        if (angle < 0)
            angle += 360;
        if (angle != 0 && angle != -0) {
            if (angle > 315 || angle < 45) {
                playableSprite.setFacing(Direction.RIGHT);
            } else if (angle > 255 && angle <= 315) {
                playableSprite.setFacing(Direction.DOWN);
            } else if (angle > 135 && angle <= 225) {
                playableSprite.setFacing(Direction.LEFT);
            } else if (angle >= 45 && angle <= 135) {
                playableSprite.setFacing(Direction.UP);
            }
        }
    }

    @Override
    public void checkKeys(PlayableSprite playableSprite) {
        if (!playableSprite.use_switch && playableSprite.party.size() > 0) {
            if (controller.isButtonPressed(InputKeys.SWITCH)) {
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
        } else if (!controller.isButtonPressed(InputKeys.SWITCH) && playableSprite.use_switch) playableSprite.use_switch = false;

        if (!playableSprite.use_attack && !playableSprite.is_dodging) {
            if (controller.isButtonPressed(InputKeys.ATTACK)) {
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
        } else if (!controller.isButtonPressed(InputKeys.ATTACK)) playableSprite.use_attack = false;

        if (!playableSprite.use_dodge && !playableSprite.is_dodging && playableSprite.allow_dodge) {
            if (controller.isButtonPressed(InputKeys.DODGE)) {
                playableSprite.dodge(playableSprite.getDirection());
            }
        } else if (!controller.isButtonPressed(InputKeys.DODGE)) playableSprite.use_dodge = false;

        if (!playableSprite.usespell1) {
            if (controller.isButtonPressed(InputKeys.MAGIC1)) {
                if (playableSprite.hasSpell1())
                    playableSprite.useSpell1();
                else {
                    //TODO Play sound
                }
                playableSprite.usespell1 = true;
            }
        } else if (!controller.isButtonPressed(InputKeys.MAGIC1)) playableSprite.usespell1 = false;

        if (!playableSprite.usespell2) {
            if (controller.isButtonPressed(InputKeys.MAGIC2)) {
                if (playableSprite.hasSpell2())
                    playableSprite.useSpell2();
                else {
                    //TODO Play sound
                }
                playableSprite.usespell2 = true;
            }
        } else if (!controller.isButtonPressed(InputKeys.MAGIC2)) playableSprite.usespell2 = false;
    }

    @Override
    public String getName() {
        return controller.getController().getName();
    }

    @Override
    public void update() {
        controller.getController().poll();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ControllerInput) {
            ControllerInput input = (ControllerInput)obj;
            return input.controller.equals(controller);
        }
        return false;
    }
}
