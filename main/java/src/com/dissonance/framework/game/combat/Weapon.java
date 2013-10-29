package com.dissonance.framework.game.combat;

import com.dissonance.framework.game.item.impl.WeaponItem;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
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

public class Weapon {

    private int attack;
    private int defense;
    private int speed;
    private int vigor;
    private int stamina;
    private int willPower;
    private int focus;
    private int marksmanship;
    private int magicResistance;
    private int range;

    //==Spell Animation Stuff==
    //If this weapon is not a spell, then play the normal sprite
    //attack animation
    private boolean isSpell;
    private String spellSpriteClass;
    private int animationRow;
    private int animationSpeed;


    private static HashMap<String, Weapon> weapons = new HashMap<String, Weapon>();

    private Weapon() { }

    public static Weapon getWeapon(String weapon) {
        if (weapons.containsKey(weapon))
            return weapons.get(weapon);
        else {
            String w_path = "weapons/" + weapon + ".xml";
            InputStream in = Weapon.class.getClassLoader().getResourceAsStream(w_path);

            if (in == null)
                return null;
            else {
                DocumentBuilder db = null;
                try {
                    db = SpriteTexture.DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
                    Document dom = db.parse(in);
                    Element elm = dom.getDocumentElement();

                    NodeList nl = elm.getElementsByTagName("weapon");
                    Weapon w = new Weapon();
                    if (nl != null && nl.getLength() > 0) {
                        for (int i = 0; i < nl.getLength(); i++) {
                            Element el = (Element)nl.item(i);

                            String nodeName = el.getNodeName();

                            if (nodeName.equals("attack"))
                                w.attack = Integer.parseInt(el.getFirstChild().getNodeValue());
                            else if (nodeName.equals("defense"))
                                w.defense = Integer.parseInt(el.getFirstChild().getNodeValue());
                            else if (nodeName.equals("speed"))
                                w.speed = Integer.parseInt(el.getFirstChild().getNodeValue());
                            else if (nodeName.equals("vigor"))
                                w.vigor = Integer.parseInt(el.getFirstChild().getNodeValue());
                            else if (nodeName.equals("stamina"))
                                w.stamina = Integer.parseInt(el.getFirstChild().getNodeValue());
                            else if (nodeName.equals("willpower"))
                                w.willPower = Integer.parseInt(el.getFirstChild().getNodeValue());
                            else if (nodeName.equals("focus"))
                                w.focus = Integer.parseInt(el.getFirstChild().getNodeValue());
                            else if (nodeName.equals("marksmanship"))
                                w.marksmanship = Integer.parseInt(el.getFirstChild().getNodeValue());
                            else if (nodeName.equals("magicresistance"))
                                w.magicResistance = Integer.parseInt(el.getFirstChild().getNodeValue());
                            else if (nodeName.equals("range"))
                                w.range = Integer.parseInt(el.getFirstChild().getNodeValue());
                            else if (nodeName.equals("spell"))
                                w.isSpell = el.getFirstChild().getNodeValue().equalsIgnoreCase("true");
                            else if (nodeName.equals("spellSpriteClass"))
                                w.spellSpriteClass = el.getFirstChild().getNodeValue();
                            else if (nodeName.equals("animationRow"))
                                w.animationRow = Integer.parseInt(el.getFirstChild().getNodeValue());
                            else if (nodeName.equals("animationSpeed"))
                                w.animationSpeed = Integer.parseInt(el.getFirstChild().getNodeValue());
                        }
                    }

                    in.close();

                    weapons.put(weapon, w);

                    return w;
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                    return null;
                } catch (SAXException e) {
                    e.printStackTrace();
                    return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

    public WeaponItem createItem(CombatSprite holder) {
        return new WeaponItem(holder, this);
    }
}
