package com.dissonance.framework.game.sprites.impl;

import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.scene.dialog.DialogFactory;
import com.dissonance.framework.game.scene.dialog.DialogUI;

import java.util.Collections;
import java.util.LinkedList;

public abstract class NPCSprite extends AbstractWaypointSprite {

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
    public void onSelected(PlayableSprite player) {
        Dialog dialog = getNextDialog();

        if (dialog != null) {
            dialogUI = new DialogUI("NPC-DIALOG-" + super.hashCode(), dialog);
            onSpeak();
            dialogUI.displayUI();
        }
    }

    /**
     * The onSpeak method is called before the dialog has started.
     * It registers the dialog events needed to call the {@link #onSpeakingFinished()} method.
     */
    public void onSpeak() {
        dialogUI.setDialogListener(new DialogUI.DialogListener() {
            @Override
            public void onDialogAdvance(Dialog dialog) {
                System.out.println("NPCSprite: Dialog advanced event called");
            }

            @Override
            public void onDialogStarted(Dialog dialog) {
                System.out.println("NPCSprite: Dialog started event called");
            }

            @Override
            public void onDialogEnded() {
                System.out.println("NPCSprite: Dialog end event called");
                onSpeakingFinished();
            }
        });
    }

    /**
     * The onSpeakingFinished method is called when the dialog has ended.
     */
    public void onSpeakingFinished() {}
}


