package com.dissonance.editor.ui.components;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public final class JImageButton extends JLabel {

    public JImageButton(final ImageIcon baseImage, final ImageIcon clickImage) {
        setIcon(baseImage);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setIcon(clickImage);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                setIcon(baseImage);
            }
        });
    }
}
