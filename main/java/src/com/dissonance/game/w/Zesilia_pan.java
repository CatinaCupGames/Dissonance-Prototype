package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.game.sprites.SideSign1;
import com.dissonance.game.sprites.SideSign2;
import com.dissonance.game.sprites.WallLight;

import java.awt.*;

public class Zesilia_pan implements WorldLoader {
    public static WallLight l2;
    public static WallLight l3;
    public static WallLight l4;
    public static WallLight l5;
    public static WallLight l6;
    public static SideSign1 s1;
    public static SideSign2 s2;


    @Override
    public void onLoad(World w) {
        w.setWorldBrightness(0.79f);

        //w.createLight(0f, 0f, 1.1f, 1.f);
        //w.createLight(0f, 0f, 1.1f, 1.f);



        l2 = new WallLight();
        w.loadAndAdd(l2);
        l2.setX(19*16);
        l2.setY(22*16);
        w.createLight(19*16, 22*16, 1.8f, 0.05f);
        w.createLight(19*16, 22*16, 1.5f, 0.85f);


        l3 = new WallLight();
        w.loadAndAdd(l3);
        l3.setX(36*16);
        l3.setY(22*16);
        w.createLight(36*16, 22*16, 1.8f, 0.05f);
        w.createLight(36*16, 22*16, 1.5f, 0.85f);

        l4 = new WallLight();
        w.loadAndAdd(l4);
        l4.setX(43*16);
        l4.setY(22*16);
        w.createLight(43*16, 22*16, 1.8f, 0.05f);
        w.createLight(43*16, 22*16, 1.5f, 0.85f);

        l5 = new WallLight();
        w.loadAndAdd(l5);
        l5.setX(71*16);
        l5.setY(22*16);
        w.createLight(71*16, 22*16, 1.8f, 0.05f);
        w.createLight(71*16, 22*16, 1.5f, 0.85f);

        l6 = new WallLight();
        w.loadAndAdd(l6);
        l6.setX(74*16);
        l6.setY(22 * 16);
        w.createLight(74*16, 22*16, 1.8f, 0.05f);
        w.createLight(74*16, 22*16, 1.5f, 0.85f);

        s1 = new SideSign1();
        w.loadAndAdd(s1);
        s1.setX(50*16);
        s1.setY(20*16);
        //w.createLight(50*16, 19*16, 1.0f, 0.03f, Color.RED);
        //w.createLight(50*16, 20*16, 1.0f, 0.03f, Color.RED);

        s2 = new SideSign2();
        w.loadAndAdd(s2);
        s2.setX(62*16);
        s2.setY(22*16);
        w.createLight(62*16, 20*16, 1.4f, 0.1f, Color.RED);
        w.createLight(62*16, 22*16, 1.4f, 0.1f, Color.RED);





    }

    @Override
    public void onDisplay(World world) {

    }
}
