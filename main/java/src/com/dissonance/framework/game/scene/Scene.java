package com.dissonance.framework.game.scene;

import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.scene.dialog.DialogUI;
import com.dissonance.framework.render.RenderService;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Scene {
    private ArrayList<Dialog> current_dialog = new ArrayList<Dialog>();
    private boolean sceneStarted;
    private static boolean scenePlaying = false;
    private int part = 0;
    private boolean kill = false;

    public void beginScene() {
        if (sceneStarted)
            return;
        if (scenePlaying)
            return;
        sceneStarted = true;
        scenePlaying = true;
        initScene();
        boolean stuffTodo = true;
        while (stuffTodo && !kill) {
            boolean b = anythingToSay(); //Is there anything to say?
            Dialog d = null;
            while (b && !kill) {
                d = current_dialog.get(0); //If so, get the next thing to say
                current_dialog.remove(0); //Remove it
                b = displayDialog(d); //And display it
            }
            //Nothing else to say for now
            while (anythingToMove(d, part) && !kill) { //Is there anything to move?
                try {
                    moveThings(part); //If so, lets move them.
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    protected void waitFor(double seconds) throws InterruptedException {
        Thread.sleep((long) (seconds * 1000));
    }

    protected boolean displayDialog(Dialog d) {
        DialogUI ui = new DialogUI("SCENE-DIALOG-" + super.hashCode(), d);
        ui.displayUI(true);
        try {
            ui.waitForEnd();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return anythingToSay();
    }

    protected abstract boolean anythingToMove(Dialog lastDialog, int part);

    protected abstract void moveThings(int part) throws Exception;

    protected abstract void initScene();

    protected boolean anythingToSay() {
        return current_dialog.size() > 0;
    }

    /**
     * Terminate this scene. <br></br>
     * Invoking this method will not terminate the scene immediately. You can call {@link com.dissonance.framework.game.scene.Scene#waitForSceneEnd()} to wait for the end of
     * the scene.
     */
    public void terminateScene() {
        kill = true;
    }

    private boolean invoked = false;
    private void endScene() {
        invoked = false;
        onEndScene();
        if (!invoked)
            throw new RuntimeException("super.onEndScene was not invoked. Please add super.onEndScene to the top of your method.");
    }

    protected void onEndScene() {
        scenePlaying = false;
        sceneStarted = false;
        invoked = true;
        _wakeup();
    }

    private synchronized void _wakeup() {
        super.notifyAll();
    }

    public synchronized void waitForSceneEnd() throws InterruptedException {
        if (RenderService.isInRenderThread())
            throw new IllegalAccessError("You cant access this method in the render thread!");
        while (true) {
            if (!sceneStarted)
                break;
            super.wait(0L);
        }
    }
}
