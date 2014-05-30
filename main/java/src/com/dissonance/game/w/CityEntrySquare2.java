package com.dissonance.game.w;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.game.sprites.Admin;
import com.dissonance.game.sprites.BlueGuard;
import com.dissonance.game.sprites.RedGuard;

public class CityEntrySquare2 extends DemoLevelWorldLoader {
    public static Admin admin1;
    public static Admin admin2;
    public static Admin admin3;
    public static BlueGuard[] meleeguards = new BlueGuard[10];
    public static RedGuard[] gunguards = new RedGuard[10];
    public static Admin[] adminguards = new Admin[5];
    @Override
    public void onLoad(World w){
        super.onLoad(w);

        farrand.setX(47*16);
        farrand.setY(28*16);

        jeremiah.setX(49*16);
        jeremiah.setY(29*16);

        admin1 = new Admin();
        w.loadAndAdd(admin1);
        admin1.setX(43*16);
        admin1.setY(56*16);
        admin1.setHostile(false);

        admin2 = new Admin();
        w.loadAndAdd(admin2);
        admin2.setX(57 * 16);
        admin2.setY(56*16);
        admin2.setHostile(false);

        admin3 = new Admin();
        w.loadAndAdd(admin3);
        admin3.setX(43 * 16);
        admin3.setY(56 * 16);
        admin3.setHostile(false);
    }

    @Override
    public void onDisplay(World w){
        super.onDisplay(w);
        farrand.setVisible(true);
        jeremiah.setVisible(true);
    }

    public static void getShrekt(World w) throws InterruptedException {
        meleeguards[0] = new BlueGuard();
        w.loadAndAdd(meleeguards[0]);
        meleeguards[0].setX(21*16);
        meleeguards[0].setY(44*16);

        meleeguards[1] = new BlueGuard();
        w.loadAndAdd(meleeguards[1]);
        meleeguards[1].setX(21*16);
        meleeguards[1].setY(46*16);

        meleeguards[2] = new BlueGuard();
        w.loadAndAdd(meleeguards[2]);
        meleeguards[2].setX(21*16);
        meleeguards[2].setY(48*16);

        meleeguards[3] = new BlueGuard();
        w.loadAndAdd(meleeguards[3]);
        meleeguards[3].setX(21*16);
        meleeguards[3].setY(50*16);

        meleeguards[4] = new BlueGuard();
        w.loadAndAdd(meleeguards[4]);
        meleeguards[4].setX(21*16);
        meleeguards[4].setY(52*16);

        meleeguards[5] = new BlueGuard();
        w.loadAndAdd(meleeguards[5]);
        meleeguards[5].setX(23*16);
        meleeguards[5].setY(44*16);

        meleeguards[6] = new BlueGuard();
        w.loadAndAdd(meleeguards[6]);
        meleeguards[6].setX(23*16);
        meleeguards[6].setY(46*16);

        meleeguards[7] = new BlueGuard();
        w.loadAndAdd(meleeguards[7]);
        meleeguards[7].setX(23*16);
        meleeguards[7].setY(48*16);

        meleeguards[8] = new BlueGuard();
        w.loadAndAdd(meleeguards[8]);
        meleeguards[8].setX(23*16);
        meleeguards[8].setY(50*16);

        meleeguards[9] = new BlueGuard();
        w.loadAndAdd(meleeguards[9]);
        meleeguards[9].setX(23*16);
        meleeguards[9].setY(52*16);


        gunguards[0] = new RedGuard();
        w.loadAndAdd(gunguards[0]);
        gunguards[0].setX(25*16);
        gunguards[0].setY(44*16);

        gunguards[1] = new RedGuard();
        w.loadAndAdd(gunguards[1]);
        gunguards[1].setX(25*16);
        gunguards[1].setY(46*16);

        gunguards[2] = new RedGuard();
        w.loadAndAdd(gunguards[2]);
        gunguards[2].setX(25*16);
        gunguards[2].setY(48*16);

        gunguards[3] = new RedGuard();
        w.loadAndAdd(gunguards[3]);
        meleeguards[3].setX(25*16);
        meleeguards[3].setY(50*16);

        gunguards[4] = new RedGuard();
        w.loadAndAdd(gunguards[4]);
        gunguards[4].setX(25*16);
        gunguards[4].setY(52*16);

        gunguards[5] = new RedGuard();
        w.loadAndAdd(gunguards[5]);
        gunguards[5].setX(27*16);
        gunguards[5].setY(44*16);

        gunguards[6] = new RedGuard();
        w.loadAndAdd(gunguards[6]);
        gunguards[6].setX(27*16);
        gunguards[6].setY(46*16);

        gunguards[7] = new RedGuard();
        w.loadAndAdd(gunguards[7]);
        gunguards[7].setX(27*16);
        gunguards[7].setY(48*16);

        gunguards[8] = new RedGuard();
        w.loadAndAdd(gunguards[8]);
        gunguards[8].setX(27*16);
        gunguards[8].setY(50*16);

        gunguards[9] = new RedGuard();
        w.loadAndAdd(gunguards[9]);
        gunguards[9].setX(27*16);
        gunguards[9].setY(52*16);


        adminguards[0] = new Admin();
        w.loadAndAdd(adminguards[0]);
        gunguards[0].setX(25*16);
        gunguards[0].setY(44*16);

        adminguards[1] = new Admin();
        w.loadAndAdd(adminguards[1]);
        gunguards[1].setX(25*16);
        gunguards[1].setY(46*16);

        adminguards[2] = new Admin();
        w.loadAndAdd(adminguards[2]);
        gunguards[2].setX(25*16);
        gunguards[2].setY(48*16);

        adminguards[3] = new Admin();
        w.loadAndAdd(adminguards[3]);
        meleeguards[3].setX(25*16);
        meleeguards[3].setY(50*16);

        adminguards[4] = new Admin();
        w.loadAndAdd(adminguards[4]);
        gunguards[4].setX(25*16);
        gunguards[4].setY(52*16);

        for (BlueGuard meleeguard : meleeguards) {
            meleeguard.setHostile(false);
            meleeguard.face(Direction.RIGHT);
        }

        for (RedGuard gunguard : gunguards) {
            gunguard.setHostile(false);
            gunguard.face(Direction.RIGHT);
        }

        for (Admin adminguard : adminguards) {
            adminguard.setHostile(false);
            adminguard.face(Direction.RIGHT);
        }


    }

    @Override
    public void onRespawn(World w){
        super.onRespawn(w);

        farrand.setX(47*16);
        farrand.setY(28*16);

        jeremiah.setX(49*16);
        jeremiah.setY(29*16);

        admin1 = new Admin();
        w.loadAndAdd(admin1);
        admin1.setX(43*16);
        admin1.setY(56*16);
        admin1.setHostile(false);

        admin2 = new Admin();
        w.loadAndAdd(admin2);
        admin2.setX(57 * 16);
        admin2.setY(56*16);
        admin2.setHostile(false);

        admin3 = new Admin();
        w.loadAndAdd(admin3);
        admin3.setX(43 * 16);
        admin3.setY(56 * 16);
        admin3.setHostile(false);
    }
}
