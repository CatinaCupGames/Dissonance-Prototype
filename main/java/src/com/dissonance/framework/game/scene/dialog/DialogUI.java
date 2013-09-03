package com.dissonance.framework.game.scene.dialog;

import com.dissonance.framework.game.input.InputKeys;
import com.dissonance.framework.game.sprites.UIElement;
import com.dissonance.framework.game.sprites.impl.PlayableSprite;
import com.dissonance.framework.render.RenderService;

import java.awt.*;

public class DialogUI extends UIElement {
    private com.dissonance.framework.game.scene.dialog.Dialog dialog;
    private boolean ended;

    public DialogUI(String name, com.dissonance.framework.game.scene.dialog.Dialog dialog) {
        super(name);
        this.dialog = dialog;
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.setColor(Color.BLUE);
        graphics2D.fillRect(0, 0, (int) getWidth(), (int) getHeight());
        graphics2D.setColor(Color.BLACK);
        graphics2D.setFont(graphics2D.getFont().deriveFont(12f));
        graphics2D.drawString(dialog.getCurrentHeader(), 10, graphics2D.getFont().getSize2D());
        graphics2D.drawString(dialog.getCurrentLine(), 10, graphics2D.getFont().getSize2D() * 2);
    }

    @Override
    public void init() {
        if (PlayableSprite.getCurrentlyPlayingSprite() != null) {
            PlayableSprite.getCurrentlyPlayingSprite().freeze();
        }
        setWidth(RenderService.GAME_WIDTH / 4);
        setHeight(64);
        /*Vec2 pos = new Vec2(0, -256);
        pos = Camera.translateToScreenCord(pos);
        setX(pos.x);
        setY(pos.y);*/
    }

    private boolean pressed;
    @Override
    public void update() {
        if (!pressed) {
            pressed = InputKeys.isButtonPressed(InputKeys.ATTACK);
            if (pressed) {
                boolean finished = dialog.advanceDialog();
                if (finished)
                    endDialog();
            }
        } else if (!InputKeys.isButtonPressed(InputKeys.ATTACK)) {
            pressed = false;
        }
    }

    public synchronized void waitForEnd() throws InterruptedException {
        while (true) {
            if (ended)
                break;
            super.wait(0L);
        }
    }

    private void endDialog() {
        ended = true;
        close();
        doWakeUp();
        if (PlayableSprite.getCurrentlyPlayingSprite() != null) {
            PlayableSprite.getCurrentlyPlayingSprite().unfreeze();
        }
    }

    private synchronized void doWakeUp() {
        super.notifyAll();
    }
}
