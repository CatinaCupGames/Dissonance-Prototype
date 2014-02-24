package com.dissonance.dialogcreator;

import com.dissonance.dialogcreator.ui.DialogCreator;

import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        DialogCreator editor = new DialogCreator();
        editor.setVisible(true);
/*
        GameService.loadEssentials(args);
        System.out.println("Starting MainQuest");
        GameService.beginQuest(new MainQuest());*/


    }
}
