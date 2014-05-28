package com.dissonance.game.triggers;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.game.ai.waypoint.WaypointType;
import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.sprites.impl.game.PhysicsSprite;
import com.dissonance.framework.game.world.tiled.impl.AbstractTrigger;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;
import com.dissonance.game.quests.GameQuest;
import com.dissonance.game.sprites.Farrand;
import com.dissonance.game.w.OutsideFighting;

public class FactoryEntry extends AbstractTrigger {
    @Override
    protected void onTrigger(PhysicsSprite sprite) throws Throwable {
        PlayableSprite player = (PlayableSprite)sprite;
        player.freeze();
        if (GameService.coop_mode) {
            Player player1 = Players.getPlayer1();
            Player player2 = Players.getPlayer(2);
            if (player1.getSprite().equals(player)) {
                if (player2 != null && player2.getSprite() != null) {
                    player2.getSprite().freeze();
                    player2.getSprite().setWaypoint(player.getX(), player.getY() + (player.getHeight()) + 16, WaypointType.SMART);
                    player2.getSprite().waitForEndOfWaypointQueue();
                }
            } else {
                player1.getSprite().freeze();
                player1.getSprite().setWaypoint(player.getX(), player.getY() + (player.getHeight()) + 16, WaypointType.SMART);
                player1.getSprite().waitForEndOfWaypointQueue();
            }
        } else {
            if (player.equals(OutsideFighting.farrand)) {
                OutsideFighting.jeremiah.setX(player.getX() + player.getWidth() + 8);
                OutsideFighting.jeremiah.setY(player.getY());
                OutsideFighting.jeremiah.appear();
                OutsideFighting.jeremiah.face(Direction.LEFT);
            } else {
                OutsideFighting.farrand.setX(player.getX() + player.getWidth() + 8);
                OutsideFighting.farrand.setY(player.getY());
                OutsideFighting.farrand.appear();
                OutsideFighting.farrand.face(Direction.LEFT);
            }

            Thread.sleep(750);
        }

        Dialog.displayDialog("factory_enter");

        RenderService.INSTANCE.fadeToBlack(800);
        RenderService.INSTANCE.waitForFade();

        GameQuest.INSTANCE.changeToFactory();
    }

    @Override
    protected long triggerTimeout() {
        return 500;
    }
}
