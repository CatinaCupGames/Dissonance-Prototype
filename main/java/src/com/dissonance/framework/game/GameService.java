package com.dissonance.framework.game;

import com.sun.istack.internal.NotNull;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.system.utils.Validator;

public class GameService {
    private static long TID;
    private static Thread questThread;
    private static AbstractQuest currentQuest;

    public static void beginQuest(@NotNull AbstractQuest quest) {
        Validator.validateNotNull(quest, "quest");
        while (quest != null) {
            TID = Thread.currentThread().getId();
            currentQuest = quest;
            currentQuest.setNextQuest((AbstractQuest) null);
            questThread = new Thread(questRun);
            questThread.start();
            System.out.println("[GameService] QUEST " + quest.getClass().getCanonicalName() + " STARTED");
            try {
                System.out.println("[GameService] Waiting for end");
                currentQuest.waitForEnd();
                System.out.println("[GameService] Quest Ended");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            quest = currentQuest.getNextQuest();
            if (quest != null) {
                quest.setWorld(currentQuest.getWorld()); //Set the world to the last world used by the last quest
            }
        }
        //...what do we do now..?
    }

    public static World getCurrentWorld() {
        return currentQuest.getWorld();
    }

    public static long getGameServiceThreadID() {
        return TID;
    }

    public static Thread getQuestThread() {
        return questThread;
    }

    public static void saveGame() {

    }


    private static final Runnable questRun = new Runnable() {

        @Override
        public void run() {
            currentQuest.startQuest();
        }
    };
}
