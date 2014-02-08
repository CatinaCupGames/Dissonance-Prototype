package com.dissonance.editor;

import com.dissonance.editor.quest.MainQuest;
import com.dissonance.framework.game.GameService;
import com.dissonance.framework.render.texture.TextureLoader;

import javax.swing.*;
import java.io.File;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        GameService.loadEssentials(args);
        System.out.println("Starting MainQuest");
        GameService.beginQuest(new MainQuest());
    }
}
