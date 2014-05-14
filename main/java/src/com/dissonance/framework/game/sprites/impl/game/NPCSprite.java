package com.dissonance.framework.game.sprites.impl.game;

import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.scene.dialog.DialogFactory;
import com.dissonance.framework.game.scene.dialog.DialogUI;
import com.dissonance.framework.game.sprites.Selectable;

import java.util.Collections;
import java.util.LinkedList;

public abstract class NPCSprite extends PhysicsSprite implements Selectable {

    private NPCSpriteEvent.OnTalkEvent talkEvent;
    private NPCSpriteEvent.OnTalkFinishedEvent talkFinishedEvent;

    private LinkedList<Dialog> dialogQueue = new LinkedList<>();
    private DialogUI dialogUI;

    /**
     * Returns the human-readable name of this NPC sprite.
     */
    public abstract String getReadableName();

    /**
     * Creates a new instance of the NPCSprite class and {@link #enqueueDialogs(Dialog...) enqueues}
     * the specified {@link Dialog Dialogs}.
     *
     * @param dialogs The dialogs to enqueue.
     */
    public NPCSprite(Dialog... dialogs) {
        enqueueDialogs(dialogs);
    }

    /**
     * Creates a new instance of the NPCSprite class and {@link #enqueueDialogs(String...) enqueues}
     * the {@link Dialog dialogs} retrieved by the specified ids if they exist. If a dialog is not
     * foundit won't be added to the queue.
     *
     * @param dialogIds The ids of the dialogs to enqueue
     */
    public NPCSprite(String... dialogIds) {
        enqueueDialogs(dialogIds);
    }

    /**
     * Creates a new instance of the NPCSprite without enqueueing any {@link Dialog Dialogs}.
     */
    public NPCSprite() {}

    /**
     * Sets this {@link NPCSprite NPCSprite's}
     * {@link NPCSpriteEvent.OnTalkEvent OnTalkEvent listener} to the specified listener.
     *
     * @param talkListener The new event listener.
     */
    public void setTalkListener(NPCSpriteEvent.OnTalkEvent talkListener) {
        talkEvent = talkListener;
    }

    /**
     * Sets this {@link NPCSprite NPCSprite's}
     * {@link NPCSpriteEvent.OnTalkFinishedEvent OnTalkFinished listener} to the specified listener.
     *
     * @param talkFinishedListener The new event listener.
     */
    public void setTalkFinishedListener(NPCSpriteEvent.OnTalkFinishedEvent talkFinishedListener) {
        talkFinishedEvent = talkFinishedListener;
    }

    /**
     * Gets the next {@link Dialog} for this NPC and dequeues it.
     *
     * @return The retrieved dialog, or null if the queue is empty.
     */
    public Dialog getNextDialog() {
        return dialogQueue.poll();
    }

    /**
     * Gets the next {@link Dialog} for this NPC without dequeueing it.
     *
     * @return The retrieved dialog, or null if the queue is empty.
     */
    public Dialog peekNextDialog() {
        return dialogQueue.peek();
    }

    /**
     * Enqueues the specified dialogs.
     *
     * @param dialogs The dialogs to enqueue.
     */
    public final void enqueueDialogs(Dialog... dialogs) {
        Collections.addAll(dialogQueue, dialogs);
    }

    /**
     * Enqueues the dialogs specified by their ids, if found.
     *
     * @param dialogIds The ids of the dialogs to enqueue.
     */
    public final void enqueueDialogs(String... dialogIds) {
        for (String dialogId : dialogIds) {
            Dialog dialog = DialogFactory.getDialog(dialogId);

            if (dialog == null) {
                continue;
            }

            dialogQueue.add(dialog);
        }
    }

    @Override
    public boolean onSelected(PlayableSprite player) {
        Dialog dialog = getNextDialog();

        if (dialog != null) {
            dialogUI = new DialogUI(dialog);

            onSpeak();
            if (talkEvent != null) {
                talkEvent.onTalk(this, dialog);
            }

            dialogUI.displayUI();
            return true;
        }
        return false;
    }

    /**
     * The onSpeak method is called before the dialog has started.
     * It registers the dialog events needed to call the {@link #onSpeakingFinished()} method.
     */
    public void onSpeak() {
        dialogUI.setDialogListener(new DialogUI.DialogListener() {
            @Override
            public void onDialogAdvance(Dialog dialog) {
            }

            @Override
            public void onDialogStarted(Dialog dialog) {
            }

            @Override
            public void onDialogEnded() {
                onSpeakingFinished();

                if (talkFinishedEvent != null) {
                    talkFinishedEvent.onTalkFinished(NPCSprite.this);
                }
            }
        });
    }

    /**
     * The onSpeakingFinished method is called when the dialog has ended.
     */
    public void onSpeakingFinished() {}

    public interface NPCSpriteEvent {
        /**
        * Interface definition for a callback to be invoked when the {@link NPCSprite} has started talking.
        */
        public interface OnTalkEvent {
            public void onTalk(NPCSprite sprite, Dialog dialog);
        }

        /**
         * Interface definition for a callback to be invoked when the {@link NPCSprite} has finished talking.
         */
        public interface OnTalkFinishedEvent {
            public void onTalkFinished(NPCSprite sprite);
        }
    }
}
