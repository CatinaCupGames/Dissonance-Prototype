package com.dissonance.dialogcreator.ui.components;

import com.dissonance.dialogcreator.style.StyleRange;
import com.dissonance.dialogcreator.ui.DialogCreator;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorChooserComponentFactory;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.*;
import java.security.SecureRandom;
import java.util.HashMap;

public final class DialogList extends JPanel {

    private static final SecureRandom colorRandom = new SecureRandom();
    private static final Color colorNormal = new Color(0xf0f0f0);
    private static final Color colorHighlight = new Color(0xfeffef);

    private DialogTextBox box;
    private DialogPanel clickedComponent;
    private final HashMap<String, Color> headers = new HashMap<>();

    private int y = 5;

    public DialogList() {
        setLayout(null);

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (e.getY() <= 7) {
                    DialogCreator creator = ((DialogCreator) DialogList.this.getTopLevelAncestor());
                    JScrollPane scrollPane = creator.getScrollPane();
                    if (creator.menuBar.isVisible()) {
                        return;
                    }

                    scrollPane.setSize(scrollPane.getWidth(), scrollPane.getHeight() - 20);
                    scrollPane.setLocation(scrollPane.getX(), scrollPane.getY() + 20);
                    creator.menuBar.setVisible(true);
                }
            }
        });
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
                    headers.put(text, generateRandomColor());
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

    private final MouseAdapter clickAdapter = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);

            if (SwingUtilities.isLeftMouseButton(e)) {
                clickedComponent.getDialogPane().setBackground(colorNormal);
                clickedComponent = (DialogPanel) e.getSource();

                box.text.setStyledDocument(clickedComponent.getDialogPane().getStyledDocument());

                box.title.setStyledDocument(clickedComponent.getDialogHeader().getStyledDocument());
                box.styles = clickedComponent.getStyles();

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

    public DialogPanel addComponent() {
        final DialogPanel panel = new DialogPanel();
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
        panel.getDialogPane().getStyledDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                int start = e.getOffset();
                int end = e.getOffset() + e.getLength() - 1;
                box.styles.insert(new StyleRange(start, end));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                box.styles.remove(e.getOffset(), e.getLength() + e.getOffset() - 1);

                if (panel.getDialogPane().getText().length() == 0) {
                    SimpleAttributeSet set = new SimpleAttributeSet();
                    set.addAttribute(StyleConstants.Foreground, Color.BLACK);
                    set.addAttribute(StyleConstants.Bold, false);
                    set.addAttribute(StyleConstants.Italic, false);
                    panel.getDialogPane().setCharacterAttributes(set, true);
                    DialogList.this.box.text.setCharacterAttributes(set, true);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        add(pane);

        panel.linkScrollPane(pane);

        return panel;
    }

    private Color generateRandomColor() {
        int red = colorRandom.nextInt(256);
        int green = colorRandom.nextInt(256);
        int blue = colorRandom.nextInt(256);

        red = (2 * red + Color.WHITE.getRed()) / 3;
        green = (2 * green + Color.WHITE.getGreen()) / 3;
        blue = (2 * blue + Color.WHITE.getBlue()) / 3;

        return new Color(red, green, blue);
    }

    public void clear() {
        removeAll();
        headers.clear();
        clickedComponent = null;
        y = 5;
    }
}
