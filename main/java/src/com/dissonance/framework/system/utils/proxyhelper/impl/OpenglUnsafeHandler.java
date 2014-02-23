package com.dissonance.framework.system.utils.proxyhelper.impl;

import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.proxyhelper.Handler;

import java.lang.reflect.Method;

public class OpenglUnsafeHandler implements Handler {
    @Override
    public boolean handleInvoke(Method method, Object caller, Object[] args) {
        if (RenderService.isInRenderThread())
            throw new IllegalStateException("This invocation cannot happen inside the render thread!");
        return true;
    }
}
