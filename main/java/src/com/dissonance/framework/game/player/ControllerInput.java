package com.dissonance.framework.game.player;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.player.input.InputKeys;
import com.dissonance.framework.game.player.input.joypad.Joypad;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import org.lwjgl.util.vector.Vector2f;

public class ControllerInput implements Input {
    private final Joypad controller;
    private static boolean pause = false;
    private boolean party_switch = false;
    private boolean usespell1, usespell2;
    private boolean use_lock;
    private boolean use_lock_controller;
    private boolean use_attack;
    boolean use_switch;
    private boolean use_select;
    private boolean use_dodge;

    public ControllerInput(Joypad controller) {
        this.controller = controller;
    }

    Controller getController() {
        return controller.getController();
    }

    @Override
    public boolean isMoving(PlayableSprite player) {
        if (player.isPlayer1() && !GameService.coop_mode) {
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
        if (values.lengthSquared() < 0.3f)
            values = new Vector2f(0,0);

        double angle = Math.toDegrees(Math.atan2(-values.y, values.x));

        if (angle < 0)
            angle += 360;

        if (playableSprite.getLocker() != null) {
            playableSprite.setFacingDirection(playableSprite.directionTowards(playableSprite.getLocker()));

            double langle = playableSprite.angleTowards(playableSprite.getLocker());
            float x = values.x;
            float y = values.y;

            if (langle <= 180)
                y = -y;

            values.x = -(float) ((x*Math.cos(Math.toRadians(langle))) + (y*Math.sin(Math.toRadians(langle))));
            values.y = (float) ((x*-Math.sin(Math.toRadians(langle)))+(y*Math.cos(Math.toRadians(langle))));

            playableSprite.rawSetX(playableSprite.getX() + values.y * (playableSprite.movementSpeed() * RenderService.TIME_DELTA));
            playableSprite.rawSetY(playableSprite.getY() + values.x * (playableSprite.movementSpeed() * RenderService.TIME_DELTA));

            if (angle != 0 && angle != -0) {
                if (angle > 315 || angle < 45) {
                    playableSprite.d = true;
                } else if (angle > 255 && angle <= 315) {
                    playableSprite.s = true;
                } else if (angle > 135 && angle <= 225) {
                    playableSprite.a = true;
                } else if (angle >= 45 && angle <= 135) {
                    playableSprite.w = true;
                }
            }
        } else {
            playableSprite.rawSetX(playableSprite.getX() + values.x * (playableSprite.movementSpeed() * RenderService.TIME_DELTA));
            playableSprite.rawSetY(playableSprite.getY() + values.y * (playableSprite.movementSpeed() * RenderService.TIME_DELTA));
            if (angle != 0 && angle != -0) {
                if (angle > 315 || angle < 45) {
                    playableSprite.setFacingDirection(Direction.RIGHT);
                    playableSprite.d = true;
                } else if (angle > 255 && angle <= 315) {
                    playableSprite.setFacingDirection(Direction.DOWN);
                    playableSprite.s = true;
                } else if (angle > 135 && angle <= 225) {
                    playableSprite.setFacingDirection(Direction.LEFT);
                    playableSprite.a = true;
                } else if (angle >= 45 && angle <= 135) {
                    playableSprite.setFacingDirection(Direction.UP);
                    playableSprite.w = true;
                }
            }
        }

        if (!playableSprite.ignore_movement) {
            if (values.x == 0 && values.y == 0)
                playableSprite._onNoMovement();
            else
                playableSprite._onMovement(playableSprite.getFacingDirection());
        }
    }

    @Override
    public void checkKeys(PlayableSprite playableSprite) {
        if (playableSprite != null) {
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

            if (!use_lock_controller) {
                if (controller.isButtonPressed(InputKeys.STRAFE)) {
                    use_lock_controller = true;
                    playableSprite.findLock();
                }
            } else if (!controller.isButtonPressed(InputKeys.STRAFE)) {
                use_lock_controller = false;
                playableSprite.clearLock();
            }

            if (!use_attack && !playableSprite.isDodging() && !playableSprite.isFrozen()) {
                if (controller.isButtonPressed(InputKeys.ATTACK)) {
                    if (playableSprite.getCurrentWeapon() != null) {
                        playableSprite.getCurrentWeapon().use("swipe");
                        playableSprite.ignore_movement = true;
                    }
                    use_attack = true;
                }
            } else if (!controller.isButtonPressed(InputKeys.ATTACK)) use_attack = false;

            if (!use_select) {
                if (controller.isButtonPressed(InputKeys.SELECT)) {
                    playableSprite.checkSelect();
                    use_select = false;
                }
            } else if (!controller.isButtonPressed(InputKeys.SELECT)) use_select = false;

            if (!use_dodge && !playableSprite.isDodging() && playableSprite.canDodge()) {
                if (controller.isButtonPressed(InputKeys.DODGE)) {
                    playableSprite.dodge(playableSprite.getFacingDirection());
                    use_dodge = true;
                }
            } else if (!controller.isButtonPressed(InputKeys.DODGE)) use_dodge = false;

            if (!usespell1 && !playableSprite.isFrozen()) {
                if (controller.isButtonPressed(InputKeys.MAGIC1)) {
                    if (playableSprite.hasSpell1())
                        playableSprite.useSpell1();
                    else {
                        //TODO Play sound
                    }
                    usespell1 = true;
                }
            } else if (!controller.isButtonPressed(InputKeys.MAGIC1)) usespell1 = false;

            if (!usespell2 && !playableSprite.isFrozen()) {
                if (controller.isButtonPressed(InputKeys.MAGIC2)) {
                    if (playableSprite.hasSpell2())
                        playableSprite.useSpell2();
                    else {
                        //TODO Play sound
                    }
                    usespell2 = true;
                }
            } else if (!controller.isButtonPressed(InputKeys.MAGIC2)) usespell2 = false;

        }

        if (GameService.getCurrentQuest() != null && !pause) {
            if (controller.isButtonPressed(InputKeys.PAUSE)) {
                AbstractQuest quest = GameService.getCurrentQuest();
                pause = true;
                if (quest.isPaused())
                    quest.resumeGame();
                else
                    quest.pauseGame();
            }
        } else if (pause && !Players.isAnyPlayerPressingButton(InputKeys.PAUSE)) pause = false;
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
    public boolean isKeyPressed(String key) {
        if (!GameService.coop_mode) {
            boolean value = Input.KEYBOARD.isKeyPressed(key);
            if (value)
                return true;
        }
        controller.getController().poll();
        return controller.isButtonPressed(key);
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
