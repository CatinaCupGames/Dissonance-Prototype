package com.dissonance.framework.game.scene.dialog;

import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.player.input.InputKeys;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.text.RenderText;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.sound.Sound;
import com.dissonance.framework.system.GameSettings;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.*;

public class DialogUI extends AbstractUI {
    public static Font original = GameSettings.Display.GAME_FONT;
    public static TrueTypeFont font;
    public static TrueTypeFont bold_font;
    public static TrueTypeFont italic_font;
    public static TrueTypeFont bold_italic_font;
    private static Texture texture_background;
    private static Texture texture_header;

    private com.dissonance.framework.game.scene.dialog.Dialog dialog;
    private boolean ended;
    private ArrayList<SH> text = new ArrayList<SH>();
    private long speed = 20L;
    private boolean autoScroll = false;

    public DialogUI(Dialog dialog) {
        this(dialog, false);
    }

    public DialogUI(Dialog dialog, boolean autoScroll) {
        super();
        this.dialog = dialog;
        this.autoScroll = autoScroll;
    }

    @Override
    protected void onRender() {
        glColor4f(1f, 1f, 1f, 1f);

        float x = getX();
        float y = getY();
        float z = 0f;
        float bx = getWidth() / 2f, by = getHeight() / 2f;

        texture_background.bind();
        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex3f(x - bx, y - by, z);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex3f(x + bx, y - by, z);
        glTexCoord2f(1f, 1f); //top right
        glVertex3f(x + bx, y + by, z);
        glTexCoord2f(0f, 1f); //top left
        glVertex3f(x - bx, y + by, z);
        glEnd();
        texture_background.unbind();

        texture_header.bind();
        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex3f(x - bx, y - by, z);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex3f(x + bx, y - by, z);
        glTexCoord2f(1f, 1f); //top right
        glVertex3f(x + bx, y + by, z);
        glTexCoord2f(0f, 1f); //top left
        glVertex3f(x - bx, y + by, z);
        glEnd();
        texture_header.unbind();

        RenderText.drawString(font, dialog.getCurrentHeader(), (x - bx) + 10f, (y - by) + 5f, Color.white);
        drawText(x - bx, y - by);

        glColor4f(1f, 1f, 1f, RenderService.getCurrentAlphaValue());
    }

