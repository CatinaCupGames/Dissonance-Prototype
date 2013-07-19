package com.tog.framework.system.utils;

import java.security.InvalidParameterException;

public class Validator {
    public static void validateClass(Object obj, Class<?> type) {
        if (!obj.getClass().isAssignableFrom(type))
            throw new InvalidParameterException("The object provided is not the type of " + type.getClass().getCanonicalName());
    }

    public static void validateNotNull(Object obj, String obj_name) {
        if (obj == null)
            throw new InvalidParameterException("The object \"" + obj_name + "\" can't be null!");
    }
}
