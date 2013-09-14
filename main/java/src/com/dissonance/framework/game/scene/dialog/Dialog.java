package com.dissonance.framework.game.scene.dialog;

public class Dialog {
    private String[] lines;
    private String[] image_path;
    private String[] header;
    private int index;
    private String id;

    Dialog(String[] lines, String[] image_path, String[] header, String id) {
        this.header = header;
        this.image_path = image_path;
        this.lines = lines;
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public String getId() {
        return id;
    }

    public String getCurrentLine() {
        if (index >= lines.length) {
            return "";
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
}