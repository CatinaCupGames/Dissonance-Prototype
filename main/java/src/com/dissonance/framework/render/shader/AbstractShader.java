package com.dissonance.framework.render.shader;

import org.lwjgl.opengl.ARBShaderObjects;

import java.security.InvalidParameterException;
import static org.lwjgl.opengl.GL20.*;

public abstract class AbstractShader {
    private int[] shaderID;
    private int program;
    private boolean active;

    public int getVertexID() {
        return shaderID[0];
    }

    public int getFragmentID() {
        return shaderID[1];
    }

    void setShaders(int[] id) {
        this.shaderID = id;
    }

    void setProgram(int program) {
        this.program = program;
    }

    public int getProgramID() {
        return program;
    }

    public abstract String getVertexFile();

    public abstract String getFragmentFile();

    public abstract String getName();

    private boolean check1;
    void postRender() {
        check1 = false;
        onPostRender();
        if (!check1)
            throw new RuntimeException("super.onPostRender was not invoked! Try putting super.onPostRender at the top of your method!");
    }

    protected void onPostRender() {
        check1 = true;
        ARBShaderObjects.glUseProgramObjectARB(0);
    }

    private boolean check2;
    void preRender() {
        check2 = false;
        onPreRender();
        if (!check2)
            throw new RuntimeException("super.onPreRender was not invoked! Try putting super.onPreRender at the top of your method!");
    }


    protected void onPreRender() {
        check2 = true;
        ARBShaderObjects.glUseProgramObjectARB(program);
    }

    protected void build() {
        ShaderFactory.registerShader(this);
    }

    public void setActive(boolean value) {
        if (value && !this.active) {
            ShaderFactory.cacheActiveShader(this);
        } else if (!value && this.active) {
            ShaderFactory.decacheActiveShader(this);
        }
        this.active = value;
    }

    public boolean isActive() {
        return active;
    }
}
