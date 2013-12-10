package com.dissonance.framework.game.scene.dialog;

import com.dissonance.framework.game.GameSettings;
import com.dissonance.framework.game.input.InputKeys;
import com.dissonance.framework.game.sprites.UIElement;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.sound.Sound;
import org.lwjgl.util.vector.Vector2f;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.glColor4f;

public class DialogUI extends UIElement {
    private static DialogUI currentdialog;

    private com.dissonance.framework.game.scene.dialog.Dialog dialog;
    private boolean ended;
    private float cx;
    private float cy;
    private boolean unfreeze;
    private ArrayList<SH> text = new ArrayList<SH>();
    private static Font font;
    static Font text_font;
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
        drawText(graphics2D);
    }

    @Override
    public void init() {
        currentdialog = this;
        setWidth(512);
        setHeight(64);
        Vector2f pos = new Vector2f(GameSettings.Display.window_width / 4, (GameSettings.Display.window_height / 2.0f) - ((getHeight() / 2.0f) + 8));
        pos = Camera.translateToScreenCord(pos);
        setX(pos.x);
        setY(pos.y);
        cx = Camera.getX();
        cy = Camera.getY();
        if (events != null) {
            events.onDialogStarted(dialog);
        }
        funOnTheBun();
    }

    private void funOnTheBun() {
        for (int i = dialog.getIndex(); i < dialog.getAllLines().length; i++) {
            if (i == dialog.getIndex() || dialog.getAllLines()[i].isAppend()) {
                addText(dialog.getAllLines()[i]);
            } else {
                break;
            }
        }
    }

    private void addText(CustomString string) {
        SH ss = new SH();
        ss.s = string;
        ss.line = nextLine(string);
        ss.ID = text.size();
        text.add(ss);
    }

    private int nextLine(CustomString string) {
        String text = string.getString();
        while (text.length() >= 60) {
            char[] temp = text.toCharArray();
            int i = temp.length - 1;
            for (; i >= 0; i--) {
                if (temp[i] == ' ')
                    break;
            }
            text = text.substring(0, i);
        }
        int line = 0;
        while (getTotalChars(line) + text.toCharArray().length >= 60)
            line++;
        return line;
    }

    @Override
    public void render() {
        //The dialog box should always be visible, regardless of the current alpha of the overall
        //display
        glColor4f(1f, 1f, 1f, 1f);
        super.render();
        glColor4f(1f, 1f, 1f, RenderService.getCurrentAlphaValue());
    }

    private boolean pressed;
    private int i;
    private boolean done = false;
    @Override
    public void update() {
        boolean fast_moving = InputKeys.isButtonPressed(InputKeys.JUMP);

        if (i % (fast_moving ? 3 : 13) == 0 && !done) {
            char_offset++;
            completelyInvalidateView();
        }
        i++;
        if (i >= 500)
            i = 0;

        if (cx != Camera.getX() || cy != Camera.getY()) {
            Vector2f pos = new Vector2f(GameSettings.Display.window_width / 4, (GameSettings.Display.window_height / 2.0f) - ((getHeight() / 2.0f) + 8));
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
                boolean finished = false;
                for (SH ignored : text) {
                    finished = dialog.advanceDialog();
                }
                text.clear();
                char_offset = 0;
                line_offset = 0;
                funOnTheBun();
                done = false;
                i = 0;
                if (finished)
                    endDialog();
                else {
                    completelyInvalidateView();
                    if (events != null) {
                        events.onDialogAdvance(dialog);
                    }
                }
            }
        } else if (!InputKeys.isButtonPressed(InputKeys.ATTACK) && !InputKeys.isButtonPressed(InputKeys.JUMP)) {
            pressed = false;
        }
    }

    int char_offset;
    int line_offset;
    public void drawText(Graphics g) {
        ArrayList<SH> used = new ArrayList<SH>();
        int yoffset = (int)(dialog.getCurrentHeader().equals("") ? font.getSize2D() : ((font.getSize2D() * 31) + 5));
        int xoffset = 10;
        for (int i = 0; i <= line_offset; i++) {
            if (i >= 3)
                break;
            if (i == line_offset) {
                int current_char_offset = 0;
                final int total_chars = getTotalChars(i);
                boolean ignore = false;
                while (current_char_offset < char_offset) {
                    SH current = null;
                    for (SH s : text) {
                        if (used.contains(s))
                            continue;
                        if (s.line == i && current == null)
                            current = s;
                        else if (s.line == i && s.ID < current.ID)
                            current = s;
                    }
                    if (current == null && textOnLine(line_offset + 1)) {
                        char_offset = 0;
                        line_offset++;
                        ignore = true;
                        break;
                    } else if (current == null) {
                        done = true;
                        break;
                    }
                    for (int z = 0; z < current.s.getString().toCharArray().length; z++) {
                        if (current_char_offset >= char_offset)
                            break;
                        if (current_char_offset  >= total_chars)
                            break;
                        g.setFont(current.s.getFont());
                        g.drawString("" + current.s.getString().toCharArray()[z], xoffset, yoffset);
                        current_char_offset++;
                        xoffset += g.getFontMetrics().stringWidth("" + current.s.getString().toCharArray()[z]);
                    }
                    used.add(current);
                }
            }
            else {
                while (true) {
                    SH current = null;
                    for (SH s : text) {
                        if (used.contains(s))
                            continue;
                        if (s.line == i && current == null)
                            current = s;
                        else if (s.line == i && s.ID < current.ID)
                            current = s;
                    }
                    if (current == null)
                        break;
                    for (int z = 0; z < current.s.getString().toCharArray().length; z++) {
                        g.setFont(current.s.getFont());
                        g.drawString("" + current.s.getString().toCharArray()[z], xoffset, yoffset);
                        xoffset += g.getFontMetrics().charWidth(current.s.getString().toCharArray()[z]);
                    }
                    used.add(current);
                }
            }
            yoffset += g.getFontMetrics().getHeight();
            xoffset = 10;
        }
    }

    private int getTotalChars(int line) {
        int toreturn = 0;
        for (SH s : text) {
            if (s.line == line)
                toreturn += s.s.getString().toCharArray().length;
        }
        return toreturn;
    }

    private boolean textOnLine(int line) {
        for (SH s : text) {
            if (s.line == line)
                return true;
        }
        return false;
    }

    public synchronized void waitForEnd() throws InterruptedException {
        if (RenderService.isInRenderThread())
            throw new IllegalAccessError("You cant access this method in the render thread!");
        while (true) {
            if (ended)
                break;
            super.wait(0L);
        }
    }
    private static Sound sound_advance;
    public void endDialog() {
        ended = true;
        close();
        doWakeUp();
        currentdialog = null;
        if (events != null) {
            events.onDialogEnded();
        }
    }

    private DialogListener events;
    public void setDialogListener(DialogListener events) {
        this.events = events;
    }

    private synchronized void doWakeUp() {
        super.notifyAll();
    }

    public static DialogUI currentDialogBox() {
        return currentdialog;
    }

    public static interface DialogListener {
        public void onDialogAdvance(Dialog dialog);

        public void onDialogStarted(Dialog dialog);

        public void onDialogEnded();
    }

    private class SH {
        public CustomString s;
        public int line;
        public int ID;

        @Override
        public int hashCode() {
            return ID;
        }
    }
}
