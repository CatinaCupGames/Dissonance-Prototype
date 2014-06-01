package com.dissonance.game.sprites.menu.tutorial;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.player.input.InputKeys;
import com.dissonance.framework.game.sprites.ui.impl.AbstractUI;
import com.dissonance.framework.render.shader.ShaderFactory;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.game.quests.GameQuest;
import com.dissonance.game.sprites.ImageSprite;
import com.dissonance.game.sprites.menu.TextButton;
import com.dissonance.game.sprites.menu.pause.PauseMenu;

import java.io.IOException;

public class ControllerTutorial extends ImageSprite {
    public ControllerTutorial() {
        super("sprites/menu/Menus/ControllerTutorial.png");
    }
    private long start;

    @Override
    public void onLoad() {
        super.onLoad();

        setLayer(101);
        start = System.currentTimeMillis();
    }

    @Override
    public void render() {
        update();
        ShaderFactory.executePostRender();
        super.render();
        ShaderFactory.executePreRender();
    }

    private void update() {
        if (System.currentTimeMillis() - start < 3000)
            return;
        if (Players.isAnyPlayerPressingButton(InputKeys.SELECT) || Players.isAnyPlayerPressingButton(InputKeys.DODGE)) {
            if (GameService.getCurrentQuest().isPaused()) {
                PauseMenu.INSTANCE.switchTo(PauseMenu.MAIN_MENU);
            } else if (GameService.getCurrentQuest() instanceof GameQuest) {
                ((GameQuest)GameService.getCurrentQuest()).closeTutorial();
            }
        }
    }
}
