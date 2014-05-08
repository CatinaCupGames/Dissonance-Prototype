package com.dissonance.framework.render.shader.impl;

import com.dissonance.framework.render.shader.AbstractShader;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;

public class BatchShader extends AbstractShader {
    public final int[] SIZE = new int[] {
            2,
            4,
            2
    };
    public int[] positions = new int[3];

    public int projectionLoc = -1, textureLoc = -1;

    @Override
    public String getVertexFile() {
        return "sbatch.vert";
    }

    @Override
    public String getFragmentFile() {
        return "sbatch.frag";
    }

    @Override
    public String getName() {
        return "Sprite Batch Shader";
    }

    @Override
    public void build() {
        super.build();
        positions[0] = 1;
        positions[1] = 2;
        positions[2] = 3;
    }

    @Override
    protected void onPreRender() {
        super.onPreRender();
        final int program = super.getProgramID();
        if (projectionLoc == -1) {
            projectionLoc = GL20.glGetUniformLocation(program, "projection");
        }
        if (textureLoc == -1) {
            textureLoc = GL20.glGetUniformLocation(program, "texture");
        }

        if (positions[0] == -1) {
            positions[0] = GL20.glGetUniformLocation(program, "position");
        }

        if (positions[1] == -1) {
            positions[1] = GL20.glGetUniformLocation(program, "color");
        }

        if (positions[2] == -1) {
            positions[2] = GL20.glGetUniformLocation(program, "texcoord");
        }
    }

    public void setUniforms(Matrix4f projection, int texture) {
        FloatBuffer temp = BufferUtils.createFloatBuffer(16);
        projection.store(temp);
        temp.flip();

        GL20.glUniformMatrix4(projectionLoc, false, temp);

        GL20.glUniform1i(textureLoc, texture);
    }
}
