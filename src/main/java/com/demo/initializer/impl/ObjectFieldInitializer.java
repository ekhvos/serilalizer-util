package com.demo.initializer.impl;

import java.lang.reflect.Field;
import java.util.function.Function;

import com.demo.initializer.FieldInitializer;

/**
 * Object Field Initializer.
 */
public class ObjectFieldInitializer implements FieldInitializer {
    @Override
    public Function<String, ?> getConverter() {
        throw new UnsupportedOperationException("Method does not implemented");
    }

    public Object convert(Object value) {
        return value == null ? null : value;
    }

    @Override
    public void init(Field field, String actualType, Object targetObj, Object value) throws IllegalAccessException {
        field.set(targetObj, value);
    }
}
