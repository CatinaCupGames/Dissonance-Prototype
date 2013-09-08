package com.dissonance.framework.game.ai;

import org.jbox2d.common.Vec2;

import java.io.Serializable;

public final class Position implements Serializable {

    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(Vec2 vector) {
        this((int)vector.x, (int)vector.y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
         if (!(obj instanceof Position)) {
            return false;
         }

         Position pos = (Position) obj;

         return (pos.x == x && pos.y == y);
    }
}
