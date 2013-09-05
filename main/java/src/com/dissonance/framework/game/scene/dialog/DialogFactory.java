package com.dissonance.framework.game.scene.dialog;

import com.dissonance.framework.render.texture.sprite.SpriteTexture;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class DialogFactory {
    private static final HashMap<String, Dialog>  text = new HashMap<String, Dialog>();

    public static boolean loadDialog() {
        InputStream in = DialogFactory.class.getClassLoader().getResourceAsStream("IND/dialog.xml");
        if (in == null)
            return false;

        DocumentBuilder db = null;
        try {
            db = SpriteTexture.DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
            Document dom = db.parse(in);
            Element elm = dom.getDocumentElement();

            NodeList nl = elm.getElementsByTagName("dialog");
            if (nl != null && nl.getLength() > 0) {
                for (int i = 0; i < nl.getLength(); i++) {
                    Element el = (Element)nl.item(i);
                    String id;
                    if (el.hasAttribute("dialog_id")) {
                        id = el.getAttribute("dialog_id");
                    } else {
                        continue;
                    }

                    NodeList nodelist = el.getElementsByTagName("message");
                    String[] lines;
                    if (nodelist != null && nodelist.getLength() > 0) {
                        lines = new String[nodelist.getLength()];
                        for (int ii = 0; ii < nodelist.getLength(); ii++) {
                            if (nodelist.item(ii) == null)
                                continue;
                            lines[ii] = ((Element)nodelist.item(ii)).getFirstChild().getNodeValue();
                        }
                    } else {
                        lines = new String[0];
                    }

                    nodelist = el.getElementsByTagName("header");
                    String[] headers;
                    if (nodelist != null && nodelist.getLength() > 0) {
                        headers = new String[nodelist.getLength()];
                        for (int ii = 0; ii < nodelist.getLength(); ii++) {
                            if (nodelist.item(ii) == null)
                                continue;
                            headers[ii] = ((Element)nodelist.item(ii)).getFirstChild().getNodeValue();
                        }
                    } else {
                        headers = new String[0];
                    }

                    nodelist = el.getElementsByTagName("image");
                    String[] images;
                    if (nodelist != null && nodelist.getLength() > 0) {
                        images = new String[nodelist.getLength()];
                        for (int ii = 0; ii < nodelist.getLength(); ii++) {
                            if (nodelist.item(ii) == null)
                                continue;
                            images[ii] = ((Element)nodelist.item(ii)).getFirstChild().getNodeValue();
                        }
                    } else {
                        images = new String[0];
                    }

                    Dialog temp = new Dialog(lines, images, headers, id);
                    text.put(id, temp);
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return false;
        } catch (SAXException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static Dialog getDialog(String ID) {
        if (!text.containsKey(ID))
            return null;
        return text.get(ID);
    }
}
