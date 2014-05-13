package com.dissonance.framework.game.player;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.player.input.InputKeys;
import com.dissonance.framework.game.player.input.joypad.Joypad;
import com.dissonance.framework.render.Camera;
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
    public boolean isMoving(PlayableSprite player) {
        if (player.isPlayer1()) {
            boolean value = Input.KEYBOARD.isMoving(player); //Check the keyboard as well as the gamepad.
            if (value)
                return true;
        }
        Vector2f values = new Vector2f(InputKeys.getJoypadValue(InputKeys.MOVEX, controller.getController()), InputKeys.getJoypadValue(InputKeys.MOVEY, controller.getController()));
        return values.lengthSquared() >= 0.25f;
    }

    @Override
    public void checkMovement(PlayableSprite playableSprite) {
        playableSprite.w = false;
        playableSprite.s = false;
        playableSprite.d = false;
        playableSprite.a = false;

        if (playableSprite.isPlayer1() && !GameService.coop_mode) {
            Input.KEYBOARD.checkMovement(playableSprite); //Check the keyboard as well as the gamepad.
            if (playableSprite.w || playableSprite.s || playableSprite.d || playableSprite.a)
                return;
        }

        Vector2f values = new Vector2f(InputKeys.getJoypadValue(InputKeys.MOVEX, controller.getController()), InputKeys.getJoypadValue(InputKeys.MOVEY, controller.getController()));
        if (values.lengthSquared() < 0.25f)
            values = new Vector2f(0,0);

        playableSprite.rawSetX(playableSprite.getX() + values.x * (playableSprite.movementSpeed() * RenderService.TIME_DELTA));
        playableSprite.rawSetY(playableSprite.getY() + values.y * (playableSprite.movementSpeed() * RenderService.TIME_DELTA));
        double angle = Math.toDegrees(Math.atan2(-values.y, values.x));

        if (angle < 0)
            angle += 360;
        if (angle != 0 && angle != -0) {
            if (angle > 315 || angle < 45) {
                playableSprite.setFacing(Direction.RIGHT);
                playableSprite.d = true;
            } else if (angle > 255 && angle <= 315) {
                playableSprite.setFacing(Direction.DOWN);
                playableSprite.s = true;
            } else if (angle > 135 && angle <= 225) {
                playableSprite.setFacing(Direction.LEFT);
                playableSprite.a = true;
            } else if (angle >= 45 && angle <= 135) {
                playableSprite.setFacing(Direction.UP);
                playableSprite.w = true;
            }
        }

        if (!playableSprite.ignore_movement) {
            if (values.x == 0 && values.y == 0)
                playableSprite._onNoMovement();
            else
                playableSprite._onMovement(playableSprite.getDirection());
        }
    }

    @Override
    public void checkKeys(PlayableSprite playableSprite) {
        if (playableSprite.isPlayer1() && !GameService.coop_mode)
            Input.KEYBOARD.checkKeys(playableSprite); //Check the keyboard as well as the gamepad.

        Vector2f values = new Vector2f(InputKeys.getJoypadValue(InputKeys.EXTENDX, controller.getController()), InputKeys.getJoypadValue(InputKeys.EXTENDY, controller.getController()));
        if (values.lengthSquared() < 0.03)
            values = new Vector2f(0,0);

        if (!playableSprite.keyboard_extend && values.x == 0f && values.y == 0f || values.x > 0f || values.y > 0f || values.x < 0f || values.y < 0f) {
            Camera.setExtendX(values.x * Camera.MAX_EXTENDX);
            Camera.setExtendY(values.y * Camera.MAX_EXTENDY);
        }
        playableSprite.controller_extend = values.x > 0f || values.y > 0f || values.x < 0f || values.y < 0f;


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

        if (GameService.getCurrentQuest() != null && !playableSprite.usepause) {
            if (controller.isButtonPressed(InputKeys.PAUSE)) {
                AbstractQuest quest = GameService.getCurrentQuest();
                playableSprite.usepause = true;
                if (quest.isPaused())
                    quest.resumeGame();
                else
                    quest.pauseGame();
            }
        } else if (playableSprite.usepause && !controller.isButtonPressed(InputKeys.PAUSE)) playableSprite.usepause = false;
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
