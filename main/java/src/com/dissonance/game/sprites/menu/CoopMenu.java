package com.dissonance.game.sprites.menu;

import com.dissonance.framework.game.player.*;
import com.dissonance.framework.game.player.input.joypad.Joypad;
import com.dissonance.framework.game.player.input.joypad.JoypadService;
import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.text.RenderText;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.framework.system.ServiceManager;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

import java.awt.*;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;

public class CoopMenu extends AbstractUI {
    private static Texture controller;
    private static Texture keyboard;
    private static TrueTypeFont font;
    private static TrueTypeFont footer_font;
    private static final Color color = new Color(1f, 1f, 1f, 1f);
    private static final Color warning = new Color(Players.PLAYER_COLORS[0][0], Players.PLAYER_COLORS[0][1], Players.PLAYER_COLORS[0][2]);

    @Override
    protected void onRender() {
        Color color = new Color(CoopMenu.color);
        color.a = RenderService.getCurrentAlphaValue();

        String text = "CO-OP SETUP";
        RenderText.drawString(font, text, GameSettings.Display.game_width / 4f - (font.getWidth(text) / 2f), 22f, color);

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

        if (Players.getPlayersWithInput().length > 2) {
            Color warning = new Color(CoopMenu.warning);
            warning.a = RenderService.getCurrentAlphaValue();
            String str1 = "Only 2 players can play in this demo.";
            RenderText.drawString(footer_font, str1, 140f, (GameSettings.Display.game_height / 2f - font.getHeight()) - font.getHeight() - 4f, warning);
        }
    }

    private void drawPlayer(float r, float g, float b, float x, float y, float bx, float by) {
        float z = 0f;

        glColor4f(r, g, b, RenderService.getCurrentAlphaValue());
        glBegin(GL_QUADS);
        glTexCoord2f(0f, 0f); //bottom left
        glVertex3f(x-bx, y-by, z);
        glTexCoord2f(1f, 0f); //bottom right
        glVertex3f(x+bx, y-by, z);
        glTexCoord2f(1f, 1f); //top right
        glVertex3f(x+bx, y+by, z);
        glTexCoord2f(0f, 1f); //top left
        glVertex3f(x - bx, y + by, z);
        glEnd();
    }

    JoypadService joypadService;
    @Override
    protected void onOpen() {
        if (font == null) {
            font = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(48f).deriveFont(Font.BOLD), Font.BOLD);
        }

        if (footer_font == null) {
            footer_font = RenderText.getFont(GameSettings.Display.GAME_FONT.deriveFont(24f), Font.PLAIN);
        }

        Players.createPlayer1(); //Ensure we have a player 1

        joypadService = ServiceManager.createService(JoypadService.class);
        while (!joypadService.hasStarted()) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

        setWidth(GameSettings.Display.game_width);
        setHeight(GameSettings.Display.game_height);
    }

    @Override
    protected void onClose() {
        joypadService.setServiceListener(null);
        joypadService.pause();
    }

    @Override
    public void update() {

    }

    private JoypadService.ControllerServiceListener JLISTEN = new JoypadService.ControllerServiceListener() {

        @Override
        public void onNewJoypad(Joypad joypad) { }

        @Override
        public void onJoypadRemoved(Joypad joypad) { }

        @Override
        public void onJoypadJoin(Joypad joypad) {
            if (Players.isInputUsed(joypad.createInput()))
                return;
            Players.createPlayer(joypad.createInput());
        }

        @Override
        public void onKeyboardJoin() {
            if (Players.isInputUsed(Input.KEYBOARD))
                return;
            Players.createPlayer(Input.KEYBOARD);
        }
    };
}
