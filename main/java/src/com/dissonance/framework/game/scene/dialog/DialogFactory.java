package com.dissonance.framework.game.scene.dialog;

import com.dissonance.framework.render.texture.sprite.SpriteTexture;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;

public class DialogFactory {
    private static final HashMap<String, Dialog>  text = new HashMap<String, Dialog>();

    public static boolean loadDialog(InputStream in) {
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
                    CustomString[] lines;
                    if (nodelist != null && nodelist.getLength() > 0) {
                        lines = new CustomString[nodelist.getLength()];
                        for (int ii = 0; ii < nodelist.getLength(); ii++) {
                            if (nodelist.item(ii) == null)
                                continue;
                            String text = nodelist.item(ii).getFirstChild().getNodeValue();
                            Style style = Style.NORMAL;
                            Color color = Color.WHITE;
                            long speed = 15L;
                            if (nodelist.item(ii).getAttributes().getNamedItem("style") != null) {
                                String stype = nodelist.item(ii).getAttributes().getNamedItem("style").getNodeValue();
                                for (Style l : Style.values()) {
                                    if (l.name().toLowerCase().equals(stype)) {
                                        style = l;
                                        break;
                                    }
                                }
                            }
                            if (nodelist.item(ii).getAttributes().getNamedItem("color") != null) {
                                String scolor = nodelist.item(ii).getAttributes().getNamedItem("color").getNodeValue();
                                try {
                                    Field field = Class.forName("java.awt.Color").getField(scolor);
                                    color = (Color)field.get(null);
                                } catch (Exception e) {
                                    color = Color.WHITE;
                                }
                            }

                            if (nodelist.item(ii).getAttributes().getNamedItem("speed") != null) {
                                String sSpeed = nodelist.item(ii).getAttributes().getNamedItem("speed").getNodeValue();
                                try {
                                    speed = Long.parseLong(sSpeed);
                                } catch (Throwable t) {
                                    speed = 15L;
                                }
                            }
                            boolean append = false;
                            if (nodelist.item(ii).getAttributes().getNamedItem("type") != null) {
                                append = nodelist.item(ii).getAttributes().getNamedItem("type").getNodeValue().equalsIgnoreCase("append");
                            }
                            lines[ii] = new CustomString(text, style, append, color, speed);
                        }
                    } else {
                        lines = new CustomString[0];
                    }

                    nodelist = el.getElementsByTagName("header");
                    String[] headers;
                    if (nodelist != null && nodelist.getLength() > 0) {
                        headers = new String[nodelist.getLength()];
                        for (int ii = 0; ii < nodelist.getLength(); ii++) {
                            if (nodelist.item(ii) == null)
                                continue;
                            headers[ii] = nodelist.item(ii).getFirstChild().getNodeValue();
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
                            images[ii] = nodelist.item(ii).getFirstChild().getNodeValue();
                        }
                    } else {
                        images = new String[0];
                    }

                    Dialog temp = new Dialog(lines, images, headers, id);
                    text.put(id, temp);
                }
            }

            in.close();
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

    public static boolean loadDialog() {
        InputStream in = DialogFactory.class.getClassLoader().getResourceAsStream("IND/dialog.xml");
        return loadDialog(in);
    }

    public static boolean loadDialog(File file) {
        InputStream in;
        try {
            in = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return loadDialog(in);
    }

    public static Dialog getDialog(String ID) {
        if (!text.containsKey(ID))
            return null;
        return text.get(ID);
    }
}
