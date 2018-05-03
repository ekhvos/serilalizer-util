package com.demo.initializer.impl;

import static com.demo.util.Tags.ARRAY_VALUE_SEPARATOR;
import static com.demo.util.Tags.COLLECTION_INITIALIZED;
import static com.demo.util.Tags.KEY_VALUE_SEPARATOR;
import static com.demo.util.Types.BASIC_SIMPLE_TYPES;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.demo.initializer.FieldInitializationFactory;
import com.demo.initializer.FieldInitializer;

/**
 * Map Field Initializer.
 */
public class MapFieldInitializer implements FieldInitializer {
    private static final Logger LOGGER = Logger.getLogger("MapFieldInitializer");

    @Override
    public Function<String, ?> getConverter() {
        throw new UnsupportedOperationException("Method does not implemented");
    }

    @Override
    public void init(Field field, String actualType, Object targetObj, Object value) throws IllegalAccessException {

        try {
            boolean isMapInitialized = actualType.endsWith(COLLECTION_INITIALIZED);

            if (isMapInitialized) {
                String mapActualType = actualType.substring(0, actualType.length() - 1);

                Object instance = Class.forName(mapActualType).newInstance();

                if (!"[]".equals(value)) {
                    if (value instanceof Map) {
                        Map map = (Map) instance;
                        map.putAll((Map) value);
                    } else {
                        int arrayBeginIndex = value.toString().indexOf("[") + 1;
                        String[] typeOfCollectionElements = value.toString().substring(0, arrayBeginIndex).split(KEY_VALUE_SEPARATOR);
                        String[] values =
                            value.toString().substring(arrayBeginIndex, value.toString().length() - 1).split(ARRAY_VALUE_SEPARATOR);

                        if (BASIC_SIMPLE_TYPES.contains(typeOfCollectionElements[0])
                            && BASIC_SIMPLE_TYPES.contains(typeOfCollectionElements[1])) {
                            FieldInitializer keyInitializer = FieldInitializationFactory.getFieldInitializer(typeOfCollectionElements[0]);
                            FieldInitializer valueInitializer = FieldInitializationFactory.getFieldInitializer
                                (typeOfCollectionElements[1]);
                            Map map = (Map) instance;
                            for (String v : values) {
                                String[] keyValue = v.split(KEY_VALUE_SEPARATOR);
                                map.put(keyInitializer.convert(keyValue[0]), valueInitializer.convert(keyValue[1]));
                            }
                        }

                    }

                    field.set(targetObj, instance);
                }
            }
        } catch (ClassNotFoundException | InstantiationException e) {
            LOGGER.log(Level.SEVERE, "Cannot create instance of:" + actualType);
            throw new IllegalStateException("Object cannot be deserialized");
        }
    }
}
