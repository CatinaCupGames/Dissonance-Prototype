package com.dissonance.framework.game.player;

import com.dissonance.framework.game.AbstractQuest;
import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.player.input.InputKeys;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;

public class KeyboardInput implements Input {
    private static boolean pause;
    static int p_index;
    private boolean party_switch = false;
    private boolean usespell1, usespell2;
    private boolean use_lock;
    private boolean use_lock_controller;
    private boolean use_attack;
    boolean use_switch;
    private boolean use_select;
    private boolean use_dodge;

    KeyboardInput() { }

    @Override
    public boolean isMoving(PlayableSprite player) {
        return InputKeys.checkKeyboard(InputKeys.MOVEUP) ||
                InputKeys.checkKeyboard(InputKeys.MOVERIGHT) ||
                InputKeys.checkKeyboard(InputKeys.MOVEDOWN) ||
                InputKeys.checkKeyboard(InputKeys.MOVELEFT);
    }

    @Override
    public void checkMovement(PlayableSprite playableSprite) {
        float speed = playableSprite.movementSpeed();

        playableSprite.w = InputKeys.checkKeyboard(InputKeys.MOVEUP);
        playableSprite.d = InputKeys.checkKeyboard(InputKeys.MOVERIGHT);
        playableSprite.s = InputKeys.checkKeyboard(InputKeys.MOVEDOWN);
        playableSprite.a = InputKeys.checkKeyboard(InputKeys.MOVELEFT);
        if (playableSprite.w) {
            playableSprite.setY(playableSprite.getY() - (speed * RenderService.TIME_DELTA));
            playableSprite.setFacingDirection(playableSprite.a ? Direction.UP_LEFT : playableSprite.d ? Direction.UP_RIGHT : Direction.UP);
        }
        if (playableSprite.s) {
            playableSprite.setY(playableSprite.getY() + (speed * RenderService.TIME_DELTA));
            playableSprite.setFacingDirection(playableSprite.a ? Direction.DOWN_LEFT : playableSprite.d ? Direction.DOWN_RIGHT : Direction.DOWN);
        }
        if (playableSprite.a) {
            playableSprite.setX(playableSprite.getX() - (speed * RenderService.TIME_DELTA));
            playableSprite.setFacingDirection(playableSprite.w ? Direction.UP_LEFT : playableSprite.s ? Direction.DOWN_LEFT : Direction.LEFT);
        }
        if (playableSprite.d) {
            playableSprite.setX(playableSprite.getX() + (speed * RenderService.TIME_DELTA));
            playableSprite.setFacingDirection(playableSprite.w ? Direction.UP_RIGHT : playableSprite.s ? Direction.DOWN_RIGHT : Direction.RIGHT);
        }

        if (!playableSprite.ignore_movement) {
            if (!playableSprite.w && !playableSprite.d && !playableSprite.s && !playableSprite.a)
                playableSprite._onNoMovement();
            else
                playableSprite._onMovement(playableSprite.getFacingDirection());
        }
    }

    private void checkExtend(PlayableSprite sprite) {
        if (InputKeys.checkKeyboard(InputKeys.EXTENDUP) || InputKeys.checkKeyboard(InputKeys.EXTENDDOWN)) {
            sprite.keyboard_extend = true;
            if (InputKeys.checkKeyboard(InputKeys.EXTENDUP))
                Camera.setExtendY(Camera.getExtendY() - (RenderService.TIME_DELTA * 32f));
            else
                Camera.setExtendY(Camera.getExtendY() + (RenderService.TIME_DELTA * 32f));
        } else if (Camera.getExtendY() != 0f && !sprite.controller_extend) {
            float value = RenderService.TIME_DELTA * 64f;
            if (Math.abs(Camera.getExtendY() - 0f) < 6f)
                Camera.setExtendY(0f);
            else if (Camera.getExtendY() > 0f) {
                if (Camera.getExtendY() - value < 0f)
                    Camera.setExtendY(0f);
                else
                    Camera.setExtendY(Camera.getExtendY() - (RenderService.TIME_DELTA * 64f));
            }
            else {
                if (Camera.getExtendY() + value > 0f)
                    Camera.setExtendY(0f);
                else
                    Camera.setExtendY(Camera.getExtendY() + (RenderService.TIME_DELTA * 64f));
            }
        }

        if (InputKeys.checkKeyboard(InputKeys.EXTENDLEFT) || InputKeys.checkKeyboard(InputKeys.EXTENDRIGHT)) {
            sprite.keyboard_extend = true;
            if (InputKeys.checkKeyboard(InputKeys.EXTENDLEFT))
                Camera.setExtendX(Camera.getExtendX() - (RenderService.TIME_DELTA * 32f));
            else
                Camera.setExtendX(Camera.getExtendX() + (RenderService.TIME_DELTA * 32f));
        } else if (Camera.getExtendX() != 0f && !sprite.controller_extend) {
            float value = RenderService.TIME_DELTA * 64f;
            if (Math.abs(Camera.getExtendX() - 0f) < 6f)
                Camera.setExtendX(0f);
            else if (Camera.getExtendX() > 0f) {
                if (Camera.getExtendX() - value < 0f)
                    Camera.setExtendX(0f);
                else
                    Camera.setExtendX(Camera.getExtendX() - (RenderService.TIME_DELTA * 64f));
            }
            else {
                if (Camera.getExtendX() + value > 0f)
                    Camera.setExtendX(0f);
                else
                    Camera.setExtendX(Camera.getExtendX() + (RenderService.TIME_DELTA * 64f));
            }
        }
    }

