package com.tog.framework.system.utils;

import java.security.InvalidParameterException;

/**
 * The Validator class contains various utility methods to validate low-level methods.
 */
public class Validator {

    /**
     * Checks if the specified object is the type of the specified class.
     *
     * @param obj  The object to check.
     * @param type The class to check.
     * @throws InvalidParameterException If the object isn't the type of the specified class.
     */
    public static void validateClass(Object obj, Class<?> type) {
        if (!obj.getClass().isAssignableFrom(type))
            throw new InvalidParameterException("The object provided is not the type of " + type.getClass().getCanonicalName());
    }

    /**
     * Checks if the specified object implements the specified interface.
     *
     * @param obj  The object to check.
     * @param type The class of the interface to check.
     * @throws InvalidParameterException If the object doesn't implement the interface.
     */
    public static void validateInterface(Object obj, Class<?> type) {
        if (!type.isAssignableFrom(obj.getClass()))
            throw new InvalidParameterException("The object provided does not implement the interface " + type.getClass().getCanonicalName());
    }

    /**
     * Checks if the specified object is null.
     *
     * @param obj      The object to check.
     * @param obj_name The name of the object. This will be used in the exception message if the object is null.
     * @throws InvalidParameterException If the object is null.
     */
    public static void validateNotNull(Object obj, String obj_name) {
        if (obj == null)
            throw new InvalidParameterException("The object \"" + obj_name + "\" can't be null!");
    }

    /**
     * Checks if the specified parameter is in the specified range.
     *
     * @param parameter      The parameter to check.
     * @param low            The lowest integer of the range. Inclusive.
     * @param high           The greatest integer of the range. Inclusive.
     * @param parameter_name The name of the parameter. This will be used in the
     *                       exception message if the parameter is out of range.
     * @throws InvalidParameterException If the parameter is out of the specified range.
     */
    public static void validateInRange(int parameter, int low, int high, String parameter_name) {
        if (parameter < low || parameter > high)
            throw new InvalidParameterException("The parameter \"" + parameter_name + "\" must be between " + low + "-" + high);
    }

    /**
     * Checks if the specified parameter is greater than the specified number.
     *
     * @param parameter      The parameter to check.
     * @param high           The number to check.
     * @param parameter_name The name of the parameter. This will be used in the
     *                       exception message if the parameter is greater than the number.
     * @throws InvalidParameterException If the parameter is greater than the number specified.
     */
    public static void validateNotOver(int parameter, int high, String parameter_name) {
        if (parameter > high)
            throw new InvalidParameterException("The parameter \"" + parameter_name + "\" can't be above " + high);
    }

    /**
     * Checks if the specified parameter is lower than the specified number.
     *
     * @param parameter      The parameter to check.
     * @param low            The number to check.
     * @param parameter_name The name of the parameter. This will be used in the
     *                       exception message if the parameter is lower than the parameter.
     * @throws InvalidParameterException If the parameter is lower than the number specified.
     */
    public static void validateNotBelow(int parameter, int low, String parameter_name) {
        if (parameter < low)
            throw new InvalidParameterException("The parameter \"" + parameter_name + "\" can't be below " + low);
    }

    /**
     * Checks if the specified parameter is in the specified range.
     *
     * @param parameter      The parameter to check.
     * @param low            The lowest integer of the range. Inclusive.
     * @param high           The greatest integer of the range. Inclusive.
     * @param parameter_name The name of the parameter. This will be used in the
     *                       exception message if the parameter is out of range.
     * @throws InvalidParameterException If the parameter is out of the specified range.
     */
    public static void validateInRange(float parameter, float low, float high, String parameter_name) {
        if (parameter < low || parameter > high)
            throw new InvalidParameterException("The parameter \"" + parameter_name + "\" must be between " + low + "-" + high);
    }

    /**
     * Checks if the specified parameter is greater than the specified number.
     *
     * @param parameter      The parameter to check.
     * @param high           The number to check.
     * @param parameter_name The name of the parameter. This will be used in the
     *                       exception message if the parameter is greater than the number.
     * @throws InvalidParameterException If the parameter is greater than the number specified.
     */
    public static void validateNotOver(float parameter, float high, String parameter_name) {
        if (parameter > high)
            throw new InvalidParameterException("The parameter \"" + parameter_name + "\" can't be above " + high);
    }

    /**
     * Checks if the specified parameter is lower than the specified number.
     *
     * @param parameter      The parameter to check.
     * @param low            The number to check.
     * @param parameter_name The name of the parameter. This will be used in the
     *                       exception message if the parameter is lower than the parameter.
     * @throws InvalidParameterException If the parameter is lower than the number specified.
     */
    public static void validateNotBelow(float parameter, float low, String parameter_name) {
        if (parameter < low)
            throw new InvalidParameterException("The parameter \"" + parameter_name + "\" can't be below " + low);
    }

    /**
     * Checks if the specified parameter is in the specified range.
     *
     * @param parameter      The parameter to check.
     * @param low            The lowest integer of the range. Inclusive.
     * @param high           The greatest integer of the range. Inclusive.
     * @param parameter_name The name of the parameter. This will be used in the
     *                       exception message if the parameter is out of range.
     * @throws InvalidParameterException If the parameter is out of the specified range.
     */
    public static void validateInRange(double parameter, double low, double high, String parameter_name) {
        if (parameter < low || parameter > high)
            throw new InvalidParameterException("The parameter \"" + parameter_name + "\" must be between " + low + "-" + high);
    }

    /**
     * Checks if the specified parameter is greater than the specified number.
     *
     * @param parameter      The parameter to check.
     * @param high           The number to check.
     * @param parameter_name The name of the parameter. This will be used in the
     *                       exception message if the parameter is greater than the number.
     * @throws InvalidParameterException If the parameter is greater than the number specified.
     */
    public static void validateNotOver(double parameter, double high, String parameter_name) {
        if (parameter > high)
            throw new InvalidParameterException("The parameter \"" + parameter_name + "\" can't be above " + high);
    }

    /**
     * Checks if the specified parameter is lower than the specified number.
     *
     * @param parameter      The parameter to check.
     * @param low            The number to check.
     * @param parameter_name The name of the parameter. This will be used in the
     *                       exception message if the parameter is lower than the parameter.
     * @throws InvalidParameterException If the parameter is lower than the number specified.
     */
    public static void validateNotBelow(double parameter, double low, String parameter_name) {
        if (parameter < low)
            throw new InvalidParameterException("The parameter \"" + parameter_name + "\" can't be below " + low);
    }


}
