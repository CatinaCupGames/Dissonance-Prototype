package com.dissonance.framework.system.settings;

import org.w3c.dom.NodeList;

import java.util.ArrayList;

public interface ConfigParser {

    public void parse(NodeList childNodes);

    public void save(ArrayList<String> lines);
}
