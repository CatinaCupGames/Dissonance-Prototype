package com.dissonance.framework.config.util;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Defines an object as a node.
 *
 * @author Oliver Yasuna
 * @version 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ANode
{
    public String name() default "Unknown|Type=ANode";
}
