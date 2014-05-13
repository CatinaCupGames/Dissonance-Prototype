package com.dissonance.test;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.world.World;
import com.dissonance.game.quests.OfficeQuest;
import com.dissonance.test.quests.AITestQuest;
import com.dissonance.test.quests.CoopQuest;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

import javax.naming.ldap.Control;

public class Main {

    public static void main(String[] args) throws Exception {
        World.setDefaultLoaderPackage("com.dissonance.test.w");

        GameService.loadEssentials(args);
<<<<<<< HEAD
        GameService.beginQuest(new CoopQuest());

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
=======
        GameService.beginQuest(new OfficeQuest());
>>>>>>> master
    }
}
