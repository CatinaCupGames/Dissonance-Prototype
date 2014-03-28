package com.dissonance.dialogcreator.ui.components;

import com.dissonance.dialogcreator.style.StyleList;
import com.dissonance.dialogcreator.style.StyleRange;
import com.dissonance.dialogcreator.system.AdvanceDialogFactory;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.scene.dialog.DialogFactory;
import com.dissonance.framework.game.scene.dialog.Style;
import org.w3c.dom.*;

import javax.swing.*;
import javax.swing.border.MatteBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorChooserComponentFactory;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public final class DialogTextBox extends JPanel {

    private JPanel header;

    private JPanel formatting;
    private JLabel controlBold;
    private JLabel controlItalic;
    private JLabel controlColor;

    private JPanel controls;
    private JLabel controlRemove;
    private JLabel controlClear;
    private JLabel controlNew;
    private JLabel controlSave;
    private JLabel controlPreview;

    protected JTextPane title;
    protected JTextPane text;

    private DialogList list;
    private DialogPanel panel;

    private JDialog colorChooser;

    protected StyleList styles;

    private String dialogId;
    private String dialogPath;

    private static final Color borderColor = new Color(0xcccccc);
    private static final Color headerBgColor = new Color(0xf0f0f0);
    private static final Color hoverColor = new Color(0x5ea7ff);
    private static final Color hoverBgColor = new Color(0xeff6ff);
    private static final Color labelColor = new Color(0x666666);
    private static Font fontAwesome;

    static {
        try (InputStream in = DialogTextBox.class.getClassLoader().getResourceAsStream("fonts/fontawesome-webfont.ttf")) {
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
                        int selectionStart = text.getSelectionStart();
                        int selectionEnd = text.getSelectionEnd();

                        if (selectionStart == selectionEnd) {
                            return;
                        }

                        styles.add(new StyleRange(selectionStart, --selectionEnd, colorChooser.getColor()));
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
        controls.setSize(139, 25);
        controls.setLocation(353, 0);
        controls.setLayout(null);
        header.add(controls);
        //endregion

        //region controlRemove
        controlRemove = new JLabel();
        initializeLabel(controlRemove, 0xf00d);
        controlRemove.setLocation(8, 0);
        controlRemove.setToolTipText("Remove the currently selected dialog box.");
        controls.add(controlRemove);
        //endregion

        //region controlClear
        controlClear = new JLabel();
        initializeLabel(controlClear, 0xf12d);
        controlClear.setLocation(33, 0);
        controlClear.setToolTipText("Clear the contents of the selected dialog box.");
        controls.add(controlClear);
        //endregion

        //region controlNew
        controlNew = new JLabel();
        initializeLabel(controlNew, 0xf067);
        controlNew.setLocation(58, 0);
        controlNew.setToolTipText("Create a new dialog box");
        controls.add(controlNew);
        //endregion

        //region controlSave
        controlSave = new JLabel();
        initializeLabel(controlSave, 0xf0c7);
        controlSave.setLocation(83, 0);
        controlSave.setToolTipText("Export the dialog to xml");
        controls.add(controlSave);
        //endregion

        //region controlPreview
        controlPreview = new JLabel();
        initializeLabel(controlPreview, 0xf04b);
        controlPreview.setLocation(108, 0);
        controlPreview.setToolTipText("Launch a preview of the dialog");
        controls.add(controlPreview);
        //endregion

        //region title
        title = new JTextPane();
        title.setLocation(89, 0);
        title.setSize(264, 25);
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
        controlRemove.addMouseListener(hoverAdapter);
        controlClear.addMouseListener(hoverAdapter);
        controlNew.addMouseListener(hoverAdapter);
        controlSave.addMouseListener(hoverAdapter);
        controlPreview.addMouseListener(hoverAdapter);

        controlBold.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectionStart = text.getSelectionStart();
                int selectionEnd = text.getSelectionEnd();

                if (selectionStart == selectionEnd) {
                    return;
                }

                styles.add(new StyleRange(selectionStart, --selectionEnd, Style.BOLD));

                new StyledEditorKit.BoldAction().actionPerformed(new ActionEvent(text, e.getID(), ""));
            }
        });

        controlItalic.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectionStart = text.getSelectionStart();
                int selectionEnd = text.getSelectionEnd();

                if (selectionStart == selectionEnd) {
                    return;
                }

                styles.add(new StyleRange(selectionStart, --selectionEnd, Style.ITALIC));

                new StyledEditorKit.ItalicAction().actionPerformed(new ActionEvent(text, e.getID(), ""));
            }
        });

        controlColor.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectionStart = text.getSelectionStart();
                int selectionEnd = text.getSelectionEnd();

                if (selectionStart == selectionEnd) {
                    return;
                }
                colorChooser.setVisible(true);
            }
        });

        controlRemove.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (list != null && panel != null) {
                    int result = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to clear the contents of the current dialog box?",
                            "Confirm", JOptionPane.YES_NO_OPTION);

                    if (result != JOptionPane.YES_OPTION) {
                        return;
                    }

                    list.remove(panel);
                }
            }
        });

        controlClear.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (panel != null) {
                    int result = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to clear the contents of the current dialog box?",
                            "Confirm", JOptionPane.YES_NO_OPTION);

                    if (result != JOptionPane.YES_OPTION) {
                        return;
                    }

                    panel.getDialogHeader().setText("");
                    panel.getDialogPane().setText("");

                    SimpleAttributeSet set = new SimpleAttributeSet();
                    set.addAttribute(StyleConstants.Foreground, Color.BLACK);
                    set.addAttribute(StyleConstants.Bold, false);
                    set.addAttribute(StyleConstants.Italic, false);
                    text.setCharacterAttributes(set, true);

                    panel.getDialogPane().setCharacterAttributes(set, true);
                }
            }
        });

        controlNew.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                DialogPanel panel = list.addComponent();
                title.setStyledDocument(panel.getDialogHeader().getStyledDocument());
                text.setStyledDocument(panel.getDialogPane().getStyledDocument());

                SimpleAttributeSet set = new SimpleAttributeSet();
                set.addAttribute(StyleConstants.Foreground, Color.BLACK);
                set.addAttribute(StyleConstants.Bold, false);
                set.addAttribute(StyleConstants.Italic, false);
                text.setCharacterAttributes(set, true);

                styles = panel.getStyles();
                DialogTextBox.this.panel = panel;
            }
        });

        controlSave.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    generateConfigFile();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        controlPreview.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (!generateConfigFile()) {
                        return;
                    }

                    DialogFactory.unloadAll();
                    AdvanceDialogFactory.createDialogFromId(dialogId, dialogPath);

                    Dialog.displayDialog(dialogId);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void promptDialogId() {
        if (dialogId == null || dialogId.isEmpty()) {
            int choice = JOptionPane.showConfirmDialog(null, "You haven't specified an id for your dialog.\n" +
                    "Do you want to do it now?", "Pony", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                dialogId = JOptionPane.showInputDialog(null, "Enter an id for your dialog");
            } else {
                dialogId = "dialog_" + new Random().nextInt(1_000_000);
            }
        }
    }

    private boolean promptDialogPath() {
        if (dialogPath == null || dialogPath.isEmpty()) {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(false);
            chooser.setDialogTitle("Choose the path of your dialog.xml file");
            chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION) {
                return false;
            }

            dialogPath = chooser.getSelectedFile().getPath();

            File file = new File(dialogPath);
            if (file.isDirectory()) {
                String fs = File.separator;

                //noinspection StringConcatenationMissingWhitespace
                File dialog = new File(dialogPath + fs + "main" + fs + "resources" + fs + "IND" + fs + "dialog.xml");

                if (dialog.exists()) {
                    dialogPath = dialog.getPath();
                } else {
                    JOptionPane.showMessageDialog(null, "Cannot find dialog.xml!", "Oops", JOptionPane.ERROR_MESSAGE);
                    dialogPath = null;
                    return false;
                }
            }
        }

        return true;
    }

    private boolean generateConfigFile() throws Exception {
        if (!promptDialogPath()) {
            JOptionPane.showMessageDialog(null, "Cannot find dialog.xml!", "Oops", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        promptDialogId();

        File file = new File(dialogPath);

        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = builder.parse(file);

        Node dialogNode = document.getFirstChild();

        Element dialog = document.createElement("dialog");

        NodeList dialogElements = document.getElementsByTagName("dialog");
        boolean replaced = false;

        for (int i = 0; i < dialogElements.getLength(); i++) {
            Node node = dialogElements.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;

                if (element.getAttribute("dialog_id").equals(dialogId)) {
                    Node child;
                    while ((child = element.getFirstChild()) != null) {
                        element.removeChild(child);
                    }

                    dialog = element;
                    replaced = true;
                }
            }
        }

        if (!replaced) {
            dialogNode.appendChild(document.createTextNode("\n"));
        }

        dialog.setAttribute("dialog_id", dialogId);
        Text tab = document.createTextNode("\t");

        Component[] components = list.getComponents();
        for (int i = 0; i < components.length; i++) {
            Component component = components[i];
            if (component instanceof JScrollPane) {
                DialogPanel panel = (DialogPanel) ((JScrollPane) component).getViewport().getView();
                String paneText = panel.getDialogPane().getText();
                Element header = document.createElement("header");
                header.appendChild(document.createTextNode(panel.getDialogHeader().getText()));

                dialog.appendChild(header);

                for (int j = 0; j < panel.getStyles().size(); j++) {
                    Element message = document.createElement("message");

                    if (j != 0) {
                        message.setAttribute("type", "append");
                    }

                    StyleRange range = panel.getStyles().get(j);
                    String text = paneText.substring(range.getStart(), range.getEnd() + 1);

                    if (range.getStyle() != Style.NORMAL) {
                        message.setAttribute("style", range.getStyle().getId());
                    }

                    if (range.getColor() != null && !range.getColor().equals(Color.WHITE)) {
                        message.setAttribute("color", range.getHexColor());
                    }

                    message.appendChild(document.createTextNode(text));
                    dialog.appendChild(document.createTextNode("\n\t\t"));
                    dialog.appendChild(message);

                }

                if (i != components.length - 1) {
                    dialog.appendChild(document.createTextNode("\n\n\t\t"));
                }
            }
        }

        if (!replaced) {
            dialogNode.appendChild(tab);
            dialogNode.appendChild(dialog);
        }

        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);

        return true;
    }

    private void initializeLabel(JLabel label, int character) {
        label.setText(String.valueOf((char) character));
        label.setSize(24, 24);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setFont(fontAwesome.deriveFont(15f));
        label.setForeground(labelColor);
    }

    public JTextPane getTitle() {
        return title;
    }

    public JTextPane getText() {
        return text;
    }

    public void setDialogId(String dialogId) {
        this.dialogId = dialogId;
    }

    public void setDialogPath(String dialogPath) {
        this.dialogPath = dialogPath;
    }
}
