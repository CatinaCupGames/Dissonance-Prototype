package com.dissonance.dialogcreator.system;

import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.scene.dialog.DialogFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class AdvanceDialogFactory {
    public static boolean createDialogFromXMLString(String xml) {
        try {
            return createDialogFromXMLString(xml, "UTF-8");
        } catch (UnsupportedEncodingException ignored) { }
        return false;
    }

    public static boolean createDialogFromXMLString(String xml, String format) throws UnsupportedEncodingException {
        InputStream stream = new ByteArrayInputStream(xml.getBytes(format));
        return DialogFactory.loadDialog(stream);
    }
}
