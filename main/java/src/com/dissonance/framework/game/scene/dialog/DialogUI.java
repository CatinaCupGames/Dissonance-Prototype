package com.dissonance.framework.game.scene.dialog;

import com.dissonance.framework.game.input.InputKeys;
import com.dissonance.framework.game.sprites.UIElement;
import com.dissonance.framework.game.sprites.impl.PlayableSprite;
import com.dissonance.framework.render.Camera;
import org.jbox2d.common.Vec2;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class DialogUI extends UIElement {
    private com.dissonance.framework.game.scene.dialog.Dialog dialog;
    private boolean ended;
    private static Font font;
    private static Font text_font;
    private static Font header_font;
    private static BufferedImage dialog_box;
    private static BufferedImage dialog_header;

    static {
        InputStream in = DialogUI.class.getClassLoader().getResourceAsStream("fonts/term.ttf");
        if (in != null) {
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, in);
                text_font = font.deriveFont(16f);
                header_font = font.deriveFont(12f);
                in.close();
            } catch (FontFormatException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        in = DialogUI.class.getClassLoader().getResourceAsStream("IND/msgbox.png");
        if (in != null) {
            try {
                dialog_box = ImageIO.read(in);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        in = DialogUI.class.getClassLoader().getResourceAsStream("IND/header.png");
        if (in != null) {
            try {
                dialog_header = ImageIO.read(in);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public DialogUI(String name, com.dissonance.framework.game.scene.dialog.Dialog dialog) {
        super(name);
        this.dialog = dialog;
    }

    @Override
    public void draw(Graphics2D graphics2D) {
        graphics2D.drawImage(dialog_box, 0, 0, dialog_box.getWidth(), dialog_box.getHeight(), null);
        if (!dialog.getCurrentHeader().equals("")) {
            graphics2D.drawImage(dialog_header, 0, 0, dialog_header.getWidth(), dialog_header.getHeight(), null);
        }
        graphics2D.setColor(Color.BLACK);
        graphics2D.setFont(text_font);
        graphics2D.drawString(dialog.getCurrentLine(), 10, graphics2D.getFont().getSize2D() * 2);
    }

    @Override
    public void init() {
        if (PlayableSprite.getCurrentlyPlayingSprite() != null) {
            PlayableSprite.getCurrentlyPlayingSprite().freeze();
        }
        setWidth(512);
        setHeight(64);
        Vec2 pos = new Vec2(256, -256);  //I...I'm not quite sure what its doing here..
        pos = Camera.translateToScreenCord(pos);
        setX(pos.x);
        setY(pos.y);
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
                else
                    completelyInvalidateView();
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
