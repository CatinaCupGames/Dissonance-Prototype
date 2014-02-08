package com.dissonance.framework.game.sprites.ui;

import com.dissonance.framework.render.UpdatableDrawable;

/**
 * Classes that implement {@link UI} are drawn in Screen Space instead of World Space. <br></br>
 * For example, if {@link UI#getX()} returns half the screen width, then its <br></br>
 * drawn in the center of the screen. <br></br>
 * This is useful for creating Menus and HUD's.
 */
public interface UI extends UpdatableDrawable {

    public void setX(float x);

    public void setY(float y);
}
