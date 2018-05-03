package com.demo.initializer.impl;

import java.lang.reflect.Field;
import java.util.function.Function;

import org.apache.commons.lang3.StringUtils;

import com.demo.initializer.FieldInitializer;

/**
 * Char Field Initializer.
 */
public class CharFieldInitializer implements FieldInitializer {
	@Override
	public Function<String, ?> getConverter() {
		return String::valueOf;
	}

	@Override
	public void init(Field field, String actualType, Object targetObj, Object value) throws IllegalAccessException {
		String valueStr = (String) value;
		if (StringUtils.isNotEmpty(valueStr)) {
			field.set(targetObj, valueStr.charAt(0));
		}
	}
}
