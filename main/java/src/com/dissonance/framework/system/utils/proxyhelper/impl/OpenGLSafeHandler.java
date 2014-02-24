package com.dissonance.framework.system.utils.proxyhelper.impl;

import com.dissonance.framework.render.RenderService;
import com.dissonance.framework.system.utils.proxyhelper.Handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class OpenGLSafeHandler implements Handler {
    @Override
    public boolean handleInvoke(final Method method, final Object caller, final Object[] args) {
        if (RenderService.INSTANCE == null)
            throw new IllegalStateException("The RenderService is not running, cannot invoke OpenGL Safe Method!");
        if (!RenderService.isInRenderThread()) {
            RenderService.INSTANCE.runOnServiceThread(new Runnable() {

                @Override
                public void run() {
                    try {
                        method.invoke(caller, args);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }, true);
            return false;
        }
        return true;
    }
}
