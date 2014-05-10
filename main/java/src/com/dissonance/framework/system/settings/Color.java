package com.dissonance.framework.system.settings;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.lang.annotation.ElementType;
import java.util.ArrayList;

public class Color implements ConfigParser {
    public Color() { }

    public Color(float brightness, float contrast, float saturation, float red, float green, float blue)
    {
        this.brightness = brightness;
        this.contrast = contrast;
        this.saturation = saturation;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public float brightness;
    public float contrast;
    public float saturation;
    public float red;
    public float green;
    public float blue;

    @Override
    public void parse(NodeList childNodes) {
        if (childNodes != null && childNodes.getLength() > 0) {
            for (int ii = 0; ii < childNodes.getLength(); ii++) {
                if (!(childNodes.item(ii) instanceof Element))
                    continue;
                Element item = (Element)childNodes.item(ii);
                if (item.getNodeName().equals("brightness"))
                    brightness = Float.parseFloat(item.getFirstChild().getNodeValue());
                else if (item.getNodeName().equals("contrast"))
                    contrast = Float.parseFloat(item.getFirstChild().getNodeValue());
                else if (item.getNodeName().equals("saturation"))
                    saturation = Float.parseFloat(item.getFirstChild().getNodeValue());
                else if (item.getNodeName().equals("red"))
                    red = Float.parseFloat(item.getFirstChild().getNodeValue());
                else if (item.getNodeName().equals("green"))
                    green = Float.parseFloat(item.getFirstChild().getNodeValue());
                else if (item.getNodeName().equals("blue"))
                    blue = Float.parseFloat(item.getFirstChild().getNodeValue());
            }
        }
    }

    @Override
    public void save(ArrayList<String> lines) {
        lines.add("<brightness>" + brightness + "</brightness>");
        lines.add("<contrast>" + contrast + "</contrast>");
        lines.add("<saturation>" + saturation + "</saturation>");
        lines.add("<red>" + red + "</red>");
        lines.add("<green>" + green + "</green>");
        lines.add("<blue>" + blue + "</blue>");
    }
}
