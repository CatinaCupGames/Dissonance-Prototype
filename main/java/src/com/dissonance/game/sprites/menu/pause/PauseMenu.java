package com.dissonance.game.sprites.menu.pause;

import com.dissonance.framework.game.player.*;
import com.dissonance.framework.game.player.input.InputKeys;
import com.dissonance.framework.game.player.input.joypad.Joypad;
import com.dissonance.framework.game.player.input.joypad.JoypadService;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.text.RenderText;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.framework.system.Service;
import com.dissonance.framework.system.ServiceManager;
import com.dissonance.game.sprites.menu.*;
import com.dissonance.game.sprites.menu.Button;
import com.dissonance.game.sprites.menu.buttons.*;
import com.dissonance.game.sprites.menu.tutorial.Tutorial;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class PauseMenu extends AbstractUI  {
    public static PauseMenu INSTANCE;
    public static final int MAIN_MENU = 0;
    static final int COOP_MENU = 1;
    static final int TUTORIAL_MENU = 2;

    private static String[] HEADER = {
            "PAUSED",
            "CO-OP MODE"
    };

    private static com.dissonance.game.sprites.menu.Button[] MAIN_BUTTONS = new Button[] {
            new ResumeButon(),
            new CoopButton(),
            new TutorialButton(),
            new QuitButton()
    };

    private static BackButton genericBack = new BackButton();

    private static Texture texture;

    private static Texture controller;
    private static Texture keyboard;

    private Sprite[] toDisplay;
    private Service.ServiceRunnable runnable;
    private int type;
    private static TrueTypeFont font;
    private static TrueTypeFont footer_font;
    private static final Color color = new Color(1f, 1f, 1f, 1f);
    private static final Color warning = new Color(Players.PLAYER_COLORS[0][0], Players.PLAYER_COLORS[0][1], Players.PLAYER_COLORS[0][2]);

    @Override
    protected void onRender() {
        if (toDisplay != null) {
            return;
        }

        float x = texture.getTextureWidth() / 2f;
        float y = texture.getTextureHeight() / 2f;
        float bx = texture.getTextureWidth() / 2f;
        float by = texture.getTextureHeight() / 2f;
        float z = 0f;

        texture.bind();
        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex3f(x-bx, y-by, z);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex3f(x+bx, y-by, z);
        glTexCoord2f(1f, 1f); //top right
        glVertex3f(x+bx, y+by, z);
        glTexCoord2f(0f, 1f); //top left
        glVertex3f(x-bx, y+by, z);
        glEnd();
        texture.unbind();

        RenderText.drawString(font, HEADER[type], GameSettings.Display.game_width / 4f - (font.getWidth(HEADER[type]) / 2f), 22f, color);

        if (type == MAIN_MENU)
            mainMenu();
        else if (type == COOP_MENU)
            coopMenu();
    }

    private void mainMenu() {

    }

    private void coopMenu() {
        String str = "Press any button on the controllers/keyboard to join.";
        RenderText.drawString(footer_font, str, 62f, GameSettings.Display.game_height / 2f - font.getHeight(), color);

        Player[] players = Players.getPlayersWithInput();
        float bx = controller.getTextureWidth() / 2f;
        float by = controller.getTextureHeight() / 2f;
        controller.bind();
        for (int i = 0; i < players.length; i++) {
            if (players[i].getInput() instanceof ControllerInput)
                drawPlayer(Players.PLAYER_COLORS[i][0], Players.PLAYER_COLORS[i][1], Players.PLAYER_COLORS[i][2], ((GameSettings.Display.game_width / 2f) / 5f) * (i + 1), GameSettings.Display.game_height / 4f, bx, by);
        }
        controller.unbind();

        bx = controller.getTextureWidth() / 2f;
        by = controller.getTextureHeight() / 2f;
        keyboard.bind();
        for (int i = 0; i < players.length; i++) {
            if (players[i].getInput() instanceof KeyboardInput)
                drawPlayer(Players.PLAYER_COLORS[i][0], Players.PLAYER_COLORS[i][1], Players.PLAYER_COLORS[i][2], ((GameSettings.Display.game_width / 2f) / 5f) * (i + 1), GameSettings.Display.game_height / 4f, bx, by);
        }
        keyboard.unbind();

        if (Players.getPlayersWithInput().length > Players.getMaxPlayerCount()) {
            String str1 = "Only " + Players.getMaxPlayerCount() + " of the " + Players.getPlayersWithInput().length + " can play at this time.";
            RenderText.drawString(footer_font, str1, 140f, (GameSettings.Display.game_height / 2f - font.getHeight()) - font.getHeight() - 4f, warning);
        }
    }

    private void drawPlayer(float r, float g, float b, float x, float y, float bx, float by) {
        float z = 0f;

        glColor4f(r, g, b, 1f);
        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex3f(x-bx, y-by, z);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex3f(x+bx, y-by, z);
        glTexCoord2f(1f, 1f); //top right
        glVertex3f(x+bx, y+by, z);
        glTexCoord2f(0f, 1f); //top left
        glVertex3f(x-bx, y+by, z);
        glEnd();
    }



    @Override
    protected void onOpen() {
        INSTANCE = this;
        if (texture == null) {
            try {
                texture = Texture.retrieveTexture("sprites/img/pause.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (font == null) {
            font = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(48f).deriveFont(Font.BOLD), Font.BOLD);
        }

        if (footer_font == null) {
            footer_font = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(24f), Font.PLAIN);
        }

        runnable = RenderService.INSTANCE.runOnServiceThread(new Runnable() {
            @Override
            public void run() {
                update();
            }
        }, false, true);

        setWidth(GameSettings.Display.game_width);
        setHeight(GameSettings.Display.game_height);

        switchTo(MAIN_MENU);
    }

    JoypadService joypadService;
    public void switchTo(int type) {
        /*
        ON CLOSE
         */
        switch (this.type) {
            case MAIN_MENU:
                for (Button button : MAIN_BUTTONS) {
                    button.close();
                }
                break;
            case COOP_MENU:
                genericBack.close();
                joypadService.setServiceListener(null);
                joypadService.pause();
                break;
            case TUTORIAL_MENU:
                RenderService.INSTANCE.provideData(true, RenderService.SET_UI_VISIBILITY);
                for (Sprite s : toDisplay) {
                    world.removeSprite(s);
                }
                toDisplay = null;
                break;
        }

        this.type = type;

        /*
        ON MENU OPEN
         */
        switch (type) {
            case MAIN_MENU:
                for (Button button : MAIN_BUTTONS) {
                    button.display(world);
                }
                break;
            case COOP_MENU:
                genericBack.display();
                genericBack.setX(100);
                genericBack.setY(500);

                joypadService = ServiceManager.createService(JoypadService.class);
                joypadService.resetJoin();
                joypadService.resume();
                joypadService.setServiceListener(JLISTEN);

                if (controller == null) {
                    try {
                        controller = Texture.retrieveTexture("sprites/menu/coop/controller.png");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                if (keyboard == null) {
                    try {
                        keyboard = Texture.retrieveTexture("sprites/menu/coop/keyboard.png");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case TUTORIAL_MENU:
                RenderService.INSTANCE.provideData(false, RenderService.SET_UI_VISIBILITY);
                toDisplay = Tutorial.display();

                for (Sprite s : toDisplay) {
                    world.loadAndAdd(s);
                }
                break;
        }
    }

    @Override
    protected void onClose() {
        if (type == TUTORIAL_MENU)
            switchTo(MAIN_MENU);
        runnable.kill();
        for (Button button : MAIN_BUTTONS) {
            button.close();
        }
    }

    @Override
    public void update() {
        Players.getPlayer1().getInput().update();
        Players.getPlayer1().getInput().checkKeys(null);
        if (type == MAIN_MENU) {
            for (Button button : MAIN_BUTTONS) {
                button.update();
            }
        }

        if (genericBack.isOpened())
            genericBack.update();

        if (type != MAIN_MENU) {
            for (Player p : Players.getPlayersWithInput()) {
                if (p.getInput() instanceof ControllerInput && p.getInput().isKeyPressed(InputKeys.SELECT)) {
                    switchTo(MAIN_MENU);
                }
            }
        }
    }

    public void reset() {

    }


    private JoypadService.ControllerServiceListener JLISTEN = new JoypadService.ControllerServiceListener() {

        @Override
        public void onNewJoypad(Joypad joypad) { }

        @Override
        public void onJoypadRemoved(Joypad joypad) { }

        @Override
        public void onJoypadJoin(Joypad joypad) {
            if (type == COOP_MENU) {
                if (Players.isInputUsed(joypad.createInput()))
                    return;
                Players.createPlayer(joypad.createInput());
            }
        }

        @Override
        public void onKeyboardJoin() {
            if (type == COOP_MENU) {
                if (Players.isInputUsed(Input.KEYBOARD))
                    return;
                Players.createPlayer(Input.KEYBOARD);
            }
        }
    };
}
