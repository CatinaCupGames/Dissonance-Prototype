package com.dissonance.test.triggers;

import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.scene.dialog.DialogFactory;
import com.dissonance.framework.game.scene.dialog.DialogUI;
import com.dissonance.framework.game.sprites.impl.game.PlayableSprite;
import com.dissonance.framework.game.world.tiled.impl.AbstractTrigger;
import com.dissonance.framework.render.RenderService;

public class TestTrigger extends AbstractTrigger {
    @Override
    protected void onTrigger(final PlayableSprite player) throws Throwable { //This method is executed outside the render thread, so we can safely wait for things without locking up
        player.freeze(); //Freeze the player, he can't use any buttons but can still be moved manually.

        Dialog dialog = DialogFactory.getDialog("testingTrigger"); //Load up some dialog
        DialogUI ui = new DialogUI(dialog); //Create a new dialog box
        ui.displayUI(false, player.getWorld()); //Display the dialog box, do not handle freezing the player, and provide the world
        ui.waitForEnd(); //Wait for the player to dismiss the dialog box

        RenderService.INSTANCE.fadeToBlack(1000); //Fade the screen to black, the speed will be 1 second (or 1000ms)
        RenderService.INSTANCE.waitForFade(); //Wait for the screen to be black

        player.setX(getParent().getX() + (getParent().getWidth() / 2)); //Move the player's X to the middle of the trigger
        player.setY(getParent().getY() - 100); //Move the player up

        RenderService.INSTANCE.fadeFromBlack(1000); //Fade the screen back to normal, the speed will be 1 second (or 1000ms)
        RenderService.INSTANCE.waitForFade(); //Wait for the screen to be normal

        dialog = DialogFactory.getDialog("testTrigger2"); //Load up some dialog
        ui = new DialogUI(dialog); //Create a new dialog box
        ui.displayUI(false, player.getWorld()); //Display the dialog box, do not handle freezing the player, and provide the world
        ui.waitForEnd(); //Wait for the player to dismiss the dialog box

        player.unfreeze(); //Unfreeze the player, the player can now use his joystick/keyboard
    }

    @Override
    protected long triggerTimeout() {
        return 5000; //The timeout for this trigger will be every 5 seconds (or 5000ms)
    }
}
