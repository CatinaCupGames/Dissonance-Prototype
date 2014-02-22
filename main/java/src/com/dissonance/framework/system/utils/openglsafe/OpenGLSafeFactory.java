package com.dissonance.framework.system.utils.openglsafe;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class OpenGLSafeFactory {
    private static class SafeInvocationHandler<T> implements InvocationHandler {
        private T object;

        public SafeInvocationHandler(T object) {
            this.object = object;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method realMethod = object.getClass().getMethod(method.getName(), method.getParameterTypes());

            OpenGLSafe safe = realMethod.getAnnotation(OpenGLSafe.class);
            if (safe != null) {
                boolean status = safe.handler().newInstance().handleInvoke(method, object, args);
                if (!status)
                    return null;
            }

            return method.invoke(object, args);
        }
    }

    public static <T> T unwrapObject(Object obj) {
        if (Proxy.isProxyClass(obj.getClass())) {
            InvocationHandler handler = Proxy.getInvocationHandler(obj);
            if (handler instanceof SafeInvocationHandler)
                return ((SafeInvocationHandler<T>)handler).object;
        }
        return (T)obj;
    }

    public static <T> T createSafeObject(T object, Class<?>... interfaces_) {
        return (T) Proxy.newProxyInstance(OpenGLSafeFactory.class.getClassLoader(), interfaces_, new SafeInvocationHandler<T>(object));
    }
}
