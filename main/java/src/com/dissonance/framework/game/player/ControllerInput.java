package com.dissonance.framework.game.player;

import com.dissonance.framework.game.input.InputKeys;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;
import net.java.games.input.Controller;
import org.lwjgl.util.vector.Vector2f;

public class ControllerInput implements Input {
    private Controller controller;
    public ControllerInput(Controller controller) {
        this.controller = controller;
    }
    
    @Override
    public void checkMovement(PlayableSprite playableSprite) {
        Vector2f values = new Vector2f(InputKeys.getJoypadValue(InputKeys.MOVEX, controller), InputKeys.getJoypadValue(InputKeys.MOVEY, controller));
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
            if (InputKeys.checkController(InputKeys.SWITCH, controller)) {
                playableSprite.use_switch = true;
                playableSprite.setVisible(false);
                //TODO Find next open party member and switch to it.
                /*PlayableSprite next = playableSprite.party.get(PlayableSprite.s_index);
                next.teleportX(playableSprite.getX());
                next.teleportY(playableSprite.getY());
                PlayableSprite.s_index++;
                if (s_index >= next.party.size())
                    s_index = 0;
                next.select();
                next.setVisible(true);*/
            }
        } else if (!InputKeys.checkController(InputKeys.SWITCH, controller) && playableSprite.use_switch) playableSprite.use_switch = false;

        if (!playableSprite.use_attack && !playableSprite.is_dodging) {
            if (InputKeys.checkController(InputKeys.ATTACK, controller)) {
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
        } else if (!InputKeys.checkController(InputKeys.ATTACK, controller)) playableSprite.use_attack = false;

        if (!playableSprite.use_dodge && !playableSprite.is_dodging && playableSprite.allow_dodge) {
            if (InputKeys.checkController(InputKeys.DODGE, controller)) {
                playableSprite.dodge(playableSprite.getDirection());
            }
        } else if (!InputKeys.checkController(InputKeys.DODGE, controller)) playableSprite.use_dodge = false;

        if (!playableSprite.usespell1) {
            if (InputKeys.checkController(InputKeys.MAGIC1, controller)) {
                if (playableSprite.hasSpell1())
                    playableSprite.useSpell1();
                else {
                    //TODO Play sound
                }
                playableSprite.usespell1 = true;
            }
        } else if (!InputKeys.checkController(InputKeys.MAGIC1, controller)) playableSprite.usespell1 = false;

        if (!playableSprite.usespell2) {
            if (InputKeys.checkController(InputKeys.MAGIC2, controller)) {
                if (playableSprite.hasSpell2())
                    playableSprite.useSpell2();
                else {
                    //TODO Play sound
                }
                playableSprite.usespell2 = true;
            }
        } else if (!InputKeys.checkController(InputKeys.MAGIC2, controller)) playableSprite.usespell2 = false;
    }

    @Override
    public String getName() {
        return controller.getName();
    }
}
