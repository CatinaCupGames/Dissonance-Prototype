/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: $date
=====================================
*/
package com.dissonance.editor.ui;

import com.dissonance.editor.quest.MainQuest;
import com.dissonance.framework.render.Drawable;
import com.dissonance.framework.render.RenderService;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class EditorUI {
    private static final String HEADER = "/*\n" +
            "=====================================\n" +
            "\n" +
            "This file was automatically generated with the\n" +
            "World Loader Editor\n" +
            "\n" +
            "Date: $date\n" +
            "=====================================\n" +
            "*/";
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

        INSTANCE.speedSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                MainQuest.SPEED = (int)INSTANCE.speedSpinner.getValue();
            }
        });

        INSTANCE.exportWorldLoaderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!EditorUI.INSTANCE.codeTextArea.getText().isEmpty()) {
                    boolean value = MainQuest.INSTANCE.compileAndShow(EditorUI.INSTANCE.codeTextArea.getText());
                    if (!value)
                        return;
                }
                File defaultDir = new File("main/java/src/com/dissonance/game/w");
                if (!defaultDir.exists())
                    defaultDir = new File(".");
                JFileChooser saveFile = new JFileChooser(defaultDir);
                saveFile.setDialogTitle("Choose export location");
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "Java Source File", "java");
                saveFile.setFileFilter(filter);
                saveFile.setSelectedFile(new File(MainQuest.INSTANCE.mapName + ".java"));
                saveFile.setApproveButtonText("Export");
                int n = saveFile.showSaveDialog(FRAME);
                if (n == JFileChooser.APPROVE_OPTION) {
                    File file = saveFile.getSelectedFile();
                    try {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Calendar cal = Calendar.getInstance();
                        String date = dateFormat.format(cal.getTime());
                        String text = INSTANCE.codeTextArea.getText();
                        text = HEADER.replace("$date", date) + "\n" + text;
                        PrintWriter out = new PrintWriter(file);
                        out.print(text);
                        out.flush();
                        out.close();
                        JOptionPane.showMessageDialog(FRAME, "The World Loader was successfully exported!", "All done", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Throwable e1) {
                        e1.printStackTrace();
                        JOptionPane.showMessageDialog(FRAME, "There was an error exporting the World Loader\nSee the console for more details..", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
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
        INSTANCE.innerInnerInnerInnerField.setBackground(c);
        INSTANCE.speedSpinner.setValue(5);
        INSTANCE.lable.setForeground(Color.WHITE);
        INSTANCE.codeTextArea.setBackground(c1);
        INSTANCE.codeTextArea.setForeground(Color.WHITE);
        INSTANCE.codeTextArea.setCaretColor(Color.WHITE);
        INSTANCE.lable2.setForeground(Color.WHITE);
        INSTANCE.setComboBox(new ArrayList<Drawable>());

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    public void setComboBox(ArrayList<Drawable> sprites) {
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
    private JScrollPane scrollPane1;
    private JPanel innerInnerInnerInnerField;
    private JSpinner speedSpinner;
    private JLabel lable2;

    private void createUIComponents() {
        codeTextArea = new JTextArea();
        DefaultCaret caret = (DefaultCaret)codeTextArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        scrollPane1 = new JScrollPane();
        scrollPane1.setViewportView(codeTextArea);
    }
}
