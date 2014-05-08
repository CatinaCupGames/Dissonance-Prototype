package com.dissonance.framework.render.shader.impl;

import com.dissonance.framework.render.shader.AbstractShader;

public class EdgeGlowShader extends AbstractShader {
    @Override
    public String getVertexFile() {
        return "eglow.vert";
    }

    @Override
    public String getFragmentFile() {
        return "eglow.frag";
    }

    @Override
    public String getName() {
        return "Edge Glow";
    }

    @Override
    public void build() {
        super.build();
    }
}
