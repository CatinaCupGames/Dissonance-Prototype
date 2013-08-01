package com.tog.framework.render.texture.sprite;

import com.tog.framework.render.texture.Texture;
import com.tog.framework.system.utils.Validator;
import org.lwjgl.util.vector.Vector2f;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidParameterException;

public class SpriteTexture extends Texture {
    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

    private SpriteAnimationInfo[] animations;
    private int row;
    private int step;
    private int width;
    private int height;
    private int max_frames;


    public static SpriteTexture retriveSpriteTexture(String sprite_name) throws IOException {
        SpriteTexture texture = new SpriteTexture(Texture.retriveTexture("sprite/" + sprite_name + "/" + sprite_name + "_sheet.png"));
        InputStream in = texture.getClass().getClassLoader().getResourceAsStream("sprite/" + sprite_name + "/" + sprite_name + ".xml");
        if (in != null) {
            try {
                DocumentBuilder db = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
                Document dom = db.parse(in);
                Element elm = dom.getDocumentElement();
                NodeList nl = elm.getElementsByTagName("animation");
                if (nl != null && nl.getLength() > 0) {
                    texture.animations = new SpriteAnimationInfo[nl.getLength()];
                    if (elm.hasAttribute("width")) {
                        try {
                            texture.width = Integer.parseInt(elm.getAttribute("width"));
                            //texture.setTextureSize();
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                    if (elm.hasAttribute("height")) {
                        try {
                            texture.height = Integer.parseInt(elm.getAttribute("height"));
                            //texture.setTextureSize();
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                    if (elm.hasAttribute("max_frames")) {
                        try {
                            texture.max_frames = Integer.parseInt(elm.getAttribute("max_frames"));
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                    for (int i = 0; i < nl.getLength(); i++) {
                        Element el = (Element)nl.item(i);
                        String name = "";
                        int row = 0;
                        long default_speed = 1;
                        boolean loop = false;
                        int frames = 1;
                        if (el.hasAttribute("name")) {
                            name = el.getAttribute("name");
                        }
                        if (el.hasAttribute("row")) {
                            try {
                                row = Integer.parseInt(el.getAttribute("row"));
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        }
                        if (el.hasAttribute("loop")) {
                            loop = el.getAttribute("loop").toLowerCase().equals("true");
                        }
                        if (el.hasAttribute("frames")) {
                            try {
                                frames = Integer.parseInt(el.getAttribute("frames"));
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        }
                        if (el.hasAttribute("default_speed")) {
                            try {
                                String temp = el.getAttribute("default_speed");
                                String type = temp.substring(temp.length() - 2);
                                if (type.equals("ms")) {
                                    default_speed = Long.parseLong(temp.substring(0, temp.length() - 2));
                                } else if (type.equals("sc")) {
                                    default_speed = Long.parseLong(temp.substring(0, temp.length() - 2)) * 1000;
                                } else if (type.equals("mn")) {
                                    default_speed = (Long.parseLong(temp.substring(0, temp.length() - 2)) * 1000) * 60000;
                                } else {
                                    default_speed = Long.parseLong(temp.substring(0, temp.length() - 2));
                                }
                            } catch (Throwable t) {
                                t.printStackTrace();
                            }
                        }

                        texture.animations[i] = new SpriteAnimationInfo(name, default_speed, row, frames, loop);
                    }
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            }
        }
        return texture;
    }

    protected SpriteTexture(int targetId, int textureId) {
        super(targetId, textureId);
    }


    protected SpriteTexture(Texture texture) {
        super(texture);
    }

    protected void setTextureSize() {
        int texWidth = 2;
        int texHeight = 2;

        // find the closest power of 2 for the width and height
        // of the produced texture
        while (texWidth < width) {
            texWidth *= 2;
        }
        while (texHeight < height) {
            texHeight *= 2;
        }

        super.setTextureWidth(texWidth);
        super.setTextureHeight(texHeight);
    }


    public void step() {
        if (step + 1 < animations[row].size() || animations[row].doesLoop()) {
            step++;
            if (step >= animations[row].size())
                step = 0;
        }
    }

    public SpriteAnimationInfo setCurrentAnimation(String name) {
        int index = indexOfAnimation(name);
        if (index == -1)
            return null;
        setCurrentAnimation(index);
        return getAnimationInfo(name);
    }

    public void setCurrentAnimation(int index) {
        Validator.validateNotBelow(index, 0, "index");
        Validator.validateNotOver(index, animations.length - 1, "index");

        row = index;
    }

    public SpriteAnimationInfo getAnimationInfo(String name) {
        int index = indexOfAnimation(name);
        if (index == -1)
            return null;
        return animations[index];
    }

    public SpriteAnimationInfo getCurrentAnimation() {
        return animations[row];
    }

    public int indexOfAnimation(String name) {
        for (int i = 0; i < animations.length; i++) {
            if (animations[i].getName().equals(name))
                return i;
        }
        return -1;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    public static final int BOTTOM_LEFT = 0;
    public static final int BOTTOM_RIGHT = 1;
    public static final int TOP_RIGHT = 2;
    public static final int TOP_LEFT = 3;
    public Vector2f getTextureCord(int type) {
        float st = 1.0f/max_frames;
        float rt = 1.0f/(float)animations.length;
        float cx = (float)(step % animations[row].size()) / max_frames;
        float cy = (float)(row / animations.length) / height;
        if (type == 0) {
            return new Vector2f(cx, cy);
        }  else if (type == 1) {
            return new Vector2f(cx + st, cy);
        }  else if (type == 2) {
            return new Vector2f(cx + st, cy + rt);
        }  else if (type == 3) {
            return new Vector2f(cx, cy + rt);
        } else {
            throw new InvalidParameterException("The parameter \"type\"'s value can only be 0, 1, 2, or 3");
        }
    }
}
