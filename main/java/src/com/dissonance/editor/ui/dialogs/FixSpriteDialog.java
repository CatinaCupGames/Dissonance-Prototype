package com.dissonance.editor.ui.dialogs;

import com.dissonance.editor.ui.EditorUI;
import com.dissonance.framework.render.Drawable;

import javax.swing.*;
import javax.swing.text.EditorKit;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;

public class FixSpriteDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<String> list1;
    private JList<Drawable> list2;
    private JButton upButton;
    private JButton downButton;
    private ArrayList<String> tempVars = new ArrayList<String>();
    private ArrayList<Drawable> tempDraws = new ArrayList<Drawable>();

    public FixSpriteDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

// call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

// call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        HashMap<String, Drawable> varNames = EditorUI.INSTANCE.getVarNames();

        for (String key : varNames.keySet()) {
            tempVars.add(key);
            tempDraws.add(varNames.get(key));
        }

        recreateList();

        upButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int sIndex = list1.getSelectedIndex();
                if (sIndex < 0 || sIndex >= tempVars.size() || sIndex - 1 < 0) return;

                String temp = tempVars.get(sIndex);
                tempVars.remove(sIndex);
                tempVars.add(sIndex - 1, temp);

                recreateList();

                list1.setSelectedIndex(sIndex - 1);
            }
        });

        downButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int sIndex = list1.getSelectedIndex();
                if (sIndex < 0 || sIndex >= tempVars.size() || sIndex + 1 >= tempVars.size()) return;

                String temp = tempVars.get(sIndex);
                tempVars.remove(sIndex);
                tempVars.add(sIndex + 1, temp);

                recreateList();

                list1.setSelectedIndex(sIndex + 1);
            }
        });
    }

    private void recreateList() {
        DefaultListModel<String> list1Model = new DefaultListModel<String>();
        list1.setModel(list1Model);

        DefaultListModel<Drawable> listModel2 = new DefaultListModel<Drawable>();
        list2.setModel(listModel2);

        for (int i = 0; i < tempVars.size(); i++) {
            listModel2.addElement(tempDraws.get(i));
            list1Model.addElement(tempVars.get(i));
        }
    }

    private void onOK() {
        HashMap<String, Drawable> toPut = new HashMap<String, Drawable>();

        for (int i = 0; i < tempVars.size(); i++) {
            toPut.put(tempVars.get(i), tempDraws.get(i));
        }

        EditorUI.INSTANCE.setVarNames(toPut);

        dispose();
    }

    private void onCancel() {
// add your code here if necessary
        dispose();
    }
}
