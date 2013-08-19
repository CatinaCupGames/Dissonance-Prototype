package com.tog.framework.game;

import com.tog.framework.game.world.World;
import com.tog.framework.system.exceptions.QuestNotFoundException;

public abstract class AbstractQuest {
    private AbstractQuest next;
    private boolean ended;
    private World world;
    private boolean paused;

    public abstract void startQuest();

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

    void setWorld(World world) {
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
         while (true) {
             if (ended)
                 break;
             super.wait(0L);
         }
    }
}
