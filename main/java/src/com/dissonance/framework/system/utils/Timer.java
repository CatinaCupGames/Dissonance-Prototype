package com.dissonance.framework.system.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;

/**
 * A utility class for executing Timed events
 *
 * @author Eddie Penta
 */
public class Timer {

    public static void delayedInvokeMethod(final long delay, String methodName, final Object object, final Object... parameters) {
        final Method m = ReflectionUtils.getMethod(methodName, object, parameters);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ignored) { }

                try {
                    m.setAccessible(true);
                    m.invoke(object, parameters);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void delayedInvokeMethod(final long delay, final int count, String methodName, final Object object, final Object... parameters) {
        if (count <= 0)
            throw new InvalidParameterException("The parameter \"count\" must be greater than 0");
        final Method m = ReflectionUtils.getMethod(methodName, object, parameters);
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < count; i++) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ignored) { }

                    try {
                        m.setAccessible(true);
                        m.invoke(object, parameters);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
