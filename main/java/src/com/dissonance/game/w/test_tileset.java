package com.dissonance.game.w;

import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.game.quests.TestQuest;
import com.dissonance.game.sprites.Enemy;
import com.dissonance.game.sprites.TestNPC;
import com.dissonance.game.sprites.TestPlayer;

public class test_tileset extends GameWorldLoader {
    @Override
    public void onLoad(World w) {
        super.onLoad(w);

        TestNPC pl;
        pl = new TestNPC("TEST");
        w.loadAndAdd(pl);
        pl.setX(576);
        pl.setY(256);
        pl.setWidth(pl.getWidth() * 2);
        pl.setHeight(pl.getHeight() * 2);

        Enemy e = new Enemy("enemy1", Enemy.StatType.NON_MAGIC, CombatSprite.CombatType.CREATURE, TestQuest.testEnemyInterface);
        w.loadAndAdd(e);
        e.setX(400);
        e.setY(400);
    }
}
