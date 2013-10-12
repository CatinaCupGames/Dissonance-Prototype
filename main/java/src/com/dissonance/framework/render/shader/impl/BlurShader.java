package com.dissonance.framework.render.shader.impl;

import com.dissonance.framework.game.GameSettings;
import com.dissonance.framework.render.shader.AbstractShader;
import org.lwjgl.opengl.GL20;

public class BlurShader
{
    public BlurShader()
    {
    }

    public void build()
    {
        new Horizontal().b();
        new Vertical().b();
    }

    private class Horizontal extends AbstractShader
    {
        @Override
        public String getVertexFile()
        {
            return "main.vert";
        }

        @Override
        public String getFragmentFile()
        {
            return "hblur.frag";
        }

        @Override
        protected void onPreRender()
        {
            super.onPreRender();

            int program = getProgramID();

            int resolution_widthVar = GL20.glGetUniformLocation(program, "resolution_width");
            int radiusVar = GL20.glGetUniformLocation(program, "radius");

            GL20.glUniform1f(resolution_widthVar, GameSettings.Display.window_width);
            GL20.glUniform1f(radiusVar, 1.2f);
        }

        private void b()
        {
            build();
        }
    }

    private class Vertical extends AbstractShader
    {
        @Override
        public String getVertexFile()
        {
            return "main.vert";
        }

        @Override
        public String getFragmentFile()
        {
            return "vblur.frag";
        }

        @Override
        protected void onPreRender()
        {
            super.onPreRender();

            int program = getProgramID();

            int resolution_heightVar = GL20.glGetUniformLocation(program, "resolution_height");
            int radiusVar = GL20.glGetUniformLocation(program, "radius");

            GL20.glUniform1f(resolution_heightVar, GameSettings.Display.window_height);
            GL20.glUniform1f(radiusVar, 1.2f);
        }

        private void b()
        {
            build();
        }
    }
}
