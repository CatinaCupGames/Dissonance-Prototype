/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: 2014/05/13 14:56:39
=====================================
*/

/*
=====================================

This file was automatically generated with the
World Loader Editor

Date: 2014/05/13 14:24:16
=====================================
*/
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
   
    public static PottedPlant var8;
    public static PottedPlant var9;
    public static PottedPlant var10;
    public static PottedPlant var11;

    @Override
    public void onLoad(World w){
        super.onLoad(w);

for(int i=1; i<7; i++){
    for(int z=1; z<11; z++){
if (i == 5 && z == 1) continue;
        if (i == 6 && z == 1) continue;
      Cubicle var = new Cubicle();
        w.loadAndAdd(var);
        var.setX(i*143-40f);
        var.setY(40+z*125f);
      }  
     }  


for(int i=1; i<7; i++){
    for(int z=1; z<11; z++){
if (i == 5 && z == 1) continue;
 if (i == 6 && z == 1) continue;
      tablecropped har = new tablecropped();
        w.loadAndAdd(har);
        har.setX(i*143-16f);
        har.setY(30+z*125f);
      }  
     }  

for(int i=1; i<7; i++){
    for(int z=1; z<11; z++){
 if (i == 6 && z == 1) continue;
if (i == 5 && z == 1) continue;
      DeskChair rar = new DeskChair();
        w.loadAndAdd(rar);
        rar.setX(i*143-38f);
        rar.setY(24+z*125f);
      }  
     }  


    var8 = new PottedPlant();
        w.loadAndAdd(var8);
        var8.setX(850.3498f);
        var8.setY(1395.44995f);

    var9 = new PottedPlant();
        w.loadAndAdd(var9);
        var9.setX(850.3498f);
        var9.setY(90.44995f);

    var10 = new PottedPlant();
        w.loadAndAdd(var10);
        var10.setX(15.3498f);
        var10.setY(1400.44995f);

    var11 = new PottedPlant();
        w.loadAndAdd(var11);
        var11.setX(15.3498f);
        var11.setY(90.44995f);

    }
}