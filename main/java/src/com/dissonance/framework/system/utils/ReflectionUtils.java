package com.dissonance.framework.system.utils;

import java.lang.reflect.Field;

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
}
