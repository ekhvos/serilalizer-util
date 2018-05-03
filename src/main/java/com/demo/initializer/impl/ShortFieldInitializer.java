package com.demo.initializer.impl;

import java.util.function.Function;

import com.demo.initializer.FieldInitializer;

/**
 * String Field Initializer.
 */
public class ShortFieldInitializer implements FieldInitializer {
    @Override
    public Function<String, ?> getConverter() {
        return Short::valueOf;
    }
}
