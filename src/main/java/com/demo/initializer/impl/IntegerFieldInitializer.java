package com.demo.initializer.impl;

import java.util.function.Function;

import com.demo.initializer.FieldInitializer;

/**
 * Integer Field Initializer.
 */
public class IntegerFieldInitializer implements FieldInitializer {
    @Override
    public Function<String, ?> getConverter() {
        return Integer::valueOf;
    }
}
