package com.dissonance.framework.system.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;

public class ReflectionUtils {

    public static <T> T getPrivateField(String fieldName, Object from, Class<T> varclass) {
        try {
            Field item = from.getClass().getDeclaredField(fieldName);
            item.setAccessible(true);
            Object toreturn = item.get(from);
            if (toreturn.getClass().isAssignableFrom(varclass)) {
                return (T)item.get(from);
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> void setPrivateField(String fieldName, Object in, T value) {
        try {
            Field item = in.getClass().getDeclaredField(fieldName);
            item.setAccessible(true);
            item.set(value, in);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Method getMethod(String methodName, Class<?> class_, Class<?>... parameters) {
        final Method m;
        try {
            m = class_.getDeclaredMethod(methodName, parameters);
        } catch (NoSuchMethodException e) {
            throw new InvalidParameterException("The method \"" + methodName + "\" with the specified parameters was not found!");
        }
        return m;
    }

    public static Method getMethod(String methodName, Object obj, Object... parameters) {
        Class<?> class_ = obj.getClass();

        Class<?>[] parametersClass = new Class<?>[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            parametersClass[i] = parameters[i].getClass();
        }

        return getMethod(methodName, class_, parametersClass);
    }
}
