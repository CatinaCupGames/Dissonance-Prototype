package com.dissonance.framework.game.scene;

import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.scene.dialog.DialogUI;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Scene {
    private ArrayList<Dialog> current_dialog;
    private boolean sceneStarted;
    private static boolean scenePlaying = false;
    private int part = 0;
    public Scene(Dialog[] startDialog) {
        this.current_dialog = new ArrayList<Dialog>();
        current_dialog.addAll(Arrays.asList(startDialog));
    }

    public void beginScene() {
        if (sceneStarted)
            return;
        if (scenePlaying)
            return;
        sceneStarted = true;
        scenePlaying = true;
        boolean stuffTodo = true;
        while (stuffTodo) {
            boolean b = anythingToSay(); //Is there anything to say?
            while (b) {
                Dialog d = current_dialog.get(0); //If so, get the next thing to say
                current_dialog.remove(0); //Remove it
                b = displayDialog(d); //And display it
            }
            //Nothing else to say for now
            while (anythingToMove(part)) { //Is there anything to move?
                moveThings(part); //If so, lets move them.
                part++; //Advance to the next part of the scene
            }
            stuffTodo = anythingToSay(); //Is there anything else to say?
        }
        scenePlaying = false;
        sceneStarted = false;
    }

    public boolean hasSceneStarted() {
        return sceneStarted;
    }

    public static boolean isScenePlaying() {
        return scenePlaying;
    }

    protected void queueDialog(Dialog... dialogs) {
        current_dialog.addAll(Arrays.asList(dialogs));
    }

    protected boolean displayDialog(Dialog d) {
        DialogUI ui = new DialogUI("SCENE-DIALOG-" + super.hashCode(), d);
        ui.displayUI();
        try {
            ui.waitForEnd();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return anythingToSay();
    }

    protected abstract boolean anythingToMove(int part);

    protected abstract boolean moveThings(int part);

    protected boolean anythingToSay() {
        return current_dialog.size() > 0;
    }
}
