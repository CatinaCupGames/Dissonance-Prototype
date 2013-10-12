package com.dissonance.framework.render.shader;

import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.shader.impl.BlurShader;
import com.dissonance.framework.render.shader.impl.MainShader;
import com.dissonance.game.Main;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ShaderFactory {
    static {
        activeShaders = new ArrayList<>();
    }
    private static final ArrayList<AbstractShader> activeShaders;

    public static void registerShader(AbstractShader shader) {
        validateState();
        String verFile = shader.getVertexFile();
        String fragFile = shader.getFragmentFile();

        try {
            int vi = buildShader(ARBVertexShader.GL_VERTEX_SHADER_ARB, verFile);
            int fi = buildShader(ARBFragmentShader.GL_FRAGMENT_SHADER_ARB, fragFile);

            int program = ARBShaderObjects.glCreateProgramObjectARB();
            if (program == 0)
                throw new RuntimeException("Error binding shader to program!");

            ARBShaderObjects.glAttachObjectARB(program, vi);
            ARBShaderObjects.glAttachObjectARB(program, fi);

            ARBShaderObjects.glLinkProgramARB(program);
            if(ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
                throw new RuntimeException("Error linking shader!");
            }

            ARBShaderObjects.glValidateProgramARB(program);
            if(ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
                throw new RuntimeException("Error validating shader!");
            }

            shader.setProgram(program);
            shader.setShaders(new int[] { vi, fi });
            shader.setActive(true);
        } catch (Throwable t) {
            t.printStackTrace();
            return;
        }
    }

    static void cacheActiveShader(AbstractShader s) {
        activeShaders.add(s);
    }

    static void decacheActiveShader(AbstractShader s) {
        activeShaders.remove(s);
    }

    public static void executePreRender() {
        validateState();
        AbstractShader[] temp = activeShaders.toArray(new AbstractShader[activeShaders.size()]);
        for (AbstractShader a : temp) {
            if (!a.isActive())
                activeShaders.remove(a);
            else {
                a.preRender();
            }
        }
    }

    public static void executePostRender() {
        validateState();
        AbstractShader[] temp = activeShaders.toArray(new AbstractShader[activeShaders.size()]);
        for (AbstractShader a : temp) {
            if (!a.isActive())
                activeShaders.remove(a);
            else {
                a.postRender();
            }
        }
    }

    private static void validateState() {
        if (!RenderService.isInRenderThread())
            throw new IllegalStateException("Current thread is not an OpenGL thread!");
    }



    public static int buildShader(int shaderType, String shaderFile) throws Exception {
        int shader = 0;

        try {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

            if(shader == 0) {
                return 0;
            }

            ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(shaderFile));
            ARBShaderObjects.glCompileShaderARB(shader);

            if(ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) {
                throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
            }

            return shader;
        } catch (Throwable e) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            throw e;
        }
    }

    private static String readFileAsString(String filename) throws Exception {
        StringBuilder sb = new StringBuilder();

        InputStream in = ShaderFactory.class.getResourceAsStream("/shaders/" + filename);
        if (in == null)
            throw new IOException("File not found!");
        InputStreamReader isr = new InputStreamReader(in, "UTF-8");
        BufferedReader br = new BufferedReader(isr);

        String line;
        while((line = br.readLine()) != null) { sb.append(line).append("\n"); }

        return sb.toString();
    }

    private static String getLogInfo(int object) {
        return ARBShaderObjects.glGetInfoLogARB(object, ARBShaderObjects.glGetObjectParameteriARB(object, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }


    public static void buildAllShaders()
    {
        new MainShader().build();

        for(String s : Main.args)
        {
            if(s.equalsIgnoreCase("blur"))
            {
                new BlurShader().build();
            }
        }

        //TODO Put all shader building in here..
    }
}
