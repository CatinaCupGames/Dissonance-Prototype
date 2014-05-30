package com.dissonance.game.quests;

import com.dissonance.framework.game.player.Player;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.game.world.WorldFactory;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.game.scenes.BossStart;
import com.dissonance.game.w.CityEntrySquare;
import com.dissonance.game.w.CityEntrySquare2;

public class BossQuest extends PauseQuest {
    @Override
    public void startQuest() throws Exception {
        Camera.stopFollowing();
        WorldFactory.clearCache();
        World world = WorldFactory.getWorld("CityEntrySquare2");
        setWorld(world);
        world.waitForWorldDisplayed();
        CityEntrySquare2.farrand.freeze(true, BossQuest.class);
        CityEntrySquare2.jeremiah.freeze(true, BossQuest.class);

        RenderService.INSTANCE.fadeFromBlack(1800);
        RenderService.INSTANCE.waitForFade();

        playSceneAndWait(BossStart.class);

        Player player1 = Players.getPlayer1();
        player1.getSprite().unfreeze(BossQuest.class);
        Camera.followSprite(player1.getSprite());

        Player player2 = Players.getPlayer(2);
        if (player2 != null && player2.getSprite() != null) {
            player2.getSprite().unfreeze(BossQuest.class);
            Camera.followSprite(player2.getSprite());
        } else {
            if (player1.getSprite().equals(CityEntrySquare2.farrand)) {
                CityEntrySquare2.jeremiah.disappear();
            } else {
                CityEntrySquare2.farrand.disappear();
            }
        }
    }

    private void endBoss() throws InterruptedException {
        CityEntrySquare2.farrand.freeze(true, BossQuest.class);
        CityEntrySquare2.jeremiah.freeze(true, BossQuest.class);
        CityEntrySquare2.farrand.setUsePhysics(false);
        CityEntrySquare2.jeremiah.setUsePhysics(false);

        Player player1 = Players.getPlayer1();
        Player player2 = Players.getPlayer(2);
        if (player2 != null && player2.getSprite() != null) {
            player2.getSprite().disappear();
            Thread.sleep(900);
            player2.getSprite().rawSetX(player1.getSprite().getX());
            player2.getSprite().rawSetY(player1.getSprite().getY() + 32);
            player2.getSprite().appear();
            Thread.sleep(900);
        } else {
            if (player1.getSprite().equals(CityEntrySquare2.farrand)) {
                CityEntrySquare2.jeremiah.rawSetX(player1.getSprite().getX());
                CityEntrySquare2.jeremiah.rawSetY(player1.getSprite().getY() + 32);
                CityEntrySquare2.jeremiah.appear();
            } else {
                CityEntrySquare2.farrand.rawSetX(player1.getSprite().getX());
                CityEntrySquare2.farrand.rawSetY(player1.getSprite().getY() + 32);
                CityEntrySquare2.farrand.appear();
            }
        }

        //TODO Play and wait

        RenderService.INSTANCE.fadeToBlack(1f);
    }

    @Override
    public String getName() {
        return "The Boss";
    }

    private int death = 0;
    private final CombatSprite.CombatSpriteEvents LISTENER = new CombatSprite.CombatSpriteEvents() {
        @Override
        public void onDeath(CombatSprite sprite) {
            death++;
            if (death >= 3) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            endBoss();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    };
}
