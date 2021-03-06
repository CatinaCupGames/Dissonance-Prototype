package com.dissonance.test;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.world.World;
import com.dissonance.game.quests.DisclaimerQuest;
import com.dissonance.test.quests.CoopQuest;
import com.dissonance.test.quests.HenrysQUest;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class Main {

    public static void main(String[] args) throws Exception {
        World.setDefaultLoaderPackage("com.dissonance.test.w");

        GameService.loadEssentials(args);
        GameService.beginQuest(new HenrysQUest());

        /*Controller[] controller = ControllerEnvironment.getDefaultEnvironment().getControllers();

        Controller temp = null;
        for (Controller c : controller) {
            if (c.getType() != Controller.Type.STICK && c.getType() != Controller.Type.GAMEPAD)
                continue;
            System.out.println(c.getName());
            System.out.println(c.getType().toString());
            Component[] ca = c.getComponents();
            for (Component co : ca) {
                System.out.println(co.getName());
                System.out.println(co.getIdentifier().toString());
            }
            temp = c;
        }

        if (temp == null) {
            System.out.println("FAILED!");
            return;
        }

        while (true) {
            temp.poll();
            for (Component c : temp.getComponents()) {
                System.out.println(c.getIdentifier().getName() + " : " + c.getPollData());
            }
            Thread.sleep(1500);
        }*/
    }
}
