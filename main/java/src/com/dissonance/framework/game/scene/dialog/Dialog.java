package com.dissonance.framework.game.scene.dialog;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.render.RenderService;

public class Dialog {
    private CustomString[] lines;
    private String[] image_path;
    private String[] header;
    private int index;
    private String id;

    public static void displayDialog(String dialog_id) {
        displayDialog(dialog_id, false);
    }

    public static void displayDialog(String dialog_id, boolean autoScroll) {
        Dialog dialog = DialogFactory.getDialog(dialog_id);
        if (dialog == null)
            return;

        DialogUI ui = new DialogUI(dialog, autoScroll);

        World world = GameService.getCurrentWorld();
        if (world == null) {
            if (RenderService.INSTANCE != null)
                world = RenderService.INSTANCE.getCurrentDrawingWorld();
            else
                throw new RuntimeException("No world could be found to bound the UI to!");
        }
        PlayableSprite[] players = Players.getCurrentlyPlayingSprites();
        boolean halt = true;
        if (players.length > 0) {
            halt = !players[0].isFrozen();
        }
        if (halt && players.length > 0) {
            for (PlayableSprite player : players) {
                player.freeze(true, Dialog.class);
            }
        }

        while (GameService.getCurrentQuest() != null && GameService.getCurrentQuest().isPaused()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ui.display(world);
        try {
            ui.waitForEnd();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (halt && players.length > 0) {
            for (PlayableSprite player : players) {
                player.unfreeze(Dialog.class);
            }
        }
    }

    Dialog(CustomString[] lines, String[] image_path, String[] header, String id) {
        this.header = header;
        this.image_path = image_path;
        this.id = id;
        this.lines = lines;
    }

    public int getIndex() {
        return index;
    }

    public String getId() {
        return id;
    }

    public CustomString getCurrentLine() {
        if (index >= lines.length) {
            return new CustomString("");
        } else {
            return lines[index];
        }
    }

    public String getCurrentHeader() {
        if (header.length == 0)
            return null;
        int i = index;
        while (i >= header.length || header[i] == null) {
            i--;
        }

        return header[i];
    }

    public String getCurrentImagePath() {
        if (index >= image_path.length && image_path.length > 0) {
            return image_path[image_path.length - 1];
        } else if (index < image_path.length) {
            return image_path[index];
        } else {
            return "";
        }
    }

    public boolean advanceDialog() {
        index++;
        return index >= lines.length;
    }

    public CustomString[] getAllLines() {
        return lines;
    }

    public String[] getHeaders() {
        return header;
    }

    void reset() {
        index = 0;
    }
}
