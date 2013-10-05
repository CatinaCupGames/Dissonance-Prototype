package com.dissonance.framework.config.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines a child char.
 * Format: (name): '(value)'.
 * Array Format: (name): ['(value)', ...].
 *
 * @author Oliver Yasuna
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AChar
{
    public boolean array() default false;
}
