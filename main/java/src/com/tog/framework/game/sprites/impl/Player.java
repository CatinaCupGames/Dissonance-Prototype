package com.tog.framework.game.sprites.impl;

import com.tog.framework.game.input.InputListener;
import com.tog.framework.game.input.InputService;
import com.tog.framework.game.sprites.AnimatedSprite;
import com.tog.framework.render.Camera;
import com.tog.framework.system.ServiceManager;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player extends AnimatedSprite implements InputListener {
    private List<Integer> keys = new ArrayList<Integer>();
    private List<Integer> mouse = new ArrayList<Integer>();

    @Override
    public void onLoad() {
        super.onLoad();
        ServiceManager.getService(InputService.class).provideData(this, InputService.ADD_LISTENER);
        keys.addAll(Arrays.asList(
                Keyboard.KEY_W, Keyboard.KEY_A, Keyboard.KEY_S, Keyboard.KEY_D
        ));
    }

    @Override
    public void setX(float x) {
        super.setX(x);
        Camera.setX(Camera.translateToCameraCenter(getVector(), 32f, 32f).getX());
    }

    @Override
    public void setY(float y) {
        super.setY(y);
        Camera.setY(Camera.translateToCameraCenter(getVector(), 32f, 32f).getY());
    }

    @Override
    public String getSpriteName() {
        return "player";
    }

    @Override
    public List<Integer> getKeys() {
        return keys;
    }

    @Override
    public List<Integer> getButtons() {
        return mouse;
    }

    @Override
    public void inputPressed(int key) {
        switch (key) {
            case Keyboard.KEY_W:
                setY(getY() + 5);
                break;
            case Keyboard.KEY_S:
                setY(getY() - 5);
                break;
            case Keyboard.KEY_A:
                setX(getX() - 5);
                break;
            case Keyboard.KEY_D:
                setX(getX() + 5);
                break;
        }
    }

    @Override
    public void inputClicked(int button, int x, int y) { }
}
