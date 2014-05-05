package com.dissonance.framework.system.annotations;

import com.dissonance.framework.system.utils.proxyhelper.Handler;
import com.dissonance.framework.system.utils.proxyhelper.impl.OpenGLSafeHandler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OpenGLSafe {
    Class<? extends Handler> handler() default OpenGLSafeHandler.class;
}
