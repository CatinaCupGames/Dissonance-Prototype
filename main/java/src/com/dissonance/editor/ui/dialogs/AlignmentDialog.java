package com.dissonance.editor.ui.dialogs;

import com.dissonance.editor.quest.MainQuest;
import com.dissonance.editor.ui.EditorUI;
import com.dissonance.editor.ui.components.JImageButton;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.render.Drawable;
import sun.awt.image.ToolkitImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class AlignmentDialog extends BaseDialog {

    private JImageButton btnAlignFeet;
    private JImageButton btnAlignHeads;
    private JImageButton btnAlignLeft;
    private JImageButton btnAlignRight;
    private JImageButton btnAlignX;
    private JImageButton btnAlignY;

    private JLabel lblStatic;
    private JLabel lblAlign;

    private JComboBox<String> boxStatic;
    private JComboBox<String> boxAlign;

    private JButton btnClose;

    private static ImageIcon[][] icons = new ImageIcon[6][2];
    private static final int TEXTURE_WIDTH = 6;
    private static final int TEXTURE_HEIGHT = 2;
    private static final int TEXTURE_SIZE = 25;

    static {
        BufferedImage img = ((ToolkitImage) new ImageIcon(AlignmentDialog.class.getClassLoader().getResource("graphics/gui/alignButtons.png")).getImage()).getBufferedImage();
        for (int i = 0; i < TEXTURE_WIDTH; i++) {
            for (int j = 0; j < TEXTURE_HEIGHT; j++) {
                icons[i][j] = new ImageIcon(img.getSubimage(i * TEXTURE_SIZE, j * TEXTURE_SIZE, TEXTURE_SIZE, TEXTURE_SIZE));
            }
        }
    }

    public AlignmentDialog() {
        super("Align sprites", new Dimension(218, 202));
    }

    @Override
    protected void initializeComponents() {
        // btnAlignFeet
        btnAlignFeet = new JImageButton(icons[0][0], icons[0][1]);
        btnAlignFeet.setLocation(16, 16);
        btnAlignFeet.setSize(25, 25);
        add(btnAlignFeet);

        // btnAlignHeads
        btnAlignHeads = new JImageButton(icons[1][0], icons[1][1]);
        btnAlignHeads.setLocation(47, 16);
        btnAlignHeads.setSize(25, 25);
        add(btnAlignHeads);

        // btnAlignLeft
        btnAlignLeft = new JImageButton(icons[2][0], icons[2][1]);
        btnAlignLeft.setLocation(78, 16);
        btnAlignLeft.setSize(25, 25);
        add(btnAlignLeft);

        // btnAlignRight
        btnAlignRight = new JImageButton(icons[3][0], icons[3][1]);
        btnAlignRight.setLocation(109, 16);
        btnAlignRight.setSize(25, 25);
        add(btnAlignRight);

        // btnAlignX
        btnAlignX = new JImageButton(icons[4][0], icons[4][1]);
        btnAlignX.setLocation(140, 16);
        btnAlignX.setSize(25, 25);
        add(btnAlignX);

        // btnAlignY
        btnAlignY = new JImageButton(icons[5][0], icons[5][1]);
        btnAlignY.setLocation(171, 16);
        btnAlignY.setSize(25, 25);
        add(btnAlignY);

        // lblStatic
        lblStatic = new JLabel();
        lblStatic.setLocation(17, 51);
        lblStatic.setSize(65, 13);
        lblStatic.setText("Static sprite:");
        add(lblStatic);

        // lblAlign
        lblAlign = new JLabel();
        lblAlign.setLocation(17, 94);
        lblAlign.setSize(74, 13);
        lblAlign.setText("Sprite to align:");
        add(lblAlign);

        // boxStatic
        boxStatic = new JComboBox<>();
        boxStatic.setLocation(16, 64);
        boxStatic.setSize(180, 21);
        add(boxStatic);

        // boxAlign
        boxAlign = new JComboBox<>();
        boxAlign.setLocation(16, 107);
        boxAlign.setSize(180, 21);
        add(boxAlign);

        // btnClose
        btnClose = new JButton();
        btnClose.setLocation(15, 137);
        btnClose.setSize(182, 21);
        btnClose.setText("Close");
        add(btnClose);

        // text initialization
        for (int i = 0; i < MainQuest.INSTANCE.getSpriteCount(); i++) {
            String name = MainQuest.INSTANCE.getVarNameFor(i);
            boxStatic.addItem(name);
            boxAlign.addItem(name);
        }
    }

    @Override
    protected void initializeEvents() {

        btnAlignFeet.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (boxAlign.getSelectedItem().equals(boxStatic.getSelectedItem())) {
                    return;
                }

                Drawable staticDrawable = MainQuest.INSTANCE.getDrawableFromVar((String) boxStatic.getSelectedItem());
                Drawable alignDrawable = MainQuest.INSTANCE.getDrawableFromVar((String) boxAlign.getSelectedItem());

                if (!(staticDrawable instanceof Sprite) || !(alignDrawable instanceof Sprite)) {
                    return;
                }

                ((Sprite) alignDrawable).setY(staticDrawable.getY() + (staticDrawable.getHeight() - alignDrawable.getHeight()) / 2);

                EditorUI.INSTANCE.refreshCode();
            }
        });

        btnAlignHeads.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (boxAlign.getSelectedItem().equals(boxStatic.getSelectedItem())) {
                    return;
                }

                Drawable staticDrawable = MainQuest.INSTANCE.getDrawableFromVar((String) boxStatic.getSelectedItem());
                Drawable alignDrawable = MainQuest.INSTANCE.getDrawableFromVar((String) boxAlign.getSelectedItem());

                if (!(staticDrawable instanceof Sprite) || !(alignDrawable instanceof Sprite)) {
                    return;
                }

                ((Sprite) alignDrawable).setY(staticDrawable.getY() + (alignDrawable.getHeight() - staticDrawable.getHeight()) / 2);

                EditorUI.INSTANCE.refreshCode();
            }
        });

        btnAlignLeft.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (boxAlign.getSelectedItem().equals(boxStatic.getSelectedItem())) {
                    return;
                }

                Drawable staticDrawable = MainQuest.INSTANCE.getDrawableFromVar((String) boxStatic.getSelectedItem());
                Drawable alignDrawable = MainQuest.INSTANCE.getDrawableFromVar((String) boxAlign.getSelectedItem());

                if (!(staticDrawable instanceof Sprite) || !(alignDrawable instanceof Sprite)) {
                    return;
                }

                ((Sprite) alignDrawable).setX(staticDrawable.getX() + (staticDrawable.getWidth() - alignDrawable.getWidth()) / 2);

                EditorUI.INSTANCE.refreshCode();
            }
        });

        btnAlignRight.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (boxAlign.getSelectedItem().equals(boxStatic.getSelectedItem())) {
                    return;
                }

                Drawable staticDrawable = MainQuest.INSTANCE.getDrawableFromVar((String) boxStatic.getSelectedItem());
                Drawable alignDrawable = MainQuest.INSTANCE.getDrawableFromVar((String) boxAlign.getSelectedItem());

                if (!(staticDrawable instanceof Sprite) || !(alignDrawable instanceof Sprite)) {
                    return;
                }

                ((Sprite) alignDrawable).setX(staticDrawable.getX() + (alignDrawable.getWidth() - staticDrawable.getWidth()) / 2);

                EditorUI.INSTANCE.refreshCode();
            }
        });

        btnAlignX.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (boxAlign.getSelectedItem().equals(boxStatic.getSelectedItem())) {
                    return;
                }

                Drawable staticDrawable = MainQuest.INSTANCE.getDrawableFromVar((String) boxStatic.getSelectedItem());
                Drawable alignDrawable = MainQuest.INSTANCE.getDrawableFromVar((String) boxAlign.getSelectedItem());

                if (!(staticDrawable instanceof Sprite) || !(alignDrawable instanceof Sprite)) {
                    return;
                }

                ((Sprite) alignDrawable).setX(staticDrawable.getX());

                EditorUI.INSTANCE.refreshCode();
            }
        });

        btnAlignY.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (boxAlign.getSelectedItem().equals(boxStatic.getSelectedItem())) {
                    return;
                }

                Drawable staticDrawable = MainQuest.INSTANCE.getDrawableFromVar((String) boxStatic.getSelectedItem());
                Drawable alignDrawable = MainQuest.INSTANCE.getDrawableFromVar((String) boxAlign.getSelectedItem());

                if (!(staticDrawable instanceof Sprite) || !(alignDrawable instanceof Sprite)) {
                    return;
                }

                ((Sprite) alignDrawable).setY(staticDrawable.getY());

                EditorUI.INSTANCE.refreshCode();
            }
        });


        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AlignmentDialog.this.dispose();
            }
        });
    }
}
