package com.demo.initializer;

import java.lang.reflect.Field;
import java.util.function.Function;

/**
 * Field initializer.
 */
public interface FieldInitializer {

    /**
     * Converts string to particular object.
     *
     * @param value
     * @return the object
     */
    default Object convert(String value) {
        return value == null ? null : getConverter().apply(value);
    }

    /**
     * Initializes field of targetObject with actualType and value.
     * Uses configured converter function.
     *
     * @param field
     * @param actualType
     * @param targetObj
     * @param value
     * @throws IllegalAccessException
     */
    default void init(Field field, String actualType, Object targetObj, Object value) throws IllegalAccessException {
        field.set(targetObj, convert((String) value));
    }

    /**
     * Function for converting string to particular object.
     *
     * @return the function
     */
    Function<String, ?> getConverter();
}
