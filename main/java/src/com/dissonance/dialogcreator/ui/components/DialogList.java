package com.dissonance.dialogcreator.ui.components;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorChooserComponentFactory;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.security.SecureRandom;
import java.util.HashMap;

public final class DialogList extends JPanel {
    private static final SecureRandom colorRandom = new SecureRandom();
    private static Color colorNormal = new Color(0xf0f0f0);
    private static Color colorHighlight = new Color(0xfeffef);

    private DialogTextBox box;
    private DialogPanel clickedComponent;
    private HashMap<String, Color> headers = new HashMap<>();

    private int y = 5;

    public DialogList() {
        setLayout(null);
    }

    public void linkBox(DialogTextBox box) {
        this.box = box;

        box.title.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                JTextPane source = ((JTextPane) e.getSource());
                String text = source.getText();
                if (text.length() == 0) {
                    return;
                }

                headers.remove(text.substring(0, text.length() - 1));

                if (!headers.containsKey(text)) {
                    headers.put(text, generateRandomColor(Color.WHITE));
                }

                Color color = headers.get(text);
                if (clickedComponent != null) {
                    Border border = new CompoundBorder(BorderFactory.createLineBorder(color.darker()),
                            BorderFactory.createLineBorder(color));
                    clickedComponent.getScrollPane().setBorder(border);
                }

            }
        });
    }

    private MouseAdapter clickAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);

            if (SwingUtilities.isLeftMouseButton(e)) {
                clickedComponent.getDialogPane().setBackground(colorNormal);
                clickedComponent = (DialogPanel) e.getSource();

                box.text.setStyledDocument(clickedComponent.getDialogPane().getStyledDocument());
                box.title.setStyledDocument(clickedComponent.getDialogHeader().getStyledDocument());
                clickedComponent.getDialogPane().setBackground(colorHighlight);
            } else if (SwingUtilities.isRightMouseButton(e)) {
                final DialogPanel source = (DialogPanel) e.getSource();
                final JColorChooser colorChooser = new JColorChooser();
                AbstractColorChooserPanel[] panels = ColorChooserComponentFactory.getDefaultChooserPanels();
                AbstractColorChooserPanel[] usedPanels = {panels[1], panels[3]};
                colorChooser.setChooserPanels(usedPanels);
                JDialog chooser = JColorChooser.createDialog(null, "Choose a color", true, colorChooser, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Color color = colorChooser.getColor();
                                headers.put(source.getDialogHeader().getText(), color);

                                for (Component component : DialogList.this.getComponents()) {
                                    if (component instanceof JScrollPane) {
                                        DialogPanel panel = (DialogPanel) ((JViewport) ((JScrollPane) component).getComponent(0)).getView();
                                        if ((panel.getDialogHeader().getText().equals(source.getDialogHeader().getText()))) {
                                            panel.getScrollPane().setBorder(new CompoundBorder(BorderFactory.createLineBorder(color.darker()),
                                                    BorderFactory.createLineBorder(color)));
                                        }
                                    }
                                }

                            }
                        }, new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {

                            }
                        }
                );
                chooser.setVisible(true);
            }


        }
    };

    public StyledDocument[] addComponent() {
        DialogPanel panel = new DialogPanel();
        if (clickedComponent != null) {
            clickedComponent.getDialogPane().setBackground(colorNormal);
        }
        clickedComponent = panel;
        panel.getDialogPane().setBackground(colorHighlight);
        panel.setLocation(5, y);
        y += 85;
        setSize(getWidth(), y > 400 ? y : 400);
        setPreferredSize(getSize());

        panel.addMouseListener(clickAdapter);
        JScrollPane pane = new JScrollPane(panel);
        pane.setSize(panel.getSize());
        pane.setLocation(panel.getLocation());
        add(pane);

        panel.linkScrollPane(pane);

        return new StyledDocument[]{
                panel.getDialogHeader().getStyledDocument(), panel.getDialogPane().getStyledDocument()
        };
    }

    private Color generateRandomColor(Color mix) {
        int red = colorRandom.nextInt(256);
        int green = colorRandom.nextInt(256);
        int blue = colorRandom.nextInt(256);

        // mix the color
        if (mix != null) {
            red = (2 * red + mix.getRed()) / 3;
            green = (2 * green + mix.getGreen()) / 3;
            blue = (2 * blue + mix.getBlue()) / 3;
        }

        return new Color(red, green, blue);
    }
}
