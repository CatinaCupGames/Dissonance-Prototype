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

    private WeaponType type;

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
    private int swipeRange;

    //==Spell Animation Stuff==
    //If this weapon is not a spell, then play the normal sprite
    //attack animation
    private boolean isGun; //Whether this weapon is a gun
    //TODO This may not be needed..
    private int animationRow; //The animation row to play on the sprite
    //TODO This may not be needed..
    private int animationSpeed; //The animation speed to play at for the sprite


    private static HashMap<String, Weapon> weapons = new HashMap<String, Weapon>();
    private String name;

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
                    NodeList nl = elm.getChildNodes();
                    Weapon w = new Weapon();
                    if (nl != null && nl.getLength() > 0) {
                        for (int i = 0; i < nl.getLength(); i++) {
                            if (!(nl.item(i) instanceof Element))
                                continue;
                            Element el = (Element)nl.item(i);

                            String nodeName = el.getNodeName();

                            if (nodeName.equals("type"))
                                w.type = WeaponType.fromString(el.getFirstChild().getNodeValue());
                            else if (nodeName.equals("attack"))
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
                            else if (nodeName.equals("swipe"))
                                w.swipeRange = Integer.parseInt(el.getFirstChild().getNodeValue());
                            else if (nodeName.equals("gun"))
                                w.isGun = el.getFirstChild().getNodeValue().equalsIgnoreCase("true");
                            else if (nodeName.equals("animationRow"))
                                w.animationRow = Integer.parseInt(el.getFirstChild().getNodeValue());
                            else if (nodeName.equals("animationSpeed"))
                                w.animationSpeed = Integer.parseInt(el.getFirstChild().getNodeValue());
                            else if (nodeName.equals("name"))
                                w.name = el.getFirstChild().getNodeValue();
                        }
                    }

                    in.close();

                    if (w.name == null)
                        w.name = weapon;
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

    public String getName() {
        return name;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isGun() {
        return isGun;
    }

    public int getVigor() {
        return vigor;
    }

    public int getStamina() {
        return stamina;
    }

    public int getWillPower() {
        return willPower;
    }

    public int getFocus() {
        return focus;
    }

    public int getMarksmanship() {
        return marksmanship;
    }

    public int getMagicResistance() {
        return magicResistance;
    }

    public int getRange() {
        return range;
    }

    public int getAnimationRow() {
        return animationRow;
    }

    public int getAnimationSpeed() {
        return animationSpeed;
    }

    public int getSwipeRange() {
        return swipeRange;
    }
}
