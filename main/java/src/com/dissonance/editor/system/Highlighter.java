package com.dissonance.editor.system;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.zip.GZIPOutputStream;

public final class Highlighter {
    public String classes = "";
    public String interfaces = "";
    private JTextPane ta;

    public void addClass(String string) {
        if (!classes.equals("")) {
            classes += "|";
        }

        classes += string;
        HighlightStyle.getStyle("Class").setPattern("\\b(" + this.classes + ")\\b");

        try (DataOutputStream stream = new DataOutputStream(new GZIPOutputStream(new FileOutputStream("hClass.dat", false)))) {
            stream.writeUTF(classes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addInterface(String string) {
        if (!interfaces.equals("")) {
            interfaces += "|";
        }

        interfaces += string;
        HighlightStyle.getStyle("Interface").setPattern("\\b(" + this.interfaces + ")\\b");

        try (DataOutputStream stream = new DataOutputStream(new GZIPOutputStream(new FileOutputStream("hInterf.dat", false)))) {
            stream.writeUTF(interfaces);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Highlighter(JTextPane text) {
        ta = text;

        SimpleAttributeSet keyword = new SimpleAttributeSet();
        keyword.addAttribute(StyleConstants.Foreground, new Color(0xdc7f38));
        keyword.addAttribute(StyleConstants.Bold, true);
        String keywordRegex = "\\b(class|int|true|false|void|super|static|final|public|private|char|protected|package|new|extends|float|if|else|for|while|try|catch|boolean|import|return)\\b";
        HighlightStyle.addStyle(new HighlightStyle("Keyword", keywordRegex, keyword));

        SimpleAttributeSet annotation = new SimpleAttributeSet();
        annotation.addAttribute(StyleConstants.Foreground, new Color(0xae9e18));
        String annotationRegex = "\\B(\\@(.+))\\b";
        HighlightStyle.addStyle(new HighlightStyle("Annotation", annotationRegex, annotation));

        SimpleAttributeSet string = new SimpleAttributeSet();
        string.addAttribute(StyleConstants.Foreground, new Color(0x59c359));
        String stringRegex = "(\"(.*)\")|('(.?)')";
        HighlightStyle.addStyle(new HighlightStyle("String", stringRegex, string));

        SimpleAttributeSet comment = new SimpleAttributeSet();
        comment.addAttribute(StyleConstants.Foreground, new Color(0xcecece));
        String commentRegex = "(//(.*))";
        HighlightStyle.addStyle(new HighlightStyle("Comment", commentRegex, comment));

        SimpleAttributeSet classes = new SimpleAttributeSet();
        classes.addAttribute(StyleConstants.Foreground, new Color(0x428bca));
        classes.addAttribute(StyleConstants.Bold, true);
        HighlightStyle.addStyle(new HighlightStyle("Class", "", classes));

        SimpleAttributeSet interfaces = new SimpleAttributeSet();
        interfaces.addAttribute(StyleConstants.Foreground, new Color(0x803079));
        interfaces.addAttribute(StyleConstants.Bold, true);
        HighlightStyle.addStyle(new HighlightStyle("Interface", "", interfaces));
    }

    public void matching(boolean color) {
        if (color) {
            ta.getStyledDocument().setCharacterAttributes(0, ta.getText().length(), ta.getLogicalStyle(), true);
        }

        for (HighlightStyle style : HighlightStyle.getStyles()) {
            Matcher m = style.getPattern().matcher(ta.getText());
            while (m.find()) {
                ta.getStyledDocument().setCharacterAttributes(m.start(), (m.end() - m.start()), style.getStyle(), true);
            }
        }
    }
}
