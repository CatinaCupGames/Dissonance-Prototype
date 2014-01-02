package com.dissonance.editor.ui;

import com.dissonance.editor.quest.MainQuest;
import com.dissonance.framework.game.sprites.Sprite;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

public class EditorUI {
    public static EditorUI INSTANCE;
    public static JFrame FRAME;
    private JButton newSpriteButton;
    public JTextArea codeTextArea;
    private JButton exportWorldLoaderButton;
    private JButton compileJavaCodeButton;

    public static void displayForm() {
        INSTANCE = new EditorUI();
        if (INSTANCE.contentPane == null)
            throw new RuntimeException("The compiled method $$$setupUP$$$ was not executed.\n" +
                    "This could be caused by running the editor inside an IDE other than Intellij\n" +
                    "or the editor was compiled with an IDE other than Intellij.");
        FRAME = new JFrame("EditorUI");
        FRAME.setContentPane(INSTANCE.contentPane);
        FRAME.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        FRAME.pack();
        FRAME.setVisible(true);

        INSTANCE.newSpriteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainQuest.INSTANCE.newSprite();
            }
        });

        INSTANCE.compileJavaCodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (EditorUI.INSTANCE.codeTextArea.getText().isEmpty())
                    return;
                MainQuest.INSTANCE.compileAndShow(EditorUI.INSTANCE.codeTextArea.getText());
            }
        });

        INSTANCE.comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int index = INSTANCE.comboBox1.getSelectedIndex() - 1;
                MainQuest.INSTANCE.selectSprite(index);
            }
        });

        INSTANCE.codeTextArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (EditorUI.INSTANCE.updatingCode)
                    return;
                MainQuest.INSTANCE.customCode = true;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (EditorUI.INSTANCE.updatingCode)
                    return;
                MainQuest.INSTANCE.customCode = true;
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (EditorUI.INSTANCE.updatingCode)
                    return;
                MainQuest.INSTANCE.customCode = true;
            }
        });

        FRAME.setSize(800, 600);
        Color c1 = new Color(43, 43, 43);
        Color c = new Color(60, 63, 65);

        FRAME.setBackground(c);
        INSTANCE.contentPane.setBackground(c);
        INSTANCE.innerField.setBackground(c);
        INSTANCE.innerInnerField.setBackground(c);
        INSTANCE.innerInnerInnerField.setBackground(c);
        INSTANCE.lable.setForeground(Color.WHITE);
        INSTANCE.codeTextArea.setBackground(c1);
        INSTANCE.codeTextArea.setForeground(Color.WHITE);
        INSTANCE.codeTextArea.setCaretColor(Color.WHITE);
    }

    public void setComboBox(ArrayList<Sprite> sprites) {
        comboBox1.addItem("None");
        for (int i = 0; i < sprites.size(); i++) {
            comboBox1.addItem(MainQuest.INSTANCE.getVarNameFor(i));
        }
    }

    public void setComboIndex(int index) {
        comboBox1.setSelectedIndex(index);
    }

    public void clearComboBox() {
        comboBox1.removeAllItems();
    }

    private boolean updatingCode = false;
    public void refreshCode() {
        updatingCode = true;
        codeTextArea.setText(MainQuest.INSTANCE.generateLoaderCode());
        updatingCode = false;
    }

    private JPanel contentPane;
    private JPanel innerField;
    private JPanel innerInnerField;
    private JLabel lable;
    private JComboBox comboBox1;
    private JPanel innerInnerInnerField;
}
