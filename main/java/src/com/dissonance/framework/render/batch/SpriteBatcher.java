package com.dissonance.framework.render.batch;

import com.dissonance.framework.game.sprites.Sprite;
import com.dissonance.framework.render.shader.AbstractShader;
import com.dissonance.framework.render.shader.ShaderFactory;
import com.dissonance.framework.render.shader.impl.BatchShader;
import com.dissonance.framework.render.texture.Texture;
import com.dissonance.framework.system.GameSettings;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Matrix;
import org.lwjgl.util.vector.Matrix4f;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class SpriteBatcher {
    private static final int totalComp = 2 + 4 + 2;

    static BatchShader shader;

    protected FloatBuffer buffer;
    protected Matrix4f projectionMatrix = new Matrix4f();
    protected Matrix4f viewMatrix = new Matrix4f();
    protected Matrix4f transposition = new Matrix4f();
    protected Matrix4f cache = new Matrix4f();
    protected Texture texture;

    private FloatBuffer shaderBuffer;
    private int max;
    private int idx;

    public SpriteBatcher(int items) {
        if (shader == null) {
            shader = new BatchShader();
            shader.build();
        }

        shaderBuffer = BufferUtils.createFloatBuffer((items * 6) * totalComp);
        max = items * 6;

        projectionMatrix = toOrtho2D(projectionMatrix, 0, 0, GameSettings.Display.window_width, GameSettings.Display.window_height);
        update();
    }

    public void update() {
        cache = Matrix4f.mul(Matrix4f.transpose(projectionMatrix, transposition), viewMatrix, cache);

        shader.preRender();
        shader.setUniforms(cache, 0);
    }

    private boolean drawing;
    public void startBatch() {
        if (drawing)
            return;
        drawing = true;

        if (!shader.isBound()) shader.preRender();
        rCalls = 0;

        texture = null;
    }

    public void endBatch() {
        if (drawing)
            return;
        drawing = false;
        shader.postRender();
        flush();
    }

    public void drawSprite(Sprite s) {
        addToBatch(s.getTexture(), s.getX(), s.getY(), s.getWidth(), s.getHeight(), s.getX(), s.getY(), 0f, 0f, 0f, 1f, 1f);
    }

    public void addToBatch(Texture tex, float x, float y, float width, float height, float originX, float originY, float rotation, float u, float v, float u2, float v2) {
        checkFlush(tex);

        final float r = 1f;
        final float g = 1f;
        final float b = 1f;
        final float a = 1f;


        float x1,y1, x2,y2, x3,y3, x4,y4;

        if (rotation != 0) {
            float scaleX = 1f;//width/tex.getWidth();
            float scaleY = 1f;//height/tex.getHeight();

            float cx = originX*scaleX;
            float cy = originY*scaleY;

            float p1x = -cx;
            float p1y = -cy;
            float p2x = width - cx;
            float p2y = -cy;
            float p3x = width - cx;
            float p3y = height - cy;
            float p4x = -cx;
            float p4y = height - cy;

            final float cos = (float) Math.cos(rotation);
            final float sin = (float) Math.sin(rotation);

            x1 = x + (cos * p1x - sin * p1y) + cx; // TOP LEFT
            y1 = y + (sin * p1x + cos * p1y) + cy;
            x2 = x + (cos * p2x - sin * p2y) + cx; // TOP RIGHT
            y2 = y + (sin * p2x + cos * p2y) + cy;
            x3 = x + (cos * p3x - sin * p3y) + cx; // BOTTOM RIGHT
            y3 = y + (sin * p3x + cos * p3y) + cy;
            x4 = x + (cos * p4x - sin * p4y) + cx; // BOTTOM LEFT
            y4 = y + (sin * p4x + cos * p4y) + cy;
        } else {
            x1 = x;
            y1 = y;

            x2 = x+width;
            y2 = y;

            x3 = x+width;
            y3 = y+height;

            x4 = x;
            y4 = y+height;
        }

        // top left, top right, bottom left
        vertex(x1, y1, r, g, b, a, u, v);
        vertex(x2, y2, r, g, b, a, u2, v);
        vertex(x4, y4, r, g, b, a, u, v2);

        // top right, bottom right, bottom left
        vertex(x2, y2, r, g, b, a, u2, v);
        vertex(x3, y3, r, g, b, a, u2, v2);
        vertex(x4, y4, r, g, b, a, u, v2);
    }

    void vertex(float x, float y, float r, float g, float b, float a, float u, float v) {
        shaderBuffer.put(x).put(y).put(r).put(g).put(b).put(a).put(u).put(v);
        idx++;
    }


    public void flush() {
        if (idx > 0) {
            shaderBuffer.flip();
            render();
            idx = 0;
            shaderBuffer.clear();
        }
    }

    private void checkFlush(Texture texture) {
        if (this.texture != texture || idx >= max) {
            flush();
            this.texture = texture;
        }
    }

    private int rCalls = 0;
    private void render() {
        if (texture != null)
            texture.bind();

        int offset = 0;
        int stride = totalComp * 4;

        for (int i = 0; i < 3; i++) {
            shaderBuffer.position(offset);
            glEnableVertexAttribArray(shader.positions[i]);
            glVertexAttribPointer(shader.positions[i], shader.SIZE[i], false, stride, shaderBuffer);
            offset += shader.SIZE[i];
        }

        glDrawArrays(GL_TRIANGLES, 0, idx);

        for (int i = 0; i < 3; i++) {
            glDisableVertexAttribArray(shader.positions[i]);
        }
        rCalls++;
    }


    //Taken from: https://github.com/mattdesl/lwjgl-basics/blob/master/src/mdesl/util/MathUtil.java
    private static Matrix4f toOrtho2D(Matrix4f m, float x, float y, float width, float height) {
        return toOrtho(m, x, x + width, y + height, y, 1, -1);
    }

    private static Matrix4f toOrtho(Matrix4f m, float left, float right, float bottom, float top,
                                   float near, float far) {
        if (m==null)
            m = new Matrix4f();
        float x_orth = 2 / (right - left);
        float y_orth = 2 / (top - bottom);
        float z_orth = -2 / (far - near);

        float tx = -(right + left) / (right - left);
        float ty = -(top + bottom) / (top - bottom);
        float tz = -(far + near) / (far - near);

        m.m00 = x_orth;
        m.m10 = 0;
        m.m20 = 0;
        m.m30 = 0;
        m.m01 = 0;
        m.m11 = y_orth;
        m.m21 = 0;
        m.m31 = 0;
        m.m02 = 0;
        m.m12 = 0;
        m.m22 = z_orth;
        m.m32 = 0;
        m.m03 = tx;
        m.m13 = ty;
        m.m23 = tz;
        m.m33 = 1;
        return m;
    }

}
