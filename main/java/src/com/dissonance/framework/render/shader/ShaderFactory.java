package com.dissonance.framework.render.shader;

import com.dissonance.framework.game.GameService;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.shader.impl.BlurShader;
import com.dissonance.framework.render.shader.impl.MainShader;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.lwjgl.opengl.GL20.*;

public class ShaderFactory {
    static {
        activeShaders = new ArrayList<>();
        shaderList = new HashMap<>();
    }

    private static final ArrayList<AbstractShader> activeShaders;
    private static final HashMap<String, AbstractShader> shaderList;

    public static void registerShader(AbstractShader shader) {
        validateState();
        String verFile = shader.getVertexFile();
        String fragFile = shader.getFragmentFile();

        try {
            int vi = buildShader(GL_VERTEX_SHADER, verFile);
            int fi = buildShader(GL_FRAGMENT_SHADER, fragFile);

            int program = glCreateProgram();
            if (program == 0)
                throw new RuntimeException("Error binding shader to program!");

            glAttachShader(program, vi);
            glAttachShader(program, fi);

            glLinkProgram(program);
            if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
                throw new RuntimeException("Error linking shader! - " + shader.getLogInfo(program));
            }

            shader.setProgram(program);
            shader.setShaders(new int[]{vi, fi});
            shaderList.put(shader.getName(), shader);
        } catch (Throwable t) {
            t.printStackTrace();
            return;
        }
    }

    static void cacheActiveShader(AbstractShader s) {
        if (!shaderList.containsKey(s.getName()))
            throw new InvalidParameterException("This shader is not registered! Try executing ShaderFactory.registerShader first.");
        activeShaders.add(s);
    }

    static void decacheActiveShader(AbstractShader s) {
        if (!shaderList.containsKey(s.getName()))
            throw new InvalidParameterException("This shader is not registered! Try executing ShaderFactory.registerShader first.");
        if (s.isBound())
            s.postRender();
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

    /**
     * Get a shader with the given name. This method is case sensitive.
     *
     * @param name The name of the shader (case sensitive)
     * @return An {@link AbstractShader} who's {@link com.dissonance.framework.render.shader.AbstractShader#getName()} method is equal to the parameter <b>name</b><br></br>
     * Null if no shader was found with the given name
     */
    public static AbstractShader getShaderByName(String name) {
        return shaderList.get(name);
    }

    /**
     * Search for a shader using a keyword. If more than one shader was found using the keyword given then this
     * method will return null
     *
     * @param keyword A search term
     * @return An {@link AbstractShader} with a similar name. Null if no shader was found or if more than one shader
     * was found.
     */
    public static AbstractShader findShaderByName(String keyword) {
        AbstractShader shader = null;

        for (String key : shaderList.keySet()) {
            if (key.contains(keyword)) {
                if (shader == null) {
                    shader = shaderList.get(key);
                } else {
                    return null;
                }
            }
        }

        return shader;
    }

    public static void executePostRender() {
        validateState();
        AbstractShader[] temp = activeShaders.toArray(new AbstractShader[activeShaders.size()]);
        for (AbstractShader a : temp) {
            a.postRender();
        }
    }

    private static void validateState() {
        if (!RenderService.isInRenderThread())
            throw new IllegalStateException("Current thread is not an OpenGL thread!");
    }


    public static int buildShader(int shaderType, String shaderFile) throws Exception {
        int shader = 0;

        try {
            shader = glCreateShader(shaderType);

            if (shader == 0) {
                return 0;
            }

            glShaderSource(shader, readFileAsString(shaderFile));
            glCompileShader(shader);

            if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) {
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
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }

        return sb.toString();
    }

    private static String getLogInfo(int object) {
        return ARBShaderObjects.glGetInfoLogARB(object, ARBShaderObjects.glGetObjectParameteriARB(object, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }


    public static void buildAllShaders() {


        new MainShader().build();

        for (String s : GameService.args) {
            if (s.startsWith("blur")) {
                s = s.split("=")[1];

                if (s.equalsIgnoreCase("shader") || s.equalsIgnoreCase("both")) {
                    new BlurShader().build();
                }
            }
        }

        //TODO Put all shader building in here..
    }
}
