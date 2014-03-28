package com.dissonance.test.sprites;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.sprites.impl.game.NPCSprite;
import com.dissonance.framework.render.text.RenderText;
import com.dissonance.framework.system.GameSettings;
import org.newdawn.slick.TrueTypeFont;
import sun.java2d.pipe.TextRenderer;

import java.awt.*;

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

    TrueTypeFont font;
}
