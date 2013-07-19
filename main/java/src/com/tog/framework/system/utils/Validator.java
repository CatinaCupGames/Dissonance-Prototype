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

    public static void validateInRange(int parameter, int low, int high, String parameter_name) {
        if (parameter < low || parameter > high)
            throw new InvalidParameterException("The parameter \"" + parameter_name + "\" must be between " + low + "-" + high);
    }

    public static void validateNotOver(int parameter, int high, String parameter_name) {
        if (parameter > high)
            throw new InvalidParameterException("The parameter \"" + parameter_name + "\" can't be above " + high);
    }

    public static void validateNotBelow(int parameter, int below, String parameter_name) {
        if (parameter < below)
            throw new InvalidParameterException("The parameter \"" + parameter_name + "\" can't be below " + below);
    }

    public static void validateRange(float parameter, float low, float high, String parameter_name) {
        if (parameter < low || parameter > high)
            throw new InvalidParameterException("The parameter \"" + parameter_name + "\" must be between " + low + "-" + high);
    }

    public static void validateNotOver(float parameter, float high, String parameter_name) {
        if (parameter > high)
            throw new InvalidParameterException("The parameter \"" + parameter_name + "\" can't be above " + high);
    }

    public static void validateNotBelow(float parameter, float below, String parameter_name) {
        if (parameter < below)
            throw new InvalidParameterException("The parameter \"" + parameter_name + "\" can't be below " + below);
    }

    public static void validateRange(double parameter, double low, double high, String parameter_name) {
        if (parameter < low || parameter > high)
            throw new InvalidParameterException("The parameter \"" + parameter_name + "\" must be between " + low + "-" + high);
    }

    public static void validateNotOver(double parameter, double high, String parameter_name) {
        if (parameter > high)
            throw new InvalidParameterException("The parameter \"" + parameter_name + "\" can't be above " + high);
    }

    public static void validateNotBelow(double parameter, double below, String parameter_name) {
        if (parameter < below)
            throw new InvalidParameterException("The parameter \"" + parameter_name + "\" can't be below " + below);
    }


}
