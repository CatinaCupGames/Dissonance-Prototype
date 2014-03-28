package com.dissonance.dialogcreator.ui;

import com.dissonance.dialogcreator.ui.components.DialogList;
import com.dissonance.dialogcreator.ui.components.DialogTextBox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URI;

public final class DialogCreator extends JFrame {

    private DialogList dialogList;
    private DialogTextBox dialogTextBox;
    private JScrollPane scrollPane;
    private boolean menuBarPinned = true;
    public JMenuBar menuBar;
    private JMenu pinMenu;
    private JMenu helpMenu;
    private JMenuItem clearDialogItem;
    private JMenuItem setExportFileItem;
    private JMenuItem setDialogIdItem;

    private final MouseAdapter pinListener = new MouseAdapter() {
        @Override
        public void mouseExited(MouseEvent e) {
            menuBar.getMouseListeners()[0].mouseExited(e);
        }
    };

    public DialogCreator() {
        super("Dialog creator");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        setSize(500, 750);

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        setLocation((width - getWidth()) / 2, (height - getHeight()) / 2);

        initializeComponents();
        initializeEvents();
    }

    private void initializeComponents() {
        //region dialogList
        dialogList = new DialogList();
        dialogList.setBackground(new Color(0xeff6ff));
        dialogList.setSize(495, 529);
        dialogList.setLocation(0, 21);
        JScrollPane pane = new JScrollPane(dialogList);
        pane.setSize(dialogList.getSize());
        pane.setLocation(dialogList.getLocation());
        pane.setBorder(null);
        pane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        add(pane);
        scrollPane = pane;
        //endregion

        //region dialogTextBox
        dialogTextBox = new DialogTextBox(dialogList);
        dialogTextBox.setSize(494, 200);
        dialogTextBox.setLocation(0, 550);
        dialogList.linkBox(dialogTextBox);
        add(dialogTextBox);
        //endregion

        //region menuBar
        menuBar = new JMenuBar();
        menuBar.setLocation(0, 0);
        menuBar.setSize(getWidth(), 21);
        add(menuBar);
        //endregion

        //region menu
        JMenu menu = new JMenu("Actions");
        clearDialogItem = menu.add(new JMenuItem("Clear dialog"));
        setExportFileItem = menu.add(new JMenuItem("Set export file"));
        setDialogIdItem = menu.add(new JMenuItem("Set dialog id"));
        menuBar.add(menu);

        pinMenu = new JMenu("Unpin");
        menuBar.add(pinMenu);

        helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);
        //endregion
    }

    private void initializeEvents() {
        //region pinMenu
        pinMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                menuBarPinned = !menuBarPinned;
                pinMenu.setText(menuBarPinned ? "Unpin" : "Pin");
            }
        });
        pinMenu.addMouseListener(pinListener);
        //endregion

        //region helpMenu
        helpMenu.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                    try {
                        desktop.browse(new URI("https://github.com/hypereddie/Dissonance/wiki/Dialog-creator"));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Look at the wiki pages in the repo for help.", "Pony", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });
        helpMenu.addMouseListener(pinListener);
        //endregion

        //region menuBar
        menuBar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e) {
                if (menuBarPinned || (e.getY() > 0 && e.getY() < 20 && e.getX() > 0 && e.getX() < menuBar.getWidth())) {
                    return;
                }

                DialogCreator creator = DialogCreator.this;
                creator.scrollPane.setSize(creator.scrollPane.getWidth(), creator.scrollPane.getHeight() + 20);
                creator.scrollPane.setLocation(creator.scrollPane.getX(), creator.scrollPane.getY() - 20);
                creator.menuBar.setVisible(false);
            }
        });
        //endregion

        //region clearDialogItem
        clearDialogItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogList.clear();
                dialogTextBox.getTitle().setText("");
                dialogTextBox.getText().setText("");
                repaint();
            }
        });
        //endregion

        //region setExportFileItem
        setExportFileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setMultiSelectionEnabled(false);
                chooser.showOpenDialog(null);

                String dialogPath = chooser.getSelectedFile().getPath();

                File file = new File(dialogPath);
                if (file.isDirectory()) {
                    String fs = File.separator;


                    //noinspection StringConcatenationMissingWhitespace
                    File dialog = new File(dialogPath + fs + "main" + fs + "resources" + fs + "IND" + fs + "dialog.xml");

                    if (dialog.exists()) {
                        dialogPath = dialog.getPath();
                    } else {
                        JOptionPane.showMessageDialog(null, "Cannot find dialog.xml!", "Oops", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                dialogTextBox.setDialogPath(dialogPath);
            }
        });
        //endregion

        //region setDialogIdItem
        setDialogIdItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialogTextBox.setDialogId(JOptionPane.showInputDialog(null, "Enter an id for your dialog"));
            }
        });
        //endregion
    }

    public JScrollPane getScrollPane() {
        return scrollPane;
    }

    public JMenuBar getJMenuBar() {
        return menuBar;
    }

}
