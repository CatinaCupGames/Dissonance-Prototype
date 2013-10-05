package com.dissonance.framework.config.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines a child byte.
 * Format: (name): 0x(value).
 * Array Format: (name): [0x(value), ...].
 *
 * @author Oliver Yasuna
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AByte
{
    public boolean array() default false;
}
