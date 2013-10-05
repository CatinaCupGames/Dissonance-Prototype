package com.dissonance.framework.config.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines a child long.
 * Format: (name): (value)l.
 * Array Format: (name): [(value)l, ...].
 *
 * @author Oliver Yasuna
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ALong
{
    public boolean array() default false;
}
