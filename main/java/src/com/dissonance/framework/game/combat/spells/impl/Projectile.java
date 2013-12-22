package com.dissonance.framework.game.combat.spells.impl;

import com.dissonance.framework.game.combat.spells.Spell;
import com.dissonance.framework.game.sprites.impl.AnimatedSprite;
import com.dissonance.framework.game.sprites.impl.game.CombatSprite;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.Direction;

public abstract class Projectile extends AnimatedSprite implements Spell {
    protected CombatSprite owner;
    protected float speed = 7;

    public Projectile(CombatSprite owner) {
        this.owner = owner;
    }

    @Override
    public void castSpell() {
        owner.getWorld().addSprite(this);
        setX(owner.getX());
        setY(owner.getY());
        setFacing(owner.getDirection());
        if (owner.getDirection() == Direction.DOWN)
            setY(getY() + 32);
        else if (owner.getDirection() == Direction.LEFT)
            setX(getX() - 32);
        else if (owner.getDirection() == Direction.RIGHT)
            setX(getX() + 32);
        else if (owner.getDirection() == Direction.UP)
            setY(getY() - 32);
    }

    @Override
    public void update() {
        super.update();
        if (!isUpdateCanceled())
            return;

    }

    @Override
    public CombatSprite getOwner() {
        return owner;
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

    protected void setSpeed(float speed) {
        this.speed = speed;
    }

    protected float getSpeed() {
        return speed;
    }

    public abstract int getRange();
}
