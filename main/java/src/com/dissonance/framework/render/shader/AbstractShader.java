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
    private boolean warn = false;
    public void postRender() {
        check1 = false;
        onPostRender();
        if (!check1 && !warn) {
            System.out.println("[WARNING] This shader does not want to unbind!");
            warn = true;
        } else if (check1 && warn) {
            warn = false;
        }
    }

    protected void onPostRender() {
        check1 = true;
        ARBShaderObjects.glUseProgramObjectARB(0);
        String log = getLogInfo(program);
        if (!log.isEmpty())
            System.out.println(log);
    }

    private boolean check2;
    public void preRender() {
        check2 = false;
        onPreRender();
        if (!check2)
            throw new RuntimeException("super.onPreRender was not invoked! Try putting super.onPreRender at the top of your method!");
    }


    protected void onPreRender() {
        check2 = true;
        ARBShaderObjects.glUseProgramObjectARB(program);
    }

    public String getLogInfo(int obj) {
        return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
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
