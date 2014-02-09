package com.dissonance.editor.system;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class Highlighter {
    public static final String DATA = "config" + File.separator + "highlight.dat";
    public String classes = "";
    public String interfaces = "";
    public String methods = "";
    private JTextPane ta;

    public void writeData() {
        try (DataOutputStream stream = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(DATA, false)))) {
            stream.writeUTF(classes);
            stream.writeUTF(interfaces);
            stream.writeUTF(methods);
        } catch (IOException ignored) {
        }
    }

    public void readData() {
        try (DataInputStream stream = new DataInputStream(new GZIPInputStream(new FileInputStream(DATA)))) {
            classes = stream.readUTF();
            interfaces = stream.readUTF();
            methods = stream.readUTF();
        } catch (IOException ignored) {
        }

        HighlightStyle.getStyle("Class").setPattern("\\b(" + classes + ")\\b");
        HighlightStyle.getStyle("Interface").setPattern("\\b(" + interfaces + ")\\b");
        HighlightStyle.getStyle("Method").setPattern("\\b(" + methods + ")\\b");
    }

    public void addClass(String string) {
        if (classes.contains(string)) {
            return;
        }

        if (!classes.equals("")) {
            classes += "|";
        }

        classes += string;
        HighlightStyle.getStyle("Class").setPattern("\\b(" + classes + ")\\b");

        writeData();
    }

    public void addInterface(String string) {
        if (interfaces.contains(string)) {
            return;
        }

        if (!interfaces.equals("")) {
            interfaces += "|";
        }

        interfaces += string;
        HighlightStyle.getStyle("Interface").setPattern("\\b(" + interfaces + ")\\b");

        writeData();
    }

    public void addMethod(String string) {
        if (methods.contains(string)) {
            return;
        }
        if (!methods.equals("")) {
            methods += "|";
        }

        methods += string;
        HighlightStyle.getStyle("Method").setPattern("\\b(" + methods + ")\\b");

        writeData();
    }

    public Highlighter(JTextPane text) {
        ta = text;

        SimpleAttributeSet keyword = new SimpleAttributeSet();
        keyword.addAttribute(StyleConstants.Foreground, new Color(0xdc7f38));
        keyword.addAttribute(StyleConstants.Bold, true);
        String keywordRegex = "\\b(class|int|true|false|short|byte|void|super|static|long|double|final|public|private|char|protected|package|new|extends|float|if|else|for|while|try|catch|boolean|import|return)\\b";
        HighlightStyle.addStyle(new HighlightStyle("Keyword", keywordRegex, keyword));

        SimpleAttributeSet annotation = new SimpleAttributeSet();
        annotation.addAttribute(StyleConstants.Foreground, new Color(0xcdc42c));
        String annotationRegex = "\\B(\\@(.+))\\b";
        HighlightStyle.addStyle(new HighlightStyle("Annotation", annotationRegex, annotation));

        SimpleAttributeSet string = new SimpleAttributeSet();
        string.addAttribute(StyleConstants.Foreground, new Color(0x59c359));
        String stringRegex = "(\"(.*)\")|('(.?)')";
        HighlightStyle.addStyle(new HighlightStyle("String", stringRegex, string));

        SimpleAttributeSet number = new SimpleAttributeSet();
        number.addAttribute(StyleConstants.Foreground, new Color(0x79acce));
        String numberRegex = "\\b((\\d|_)+)((\\.)?(\\d|_)+)?(d|D|f|F|l|L)?\\b";
        HighlightStyle.addStyle(new HighlightStyle("Number", numberRegex, number));

        SimpleAttributeSet variable = new SimpleAttributeSet();
        variable.addAttribute(StyleConstants.Foreground, new Color(0xaa8aba));
        String variableRegex = "\\b(var(\\d+)|w)\\b";
        HighlightStyle.addStyle(new HighlightStyle("Variable", variableRegex, variable));

        SimpleAttributeSet classes = new SimpleAttributeSet();
        classes.addAttribute(StyleConstants.Foreground, new Color(0x428bca));
        classes.addAttribute(StyleConstants.Bold, true);
        HighlightStyle.addStyle(new HighlightStyle("Class", "", classes));

        SimpleAttributeSet interfaces = new SimpleAttributeSet();
        interfaces.addAttribute(StyleConstants.Foreground, new Color(0x80abae));
        interfaces.addAttribute(StyleConstants.Bold, true);
        HighlightStyle.addStyle(new HighlightStyle("Interface", "", interfaces));

        SimpleAttributeSet methods = new SimpleAttributeSet();
        methods.addAttribute(StyleConstants.Foreground, new Color(0xfed680));
        HighlightStyle.addStyle(new HighlightStyle("Method", "", methods));

        SimpleAttributeSet comment = new SimpleAttributeSet();
        comment.addAttribute(StyleConstants.Foreground, new Color(0xcecece));
        String commentRegex = "(//(.*))|(/\\*((.|\\n)*?)\\*/)";
        HighlightStyle.addStyle(new HighlightStyle("Comment", commentRegex, comment));
    }

    public void highlight() {
        ta.getStyledDocument().setCharacterAttributes(0, ta.getText().length(), ta.getLogicalStyle(), true);

        for (HighlightStyle style : HighlightStyle.getStyles()) {
            Matcher m = style.getPattern().matcher(ta.getText());

            while (m.find()) {
                ta.getStyledDocument().setCharacterAttributes(m.start(), (m.end() - m.start()), style.getStyle(), true);
            }
        }
    }
}
