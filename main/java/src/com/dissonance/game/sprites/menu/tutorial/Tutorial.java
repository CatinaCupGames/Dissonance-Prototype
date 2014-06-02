package com.dissonance.game.sprites.menu.tutorial;

import com.dissonance.framework.game.player.ControllerInput;
import com.dissonance.framework.game.player.KeyboardInput;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.system.GameSettings;
import com.dissonance.game.sprites.menu.Static;
import net.java.games.input.ControllerEnvironment;

public class Tutorial {
    public static Sprite[] displayKeyboard() {
        Sprite[] array = new Sprite[3];

        Overlay over = new Overlay();
        over.setX(1024 / 2f);
        over.setY(512 / 2f);
        array[0] = over;

        Static s = new Static();
        s.setX(640f / 2f);
        s.setY(360f / 2f);
        s.setLayer(200);
        array[1]= s;

        KeyboardTutorial tutorial = new KeyboardTutorial();
        tutorial.setX(GameSettings.Display.game_width / 4f);
        tutorial.setX(tutorial.getX() + 32f);
        tutorial.setY(GameSettings.Display.game_height / 4f);
        tutorial.setY(tutorial.getY() + 96f);
        array[2] = tutorial;

        return array;
    }

    public static Sprite[] displayController() {
        Sprite[] array = new Sprite[3];

        Overlay over = new Overlay();
        over.setX(1024 / 2f);
        over.setY(512 / 2f);
        array[0] = over;

        Static s = new Static();
        s.setX(640f / 2f);
        s.setY(360f / 2f);
        s.setLayer(200);
        array[1]= s;

        ControllerTutorial tutorial = new ControllerTutorial();
        tutorial.setX(GameSettings.Display.game_width / 4f);
        tutorial.setX(tutorial.getX() + 64f);
        tutorial.setY(GameSettings.Display.game_height / 4f);
        tutorial.setY(tutorial.getY());
        array[2] = tutorial;

        return array;
    }

    public static Sprite[] displayBoth() {
        Sprite[] array = new Sprite[4];

        Overlay over = new Overlay();
        over.setX(1024 / 2f);
        over.setY(512 / 2f);
        array[0] = over;

        Static s = new Static();
        s.setX(640f / 2f);
        s.setY(360f / 2f);
        s.setLayer(200);
        array[1]= s;

        ControllerTutorial tutorial = new ControllerTutorial();
        tutorial.setX(GameSettings.Display.game_width / 4f);
        tutorial.setX(tutorial.getX() + 192f);
        tutorial.setY(GameSettings.Display.game_height / 4f);
        tutorial.setY(tutorial.getY());
        array[2] = tutorial;

        KeyboardTutorial tutorial2 = new KeyboardTutorial();
        tutorial2.setX(GameSettings.Display.game_width / 4f);
        tutorial2.setX(135f);
        tutorial2.setY(GameSettings.Display.game_height / 4f);
        tutorial2.setY(tutorial.getY() + 96f);
        array[3] = tutorial2;

        return array;
    }

    public static Sprite[] display() {
        boolean key = false, controller = false;
        for (Player p : Players.getPlayersWithInput()) {
            if (p.getInput() instanceof KeyboardInput) {
                key = true;
            }
            if (p.getInput() instanceof ControllerInput) {
                controller = true;
            }
        }

        if (key && controller) {
            return Tutorial.displayBoth();
        } else if (key) {
            return Tutorial.displayKeyboard();
        } else {
            return Tutorial.displayController();
        }
    }
}