    @Override
    public void checkKeys(PlayableSprite playableSprite) {
        if (playableSprite != null) {
            checkExtend(playableSprite);

            if (!use_switch && playableSprite.party.size() > 0) {
                if (InputKeys.checkKeyboard(InputKeys.SWITCH)) {
                    use_switch = true;
                    if (playableSprite.isPlayer1()) {
                        final PlayableSprite next = playableSprite.party.get(p_index);
                        playableSprite.freeze();
                        p_index++;
                        if (p_index >= playableSprite.party.size())
                            p_index = 0;
                        next.freeze();
                        next.setUsePhysics(false);
                        next.rawSetX(playableSprite.getX());
                        next.rawSetY(playableSprite.getY());
                        next.setLayer(playableSprite.getLayer());
                        next.appear(new Runnable() {
                            @Override
                            public void run() {
                                next.setUsePhysics(true); next.unfreeze();
                            }
                        });
                        playableSprite.disappear();
                        playableSprite.getPlayer().changeSprite(next);
                        if (next.getInput() instanceof ControllerInput) {
                            ((ControllerInput)next.getInput()).use_switch = true;
                            if (!GameService.coop_mode)
                                Input.KEYBOARD.use_switch = true;
                        }
                        else
                            ((KeyboardInput)next.getInput()).use_switch = true;
                    }
                }
            } else if (!InputKeys.checkKeyboard(InputKeys.SWITCH) && use_switch) use_switch = false;

            if (!use_lock) {
                if (InputKeys.checkKeyboard(InputKeys.STRAFE)) {
                    use_lock = true;
                    playableSprite.findLock();
                }
            } else if (!InputKeys.checkKeyboard(InputKeys.STRAFE)) {
                use_lock = false;
                playableSprite.clearLock();
            }

            if (!use_attack && !playableSprite.isDodging() && !playableSprite.isFrozen()) {
                if (InputKeys.checkKeyboard(InputKeys.ATTACK)) {
                    if (playableSprite.getCurrentWeapon() != null) {
                        playableSprite.getCurrentWeapon().use("swipe");
                        playableSprite.ignore_movement = true;
                    }
                    use_attack = true;
                }
            } else if (!InputKeys.checkKeyboard(InputKeys.ATTACK)) use_attack = false;

            if (!use_select) {
                if (InputKeys.checkKeyboard(InputKeys.SELECT)) {
                    playableSprite.checkSelect();
                    use_select = false;
                }
            } else if (!InputKeys.checkKeyboard(InputKeys.SELECT)) use_select = false;

            if (!use_dodge && !playableSprite.isDodging() && playableSprite.canDodge()) {
                if (InputKeys.checkKeyboard(InputKeys.DODGE)) {
                    playableSprite.dodge(playableSprite.getFacingDirection());
                    use_dodge = true;
                }
            } else if (!InputKeys.checkKeyboard(InputKeys.DODGE)) use_dodge = false;

            if (!usespell1) {
                if (InputKeys.checkKeyboard(InputKeys.MAGIC1)) {
                    if (playableSprite.hasSpell1())
                        playableSprite.useSpell1();
                    else {
                        //TODO Play sound
                    }
                    usespell1 = true;
                }
            } else if (!InputKeys.checkKeyboard(InputKeys.MAGIC1)) usespell1 = false;

            if (!usespell2) {
                if (InputKeys.checkKeyboard(InputKeys.MAGIC2)) {
                    if (playableSprite.hasSpell2())
                        playableSprite.useSpell2();
                    else {
                        //TODO Play sound
                    }
                    usespell2 = true;
                }
            } else if (!InputKeys.checkKeyboard(InputKeys.MAGIC2)) usespell2 = false;
        }

        if (GameService.getCurrentQuest() != null && !pause) {
            if (InputKeys.checkKeyboard(InputKeys.PAUSE)) {
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
        return "Keyboard";
    }

    @Override
    public void update() { } //Do nothing

    @Override
    public boolean isKeyPressed(String key) {
        return InputKeys.checkKeyboard(key);
    }
}
