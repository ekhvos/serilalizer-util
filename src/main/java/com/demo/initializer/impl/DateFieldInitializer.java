package com.demo.initializer.impl;

import java.util.Date;
import java.util.function.Function;

import com.demo.initializer.FieldInitializer;

/**
 * Date Field Initializer.
 */
public class DateFieldInitializer implements FieldInitializer {
	@Override
	public Function<String, ?> getConverter() {
		return (String millis) -> new Date(Long.valueOf(millis));
	}
}
