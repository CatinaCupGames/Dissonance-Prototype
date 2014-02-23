package com.dissonance.dialogcreator.ui;

import com.dissonance.dialogcreator.ui.components.DialogList;
import com.dissonance.dialogcreator.ui.components.DialogTextBox;

import javax.swing.*;
import java.awt.*;

public final class DialogCreator extends JFrame {
    private DialogList dialogList;
    private DialogTextBox dialogTextBox;

    public DialogCreator() {
        super("Dissonance dialog creator");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        setSize(500, 600);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);

        initializeComponents();
        initializeEvents();
    }

    private void initializeComponents() {
        // dialogList
        dialogList = new DialogList();
        dialogList.setBackground(new Color(0xeff6ff));
        dialogList.setSize(495, 400);
        dialogList.setLocation(0, 0);
        JScrollPane pane = new JScrollPane(dialogList);
        pane.setSize(dialogList.getSize());
        pane.setLocation(dialogList.getLocation());
        pane.setBorder(null);
        dialogList.setPane(pane);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(pane);

        // dialogTextBox
        dialogTextBox = new DialogTextBox(dialogList);
        dialogTextBox.setSize(494, 200);
        dialogTextBox.setLocation(0, 400);
        add(dialogTextBox);
    }

    private void initializeEvents() {

    }
}
