package com.dissonance.dialogcreator.ui.components;

import com.dissonance.dialogcreator.style.StyleList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class DialogPanel extends JPanel {

    private JTextPane dialogHeader;
    private JTextPane dialogText;
    private JScrollPane scrollPane;

    private final StyleList styles = new StyleList();

    private volatile boolean muted = false;

    public DialogPanel() {
        setSize(468, 80);
        setLayout(null);

        initializeComponents();
        initializeEvents();
    }

    public void linkScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    private void initializeComponents() {
        //region dialogHeader
        dialogHeader = new JTextPane();
        dialogHeader.setSize(464, 20);
        dialogHeader.setEditable(false);
        dialogHeader.setMargin(new Insets(0, 10, 0, 10));
        dialogHeader.setLocation(0, 0);
        add(dialogHeader);
        //endregion

        //region dialogText
        dialogText = new JTextPane();
        dialogText.setSize(464, 56);
        dialogText.setLocation(0, 21);
        dialogText.setEditable(false);
        dialogText.setMargin(new Insets(7, 10, 7, 10));
        dialogHeader.setFont(dialogText.getFont());
        JScrollPane pane = new JScrollPane(dialogText);
        pane.setLocation(dialogText.getLocation());
        pane.setSize(dialogText.getSize());
        pane.setBorder(BorderFactory.createEmptyBorder());
        add(pane);
        //endregion
    }

    private void initializeEvents() {
        dialogHeader.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                e.setSource(DialogPanel.this);
                DialogPanel.this.getMouseListeners()[0].mouseClicked(e);
            }
        });

        dialogText.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                e.setSource(DialogPanel.this);
                DialogPanel.this.getMouseListeners()[0].mouseClicked(e);
            }
        });
    }

    public JTextPane getDialogText() {
        return dialogText;
    }

    public JTextPane getDialogHeader() {
        return dialogHeader;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public StyleList getStyles() {
        return styles;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }
}
