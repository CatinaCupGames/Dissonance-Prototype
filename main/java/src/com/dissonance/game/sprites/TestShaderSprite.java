package com.dissonance.game.sprites;

import com.dissonance.framework.game.scene.dialog.Dialog;
import com.dissonance.framework.game.sprites.impl.NPCSprite;
import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.render.Shaders;

import java.io.IOException;

import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class TestShaderSprite extends NPCSprite {

    private boolean activateShader;
    private int shaderRef;
    private int shaderProgramRef;

    public TestShaderSprite(Dialog... dialogs) {
        super(dialogs);
    }

    public TestShaderSprite(String... dialogIds) {
        super(dialogIds);

        RenderService.INSTANCE.runOnServiceThread(new Runnable() {
            @Override
            public void run() {
                try {
                    shaderRef = Shaders.buildShader(GL_VERTEX_SHADER, "test_shader");
                    shaderProgramRef = Shaders.getShaderProgram(shaderRef);
                } catch (IOException e) {
                }

            }
        });
    }

    public TestShaderSprite() {
    }

    @Override
    public String getReadableName() {
        return "ArremJr";
    }

    @Override
    public String getSpriteName() {
        return "player";
    }

    @Override
    public void onSpeakingFinished() {
        activateShader = true;
    }

    @Override
    public void render() {

        glUseProgram(shaderProgramRef);
        super.render();
        glUseProgram(0);

    }

}
