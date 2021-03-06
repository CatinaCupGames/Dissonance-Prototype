package com.dissonance.game.sprites.factory;

import com.dissonance.framework.game.player.PlayableSprite;
import com.dissonance.framework.game.player.Players;
import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.system.utils.physics.HitBox;
import com.dissonance.game.quests.GameQuest;
import com.dissonance.game.sprites.Farrand;
import com.dissonance.game.sprites.ImageSprite;

public class Key extends ImageSprite {
    private boolean pickable = false;
    public Key() {
        super("sprites/img/Keycard.png");
    }

    @Override
    public void onLoad() {
        super.onLoad();
        setLayer(getLayer() - 1);
    }

    @Override
    public void render() {
        update();
        super.render();
    }

    @Override
    protected void onBlink() {
        super.onBlink();
        pickable = true;
    }

    private void update() {
        if (!pickable)
            return;
        PlayableSprite[] sprites = Players.getCurrentlyPlayingSprites();
        for (final PlayableSprite sprite : sprites) {
            if (sprite.isPointInside(getX(), getY()) ||
                    sprite.isPointInside(getX() + (getWidth()), getY()) ||
                    sprite.isPointInside(getX() - getWidth(), getY()) ||
                    sprite.isPointInside(getX(), getY() + getHeight()) ||
                    sprite.isPointInside(getX(), getY() - getHeight())) {
                GameQuest.INSTANCE.unlockedControl = true;
                pickable = false;
                getWorld().removeSprite(this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (sprite instanceof Farrand) {
                            Dialog.displayDialog("found_key_farrand");
                        } else {
                            Dialog.displayDialog("found_key_jeremiah");
                        }
                    }
                }).start();
            }
        }
    }
}
