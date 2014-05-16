package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.Farrand;
import com.dissonance.game.sprites.Jeremiah;
import com.dissonance.game.sprites.Wyatt;

public class AtWaldomar extends GameWorldLoader {
    public static Farrand farrand;
    public static Jeremiah jeremiah;
    public static Wyatt guard1;
    public static Wyatt guard2;
    public static Wyatt guard3;
    public static Wyatt guard4;
    public static Wyatt guard5;

    @Override
    public void onLoad(World w){
        super.onLoad(w);
        farrand = new Farrand();
        w.loadAndAdd(farrand);
        farrand.setX(41*16f);
        farrand.setY(32*16f);

        jeremiah = new Jeremiah();
        w.loadAndAdd(jeremiah);
        jeremiah.setX(43*16f);
        jeremiah.setY(32*16f);

        guard1 = new Wyatt();
        w.loadAndAdd(guard1);
        guard1.setX(40*16f);
        guard1.setY(31*16f);

        guard2 = new Wyatt();
        w.loadAndAdd(guard2);
        guard2.setX(40*16f);
        guard2.setY(33*16f);

        guard3 = new Wyatt();
        w.loadAndAdd(guard3);
        guard3.setX(42*16f);
        guard3.setY(31*16f);

        guard4 = new Wyatt();
        w.loadAndAdd(guard4);
        guard4.setX(42*16f);
        guard4.setY(33*16f);

        guard5 = new Wyatt();
        w.loadAndAdd(guard5);
        guard5.setX(44*16f);
        guard5.setY(32*16f);


        farrand = new Farrand();
        w.loadAndAdd(farrand);
        //farrand.setX(41f*16, 32f*16);

    }
}
