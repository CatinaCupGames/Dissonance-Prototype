package com.dissonance.framework.system.utils.openglsafe;

import java.lang.reflect.Method;

public interface Handler {
    boolean handleInvoke(Method method, Object caller, Object[] args);
}
