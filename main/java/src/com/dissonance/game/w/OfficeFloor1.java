/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: 2014/05/13 11:28:05
=====================================
*/

/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: 2014/05/09 14:59:35
=====================================
*/
package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.game.sprites.office.*;

public class OfficeFloor1 extends GameWorldLoader {
   
  
    @Override
    public void onLoad(World w){
        super.onLoad(w);

for(int i=1; i<7; i++){
    for(int z=1; z<7; z++){
      Cubicle var = new Cubicle();
        w.loadAndAdd(var);
        var.setX(i*143-40f);
        var.setY(40+z*125f);
      }  
     }  


for(int i=1; i<7; i++){
    for(int z=1; z<7; z++){
      tablecropped har = new tablecropped();
        w.loadAndAdd(har);
        har.setX(i*143-16f);
        har.setY(30+z*125f);
      }  
     }  

for(int i=1; i<7; i++){
    for(int z=1; z<7; z++){
      DeskChair rar = new DeskChair();
        w.loadAndAdd(rar);
        rar.setX(i*143-38f);
        rar.setY(24+z*125f);
      }  
     }  



    }
}