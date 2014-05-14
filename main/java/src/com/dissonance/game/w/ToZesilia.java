package com.dissonance.game.w;

import com.dissonance.framework.game.ai.astar.FastMath;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.game.sprites.Farrand;
import com.dissonance.game.sprites.Jeremiah;
import com.dissonance.game.sprites.StreetLamp;
import com.dissonance.game.sprites.environment.Tree1;
import com.dissonance.game.sprites.environment.Tree2;
import com.dissonance.game.sprites.office.Gatehouse;

import java.util.Random;

public class ToZesilia implements WorldLoader {
    public static Farrand farrand;
    public static Jeremiah jeremiah;
    public static StreetLamp lamp1;
    public static StreetLamp lamp2;
    public static StreetLamp lamp3;
    public static StreetLamp lamp4;
    public static Gatehouse gatehouse1;
    public static Gatehouse gatehouse2;
    @Override
    public void onLoad(World w) {
        farrand = new Farrand();
        w.loadAndAdd(farrand);
        farrand.setX(2808.0275f);
        farrand.setY(338.1298f);

        jeremiah = new Jeremiah();
        w.loadAndAdd(jeremiah);
        jeremiah.setX(2856.2601f);
        jeremiah.setY(353.97f);




        final Random random = new Random();
        for (int y = 2; y <= 8; y++) {
            for (int x = 25; x <= 100; x++) {
                int num = random.nextInt(2);
                switch (num) {
                    case 0:
                        Tree1 tree = new Tree1();
                        w.loadAndAdd(tree);
                        tree.setX(FastMath.fastCeil(x * 32f) + 5f);
                        tree.setY(FastMath.fastCeil(y * 32f) + 5f);
                        break;
                    case 1:
                        Tree2 tree2 = new Tree2();
                        w.loadAndAdd(tree2);
                        tree2.setX(FastMath.fastCeil(x * 32f) + 5f);
                        tree2.setY(FastMath.fastCeil(y * 32f) + 5f);
                        break;
                }
            }
        }
        for (int y = 2; y <= 8; y++) {
            for (int x = 20; x <= 24; x++) {
                int num = random.nextInt(4);
                switch (num) {
                    case 0:
                        Tree1 tree = new Tree1();
                        w.loadAndAdd(tree);
                        tree.setX(x * 32);
                        tree.setY(y * 32);
                        break;
                    case 1:
                        Tree2 tree2 = new Tree2();
                        w.loadAndAdd(tree2);
                        tree2.setX(x * 32);
                        tree2.setY(y * 32);
                        break;
                }
            }
        }
        for (int y = 2; y <= 8; y++) {
            for (int x = 10; x <= 19; x++) {
                int num = random.nextInt(16);
                switch (num) {
                    case 0:
                        Tree1 tree = new Tree1();
                        w.loadAndAdd(tree);
                        tree.setX(x * 32);
                        tree.setY(y * 32);
                        break;
                    case 1:
                        Tree2 tree2 = new Tree2();
                        w.loadAndAdd(tree2);
                        tree2.setX(x * 32);
                        tree2.setY(y * 32);
                        break;
                }
            }
        }
        for (int y = 15; y <= 18; y++) {
            for (int x = 25; x <= 100; x++) {
                int num = random.nextInt(2);
                switch (num) {
                    case 0:
                        Tree1 tree = new Tree1();
                        w.loadAndAdd(tree);
                        tree.setX(x * 32);
                        tree.setY(y * 32);
                        break;
                    case 1:
                        Tree2 tree2 = new Tree2();
                        w.loadAndAdd(tree2);
                        tree2.setX(x * 32);
                        tree2.setY(y * 32);
                        break;
                }
            }
        }

        for (int y = 15; y <= 18; y++) {
            for (int x = 20; x <= 24; x++) {
                int num = random.nextInt(4);
                switch (num) {
                    case 0:
                        Tree1 tree = new Tree1();
                        w.loadAndAdd(tree);
                        tree.setX(x * 32);
                        tree.setY(y * 32);
                        break;
                    case 1:
                        Tree2 tree2 = new Tree2();
                        w.loadAndAdd(tree2);
                        tree2.setX(x * 32);
                        tree2.setY(y * 32);
                        break;
                }
            }
        }
        for (int y = 15; y <= 18; y++) {
            for (int x = 10; x <= 19; x++) {
                int num = random.nextInt(16);
                switch (num) {
                    case 0:
                        Tree1 tree = new Tree1();
                        w.loadAndAdd(tree);
                        tree.setX(x * 32);
                        tree.setY(y * 32);
                        break;
                    case 1:
                        Tree2 tree2 = new Tree2();
                        w.loadAndAdd(tree2);
                        tree2.setX(x * 32);
                        tree2.setY(y * 32);
                        break;
                }
            }
        }

        lamp1 = new StreetLamp();
        w.loadAndAdd(lamp1);
        lamp1.setX(136f * 16);
        lamp1.setY(20f*16);

        lamp2 = new StreetLamp();
        w.loadAndAdd(lamp2);
        lamp2.setX(92f * 16);
        lamp2.setY(20f*16);

        gatehouse1 = new Gatehouse();
        w.loadAndAdd(gatehouse1);
        gatehouse1.setX(12.9f * 16);
        gatehouse1.setY(16f * 16);

        gatehouse2 = new Gatehouse();
        w.loadAndAdd(gatehouse2);
        gatehouse2.setX(12.9f * 16);
        gatehouse2.setY(40f * 16);



        w.setWorldBrightness(0.8f);
        w.createLight(165f * 16, 18f * 16, 1.4f, 0.9f);
        w.createLight(136f*16, 16.5f*16, 1.8f, 0.03f);
        w.createLight(136f*16, 14f*16, 1.4f, 0.9f);
        w.createLight(13f*16, 18f*16, 1.4f, 0.9f);
        w.createLight(92f*16, 14f*16, 1.4f, 0.9f);
        w.createLight(92f*16, 16.5f*16, 1.8f, 0.03f);

    }

    @Override
    public void onDisplay(World world) {
    }
}