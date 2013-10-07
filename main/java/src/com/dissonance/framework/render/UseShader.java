package com.dissonance.framework.render;

import com.dissonance.framework.game.GameSettings;
import org.lwjgl.opengl.*;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver Yasuna
 * Date: 10/6/13
 * Time: 4:02 PM
 */

public class UseShader
{
    private UseShader()
    {
    }

    private static boolean useShader;
    private static int program = 0;

    private static int brightnessVar;
    private static int contrastVar;
    private static int saturationVar;
    private static int redVar;
    private static int greenVar;
    private static int blueVar;

    public static void init()
    {
        int vertShader = 0;
        int fragShader = 0;

        try
        {
            vertShader = createShader("/shaders/screenvert.glsl", ARBVertexShader.GL_VERTEX_SHADER_ARB);
            fragShader = createShader("/shaders/screenfrag.glsl", ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            if(vertShader == 0 || fragShader == 0)
            {
                return;
            }
        }

        program = ARBShaderObjects.glCreateProgramObjectARB();

        if(program == 0)
        {
            return;
        }

        ARBShaderObjects.glAttachObjectARB(program, vertShader);
        ARBShaderObjects.glAttachObjectARB(program, fragShader);

        ARBShaderObjects.glLinkProgramARB(program);
        if(ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE)
        {
            System.err.println(getLogInfo(program));
            return;
        }

        ARBShaderObjects.glValidateProgramARB(program);
        if(ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE)
        {
            System.err.println(getLogInfo(program));
            return;
        }

        useShader = true;
    }

    public static void preDraw()
    {
        if(useShader)
        {
            ARBShaderObjects.glUseProgramObjectARB(program);

            brightnessVar = GL20.glGetUniformLocation(program, "brightness");
            contrastVar = GL20.glGetUniformLocation(program, "contrast");
            saturationVar = GL20.glGetUniformLocation(program, "saturation");
            redVar = GL20.glGetUniformLocation(program, "red");
            greenVar = GL20.glGetUniformLocation(program, "green");
            blueVar = GL20.glGetUniformLocation(program, "blue");

            GL20.glUniform1f(brightnessVar, GameSettings.Graphics.color.brightness);
            GL20.glUniform1f(contrastVar, GameSettings.Graphics.color.contrast);
            GL20.glUniform1f(saturationVar, GameSettings.Graphics.color.saturation);
            GL20.glUniform1f(redVar, GameSettings.Graphics.color.red);
            GL20.glUniform1f(greenVar, GameSettings.Graphics.color.green);
            GL20.glUniform1f(blueVar, GameSettings.Graphics.color.blue);
        }
    }

    public static void postDraw()
    {
        if(useShader)
        {
            ARBShaderObjects.glUseProgramObjectARB(0);
        }
    }

    private static int createShader(String filename, int shaderType) throws Exception
    {
        int shader = 0;

        try
        {
            shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

            if(shader == 0)
            {
                return 0;
            }

            ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
            ARBShaderObjects.glCompileShaderARB(shader);

            if(ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
            {
                throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
            }

            return shader;
        } catch (Exception e) {
            ARBShaderObjects.glDeleteObjectARB(shader);
            throw e;
        }
    }

    private static String readFileAsString(String filename) throws Exception
    {
        StringBuilder sb = new StringBuilder();

        InputStreamReader isr = new InputStreamReader(UseShader.class.getResourceAsStream(filename), "UTF-8");
        BufferedReader br = new BufferedReader(isr);

        String line;
        while((line = br.readLine()) != null)
        {
            sb.append(line).append("\n");
        }

        return sb.toString();
    }

    private static String getLogInfo(int object)
    {
        return ARBShaderObjects.glGetInfoLogARB(object, ARBShaderObjects.glGetObjectParameteriARB(object, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
    }
}
