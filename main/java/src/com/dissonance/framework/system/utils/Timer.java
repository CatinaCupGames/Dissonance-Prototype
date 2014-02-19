package com.dissonance.framework.system.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A utility class for executing Timed events
 *
 * @author Eddie Penta
 */
public class Timer {

    public static void delayedInvokeMethod(final long delay, String methodName, final Object object, final Object... parameters) {
        Validator.validateNotBelow(delay, 1, "delay");
        final Method m = ReflectionUtils.getMethod(methodName, object, parameters);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException ignored) {
                }

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
        Validator.validateNotBelow(count, 1, "count");
        Validator.validateNotBelow(delay, 1, "delay");
        final Method m = ReflectionUtils.getMethod(methodName, object, parameters);
        new Thread(new Runnable() {

            @Override
            public void run() {
                for (int i = 0; i < count; i++) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException ignored) {
                    }

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

    public static void delayedInvokeRunnable(long delay, Runnable runnable) {
        delayedInvokeRunnable(delay, 1, runnable);
    }

    public static void delayedInvokeRunnable(final long delay, final int count, final Runnable runnable) {
        Validator.validateNotBelow(count, 1, "count");
        Validator.validateNotBelow(delay, 1, "delay");
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < count; i++) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runnable.run();
                }
            }
        }).start();
    }
}
