package com.dissonance.game.triggers;

import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.scene.dialog.DialogFactory;
import com.dissonance.framework.game.scene.dialog.DialogUI;
import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;
import com.dissonance.framework.game.world.tiled.impl.AbstractTrigger;
import com.dissonance.framework.render.RenderService;

public class TestTrigger extends AbstractTrigger {
    @Override
    protected void onTrigger(final PlayableSprite sprite) {
        setActive(false);
        new Thread(new Runnable() {

            @Override
            public void run() {
                sprite.freeze();
                Dialog dialog = DialogFactory.getDialog("testingTrigger");
                DialogUI ui = new DialogUI(dialog);
                ui.displayUI(false, sprite.getWorld());
                try {
                    ui.waitForEnd();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                RenderService.INSTANCE.fadeToBlack(1000);
                try {
                    RenderService.INSTANCE.waitForFade();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                sprite.setX(getParent().getX() + (getParent().getWidth() / 2));
                sprite.setY(getParent().getY() - 100);

                RenderService.INSTANCE.fadeFromBlack(1000);
                try {
                    RenderService.INSTANCE.waitForFade();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                dialog = DialogFactory.getDialog("testTrigger2");
                ui = new DialogUI(dialog);
                ui.displayUI(false, sprite.getWorld());
                try {
                    ui.waitForEnd();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                sprite.unfreeze();

                setActive(true);
            }
        }).start();
    }

    @Override
    protected long triggerTimeout() {
        return 5000;
    }
}
