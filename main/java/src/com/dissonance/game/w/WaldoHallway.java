package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.game.sprites.Farrand;
import com.dissonance.game.sprites.Jeremiah;
import com.dissonance.game.sprites.Wyatt;

/**
 * Created by Henry on 5/7/2014.
 */
public class WaldoHallway implements WorldLoader {

    public static Farrand farrand;
    public static Jeremiah jeremiah;
    public static Wyatt guard1;
    public static Wyatt guard2;
    public static Wyatt guard3;
    public static Wyatt guard4;
    public static Wyatt guard5;

   public void onLoad(World w){
       guard1 = new Wyatt();
       w.loadAndAdd(guard1);
       guard1.setX(39*16);
       guard1.setY(5*16);

       guard2 = new Wyatt();
       w.loadAndAdd(guard2);
       guard2.setX(39*16);
       guard2.setY(9*16);

      farrand = new Farrand();
      w.loadAndAdd(farrand);
      farrand.setX(41*16);
      farrand.setY(7*16);

       guard3 = new Wyatt();
       w.loadAndAdd(guard3);
       guard3.setX(42*16);
       guard3.setY(5*16);

       guard4 = new Wyatt();
       w.loadAndAdd(guard4);
       guard4.setX(42*16);
       guard4.setY(9*16);

       jeremiah = new Jeremiah();
       w.loadAndAdd(jeremiah);
       jeremiah.setX(44*16);
       jeremiah.setY(7*16);

       guard5 = new Wyatt();
       w.loadAndAdd(guard5);
       guard5.setX(46*16);
       guard5.setY(7*16);


   }




    @Override
    public void onDisplay(World world) {

    }
}