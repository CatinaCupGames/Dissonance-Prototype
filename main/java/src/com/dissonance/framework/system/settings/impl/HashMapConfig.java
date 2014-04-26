package com.dissonance.framework.system.settings.impl;

import com.dissonance.framework.system.settings.ConfigParser;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

public class HashMapConfig<K extends ConfigParser> extends HashMap<String, K> implements ConfigParser {
    @Override
    public void parse(NodeList childNodes) {
        if (childNodes != null && childNodes.getLength() > 0) {
            for (int i = 0; i < childNodes.getLength(); i++) {
                if (!(childNodes.item(i) instanceof Element))
                    continue;
                Element item = (Element)childNodes.item(i);
                if (item.getNodeName().equals("item")) {
                    NodeList list = item.getChildNodes();
                    if (list != null && list.getLength() > 0) {
                        String key = null;
                        String value;
                        for (int ii = 0; ii < list.getLength(); ii++) {
                            if (!(list.item(ii) instanceof Element))
                                continue;
                            Element item2 = (Element)list.item(ii);
                            if (item2.getNodeName().equals("key")) {
                                key = item2.getFirstChild().getNodeValue();
                            } else if (item2.getNodeName().equals("value")) {
                                if (key == null)
                                    continue;
                                NodeList deeper = item2.getChildNodes();
                                K object = (K) GenericConfigParse.parseObject(deeper);
                                put(key, object);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void save(ArrayList<String> lines) {
        for (String key : keySet()) {
            K value = get(key);
            lines.add("<item>");
            lines.add("<key>" + key + "</key>");
            lines.add("<value>");
            GenericConfigParse.saveObject(value, lines);
            lines.add("</value>");
            lines.add("</item>");
        }
    }
}
