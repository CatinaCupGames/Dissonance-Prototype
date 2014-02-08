package com.dissonance.game;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.scene.dialog.DialogFactory;
import com.dissonance.game.quests.MenuQuest;
import com.dissonance.test.quests.TestQuest;
import com.dissonance.game.sprites.ImageSprite;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static String DID;
    public static String imagePath;

    public static void main(String[] args) throws Exception {
        GameService.loadEssentials(args);
        GameService.beginQuest(new MenuQuest());
    }
}
