package com.demo.initializer.impl;

import static com.demo.util.Tags.ARRAY_VALUE_SEPARATOR;
import static com.demo.util.Tags.COLLECTION_INITIALIZED;
import static com.demo.util.Types.BASIC_SIMPLE_TYPES;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.demo.initializer.FieldInitializationFactory;
import com.demo.initializer.FieldInitializer;

/**
 * Collection Field Initializer.
 */
public class CollectionFieldInitializer implements FieldInitializer {
    private static final Logger LOGGER = Logger.getLogger("CollectionFieldInitializer");

    @Override
    public Function<String, ?> getConverter() {
        throw new UnsupportedOperationException("Method does not implemented");
    }

    @Override
    public void init(Field field, String actualType, Object targetObj, Object value) throws IllegalAccessException {

        try {
            int collectionTypeIndex = actualType.indexOf(COLLECTION_INITIALIZED);

            if (collectionTypeIndex != -1) {
                String typeOfCollection = actualType.substring(0, collectionTypeIndex);

                Object instance = Class.forName(typeOfCollection).newInstance();

                if (!"[]".equals(value)) {
                    int arrayBeginIndex = value.toString().indexOf("[");
                    String typeOfCollectionElements = value.toString().substring(0, arrayBeginIndex);
                    String[] values = value.toString().substring(arrayBeginIndex + 1, value.toString().length() - 1).split(ARRAY_VALUE_SEPARATOR);

                    if (BASIC_SIMPLE_TYPES.contains(typeOfCollectionElements)) {
                        FieldInitializer fieldInitializer = FieldInitializationFactory.getFieldInitializer(typeOfCollectionElements);
                        Collection collection = (Collection) instance;
                        for (String v : values) {
                            collection.add(fieldInitializer.convert(v));
                        }
                    } else {
                        if (value instanceof Collection) {
                            Collection collection = (Collection) instance;
                            collection.addAll((Collection) value);
                        }
                    }

                }

                field.set(targetObj, instance);
            }
        } catch (ClassNotFoundException | InstantiationException e) {
            LOGGER.log(Level.SEVERE, "Cannot create instance of:" + actualType);
            throw new IllegalStateException("Object cannot be deserialized");
        }
    }
}
