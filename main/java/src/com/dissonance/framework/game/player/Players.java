package com.dissonance.framework.game.player;

import com.dissonance.framework.game.player.input.joypad.Joypad;
import com.dissonance.framework.game.player.input.joypad.JoypadService;
import com.dissonance.framework.system.Service;
import com.dissonance.framework.system.ServiceManager;
import com.dissonance.framework.system.debug.Debug;
import com.dissonance.framework.system.utils.Validator;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Players {
    public static float[][] PLAYER_COLORS = {
            {0.77647058823f, 0f, 0f},
            {0f, 0.32156862745f, 0.77647058823f},
            {0f, 0.62745098039f, 0f},
            {1f, 0.85098039215f, 0f}
    };

    private static Player[] players = new Player[4];
    private static JoypadService joypadService;

    /**
     * Get player 1.
     * @return Returns an {@link com.dissonance.framework.game.player.Player} object that represents player 1.
     */
    public static Player getPlayer1() {
        return players[0];
    }

    /**
     * Create a new player that will use the given {@link com.dissonance.framework.game.player.Input} device.
     * @param input The input device this player will use
     * @return The created player object, or null if there are to many players.
     */
    public static Player createPlayer(Input input) {
        checkIfInputUsed(input);

        int slot = 0;
        for (; slot < players.length; slot++) {
            if (players[slot] == null) break;
        }
        if (players[slot] != null) {
            if (Debug.isDebugging()) System.err.println("TO MANY PLAYERS!");
            return null;
        }

        players[slot] = new Player(slot + 1, input);
        System.out.println("Player " + (slot + 1) + " registered with input device " + input.getName());
        return players[slot];
    }

    public static Player createPlayer1() {
        if (getPlayer1() != null)
            return getPlayer1();
        List<Joypad> joypads = getOpenControllers();
        if (joypads.size() > 0) {
            return createPlayer(joypads.get(0).createInput());
        }
        return createPlayer(Input.KEYBOARD);
    }

    static void removePlayer(Player player) {
        for (int i = 0; i < players.length; i++) {
            if (player == players[i])
                players[i] = null;
        }
    }

    /**
     * Return a list of {@link com.dissonance.framework.game.player.input.joypad.Joypad} objects that are not being used by any
     * players.
     * @return An unmodifiable list of {@link com.dissonance.framework.game.player.input.joypad.Joypad} objects that are not in use.
     */
    public static List<Joypad> getOpenControllers() {
        if (joypadService == null) {
            joypadService = ServiceManager.createService(JoypadService.class);
            while (!joypadService.hasStarted()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        Joypad[] joypads = joypadService.getJoypads();

        ArrayList<Joypad> open = new ArrayList<Joypad>();

        for (Joypad joypad : joypads) {
            boolean found = false;

            for (Player p : players) {
                if (p != null) {
                    if (p.getInput() instanceof ControllerInput) {
                        ControllerInput input = (ControllerInput)p.getInput();
                        if (joypad.getController().equals(input.getController())) {
                            found = true;
                            break;
                        }
                    }
                }
            }

            if (!found) {
                open.add(joypad);
            }
        }

        return Collections.unmodifiableList(open);
    }

    /**
     * Get the player with the specified number. This method is not 0 based, so getPlayer(1) will return the same result as
     * {@link com.dissonance.framework.game.player.Players#getPlayer1()}.
     * @param number The number of the player
     * @return Returns an {@link com.dissonance.framework.game.player.Player} object that represents the found player, or null if no player was found.
     */
    public static Player getPlayer(int number) {
        number--;
        Validator.validateNotBelow(number, 0, "number");
        Validator.validateNotOver(number, 3, "number");

        return players[number];
    }

    /**
     * Get all currently playing {@link com.dissonance.framework.game.player.PlayableSprite} objects.
     * @return All currently playing {@link com.dissonance.framework.game.player.PlayableSprite} objects
     */
    public static PlayableSprite[] getCurrentlyPlayingSprites() {
        ArrayList<PlayableSprite> sprites = new ArrayList<PlayableSprite>();
        for (Player player : players) {
            if (player != null && player.getSprite() != null && player.getSprite().isPlaying()) {
                sprites.add(player.getSprite());
            }
        }

        return sprites.toArray(new PlayableSprite[sprites.size()]);
    }

    /**
     * Get all currently playing players
     * @return All currently playing players in an array of {@link com.dissonance.framework.game.player.Player} objects
     */
    public static Player[] getPlayers() {
        ArrayList<Player> sprites = new ArrayList<Player>();
        for (Player player : players) {
            if (player != null && player.getSprite() != null && player.getSprite().isPlaying()) {
                sprites.add(player);
            }
        }

        return sprites.toArray(new Player[sprites.size()]);
    }

    /**
     * Get all player objects that have an input device. The players returned may or may not be playing.
     * @return All player objects with input in an array of {@link com.dissonance.framework.game.player.Player} objects
     */
    public static Player[] getPlayersWithInput() {
        ArrayList<Player> sprites = new ArrayList<Player>();
        for (Player player : players) {
            if (player != null && player.getInput() != null) {
                sprites.add(player);
            }
        }

        return sprites.toArray(new Player[sprites.size()]);
    }

    /**
     * Get the number of possible players that can play at this time. This method will <b>NOT</b> check to see if
     * new slots have opened
     * @return The total number of possible players that can play at this time.
     */
    public static int getMaxPlayerCount() {
        if (getPlayer1() == null)
            throw new IllegalAccessError("There is no player 1!");
        if (getPlayer1().getSprite() == null)
            throw new IllegalAccessError("Player 1 does not have a sprite!");
        return getPlayer1().getSprite().getParty().length;
    }

    public static void isAnyPlayerPressingButton(String key) {

    }

    /**
     * Get the current number of players playing <b>with a sprite</b>. <br></br>
     * This is a convenience method and is the same as calling {@link com.dissonance.framework.game.player.Players#getPlayers()}.length
     * @return The number of currently playing players
     */
    public static int getPlayingCount() {
        return getPlayers().length;
    }

    /**
     * Get the amount of open slots available. If a player is registered, but has not joined with a sprite, then they are not counted.
     * @return The total number of open slots.
     */
    public static int getOpenSlots() {
        return getMaxPlayerCount() - getPlayingCount();
    }

    public static boolean isInputUsed(Input input) {
        for (Player player : players) {
            if (player == null)
                continue;
            if (player.getInput().equals(input))
                return true;
        }
        return false;
    }

    private static void checkSlots() {
        if (getMaxPlayerCount() != players.length) {
            //Resize the player array
            Player[] temp = new Player[players.length];
            System.arraycopy(players, 0, temp, 0, players.length);

            players = new Player[getMaxPlayerCount()];
            System.arraycopy(temp, 0, players, 0, temp.length);
        }
    }

    private static void checkIfInputUsed(Input input) {
        for (Player player : players) {
            if (player == null)
                continue;
            if (player.getInput().equals(input))
                throw new InvalidParameterException("This input is currently being used by another player!");
        }
    }
}
