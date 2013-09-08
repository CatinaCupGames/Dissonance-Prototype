package com.dissonance.framework.game.input;

import java.util.List;

/**
 * An InputListener can be passed to the {@link InputService} to
 * listen for specific keyboard or mouse input.
 */
public interface InputListener {
    /**
     * Returns the list of keyboard keys this listener is listening for.
     *
     * @return A list containing the LWJGL key codes of the keys to listen for.
     */
    public List<Integer> getKeys();

    /**
     * Returns the list of mouse buttons this listener is listening for.
     *
     * @return A list containing the LWJGL button codes of the buttons to listen for.
     */
    public List<Integer> getButtons();

    /**
     * The {@link InputService} calls this method whenever a key that
     * this listener is listening for is pressed.
     * <strong>Not recommended for consistent input such as player movement.</strong>
     *
     * @param key The LWJLG key code of the key that was pressed.
     */
    public void inputPressed(int key);

    /**
     * The {@link InputService} calls this method whenever a button that
     * this listener is listening for is pressed.
     *
     * @param button The LWJGL button code of the button that was pressed.
     * @param x      The X location of the cursor on the screen.
     * @param y      The Y location of the mouse on the screen.
     */
    public void inputClicked(int button, int x, int y);
}
