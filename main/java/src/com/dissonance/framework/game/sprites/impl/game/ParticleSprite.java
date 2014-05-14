package com.dissonance.framework.game.sprites.impl.game;

import com.dissonance.framework.game.sprites.impl.UpdatableSprite;
import com.dissonance.framework.game.world.World;
import com.dissonance.framework.render.Camera;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.texture.Texture;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class ParticleSprite extends UpdatableSprite {
    private Color color;
    private static Texture t;
    private float speed;
    private float orginalX = -1;
    private long startTime;
    private double tick;
    private double aplitude;

    private ParticleSprite() { }

    public static void createParticlesAt(final float x, final float y, final float count, final float speed, final Color color, final World world) {
        final Random random = new Random();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < count; i++) {
                    ParticleSprite sprite = new ParticleSprite();
                    sprite.x = x;
                    sprite.y = y;
                    sprite.orginalX = x;
                    sprite.speed = speed;
                    sprite.color = color;
                    sprite.aplitude = random.nextDouble();
                    sprite.tick = random.nextDouble() * (2 * Math.PI) / sprite.aplitude;
                    world.loadAndAdd(sprite);

                    try {
                        Thread.sleep(random.nextInt(200) + 50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
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

        float a = Camera.ease(1f, 0f, 800f, RenderService.getTime() - startTime);
        setTint(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, a);
        if (a == 0f) {
            getWorld().removeSprite(this);
            return;
        }

        setY(getY() - (speed * RenderService.TIME_DELTA));

        tick += 0.1;
        double ding = 10.0 * Math.sin(tick * (1.0/3.0));

        setX((float) (orginalX + ding));
    }
}
