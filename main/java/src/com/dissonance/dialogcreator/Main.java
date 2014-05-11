package com.dissonance.dialogcreator;

import com.dissonance.framework.game.GameService;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception {
        GameService.loadEssentials(new String[0]);
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        GameService.beginQuest(new DialogQuest());
    }
}
