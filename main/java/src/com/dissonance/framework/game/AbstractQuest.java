package com.dissonance.framework.game;

import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.exceptions.QuestNotFoundException;

public abstract class AbstractQuest {
    private AbstractQuest next;
    private boolean ended;
    private World world;
    private boolean paused;

    public abstract void startQuest();

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
        return world;
    }

    protected void setWorld(World world) {
        WorldFactory.swapView(this.world, world);
        System.out.println("New world swapped to " + world.getID());
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
