package com.dissonance.framework.config.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines a child integer.
 * Format: (name): (value)i.
 * Array Format: (name): [(value)i, ...].
 *
 * @author Oliver Yasuna
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AInteger
{
    public boolean array() default false;
}
