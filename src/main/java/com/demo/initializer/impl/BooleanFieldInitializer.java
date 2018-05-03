package com.demo.initializer.impl;

import java.util.function.Function;

import com.demo.initializer.FieldInitializer;

/**
 * Boolean Field Initializer.
 */
public class BooleanFieldInitializer implements FieldInitializer {

	@Override
	public Function<String, ?> getConverter() {
		return Boolean::valueOf;
	}
}
