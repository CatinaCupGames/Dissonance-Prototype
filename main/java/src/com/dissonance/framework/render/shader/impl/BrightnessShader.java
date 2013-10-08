package com.dissonance.framework.render.shader.impl;

import com.dissonance.framework.game.GameSettings;
import com.dissonance.framework.render.shader.AbstractShader;
import org.lwjgl.opengl.GL20;

public class BrightnessShader extends AbstractShader {

    @Override
    public String getVertexFile() {
        return "screenvert.glsl";
    }

    @Override
    public String getFragmentFile() {
        return "screenfrag.glsl";
    }

    @Override
    protected void onPreRender() {
        super.onPreRender();
        final int program = getProgramID();

        int brightnessVar = GL20.glGetUniformLocation(program, "brightness");
        int contrastVar = GL20.glGetUniformLocation(program, "contrast");
        int saturationVar = GL20.glGetUniformLocation(program, "saturation");
        int redVar = GL20.glGetUniformLocation(program, "red");
        int greenVar = GL20.glGetUniformLocation(program, "green");
        int blueVar = GL20.glGetUniformLocation(program, "blue");

        GL20.glUniform1f(brightnessVar, GameSettings.Graphics.color.brightness);
        GL20.glUniform1f(contrastVar, GameSettings.Graphics.color.contrast);
        GL20.glUniform1f(saturationVar, GameSettings.Graphics.color.saturation);
        GL20.glUniform1f(redVar, GameSettings.Graphics.color.red);
        GL20.glUniform1f(greenVar, GameSettings.Graphics.color.green);
        GL20.glUniform1f(blueVar, GameSettings.Graphics.color.blue);
    }
}
