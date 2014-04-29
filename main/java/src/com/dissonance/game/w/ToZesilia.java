package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.game.sprites.Farrand;
import com.dissonance.game.sprites.Jeremiah;
import com.dissonance.game.sprites.environment.Tree1;
import com.dissonance.game.sprites.environment.Tree2;

import java.util.Random;

public class ToZesilia implements WorldLoader {
    public static Farrand var1;
    public static Jeremiah var2;
    @Override
    public void onLoad(World w) {
        var1 = new Farrand();
        w.loadAndAdd(var1);
        var1.setX(1208.0275f);
        var1.setY(338.1298f);

        var2 = new Jeremiah();
        w.loadAndAdd(var2);
        var2.setX(1256.2601f);
        var2.setY(353.97f);


        final Random random = new Random();
        /*for (int y = 7; y <= 20; y++) {
            for (int x = 5; x <= 75; x++) {
                int num = random.nextInt(2);
                switch (num) {
                    case 0:
                        Tree1 tree = new Tree1();
                        w.loadAndAdd(tree);
                        tree.setX(x * 16);
                        tree.setY(y * 16);
                        break;
                    case 1:
                        Tree2 tree2 = new Tree2();
                        w.loadAndAdd(tree2);
                        tree2.setX(x * 16);
                        tree2.setY(y * 16);
                        break;
                }
            }
        }*/
    }

    @Override
    public void onDisplay(World world) {
    }
}