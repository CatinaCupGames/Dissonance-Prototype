package com.dissonance.framework.game.combat;

import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;

public abstract class Spell extends AnimatedSprite {
    protected CombatSprite owner;
    protected double speed;
    protected int alive;

    private long lCheck;
    private boolean active;

    public Spell(Direction direction, CombatSprite owner) {
        this(direction, owner, 30);
    }

    public Spell(Direction direction, CombatSprite owner, double speed) {
        super();
        super.direction = direction;
        this.owner = owner;
    }

    public CombatSprite getOwner() {
        return owner;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        if (alive <= 0 && active)
            return;
        this.active = active;
    }

    public void setAliveTime(int time) {
        this.alive = time;
    }

    public int getAliveTime() {
        return alive;
    }

    @Override
    public void update() {
        super.update();
        if (isUpdateCanceled())
            return;
        if (!active)
            return;
        if (lCheck != 0) {
            long diff = System.currentTimeMillis() - lCheck;
            alive -= diff;
            if (alive <= 0) {
                onDestroy();
                setActive(false);
                getWorld().removeSprite(this);
                return;
            }
        }
        lCheck = System.currentTimeMillis();
    }

    @Override
    public void render() {
        if (!active)
            return;
        super.render();
    }

    protected void moveOneFrame() {
        if (direction == Direction.NONE)
            return;
        double xadd = 0, yadd = 0;
        if (direction == Direction.DOWN)
            yadd = -1;
        else if (direction == Direction.UP)
            yadd = 1;
        else if (direction == Direction.LEFT)
            xadd = 1;
        else if (direction == Direction.RIGHT)
            xadd = -1;

        setX((float) ((getX() + (speed * xadd)) * RenderService.TIME_DELTA));
        setY((float) ((getY() + (speed * yadd)) * RenderService.TIME_DELTA));
    }

    protected abstract void onDestroy();

    protected abstract void onActivate();

    public void activate() {
        onActivate();
        setActive(true);
    }
}
