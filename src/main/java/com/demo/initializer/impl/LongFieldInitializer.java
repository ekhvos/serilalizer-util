package com.demo.initializer.impl;

import java.util.function.Function;

import com.demo.initializer.FieldInitializer;

/**
 * Long Field Initializer.
 */
public class LongFieldInitializer implements FieldInitializer {

    @Override
    public Function<String, ?> getConverter() {
        return Long::valueOf;
    }
}
