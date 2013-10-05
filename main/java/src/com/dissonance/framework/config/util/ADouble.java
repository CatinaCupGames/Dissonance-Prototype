package com.dissonance.framework.config.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines a child double.
 * Format: (name): (value)d.
 * Array Format: (name): [(value)d, ...].
 *
 * @author Oliver Yasuna
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ADouble
{
    public boolean array() default false;
}
