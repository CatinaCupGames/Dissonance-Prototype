package com.dissonance.dialogcreator.ui.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class DialogPanel extends JPanel {

    private JTextPane dialogHeader;
    private JTextPane dialogPane;
    private JScrollPane scrollPane;

    public DialogPanel() {
        setSize(468, 80);
        setLayout(null);

        initializeComponents();
        initializeEvents();
    }

    public void linkScrollPane(JScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    public void initializeComponents() {
        //region dialogHeader
        dialogHeader = new JTextPane();
        dialogHeader.setSize(464, 20);
        dialogHeader.setEditable(false);
        dialogHeader.setMargin(new Insets(0, 10, 0, 10));
        dialogHeader.setLocation(0, 0);
        add(dialogHeader);
        //endregion

        //region dialogPane
        dialogPane = new JTextPane();
        dialogPane.setSize(464, 56);
        dialogPane.setLocation(0, 21);
        dialogPane.setEditable(false);
        dialogPane.setMargin(new Insets(7, 10, 7, 10));
        dialogHeader.setFont(dialogPane.getFont());
        JScrollPane pane = new JScrollPane(dialogPane);
        pane.setLocation(dialogPane.getLocation());
        pane.setSize(dialogPane.getSize());
        pane.setBorder(BorderFactory.createEmptyBorder());
        add(pane);
        //endregion
    }

    public void initializeEvents() {
        dialogHeader.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                e.setSource(DialogPanel.this);
                DialogPanel.this.getMouseListeners()[0].mouseClicked(e);
            }
        });

        dialogPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                e.setSource(DialogPanel.this);
                DialogPanel.this.getMouseListeners()[0].mouseClicked(e);
            }
        });
    }

    public JTextPane getDialogPane() {
        return dialogPane;
    }

    public JTextPane getDialogHeader() {
        return dialogHeader;
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }
}
