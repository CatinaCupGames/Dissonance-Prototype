package com.tog.framework.game;

import com.tog.framework.game.world.World;

public abstract class AbstractQuest {
    private AbstractQuest next;
    private boolean ended;
    private World world;

    public abstract void startQuest();

    public void onPauseGame() {
        //TODO Pause everything and show pause menu
    }

    public void onResumeGame() {
        //TODO Resume everything and get rid of pause menu
    }

    public World getWorld() {
        return world;
    }

    void setWorld(World world) {

    }

    public void setNextQuest(AbstractQuest quest) {
        this.next = quest;
    }

    public void setNextQuest(String packageName) {
        try {
            Class<? extends AbstractQuest> class_ = (Class<? extends AbstractQuest>) Class.forName(packageName);
            AbstractQuest q = class_.newInstance();
            setNextQuest(q);
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
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
