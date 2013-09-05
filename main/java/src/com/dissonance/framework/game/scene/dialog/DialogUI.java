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
import java.util.ArrayList;

public class DialogUI extends UIElement {
    private com.dissonance.framework.game.scene.dialog.Dialog dialog;
    private boolean ended;
    private float cx;
    private float cy;
    private boolean unfreeze;
    private ArrayList<LineText> text = new ArrayList<LineText>();
    private static Font font;
    private static Font text_font;
    private static Font header_font;
    private static BufferedImage dialog_box;
    private static BufferedImage dialog_header;

    static {
        InputStream in = DialogUI.class.getClassLoader().getResourceAsStream("fonts/INFO56_0.ttf");
        if (in != null) {
            try {
                font = Font.createFont(Font.TRUETYPE_FONT, in);
                text_font = font.deriveFont(16f);
                header_font = font.deriveFont(16f);
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
        graphics2D.setColor(Color.WHITE);
        if (!dialog.getCurrentHeader().equals("")) {
            graphics2D.drawImage(dialog_header, 0, 0, dialog_header.getWidth(), dialog_header.getHeight(), null);
            graphics2D.setFont(header_font);
            graphics2D.drawString(dialog.getCurrentHeader(), 10, header_font.getSize2D());
        }
        for (LineText t : text) {
            graphics2D.setFont(t.font);
            graphics2D.drawString(t.text, 10, t.y);
            if (!t.reachedEnd && graphics2D.getFontMetrics().stringWidth(t.text) > 490)
                t.reachedEnd = true;
        }
    }

    @Override
    public void init() {
        unfreeze = !PlayableSprite.getCurrentlyPlayingSprite().isFrozen();
        if (unfreeze && PlayableSprite.getCurrentlyPlayingSprite() != null) {
            PlayableSprite.getCurrentlyPlayingSprite().freeze();
        }
        setWidth(512);
        setHeight(64);
        Vec2 pos = new Vec2(getWidth() / 2, getHeight() / 2);
        pos = Camera.translateToScreenCord(pos);
        setX(pos.x);
        setY(pos.y);
        cx = Camera.getX();
        cy = Camera.getY();
    }

    private boolean pressed;
    private int i;
    private int ii;
    private boolean done = false;
    private String temp = "";
    @Override
    public void update() {
        boolean fast_moving = InputKeys.isButtonPressed(InputKeys.JUMP);

        if (i % (fast_moving ? 3 : 13) == 0 && !done) {
            LineText line;
            if (text.size() == 0) {
                line = new LineText();
                line.font = text_font;
                line.y = (int)(dialog.getCurrentHeader().equals("") ? text_font.getSize2D() : (text_font.getSize2D() * 2) + 5);
                line.text = "";
                text.add(line);
            } else if (!text.get(text.size() - 1).reachedEnd) {
                line = text.get(text.size() - 1);
            } else {
                line = new LineText();
                line.font = text_font;
                line.y = (int)(dialog.getCurrentHeader().equals("") ? text_font.getSize2D() * (text.size() + 1) : ((text_font.getSize2D() * (text.size() + 2)) + 5));
                line.text = "";
                text.add(line);
            }
            if (!temp.equals(dialog.getCurrentLine())) {
                char toadd = dialog.getCurrentLine().toCharArray()[ii];
                line.text += toadd;
                temp += toadd;
                ii++;
                completelyInvalidateView();
            } else {
                done = true;
            }
        }
        i++;
        if (i >= 500)
            i = 0;

        if (cx != Camera.getX() || cy != Camera.getY()) {
            Vec2 pos = new Vec2(getWidth() / 2, getHeight() / 2);
            pos = Camera.translateToScreenCord(pos);
            setX(pos.x);
            setY(pos.y);
            cx = Camera.getX();
            cy = Camera.getY();
            //completelyInvalidateView();
        }

        if (!pressed) {
            pressed = InputKeys.isButtonPressed(InputKeys.ATTACK) || InputKeys.isButtonPressed(InputKeys.JUMP);
            if (pressed && done) {
                boolean finished = dialog.advanceDialog();
                temp = "";
                text.clear();
                done = false;
                i = 0;
                ii = 0;
                if (finished)
                    endDialog();
                else
                    completelyInvalidateView();
            }
        } else if (!InputKeys.isButtonPressed(InputKeys.ATTACK) && !InputKeys.isButtonPressed(InputKeys.JUMP)) {
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
        if (unfreeze && PlayableSprite.getCurrentlyPlayingSprite() != null) {
            PlayableSprite.getCurrentlyPlayingSprite().unfreeze();
        }
    }

    private synchronized void doWakeUp() {
        super.notifyAll();
    }


    private class LineText {
        public String text;
        public int y;
        public Font font;
        public boolean reachedEnd;
    }
}