    int char_offset;
    int line_offset;
    int line_start = 0;
    public void drawText(float x, float y) {
        ArrayList<SH> used = new ArrayList<SH>();
        float yoffset = (dialog.getCurrentHeader().equals("") ? 5f : 26f);
        float xoffset = 10f;
        xoffset = (x + xoffset);
        yoffset = (y + yoffset);
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
                        if (!done) completedWhen = System.currentTimeMillis();
                        done = true;
                        break;
                    }
                    this.speed = current.speed;
                    char[] array = current.s.getString().toCharArray();
                    TrueTypeFont font = DialogUI.font;
                    switch (current.s.getStyle()) {
                        case BOLD:
                            font = DialogUI.bold_font;
                            break;
                        case ITALIC:
                            font = DialogUI.italic_font;
                            break;
                        case BOLD_ITALIC:
                            font = DialogUI.bold_italic_font;
                            break;
                        default:
                            break;
                    }
                    for (char anArray : array) {
                        if (current_char_offset >= char_offset)
                            break;
                        if (current_char_offset >= total_chars)
                            break;
                        RenderText.drawString(font, "" + anArray, xoffset, yoffset, new Color(current.s.getColor().getRed() / 255f, current.s.getColor().getGreen() / 255f, current.s.getColor().getBlue() / 255f));
                        current_char_offset++;
                        xoffset += font.getWidth("" + anArray);
                    }
                    used.add(current);
                    array = null;
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
                    TrueTypeFont font = DialogUI.font;
                    switch (current.s.getStyle()) {
                        case BOLD:
                            font = DialogUI.bold_font;
                            break;
                        case ITALIC:
                            font = DialogUI.italic_font;
                            break;
                        case BOLD_ITALIC:
                            font = DialogUI.bold_italic_font;
                            break;
                        default:
                            break;
                    }
                    char[] array = current.s.getString().toCharArray();
                    for (char anArray : array) {
                        RenderText.drawString(font, "" + anArray, xoffset, yoffset, new Color(current.s.getColor().getRed() / 255f, current.s.getColor().getGreen() / 255f, current.s.getColor().getBlue() / 255f));
                        xoffset += font.getWidth("" + anArray);
                    }
                    used.add(current);
                    array = null;
                }
            }
            yoffset += font.getHeight();
            xoffset = (x + 10);
        }
        used.clear();
    }

    private int getTotalChars(int line) {
        int toreturn = 0;
        for (SH s : text) {
            if (s.line == line)
                toreturn += s.s.getString().length();
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
        ss.speed = string.getSpeed();
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
        while (getTotalChars(line) + text.length() >= 60)
            line++;
        return line;
    }

    @Override
    protected void onOpen() {
        if (font == null) {
            font = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(16f), Font.PLAIN);
            bold_font = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(16f).deriveFont(Font.BOLD), Font.BOLD);
            italic_font = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(16f).deriveFont(Font.ITALIC), Font.ITALIC);
            bold_italic_font = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(16f).deriveFont(Font.BOLD | Font.ITALIC), Font.BOLD | Font.ITALIC);
        }

        if (texture_background == null) {
            try {
                texture_background = Texture.retrieveTexture("IND/msgbox.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (texture_header == null) {
            try {
                texture_header = Texture.retrieveTexture("IND/header.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        setWidth(512);
        setHeight(64);
        centerHorizontal();
        marginBottom(8f);

        funOnTheBun();

        if (events != null) {
            events.onDialogStarted(dialog);
        }

        currentdialog = this;
    }

    private boolean pressed;
    private long lastUpdate = RenderService.getTime();
    private boolean done = false;
    private long completedWhen;
    @Override
    public void update() {
        boolean fast_moving = (Players.isAnyPlayerPressingButton(InputKeys.SELECT)) && !autoScroll;

        long speed = this.speed / (fast_moving ? 2 : 1);
        if (RenderService.getTime() - lastUpdate > speed && !done) {
            lastUpdate = RenderService.getTime();
            char_offset++;
        }

        if (autoScroll) {
            if (done && System.currentTimeMillis() - completedWhen > 1400) {
                next();
            }
        }

        if (!pressed) {
            pressed = Players.isAnyPlayerPressingButton(InputKeys.DODGE) || Players.isAnyPlayerPressingButton(InputKeys.SELECT);
            if (pressed && done) {
                next();
            }
        } else if (!Players.isAnyPlayerPressingButton(InputKeys.DODGE) && !Players.isAnyPlayerPressingButton(InputKeys.SELECT)) {
            pressed = false;
        }
    }

    private void next() {
        boolean finished = false;
        for (SH ignored : text) {
            finished = dialog.advanceDialog();
        }
        text.clear();
        char_offset = 0;
        line_offset = 0;
        funOnTheBun();
        done = false;
        if (finished)
            endDialog();
        else {
            Sound.playSound("dialogadvance").setVolume(0.5f);
            if (events != null) {
                events.onDialogAdvance(dialog);
            }
        }
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

    public void endDialog() {
        ended = true;
        dialog.reset();
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

    private static DialogUI currentdialog;
    public static DialogUI currentDialogBox() {
        return currentdialog;
    }

    /*
    Backwards compatibility methods
     */
    @Override
    protected void onClose() {
        if (halted) {
            Players.getPlayer1().getSprite().unfreeze(DialogUI.class);
        }
    }
    private boolean halted = false;

    /**
     * Display this dialog box and freeze the player.
     * @deprecated This method is deprecated. Please use {@link Dialog#displayDialog(String)}
     */
    @Deprecated
    public void displayUI() {
        displayUI(true);
    }

    /**
     * Display this dialog box in world <b>world</b> and don't freeze the player.
     * @param world The world to display the dialog box in.
     * @deprecated This method is deprecated. Please use {@link Dialog#displayDialog(String)}
     */
    @Deprecated
    public void displayUI(World world) {
        displayUI(false, world);
    }

    /**
     * Display this dialog box in the currently displaying world.
     * @param halt Whether or not to freeze the player
     * @deprecated This method is deprecated. Please use {@link Dialog#displayDialog(String)}
     */
    @Deprecated
    public void displayUI(boolean halt) {
        displayUI(halt, WorldFactory.getCurrentWorld());
    }

    /**
     * Display this dialog box in the world <b>world</b>
     * @param halt Whether or not to freeze the player
     * @param world The world to display the dialog box in.
     * @deprecated This method is deprecated. Please use {@link Dialog#displayDialog(String)}
     */
    @Deprecated
    public void displayUI(boolean halt, World world) {
        world.addDrawable(this);
        Player player = Players.getPlayer1();
        if (halt && player != null && player.getSprite() != null) {
            player.getSprite().freeze(true, DialogUI.class);
            halted = true;
        }
    }


    private class SH {
        public CustomString s;
        public int line;
        public int ID;
        public long speed;

        @Override
        public int hashCode() {
            return ID;
        }
    }

    public static interface DialogListener {
        public void onDialogAdvance(Dialog dialog);

        public void onDialogStarted(Dialog dialog);

        public void onDialogEnded();
    }
}
