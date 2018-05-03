package com.demo.initializer.impl;

import java.util.function.Function;

import com.demo.initializer.FieldInitializer;

/**
 * Byte Field Initializer.
 */
public class ByteFieldInitializer implements FieldInitializer {
    @Override
    public Function<String, ?> getConverter() {
        return Byte::valueOf;
    }
}
