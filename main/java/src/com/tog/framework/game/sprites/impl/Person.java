package com.tog.framework.game.sprites.impl;

import com.tog.framework.game.dialog.Dialog;
import com.tog.framework.game.dialog.DialogFactory;
import com.tog.framework.game.sprites.AnimatedSprite;
import com.tog.framework.game.world.World;

public abstract class Person extends AnimatedSprite {
    private String name;
    private Dialog current_dialog;

    public Person(World w, String sprite_name) {
        setWorld(w);
        this.name = name;
        w.loadAnimatedTextureForSprite(this);
    }

    @Override
    public String getSpriteName() {
        return name;
    }

    public abstract String getPersonName();

    public Dialog getDialog() {
        return current_dialog;
    }

    public void setDialog(String ID) {
        current_dialog = DialogFactory.getDialog(ID);
    }

    public void displayDialog() {
        if (current_dialog == null)
            return;
        //TODO Show dialog
    }

    public void onShowDialog() {
        //TODO idk
    }
}
