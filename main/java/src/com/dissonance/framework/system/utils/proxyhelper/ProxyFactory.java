package com.dissonance.framework.system.utils.proxyhelper;

import com.dissonance.framework.system.annotations.OpenGLSafe;
import com.dissonance.framework.system.annotations.OpenglUnsafe;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ProxyFactory {
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
            } else {
                safe = method.getAnnotation(OpenGLSafe.class);
                if (safe != null) {
                    boolean status = safe.handler().newInstance().handleInvoke(method, object, args);
                    if (!status)
                        return null;
                }
            }

            OpenglUnsafe unsafe = realMethod.getAnnotation(OpenglUnsafe.class);
            if (unsafe != null) {
                boolean status = unsafe.handler().newInstance().handleInvoke(method, object, args);
                if (!status)
                    return null;
            } else {
                unsafe = method.getAnnotation(OpenglUnsafe.class);
                if (unsafe != null) {
                    boolean status = unsafe.handler().newInstance().handleInvoke(method, object, args);
                    if (!status)
                        return null;
                }
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
        return (T) Proxy.newProxyInstance(ProxyFactory.class.getClassLoader(), interfaces_, new SafeInvocationHandler<T>(object));
    }
}
