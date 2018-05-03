package com.demo.initializer.impl;

import java.util.function.Function;

import com.demo.initializer.FieldInitializer;

/**
 * Double Field Initializer.
 */
public class DoubleFieldInitializer implements FieldInitializer {
    @Override
    public Function<String, ?> getConverter() {
        return Double::valueOf;
    }
}
