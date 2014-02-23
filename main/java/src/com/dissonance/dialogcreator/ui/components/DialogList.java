package com.dissonance.dialogcreator.ui.components;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public final class DialogList extends JPanel {
    private List<JScrollPane> components = new ArrayList<>();
    private int y = 5;
    int i = 0;
    private int[] colors = new int[]{
            0xe52c2c, 0xf2ad36, 0xf2ec50, 0x7fd441, 0x5ac6e5, 0x605ae5, 0xbf66d5, 0x7bf1ce, 0xf5a0ec, 0x976712
    };
    private JScrollPane pane;

    public void setPane(JScrollPane pane) {
        this.pane = pane;
    }

    public DialogList() {
        setLayout(null);
    }

    public StyledDocument addComponent() {
        JTextPane pane = new JTextPane();
        pane.setLocation(5, y);
        pane.setBackground(new Color(0xf0f0f0));
        pane.setSize(468, 80);
        pane.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(pane);
        scrollPane.setLocation(5, y);
        scrollPane.setSize(pane.getSize());
        add(scrollPane);

        y += 85;
        setSize(getWidth(), y > 400 ? y : 400);
        setPreferredSize(getSize());
        pane.setMargin(new Insets(7, 10, 7, 10));
        scrollPane.setBorder(new MatteBorder(1, 1, 1, 1, new Color(colors[i])));

        if (i < 9) {
            i++;
        }

        return pane.getStyledDocument();
    }
}
