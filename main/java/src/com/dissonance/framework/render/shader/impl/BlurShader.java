package com.dissonance.framework.render.shader.impl;

import com.dissonance.framework.game.GameSettings;
import com.dissonance.framework.render.shader.AbstractShader;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL11.*;

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
        public String getName()
        {
            return "HorizontalBlur";
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
            setActive(true);
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
        public String getName()
        {
            return "VerticalBlur";
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

        @Override
        protected void onPostRender() {
            super.onPostRender();
            glAccum(GL_MULT, 0.90f);
            glAccum(GL_ACCUM, 1.0f - 0.90f);
            glAccum(GL_RETURN, 1.0f);
        }

        private void b()
        {
            build();
            setActive(true);
        }
    }
}
