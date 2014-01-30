package com.dissonance.framework.game;

import com.dissonance.framework.game.scene.Scene;
import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.game.world.WorldPackage;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.exceptions.QuestNotFoundException;
import com.dissonance.framework.system.exceptions.WorldLoadFailedException;
import javafx.animation.Transition;

public abstract class AbstractQuest {
    private AbstractQuest next;
    private boolean ended;
    private World world;
    private boolean paused;
    private WorldPackage worldMemory;

    public abstract void startQuest() throws Exception;

    public abstract String getName();

    public void pauseGame() {
        if (paused)
            return;
        paused = true;
        onPauseGame();
    }

    public void resumeGame() {
        if (!paused)
            return;
        paused = false;
        onResumeGame();
    }


    protected void onPauseGame() {
        //TODO Pause everything and show pause menu
    }

    protected void onResumeGame() {
        //TODO Resume everything and get rid of pause menu
    }

    public World getWorld() {
        if (PlayableSprite.getCurrentlyPlayingSprite() != null && PlayableSprite.getCurrentlyPlayingSprite().getWorld() != world) {
            world = PlayableSprite.getCurrentlyPlayingSprite().getWorld();
        }
        if (world == null) {
            world = RenderService.INSTANCE.getCurrentDrawingWorld();
        }
        return world;
    }

    public void setWorld(World world) {
        WorldFactory.swapView(world, true);
        System.out.println("New world swapped to " + world.getID());
        this.world = world;
    }

    public void displayWorld(String world) {
        displayWorld(world, true);
    }

    public void displayWorld(String world, boolean fadeToBlack) {
        if (worldMemory != null) {
            for (World w : worldMemory.getWorlds()) {
                if (w.getName().equals(world)) {
                    WorldFactory.swapView(w, fadeToBlack);
                    System.out.println("New world swapped to " + w.getID());
                    this.world = w;
                    return;
                }
            }
        }

        try {
            World w = WorldFactory.getWorld(world);
            setWorld(w);
        } catch (WorldLoadFailedException e) {
            e.printStackTrace();
        }
    }

    public Scene playScene(Class<? extends Scene> sceneClass) {
        try {
            Scene scene = sceneClass.newInstance();
            scene.beginScene();
            return scene;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void playSceneAndWait(Class<? extends Scene> sceneClass) throws InterruptedException {
        Scene scene = playScene(sceneClass);
        scene.waitForSceneEnd();
    }

    public void loadWorldsIntoMemory(String... worlds) {
        if (worldMemory != null) {
            worldMemory.clear();
            worldMemory = null;
        }

        worldMemory = WorldPackage.createWorldPackage(worlds);
    }

    protected void setWorld(World world, RenderService.TransitionType transitionType) {
        if (transitionType == RenderService.TransitionType.CROSSFADE) {
            RenderService.INSTANCE.provideData(true, RenderService.ENABLE_CROSS_FADE);
        }
        WorldFactory.swapView(world, transitionType == RenderService.TransitionType.FADETOBLACK);
        this.world = world;
    }

    public void setNextQuest(AbstractQuest quest) {
        this.next = quest;
    }

    public void setNextQuest(String packageName) throws QuestNotFoundException {
        try {
            Class<? extends AbstractQuest> class_ = (Class<? extends AbstractQuest>) Class.forName(packageName);
            AbstractQuest q = class_.newInstance();
            setNextQuest(q);
        } catch (ClassNotFoundException e) {
            throw new QuestNotFoundException("The quest \"" + packageName + "\" was not found!", e);
        } catch (InstantiationException e) {
            throw new QuestNotFoundException("The quest \"" + packageName + "\" does not have a default constructor!", e);
        } catch (IllegalAccessException e) {
            throw new QuestNotFoundException("The quest \"" + packageName + "\" does not have a public default constructor!", e);
        }
    }

    public void endQuest() throws IllegalAccessException {
        if (Thread.currentThread().getId() == GameService.getGameServiceThreadID())
            throw new IllegalAccessException("You can't end the quest on this thread!");
        finish();
    }

    private synchronized void finish() {
        ended = true;
        super.notify();
    }

    public AbstractQuest getNextQuest() {
        return next;
    }

    public synchronized void waitForEnd() throws InterruptedException {
        if (RenderService.isInRenderThread())
            throw new IllegalAccessError("You cant access this method in the render thread!");
        while (true) {
            if (ended)
                break;
            super.wait(0L);
        }
    }


}
