package com.demo.initializer.impl;

import com.demo.initializer.FieldInitializer;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.function.Function;

/**
 * String Field Initializer.
 */
public class StringFieldInitializer implements FieldInitializer {
	@Override
	public Function<String, ?> getConverter() {
		return s -> {
			try {
				return URLDecoder.decode(s, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new IllegalArgumentException("Object cannot be decoded");
			}
		};
	}
}
