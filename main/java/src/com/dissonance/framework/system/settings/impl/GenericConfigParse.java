package com.dissonance.framework.system.settings.impl;

import com.dissonance.framework.system.settings.ConfigParser;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

public class GenericConfigParse {
    public static ConfigParser parseObject(NodeList list) {
        if (list != null && list.getLength() > 0) {
            String class_ = null;
            for (int ii = 0; ii < list.getLength(); ii++) {
                if (!(list.item(ii) instanceof Element))
                    continue;
                Element item2 = (Element)list.item(ii);
                if (item2.getNodeName().equals("class")) {
                    class_ = item2.getFirstChild().getNodeValue();
                } else if (item2.getNodeName().equals("parse")) {
                    if (class_ == null)
                        break;
                    try {
                        Class<? extends ConfigParser> class_item = (Class<? extends ConfigParser>) Class.forName(class_); //weeeeaaak typing yum
                        ConfigParser object = class_item.newInstance();
                        NodeList children = item2.getChildNodes();
                        object.parse(children);
                        return object;
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    public static void saveObject(ConfigParser object, ArrayList<String> currentConfig) {
        currentConfig.add("<class>" + object.getClass().getCanonicalName() + "</class>");
        currentConfig.add("<parse>");
        object.save(currentConfig);
        currentConfig.add("</parse>");
    }
}
