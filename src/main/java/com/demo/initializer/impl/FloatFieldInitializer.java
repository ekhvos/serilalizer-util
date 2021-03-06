package com.demo.initializer.impl;

import java.util.function.Function;

import com.demo.initializer.FieldInitializer;

/**
 * Float Field Initializer.
 */
public class FloatFieldInitializer implements FieldInitializer {

    @Override
    public Function<String, ?> getConverter() {
        return Float::valueOf;
    }
}
