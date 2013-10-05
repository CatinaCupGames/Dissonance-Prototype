package com.dissonance.framework.config.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines a child float.
 * Format: (name): (value)f.
 * Array Format: (name): [(value)f, ...].
 *
 * @author Oliver Yasuna
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AFloat
{
    public boolean array() default false;
}
