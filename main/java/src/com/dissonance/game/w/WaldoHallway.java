package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldLoader;
import com.dissonance.framework.system.utils.MovementType;
import com.dissonance.game.sprites.BlueGuard;
import com.dissonance.game.sprites.Farrand;
import com.dissonance.game.sprites.Jeremiah;

public class WaldoHallway implements WorldLoader {

    public static Farrand farrand;
    public static Jeremiah jeremiah;
    public static BlueGuard guard1;
    public static BlueGuard guard2;
    public static BlueGuard guard3;
    public static BlueGuard guard4;
    public static BlueGuard guard5;

   public void onLoad(World w){

       guard1 = new BlueGuard();
       w.loadAndAdd(guard1);
       guard1.setX(39*16);
       guard1.setY(5*16);
       guard1.setMovementType(MovementType.WALKING);

       guard2 = new BlueGuard();
       w.loadAndAdd(guard2);
       guard2.setX(39*16);
       guard2.setY(9*16);
       guard2.setMovementType(MovementType.WALKING);

      farrand = new Farrand();
      w.loadAndAdd(farrand);
      farrand.setX(41*16);
      farrand.setY(7*16);
       farrand.setMovementType(MovementType.WALKING);

       guard3 = new BlueGuard();
       w.loadAndAdd(guard3);
       guard3.setX(42*16);
       guard3.setY(5*16);
       guard3.setMovementType(MovementType.WALKING);

       guard4 = new BlueGuard();
       w.loadAndAdd(guard4);
       guard4.setX(42*16);
       guard4.setY(9*16);
       guard4.setMovementType(MovementType.WALKING);

       jeremiah = new Jeremiah();
       w.loadAndAdd(jeremiah);
       jeremiah.setX(44*16);
       jeremiah.setY(7*16);
       jeremiah.setMovementType(MovementType.WALKING);

       guard5 = new BlueGuard();
       w.loadAndAdd(guard5);
       guard5.setX(46*16);
       guard5.setY(7*16);
       guard5.setMovementType(MovementType.WALKING);

       guard1.setHostile(false);
       guard2.setHostile(false);
       guard3.setHostile(false);
       guard4.setHostile(false);
       guard5.setHostile(false);

       w.setWorldBrightness(0.4f);
       w.createLight(33*16, 6*16, 1.4f, 1.0f);
       w.createLight(0, 0, 0.1f, 0.1f);

   }




    @Override
    public void onDisplay(World world) {

    }
}
