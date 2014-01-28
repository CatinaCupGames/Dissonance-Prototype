package com.dissonance.framework.game.scene.dialog;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.render.RenderService;

public class Dialog {
    private CustomString[] lines;
    private String[] image_path;
    private String[] header;
    private int index;
    private String id;

    public static void displayDialog(String dialog_id) {
        Dialog dialog = DialogFactory.getDialog(dialog_id);
        if (dialog == null)
            return;

        DialogUI ui = new DialogUI(dialog);

        World world;
        PlayableSprite player = PlayableSprite.getCurrentlyPlayingSprite();
        boolean halt = true;
        if (player == null) {
            world = GameService.getCurrentWorld();
            if (world == null) {
                if (RenderService.INSTANCE != null)
                    world = RenderService.INSTANCE.getCurrentDrawingWorld();
                else
                    throw new RuntimeException("No world could be found to bound the UI to!");
            }
        } else {
            halt = !player.isFrozen();
            world = player.getWorld();
        }
        ui.displayUI(halt, world);
        try {
            ui.waitForEnd();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        if (index >= header.length && header.length > 0) {
            return header[header.length - 1];
        } else if (index < header.length) {
            return header[index];
        } else {
            return "";
        }
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

    void reset() {
        index = 0;
    }
}
