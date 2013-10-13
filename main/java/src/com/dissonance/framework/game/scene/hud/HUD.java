package com.dissonance.framework.game.scene.hud;

import com.dissonance.framework.game.GameSettings;
import com.dissonance.framework.game.sprites.UIElement;
import com.dissonance.framework.game.sprites.impl.PlayableSprite;
import com.dissonance.framework.render.Camera;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver Yasuna
 * Date: 10/11/13
 * Time: 3:02 PM
 */

public class HUD extends UIElement
{
    private static BufferedImage healthbar;
    private static BufferedImage boarder;
    private static BufferedImage mana;

    static {
        InputStream in = HUD.class.getClassLoader().getResourceAsStream("healthbar.bmp");
        if (in != null) {
            try {
                healthbar = ImageIO.read(in);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        in = HUD.class.getClassLoader().getResourceAsStream("borderbar.bmp");
        if (in != null) {
            try {
                boarder = ImageIO.read(in);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        in = HUD.class.getClassLoader().getResourceAsStream("manabar.bmp");
        if (in != null) {
            try {
                mana = ImageIO.read(in);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public HUD(String name)
    {
        super(name);
    }

//    @Override
//    public void draw(Graphics2D g)
//    {
//        /****** VARIABLES ******/
//        PlayableSprite player = PlayableSprite.getCurrentlyPlayingSprite();
//        String name = player.getSpriteName();
//        name = "WyattW"; // TODO: Remove this once we actually can get the current player.
//        int nameWidth;
//        /****** VARIABLES ******/
//
//        /****** SETUP ******/
//        //g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        //g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
//        //g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
//
//        //g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
//        /****** SETUP ******/
//
//        /****** NAME ******/
//        g.setFont(new Font("Arial", Font.BOLD, 12));
//
//        nameWidth = g.getFontMetrics().stringWidth(name) + 4;
//
//        g.setPaint(new GradientPaint(
//                0.0f, (getHeight() / 4.0f) + (getHeight() / 8.0f), Color.LIGHT_GRAY,
//                0.0f, ((getHeight() / 4.0f) + (getHeight() / 8.0f)) + (getHeight() / 4.0f), Color.GRAY
//        ));
//        g.fillRect(0, (getHeight() / 4) + (getHeight() / 8), nameWidth, getHeight() / 4);
//
//        g.setColor(Color.BLACK);
//        g.drawString(name, 3.0f, (getHeight() / 4.0f) + (getHeight() / 8.0f) + g.getFont().getSize2D());
//
//        g.setColor(Color.BLACK);
//        g.drawLine(0, (getHeight() / 4) + (getHeight() / 8), nameWidth, (getHeight() / 4) + (getHeight() / 8)); // Top.
//        g.drawLine(0, (getHeight() / 4) + (getHeight() / 8), 0, ((getHeight() / 4) + (getHeight() / 8)) + (getHeight() / 4)); // Left.
//        g.drawLine(nameWidth, (getHeight() / 4) + (getHeight() / 8), nameWidth, ((getHeight() / 4) + (getHeight() / 8)) + (getHeight() / 4)); // Right.
//        /****** NAME ******/
//
//        /****** STATS ******/
//        g.setPaint(new GradientPaint(0.0f, ((getHeight() / 4.0f) + (getHeight() / 8.0f)) + (getHeight() / 4.0f), Color.GRAY, 0.0f, getHeight(), Color.DARK_GRAY));
//        g.fillRect(0, (getHeight() / 2) + (getHeight() / 8), getWidth(), (getHeight() / 2) - (getHeight() / 8));
//
//        g.setColor(Color.BLACK);
//        g.drawLine(nameWidth, (getHeight() / 2) + (getHeight() / 8), nameWidth + ((getWidth() - 1) - nameWidth), (getHeight() / 2) + (getHeight() / 8)); // Top.
//        g.drawLine(0, getHeight() - 1, getWidth() - 1, getHeight() - 1); // Bottom.
//        g.drawLine(0, (getHeight() / 2) + (getHeight() / 8), 0, getHeight() - 1); // Left.
//        g.drawLine(getWidth() - 1, (getHeight() / 2) + (getHeight() / 8), getWidth() - 1, getHeight());
//        /****** STATS ******/
//    }


    @Override
    public void draw(Graphics2D g)
    {
        int x = 0;
        int y = (int)(getHeight() / (8.0f / 3.0f));
        int width;
        int height;

        /****** NAME ******/
        y -= 2;
        width = (getWidth() / 4) - 15;
        height = getHeight() / 4;

        String playerName = PlayableSprite.getCurrentlyPlayingSprite().getSpriteName();
        playerName = "Amynta"; // TODO: Remove this once we can actually get the player name.
        //       This was added just to find out the max width the
        //       HUD needs for the player name area.

        // TODO: Should be -3.
        int offsetx = -1;
        int offsety = +0;

        g.setColor(Color.BLACK);
        g.fillRect(x, y + offsety, width + 1 + offsetx, height + 1 + offsety);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString(playerName, x + ((width - g.getFontMetrics().stringWidth(playerName)) / 2), y + g.getFont().getSize() + offsety);

        g.setColor(Color.GREEN);
        g.drawLine(x, y + offsety, x + width + offsetx, y + offsety);
        if(offsety == 0)
        {
            g.drawLine(x, y + height, x + width + offsetx, y + height);
        }
        g.drawLine(x, y + offsety, x, y + height + offsety);
        g.drawLine(x + width + offsetx, y + offsety, x + width + offsetx, y + height + offsety);
        /****** NAME ******/

        /****** STATS ******/
        y += height + 1;
        width = getWidth() - 1;
        height = (int)(getHeight() / (8.0f / 3.0f));

        g.setColor(Color.BLACK);
        g.fillRect(x, y, width + 1, height + 1);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 10));
        int textCenter = (((height / 2) - g.getFontMetrics().getHeight()) / 2) + g.getFontMetrics().getAscent(); //=10
        g.drawString("HP ", x + 2, y + textCenter + 1);
        g.drawString("SP ", x + 2, y + (textCenter * 2) + 1);

        g.drawImage(boarder, x + g.getFontMetrics().stringWidth("HP "), y + 3, 65, 9, null);
        g.drawImage(healthbar, x + g.getFontMetrics().stringWidth("HP "), y + 4, 65, 7, null);
        //g.drawImage(boarder, x + g.getFontMetrics().stringWidth("HP "), y + 4, 64, 6, null);
        /*g.setColor(Color.RED);
        g.fillRect(x + g.getFontMetrics().stringWidth("HP "), y + 4, 64 + 1, 6 + 1);
        g.setColor(Color.WHITE);
        g.drawRect(x + g.getFontMetrics().stringWidth("HP "), y + 4, 64, 6);*/

        g.drawImage(boarder, x + g.getFontMetrics().stringWidth("SP "), y + textCenter + 4, 65, 7, null);
        g.drawImage(mana, x + g.getFontMetrics().stringWidth("SP "), y + textCenter + 4, 65, 7, null);
        /*g.setColor(Color.BLUE);
        g.fillRect(x + g.getFontMetrics().stringWidth("SP "), y + textCenter + 4, 64 + 1, 6 + 1);
        g.setColor(Color.WHITE);
        g.drawRect(x + g.getFontMetrics().stringWidth("SP "), y + textCenter + 4, 64, 6);*/

        // TODO: Add cash, level up meter, and anything else requested by developers.

        g.setColor(Color.GREEN);
        g.drawLine(x/* + ((getWidth() / 4) - 15) - 1*/, y, x + width, y);
        g.drawLine(x, y + height, x + width, y + height);
        g.drawLine(x, y, x, y + height);
        g.drawLine(x + width, y, x + width, y + height);
        /****** STATS ******/
    }

    @Override
    public void init()
    {
        setX(Camera.getX());
        setY(Camera.getY());

        setWidth(256.0f);
        setHeight(64.0f);
    }

    @Override
    public void update()
    {
        if(newX != Camera.getX() || newY != Camera.getY())
        {
            newX = Camera.getX() + (getWidth() / 2.0f);
            newY = Camera.getY() + (getHeight() / 2.0f);
            newY += (GameSettings.Display.window_height / 2.0f) - getHeight();

            setX(newX);
            setY(newY);
        }
    }

    private float newX;
    private float newY;
}
