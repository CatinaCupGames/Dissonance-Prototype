package com.dissonance.framework.game.player;

import com.dissonance.framework.system.debug.Debug;
import com.dissonance.framework.system.utils.Validator;

import java.util.ArrayList;

public class Players {
    private static Player[] players = new Player[4];

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
        return null;
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
}
