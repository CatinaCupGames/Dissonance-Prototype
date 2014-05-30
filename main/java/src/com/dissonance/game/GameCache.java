package com.dissonance.game;

import com.dissonance.framework.game.world.World;

public class GameCache {
    public static World RoofTopBeginning;
    public static World OutsideFighting;
    public static World FactoryFloor;
    public static World RooftopMid;
    public static World OfficeFloor1;
    public static World OfficeFloor2;

    public static World MainMenu;

    public static void clear() {
        if (RoofTopBeginning != null)
            RoofTopBeginning.onDispose();
        if (OutsideFighting != null)
            OutsideFighting.onDispose();
        if (FactoryFloor != null)
            FactoryFloor.onDispose();
        if (RooftopMid != null)
            RooftopMid.onDispose();
        if (OfficeFloor1 != null)
            OfficeFloor1.onDispose();
        if (OfficeFloor2 != null)
            OfficeFloor2.onDispose();

        RoofTopBeginning = null;
        OutsideFighting = null;
        FactoryFloor = null;
        RooftopMid = null;
        OfficeFloor1 = null;
        OfficeFloor2 = null;
    }
}
