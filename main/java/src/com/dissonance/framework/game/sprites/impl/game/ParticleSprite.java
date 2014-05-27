package com.dissonance.framework.game.sprites.impl.game;

import com.dissonance.framework.game.sprites.impl.UpdatableSprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.system.utils.Direction;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class ParticleSprite extends UpdatableSprite {
    private Color color;
    private static Texture t;
    private float speed;
    private float orginalX = -1;
    private float orginalY = -1;
    private long startTime;
    private double tick;
    private double aplitude;
    private Direction direction;
    private long time = 800L;

    private ParticleSprite() { }

    public static ParticleSource createParticlesAt(final float x, final float y, final float count, final float speed, final Color color, final World world) {
        ParticleSource source = new ParticleSource();

        source.world = world;

        source.setX(x)
                .setY(y)
                .setCount(count)
                .setSpeed(speed)
                .setColor(color);

        source.run();

        return source;
    }

    public static ParticleSource createParticlesAt(float x, float y, World world) {
        ParticleSource source = new ParticleSource();
        source.setX(x).setY(y);
        source.world = world;
        source.run();
        return source;
    }

    @Override
    public void onLoad() {
        if (t == null) {
            try {
                t = Texture.retrieveTexture("sprites/img/particle.png");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setTexture(t);

        startTime = RenderService.getTime();
        setWidth(getWidth() / 4f);
        setHeight(getHeight() / 4f);
    }

    @Override
    public void init() {
        setTint(color);
    }

    @Override
    public void update() {
        super.update();
        if (isUpdateCanceled())
            return;
        if (orginalX == -1)
            orginalX = getX();
        if (orginalY == -1)
            orginalY = getY();

        float a = Camera.ease(1f, 0f, time, RenderService.getTime() - startTime);
        setTint(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, a);
        if (a == 0f) {
            getWorld().removeSprite(this);
            return;
        }


        tick += 0.1;
        double ding = 10.0 * Math.sin(tick * (1.0/3.0));
        float x, y;
        switch (direction) {
            case UP:
            case UP_RIGHT:
            case UP_LEFT:
                y = getY() - (speed * RenderService.TIME_DELTA);
                x = (float)(orginalX + ding);
                break;
            case DOWN:
            case DOWN_LEFT:
            case DOWN_RIGHT:
                y = getY() + speed * RenderService.TIME_DELTA;
                x = (float)(orginalX + ding);
                break;
            case LEFT:
                x = getX() - (speed * RenderService.TIME_DELTA);
                y = (float)(orginalY + ding);
                break;
            case RIGHT:
                x = getX() + speed * RenderService.TIME_DELTA;
                y = (float)(orginalY + ding);
                break;
            default:
                return;
        }

        setY(y);

        setX(x);
    }

    public static final class ParticleSource {
        private float x;
        private float y;
        private float count = 1f;
        private float speed = 5f;
        private long time = 800L;
        private int rate = 200;
        private Direction direction = Direction.UP;
        private Color color = Color.white;
        private World world;
        private final Random random = new Random();
        private boolean end = false;

        public ParticleSource setDirection(Direction direction) {
            this.direction = direction;
            return this;
        }

        public Direction getDirection() {
            return direction;
        }

        public ParticleSource setSpeed(float speed) {
            this.speed = speed;
            return this;
        }

        public ParticleSource setCount(float count) {
            this.count = count;
            return this;
        }

        public float getX() {
            return x;
        }

        public ParticleSource setRate(int rate) {
            this.rate = rate;
            return this;
        }

        public int getRate() {
            return rate;
        }

        public ParticleSource setX(float x) {
            this.x = x;
            return this;
        }

        public float getY() {
            return y;
        }

        public ParticleSource setY(float y) {
            this.y = y;
            return this;
        }

        public float getCount() {
            return count;
        }

        public float getSpeed() {
            return speed;
        }

        public long getTime() {
            return time;
        }

        public ParticleSource setTime(long time) {
            this.time = time;
            return this;
        }

        public Color getColor() {
            return color;
        }

        public ParticleSource setColor(Color color) {
            this.color = color;
            return this;
        }

        public World getWorld() {
            return world;
        }

        private void run() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < count; i++) {
                        if (end)
                            break;
                        ParticleSprite sprite = new ParticleSprite();
                        sprite.x = x;
                        sprite.y = y;
                        sprite.orginalX = x;
                        sprite.orginalY = y;
                        sprite.direction = direction;
                        sprite.speed = speed;
                        sprite.color = color;
                        sprite.time = time;
                        sprite.aplitude = random.nextDouble();
                        sprite.tick = random.nextDouble() * (2 * Math.PI) / sprite.aplitude;
                        world.loadAndAdd(sprite);

                        try {
                            Thread.sleep(random.nextInt(rate) + rate / 4);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }

        public void end() {
            end = true;
        }

        public boolean hasEnded() {
            return end;
        }
    }
}
