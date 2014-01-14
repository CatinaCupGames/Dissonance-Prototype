package com.dissonance.game.sprites;

import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.sprites.impl.game.NPCSprite;
import com.dissonance.framework.render.texture.sprite.SpriteTexture;

public final class TestNPC extends NPCSprite {

    public TestNPC(Dialog... dialogs) {
        super(dialogs);
    }

    public TestNPC(String... dialogIds) {
        super(dialogIds);
    }

    public TestNPC() {
    }

    @Override
    public String getReadableName() {
        return "Arrem";
    }

    @Override
    public String getSpriteName() {
        return "player";
    }

    @Override
    public void onSpeakingFinished() {
    }

    @Override
    public void onLoad() {
        super.onLoad();
        setLayer(2);
    }
}
