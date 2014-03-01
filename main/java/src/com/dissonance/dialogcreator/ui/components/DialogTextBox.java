package com.dissonance.dialogcreator.ui.components;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorChooserComponentFactory;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.InputStream;

public final class DialogTextBox extends JPanel {
    private JPanel header;

    private JPanel formatting;
    private JLabel controlBold;
    private JLabel controlItalic;
    private JLabel controlColor;

    private JPanel controls;
    private JLabel controlNew;
    private JLabel controlSave;
    private JLabel controlPreview;

    protected JTextPane title;
    protected JTextPane text;

    private DialogList list;

    private JDialog colorChooser;

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

        final JColorChooser colorChooser = new JColorChooser();
        AbstractColorChooserPanel[] panels = ColorChooserComponentFactory.getDefaultChooserPanels();
        AbstractColorChooserPanel[] usedPanels = {panels[1], panels[3]};
        colorChooser.setChooserPanels(usedPanels);
        this.colorChooser = JColorChooser.createDialog(null, "Choose a color", true, colorChooser, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new StyledEditorKit.ForegroundAction("", colorChooser.getColor()).actionPerformed(new ActionEvent(text, e.getID(), ""));
                    }
                }, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                }
        );

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
        formatting.setSize(89, 25);
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

        //region controlColor
        controlColor = new JLabel();
        initializeLabel(controlColor, 0xf043);
        controlColor.setLocation(57, 0);
        formatting.add(controlColor);
        //endregion

        //region controls
        controls = new JPanel();
        controls.setBackground(headerBgColor);
        controls.setBorder(new MatteBorder(0, 1, 1, 0, borderColor));
        controls.setSize(89, 25);
        controls.setLocation(403, 0);
        controls.setLayout(null);
        header.add(controls);
        //endregion

        //region controlNew
        controlNew = new JLabel();
        initializeLabel(controlNew, 0xf067);
        controlNew.setLocation(8, 0);
        controls.add(controlNew);
        //endregion

        //region controlSave
        controlSave = new JLabel();
        initializeLabel(controlSave, 0xf0c7);
        controlSave.setLocation(33, 0);
        controls.add(controlSave);
        //endregion

        //region controlPreview
        controlPreview = new JLabel();
        initializeLabel(controlPreview, 0xf04b);
        controlPreview.setLocation(58, 0);
        controls.add(controlPreview);
        //endregion

        //region title
        title = new JTextPane();
        title.setLocation(89, 0);
        title.setSize(314, 25);
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
        controlColor.addMouseListener(hoverAdapter);
        controlNew.addMouseListener(hoverAdapter);
        controlSave.addMouseListener(hoverAdapter);
        controlPreview.addMouseListener(hoverAdapter);

        controlBold.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                text.getSelectionStart();
                text.getSelectionEnd();
                new StyledEditorKit.BoldAction().actionPerformed(new ActionEvent(text, e.getID(), ""));
            }
        });

        controlItalic.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                new StyledEditorKit.ItalicAction().actionPerformed(new ActionEvent(text, e.getID(), ""));
            }
        });

        controlColor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                colorChooser.setVisible(true);
            }
        });

        controlNew.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                StyledDocument[] docs = list.addComponent();
                title.setStyledDocument(docs[0]);
                text.setStyledDocument(docs[1]);
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
