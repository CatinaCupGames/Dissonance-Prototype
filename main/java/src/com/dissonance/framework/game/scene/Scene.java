package com.dissonance.framework.game.scene;

import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.scene.dialog.DialogUI;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Scene {
    private ArrayList<Dialog> current_dialog = new ArrayList<Dialog>();
    private boolean sceneStarted;
    private static boolean scenePlaying = false;
    private int part = 0;

    public void beginScene() {
        if (sceneStarted)
            return;
        if (scenePlaying)
            return;
        sceneStarted = true;
        scenePlaying = true;
        initScene();
        boolean stuffTodo = true;
        while (stuffTodo) {
            boolean b = anythingToSay(); //Is there anything to say?
            Dialog d = null;
            while (b) {
                d = current_dialog.get(0); //If so, get the next thing to say
                current_dialog.remove(0); //Remove it
                b = displayDialog(d); //And display it
            }
            //Nothing else to say for now
            while (anythingToMove(d, part)) { //Is there anything to move?
                moveThings(part); //If so, lets move them.
                part++; //Advance to the next part of the scene
            }
            stuffTodo = anythingToSay(); //Is there anything else to say?
        }
        onEndScene();
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
        ui.displayUI(false);
        try {
            ui.waitForEnd();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return anythingToSay();
    }

    protected abstract boolean anythingToMove(Dialog lastDialog, int part);

    protected abstract void moveThings(int part);

    protected abstract void initScene();

    protected boolean anythingToSay() {
        return current_dialog.size() > 0;
    }

    protected void onEndScene() {
        scenePlaying = false;
        sceneStarted = false;
        _wakeup();
    }

    private synchronized void _wakeup() {
        super.notifyAll();
    }

    public synchronized void waitForSceneEnd() throws InterruptedException {
        while (true) {
            if (!sceneStarted)
                break;
            super.wait(0L);
        }
    }
}
