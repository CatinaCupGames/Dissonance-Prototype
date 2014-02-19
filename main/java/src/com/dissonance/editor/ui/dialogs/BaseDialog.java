package com.dissonance.editor.ui.dialogs;

import javax.swing.*;
import java.awt.*;

public abstract class BaseDialog extends JDialog {
    public BaseDialog(String title, Dimension size) {
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(null);
        setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
        setResizable(false);

        setSize(size);
        setTitle(title);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);

        initializeComponents();
        initializeEvents();
    }

    protected abstract void initializeComponents();

    protected abstract void initializeEvents();
}
