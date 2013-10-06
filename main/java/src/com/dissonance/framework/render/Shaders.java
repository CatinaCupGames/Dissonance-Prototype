package com.dissonance.framework.render;

import java.io.*;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL20.*;

public final class Shaders {

    static {
        shaderMap = new HashMap<>();
    }

    private static HashMap<Integer, Integer> shaderMap;

    public static int buildShader(int shaderType, String shaderFile) throws IOException {
        String path = "shaders/" + shaderFile + ".shader";

        int shaderProgram = glCreateProgram();
        int shadeSource = glCreateShader(shaderType);

        StringBuilder source = new StringBuilder();
        DataInputStream reader = new DataInputStream(path.getClass().getClassLoader().getResourceAsStream(path));

        //String source = reader.readUTF();

        glShaderSource(shadeSource, source);

        glAttachShader(shaderProgram, shadeSource);
        glLinkProgram(shaderProgram);
        glValidateProgram(shaderProgram);

        if (glGetShaderi(shadeSource, GL_COMPILE_STATUS) == GL_TRUE) {
            if (shaderMap.containsKey(shadeSource)) throw new IllegalArgumentException("shader tag already exists");
            shaderMap.put(shadeSource, shaderProgram);

            System.out.println("Successfully added shader: " + shaderFile);

            return shadeSource;
        }

        System.err.println("Shader " + path + " could not be compiled.");

        return 0;
    }

    public static int buildShaderFromSource(int shaderType, String source) {

        int shaderProgram = glCreateProgram();
        int shadeSource = glCreateShader(shaderType);

        glShaderSource(shadeSource, source);
        glCompileShader(shadeSource);

        glAttachShader(shaderProgram, shadeSource);
        glLinkProgram(shaderProgram);
        glValidateProgram(shaderProgram);

        if (glGetShaderi(shadeSource, GL_COMPILE_STATUS) == GL_TRUE) {
            if (shaderMap.containsKey(shadeSource)) throw new IllegalArgumentException("shader tag already exists");
            shaderMap.put(shadeSource, shaderProgram);
            return shadeSource;
        }

        System.err.println("Shader " + shadeSource + " could not be compiled.");

        return 0;
    }

    public static int getShaderProgram(int shaderRef) {
        if (!shaderMap.containsKey(shaderRef)) throw new NullPointerException("Shader not found");
        return shaderMap.get(shaderRef).intValue();
    }


}
