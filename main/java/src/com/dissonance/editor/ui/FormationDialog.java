package com.dissonance.editor.ui;

import com.dissonance.editor.Formation;
import com.dissonance.editor.quest.MainQuest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;

public final class FormationDialog extends JDialog {

    private JLabel lblLeader;
    private JComboBox<String> boxLeader;

    private JLabel lblFollowers;
    private JList<String> listFollowers;

    private JButton btnGenerate;

    public FormationDialog() {

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setLayout(null);
        setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
        setResizable(false);
        setSize(236, 418);
        setTitle("Formation");

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);

        initializeComponents();
        initializeEvents();
    }

    private void initializeComponents() {
        // lblLeader
        lblLeader = new JLabel();
        lblLeader.setLocation(27, 25);
        lblLeader.setSize(130, 13);
        lblLeader.setText("Choose a formation leader");
        add(lblLeader);

        // boxLeader
        boxLeader = new JComboBox<>();
        boxLeader.setLocation(27, 38);
        boxLeader.setSize(176, 21);
        add(boxLeader);

        // lblFollowers
        lblFollowers = new JLabel();
        lblFollowers.setLocation(27, 72);
        lblFollowers.setSize(87, 13);
        lblFollowers.setText("Choose followers");
        add(lblFollowers);

        // listFollowers
        listFollowers = new JList<>();
        listFollowers.setLocation(27, 85);
        listFollowers.setModel(new DefaultListModel<String>());
        listFollowers.setSize(176, 238);

        JScrollPane pane = new JScrollPane(listFollowers);
        pane.setBorder(BorderFactory.createLineBorder(new Color(0xacacac)));
        pane.setLocation(listFollowers.getLocation());
        pane.setSize(listFollowers.getSize());
        add(pane);

        // btnGenerate
        btnGenerate = new JButton();
        btnGenerate.setLocation(26, 340);
        btnGenerate.setSize(178, 23);
        btnGenerate.setText("Generate formation");
        add(btnGenerate);

        // text initialization
        for (int i = 0; i < MainQuest.INSTANCE.getSpriteCount(); i++) {
            String name = MainQuest.INSTANCE.getVarNameFor(i);
            boxLeader.addItem(name);

            if (i != 0) {
                ((DefaultListModel<String>) listFollowers.getModel()).addElement(name);
            }
        }

    }

    private void initializeEvents() {
        // boxLeader
        boxLeader.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((DefaultListModel) listFollowers.getModel()).removeAllElements();

                for (int i = 0; i < boxLeader.getItemCount(); i++) {
                    String item = boxLeader.getItemAt(i);

                    if (!item.equals(boxLeader.getSelectedItem())) {
                        ((DefaultListModel<String>) listFollowers.getModel()).addElement(item);
                    }
                }
            }
        });

        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                java.util.List<String> values = listFollowers.getSelectedValuesList();
                Collections.sort(values);

                String[] array = values.toArray(new String[values.size()]);
                Formation formation = new Formation((String) boxLeader.getSelectedItem(), array);
                MainQuest.INSTANCE.addFormation(formation);
                EditorUI.INSTANCE.codeTextPane.setText(MainQuest.INSTANCE.generateLoaderCode());
                EditorUI.INSTANCE.highlighter.highlight();
                dispose();
            }
        });
    }
}
