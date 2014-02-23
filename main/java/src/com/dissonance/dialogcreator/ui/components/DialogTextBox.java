package com.dissonance.dialogcreator.ui.components;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;

public final class DialogTextBox extends JPanel {
    private JPanel header;

    private JPanel formatting;
    private JLabel controlBold;
    private JLabel controlItalic;
    private JLabel controlUnderline;
    private JLabel controlColor;

    private JPanel controls;
    private JLabel controlNew;
    private JLabel controlPush;
    private JLabel controlSave;
    private JLabel controlPreview;

    private JTextPane title;
    private JTextPane text;

    private DialogList list;

    private static Color borderColor = new Color(0xcccccc);
    private static Color headerBgColor = new Color(0xf0f0f0);
    private static Color hoverColor = new Color(0x5ea7ff);
    private static Color hoverBgColor = new Color(0xeff6ff);
    private static Color labelColor = new Color(0x666666);
    private static Font fontAwesome;

    static {
        InputStream in = DialogTextBox.class.getClassLoader().getResourceAsStream("fonts/fontawesome-webfont.ttf");
        try {
            fontAwesome = Font.createFont(Font.TRUETYPE_FONT, in);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    public DialogTextBox(DialogList list) {
        this.list = list;

        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(borderColor));
        setLayout(null);

        initializeComponents();
        initializeEvents();
    }

    private void initializeComponents() {
        //region header
        header = new JPanel();
        header.setSize(492, 25);
        header.setLocation(1, 1);
        header.setLayout(null);
        add(header);
        //endregion

        //region formatting
        formatting = new JPanel();
        formatting.setBackground(headerBgColor);
        formatting.setBorder(new MatteBorder(0, 0, 1, 1, borderColor));
        formatting.setSize(114, 25);
        formatting.setLocation(0, 0);
        formatting.setLayout(null);
        header.add(formatting);
        //endregion

        //region controlBold
        controlBold = new JLabel();
        initializeLabel(controlBold, 0xf032);
        controlBold.setLocation(7, 0);
        formatting.add(controlBold);
        //endregion

        //region controlItalic
        controlItalic = new JLabel();
        initializeLabel(controlItalic, 0xf033);
        controlItalic.setLocation(32, 0);
        formatting.add(controlItalic);
        //endregion

        //region controlUnderline
        controlUnderline = new JLabel();
        initializeLabel(controlUnderline, 0xf0cd);
        controlUnderline.setLocation(57, 0);
        formatting.add(controlUnderline);
        //endregion

        //region controlColor
        controlColor = new JLabel();
        initializeLabel(controlColor, 0xf043);
        controlColor.setLocation(82, 0);
        formatting.add(controlColor);
        //endregion

        //region controls
        controls = new JPanel();
        controls.setBackground(headerBgColor);
        controls.setBorder(new MatteBorder(0, 1, 1, 0, borderColor));
        controls.setSize(114, 25);
        controls.setLocation(378, 0);
        controls.setLayout(null);
        header.add(controls);
        //endregion

        //region controlNew
        controlNew = new JLabel();
        initializeLabel(controlNew, 0xf067);
        controlNew.setLocation(8, 0);
        controls.add(controlNew);
        //endregion

        //region controlPush
        controlPush = new JLabel();
        initializeLabel(controlPush, 0xf10d);
        controlPush.setLocation(33, 0);
        controls.add(controlPush);
        //endregion

        //region controlSave
        controlSave = new JLabel();
        initializeLabel(controlSave, 0xf0c7);
        controlSave.setLocation(58, 0);
        controls.add(controlSave);
        //endregion

        //region controlPreview
        controlPreview = new JLabel();
        initializeLabel(controlPreview, 0xf04b);
        controlPreview.setLocation(83, 0);
        controls.add(controlPreview);
        //endregion

        //region title
        title = new JTextPane();
        title.setLocation(114, 0);
        title.setSize(264, 25);
        title.setText("Title");
        title.setFont(title.getFont().deriveFont((float) (title.getFont().getSize() + 3)));

        JScrollPane titlePane = new JScrollPane(title);
        titlePane.setBorder(new MatteBorder(0, 0, 1, 0, borderColor));
        titlePane.setLocation(title.getLocation());
        titlePane.setSize(title.getSize());
        header.add(titlePane);
        //endregion

        //region text
        text = new JTextPane();
        text.setLocation(0, 26);
        text.setSize(494, 174);
        text.setFont(title.getFont());

        JScrollPane textPane = new JScrollPane(text);
        textPane.setBorder(new MatteBorder(0, 1, 1, 1, borderColor));
        textPane.setLocation(text.getLocation());
        textPane.setSize(text.getSize());
        add(textPane);
        //endregion
    }

    private void initializeEvents() {
        MouseAdapter hoverAdapter = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);

                e.getComponent().setBackground(hoverBgColor);
                e.getComponent().setForeground(hoverColor);

                if (e.getComponent() instanceof JLabel) {
                    ((JLabel) e.getComponent()).setBorder(BorderFactory.createLineBorder(hoverColor));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);

                e.getComponent().setBackground(headerBgColor);
                e.getComponent().setForeground(labelColor);

                if (e.getComponent() instanceof JLabel) {
                    ((JLabel) e.getComponent()).setBorder(null);
                }
            }
        };

        controlBold.addMouseListener(hoverAdapter);
        controlItalic.addMouseListener(hoverAdapter);
        controlUnderline.addMouseListener(hoverAdapter);
        controlColor.addMouseListener(hoverAdapter);
        controlNew.addMouseListener(hoverAdapter);
        controlPush.addMouseListener(hoverAdapter);
        controlSave.addMouseListener(hoverAdapter);
        controlPreview.addMouseListener(hoverAdapter);

        controlBold.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                new StyledEditorKit.BoldAction().actionPerformed(new ActionEvent(text, e.getID(), ""));
            }
        });

        controlItalic.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new StyledEditorKit.ItalicAction().actionPerformed(new ActionEvent(text, e.getID(), ""));
            }
        });

        controlUnderline.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new StyledEditorKit.UnderlineAction().actionPerformed(new ActionEvent(text, e.getID(), ""));
            }
        });

        controlColor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new StyledEditorKit.ForegroundAction("", Color.RED).actionPerformed(new ActionEvent(text, e.getID(), ""));
            }
        });

        controlNew.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                text.setStyledDocument(list.addComponent());
            }
        });
    }

    private void initializeLabel(JLabel label, int character) {
        label.setText(String.valueOf((char) character));
        label.setSize(24, 24);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setFont(fontAwesome.deriveFont(15f));
        label.setForeground(labelColor);
    }
}
