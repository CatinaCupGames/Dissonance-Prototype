/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: $date
=====================================
*/
package com.dissonance.editor.ui;

import com.dissonance.editor.quest.MainQuest;
import com.dissonance.editor.system.HighlightStyle;
import com.dissonance.editor.system.Highlighter;
import com.dissonance.framework.render.Drawable;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.zip.GZIPInputStream;

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
    public JTextPane codeTextPane;
    private JButton exportWorldLoaderButton;
    private JButton compileJavaCodeButton;
    private JPopupMenu menu;
    private Highlighter highlighter = new Highlighter(codeTextPane);

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
                if (EditorUI.INSTANCE.codeTextPane.getText().isEmpty())
                    return;
                MainQuest.INSTANCE.compileAndShow(EditorUI.INSTANCE.codeTextPane.getText());
            }
        });

        INSTANCE.comboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int index = INSTANCE.comboBox1.getSelectedIndex() - 1;
                MainQuest.INSTANCE.selectSprite(index);
            }
        });

        INSTANCE.codeTextPane.getDocument().addDocumentListener(new DocumentListener() {
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
                MainQuest.SPEED = (int) INSTANCE.speedSpinner.getValue();
            }
        });

        INSTANCE.exportWorldLoaderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!EditorUI.INSTANCE.codeTextPane.getText().isEmpty()) {
                    boolean value = MainQuest.INSTANCE.compileAndShow(EditorUI.INSTANCE.codeTextPane.getText());
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
                        String text = INSTANCE.codeTextPane.getText();
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
        INSTANCE.label.setForeground(Color.WHITE);
        INSTANCE.codeTextPane.setBackground(c1);
        INSTANCE.codeTextPane.setCaretColor(Color.WHITE);

        INSTANCE.highlighter = new Highlighter(INSTANCE.codeTextPane);
        try (DataInputStream stream = new DataInputStream(new GZIPInputStream(new FileInputStream("hClass.dat")))) {
            INSTANCE.highlighter.classes = stream.readUTF();
        } catch (IOException ignored) {
        }

        try (DataInputStream stream = new DataInputStream(new GZIPInputStream(new FileInputStream("hInterf.dat")))) {
            INSTANCE.highlighter.interfaces = stream.readUTF();
        } catch (IOException ignored) {
        }

        HighlightStyle.getStyle("Class").setPattern("\\b(" + INSTANCE.highlighter.classes + ")\\b");
        HighlightStyle.getStyle("Interface").setPattern("\\b(" + INSTANCE.highlighter.interfaces + ")\\b");

        INSTANCE.codeTextPane.setFont(new Font("Consolas", Font.PLAIN, 12));
        INSTANCE.codeTextPane.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                INSTANCE.highlighter.matching(true);
            }
        });

        INSTANCE.codeTextPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    String title = INSTANCE.codeTextPane.getSelectedText();
                    if (title == null) {
                        return;
                    }

                    INSTANCE.menu.setBorder(BorderFactory.createTitledBorder(title));
                    INSTANCE.menu.setLocation(e.getX() + 171, e.getY() + 62);
                    INSTANCE.menu.setVisible(true);
                } else {
                    INSTANCE.menu.setVisible(false);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    String title = INSTANCE.codeTextPane.getSelectedText();
                    if (title == null) {
                        return;
                    }

                    INSTANCE.menu.setBorder(BorderFactory.createTitledBorder(title));
                    INSTANCE.menu.setLocation(e.getX() + 171, e.getY() + 62);
                    INSTANCE.menu.setVisible(true);
                } else {
                    INSTANCE.menu.setVisible(false);
                }
            }
        });
        INSTANCE.label2.setForeground(Color.WHITE);
        INSTANCE.setComboBox(new ArrayList<Drawable>());
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

    public void refreshCode(boolean color) {
        updatingCode = true;
        codeTextPane.setText(MainQuest.INSTANCE.generateLoaderCode());

        highlighter.matching(color);

        updatingCode = false;
    }

    private JPanel contentPane;
    private JPanel innerField;
    private JPanel innerInnerField;
    private JLabel label;
    private JComboBox comboBox1;
    private JPanel innerInnerInnerField;
    private JScrollPane scrollPane1;
    private JPanel innerInnerInnerInnerField;
    private JSpinner speedSpinner;
    private JLabel label2;

    private void createUIComponents() {
        codeTextPane = new JTextPane();
        codeTextPane.setForeground(new Color(0xeeeeee));
        DefaultCaret caret = (DefaultCaret) codeTextPane.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        scrollPane1 = new JScrollPane();
        scrollPane1.setViewportView(codeTextPane);

        menu = new JPopupMenu();
        menu.setInvoker(codeTextPane);

        JMenuItem item = new JMenuItem("Add class");
        item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlighter.addClass(codeTextPane.getSelectedText());
                highlighter.matching(true);
            }
        });
        menu.add(item);

        JMenuItem itt = new JMenuItem("Add interface");
        itt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                highlighter.addInterface(codeTextPane.getSelectedText());
                highlighter.matching(true);
            }
        });
        menu.add(itt);

        Action sendAction = new AbstractAction("Send") {
            public void actionPerformed(ActionEvent ae) {
                try {
                    codeTextPane.getStyledDocument().insertString(codeTextPane.getCaretPosition(), "    ", codeTextPane.getLogicalStyle());
                } catch (BadLocationException ignored) {
                }
            }
        };

        codeTextPane.registerKeyboardAction(sendAction,
                KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), JComponent.WHEN_FOCUSED);
    }
}
