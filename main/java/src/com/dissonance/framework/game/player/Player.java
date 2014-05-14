package com.dissonance.framework.game.player;

public class Player {
    private PlayableSprite sprite;
    private int number;
    private Input input;

    Player(int number, Input input) {
        this.number = number;
        this.input = input;
    }

    public int getNumber() {
        return number;
    }

    public PlayableSprite getSprite() {
        return sprite;
    }

    public void changeSprite(PlayableSprite sprite) {
        if (this.sprite != null) {
            this.sprite.deselect();
        }
        this.sprite = sprite;
        this.sprite.setInput(input);
        this.sprite.setVisible(true);
        this.sprite.select(this);
    }

    /**
     * Make this player join the game as the selected sprite. <br></br>
     * If this player is equal to {@link com.dissonance.framework.game.player.Players#getPlayer1()}, then no checks are done and the player joins the game. <br></br>
     * Otherwise, an {@link java.lang.IllegalAccessError} will be thrown if the following occurs: <br></br>
     *   * {@link com.dissonance.framework.game.player.Players#getPlayer1()}.{@link com.dissonance.framework.game.player.Player#getSprite()} returns null (Player 1 has not joined yet) <br></br>
     *   * If another player is using the sprite in the parameter
     * @param sprite The {@link com.dissonance.framework.game.player.PlayableSprite} to join as
     * @throws java.lang.IllegalAccessError If this player already has a sprite, or if any of the conditions above are met.
     */
    public void joinAs(PlayableSprite sprite) {
        if (this.sprite != null)
            throw new IllegalAccessError("This player has already joined. Use the changeSprite method to change the player's sprite.");

        if (this == Players.getPlayer1()) {
            changeSprite(sprite);
        } else {
            Player player = Players.getPlayer1();
            if (player.getSprite() == null) {
                throw new IllegalAccessError("You can't join if player 1 hasn't joined!");
            }

            Player[] players = Players.getPlayers();
            for (Player p : players) {
                if (p.getSprite() == sprite) {
                    throw new IllegalAccessError("You can't be the same sprite as another player!");
                }
            }

            if (!player.getSprite().isAlly(sprite)) {
                sprite.joinParty(player.getSprite());
            }

            changeSprite(sprite);
        }
    }

    /**
     * Make this player join the game as the next open playablesprite in {@link com.dissonance.framework.game.player.Players#getPlayer1()} party. <br></br>
     * If there are no sprites available, then a {@link java.lang.IllegalAccessError} is thrown.
     * @throws java.lang.IllegalAccessError If no open sprite are available
     */
    public void join() {
        Player player = Players.getPlayer1();
        if (player == null)
            throw new IllegalAccessError("There is no player 1!");
        if (player.getSprite() == null)
            throw new IllegalAccessError("Player 1 hasn't joined yet!");

        PlayableSprite[] party = player.getSprite().getParty();
        PlayableSprite selected = null;
        for (PlayableSprite sprite : party) {
            if (!sprite.isPlaying()) {
                selected = sprite;
                break;
            }
        }

        joinAs(selected);
    }

    public Input getInput() {
        return input;
    }
}
